package org.eclipse.team.internal.ccvs.ui;

/*
 * (c) Copyright IBM Corp. 2000, 2002.
 * All Rights Reserved.
 */
 
import org.eclipse.ui.*;

public class CVSPerspective implements IPerspectiveFactory {

	/* (Non-javadoc)
	 * Method declared on IPerpsectiveFactory
	 */
	public void createInitialLayout(IPageLayout layout) {
		defineActions(layout);
		defineLayout(layout);
	}
	
	/**
	 * Defines the initial actions for a page.  
	 */
	public void defineActions(IPageLayout layout) {
		// Add "new wizards".
		layout.addNewWizardShortcut("org.eclipse.ui.wizards.new.project"); //$NON-NLS-1$
		layout.addNewWizardShortcut("org.eclipse.ui.wizards.new.folder"); //$NON-NLS-1$
		layout.addNewWizardShortcut("org.eclipse.ui.wizards.new.file"); //$NON-NLS-1$
	
		// Add "show views".
		layout.addShowViewShortcut(IPageLayout.ID_RES_NAV);
		layout.addShowViewShortcut(IPageLayout.ID_OUTLINE);
		layout.addShowViewShortcut(IPageLayout.ID_TASK_LIST);
		layout.addShowViewShortcut(HistoryView.VIEW_ID);
		layout.addShowViewShortcut(RepositoriesView.VIEW_ID);
	}
	
	/**
	 * Defines the initial layout for a page.  
	 */
	public void defineLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
	
		IFolderLayout top = layout.createFolder("top", IPageLayout.TOP, 0.60f, editorArea); //$NON-NLS-1$
		top.addView(RepositoriesView.VIEW_ID);
		layout.addView(HistoryView.VIEW_ID, IPageLayout.BOTTOM, 1.0f, editorArea);
	
		layout.setEditorAreaVisible(false);
	}
}