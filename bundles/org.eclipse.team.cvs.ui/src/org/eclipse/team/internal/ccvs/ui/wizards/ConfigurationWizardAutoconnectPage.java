package org.eclipse.team.internal.ccvs.ui.wizards;

/*
 * (c) Copyright IBM Corp. 2000, 2002.
 * All Rights Reserved.
 */

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.team.core.TeamException;
import org.eclipse.team.internal.ccvs.core.ICVSFolder;
import org.eclipse.team.internal.ccvs.core.ICVSRepositoryLocation;
import org.eclipse.team.internal.ccvs.core.connection.CVSRepositoryLocation;
import org.eclipse.team.internal.ccvs.core.resources.CVSWorkspaceRoot;
import org.eclipse.team.internal.ccvs.core.syncinfo.FolderSyncInfo;
import org.eclipse.team.internal.ccvs.ui.Policy;

/**
 * This configuration page explains to the user that CVS/ directories already exists and
 * it will attach the selected project to the repository that is specified in the CVS/ files.
 * 
 * This is useful for people who have checked out a project using command-line tools.
 */
public class ConfigurationWizardAutoconnectPage extends CVSWizardPage {
	private boolean validate = true;
	private FolderSyncInfo info;
	ICVSRepositoryLocation location;

	public ConfigurationWizardAutoconnectPage(String pageName, String title, ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
	}

	/*
	 * @see IDialogPage#createControl(Composite)
	 */
	public void createControl(Composite parent) {
		Composite composite = createComposite(parent, 2);
		setControl(composite);
		
		Label description = new Label(composite, SWT.WRAP);
		GridData data = new GridData();
		data.horizontalSpan = 2;
		data.widthHint = 350;
		description.setLayoutData(data);
		description.setText(Policy.bind("ConfigurationWizardAutoconnectPage.description")); //$NON-NLS-1$
		
		if (location == null) return;

		// Spacer
		createLabel(composite, ""); //$NON-NLS-1$
		createLabel(composite, ""); //$NON-NLS-1$
		
		createLabel(composite, Policy.bind("ConfigurationWizardAutoconnectPage.user")); //$NON-NLS-1$
		createLabel(composite, location.getUsername());
		createLabel(composite, Policy.bind("ConfigurationWizardAutoconnectPage.host")); //$NON-NLS-1$
		createLabel(composite, location.getHost());
		createLabel(composite, Policy.bind("ConfigurationWizardAutoconnectPage.port")); //$NON-NLS-1$
		int port = location.getPort();
		if (port == location.USE_DEFAULT_PORT) {
			createLabel(composite, Policy.bind("ConfigurationWizardAutoconnectPage.default")); //$NON-NLS-1$
		} else {
			createLabel(composite, "" + port); //$NON-NLS-1$
		}
		createLabel(composite, Policy.bind("ConfigurationWizardAutoconnectPage.connectionType")); //$NON-NLS-1$
		createLabel(composite, location.getMethod().getName());
		createLabel(composite, Policy.bind("ConfigurationWizardAutoconnectPage.repositoryPath")); //$NON-NLS-1$
		createLabel(composite, location.getRootDirectory());
		createLabel(composite, Policy.bind("ConfigurationWizardAutoconnectPage.module")); //$NON-NLS-1$
		createLabel(composite, info.getRepository());
		
		// Spacer
		createLabel(composite, ""); //$NON-NLS-1$
		createLabel(composite, ""); //$NON-NLS-1$
		
		final Button check = new Button(composite, SWT.CHECK);
		data = new GridData();
		data.horizontalSpan = 2;
		check.setText(Policy.bind("ConfigurationWizardAutoconnectPage.validate")); //$NON-NLS-1$
		check.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				validate = check.getSelection();
			}
		});
		check.setSelection(true);		
	}
	
	public FolderSyncInfo getFolderSyncInfo() {
		return info;
	}
	public boolean getValidate() {
		return validate;
	}
	public void setProject(IProject project) {
		try {
			ICVSFolder folder = (ICVSFolder)CVSWorkspaceRoot.getCVSResourceFor(project);
			info = folder.getFolderSyncInfo();
			if (info == null) {
				// This should never happen
				ErrorDialog.openError(getContainer().getShell(), Policy.bind("ConfigurationWizardAutoconnectPage.noSyncInfo"), Policy.bind("ConfigurationWizardAutoconnectPage.noCVSDirectory"), null); //$NON-NLS-1$ //$NON-NLS-2$
				return;
			}
			location = CVSRepositoryLocation.fromString(info.getRoot());
		} catch (TeamException e) {
			Shell shell = new Shell(Display.getDefault());
			ErrorDialog.openError(shell, null, null, e.getStatus());
			shell.dispose();
		}
	}
}
