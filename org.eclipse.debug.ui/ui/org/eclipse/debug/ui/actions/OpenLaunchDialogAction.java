package org.eclipse.debug.ui.actions;

/**********************************************************************
Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
This file is made available under the terms of the Common Public License v1.0
which accompanies this distribution, and is available at
http://www.eclipse.org/legal/cpl-v10.html
**********************************************************************/

import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.internal.ui.DebugUIPlugin;
import org
	.eclipse
	.debug
	.internal
	.ui
	.launchConfigurations
	.LaunchConfigurationManager;
import org.eclipse.debug.internal.ui.launchConfigurations.LaunchGroupExtension;
import org.eclipse.debug.internal.ui.launchConfigurations.LaunchHistory;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;

/**
 * Opens the launch configuration dialog in the context of a launch group.
 * <p>
 * Clients are not intended to subclass this class; clients may instantiate this
 * class.
 * </p>
 * @since 2.1
 */
public class OpenLaunchDialogAction extends Action {

	/**
	 * Launch group identifier
	 */
	private String fIdentifier;
	
	/**
	 * Constucts an action that opens the launch configuration dialog in
	 * the context of the specified launch groupd.
	 * 
	 * @param identifier unique identifier of a launch group extension
	 */
	public OpenLaunchDialogAction(String identifier) {
		fIdentifier = identifier;
		LaunchGroupExtension extension = LaunchConfigurationManager.getDefault().getLaunchGroup(identifier);
		if (extension != null) {
			setText(extension.getLabel() + "...");
			setImageDescriptor(extension.getImageDescriptor());
		}
		// TODO: help context
	}

	/**
	 * @see IAction
	 */
	public void run() {
		LaunchHistory history = LaunchConfigurationManager.getDefault().getLaunchHistory(fIdentifier);
		ILaunchConfiguration configuration = history.getRecentLaunch();
		IStructuredSelection selection = null;
		if (configuration == null) {
			selection = new StructuredSelection();
		} else {
			selection = new StructuredSelection(configuration);
		}
		DebugUITools.openLaunchConfigurationDialogOnGroup(DebugUIPlugin.getShell(), selection, fIdentifier);
	}
}
