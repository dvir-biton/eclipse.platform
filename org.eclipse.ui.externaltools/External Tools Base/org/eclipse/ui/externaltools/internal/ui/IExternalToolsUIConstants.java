package org.eclipse.ui.externaltools.internal.ui;

/**********************************************************************
Copyright (c) 2002 IBM Corp. and others. All rights reserved.
This file is made available under the terms of the Common Public License v1.0
which accompanies this distribution, and is available at
http://www.eclipse.org/legal/cpl-v10.html
**********************************************************************/

import org.eclipse.ui.externaltools.model.IExternalToolConstants;


public interface IExternalToolsUIConstants {
	
	// Action images
	public static final String IMG_REMOVE= IExternalToolConstants.PLUGIN_ID + ".remove"; //$NON-NLS-1$
	public static final String IMG_ADD= IExternalToolConstants.PLUGIN_ID + ".add"; //$NON-NLS-1$
	public static final String IMG_RUN= IExternalToolConstants.PLUGIN_ID + ".run"; //$NON-NLS-1$
	
	// Label images
	public static final String IMG_ANT_PROJECT= IExternalToolConstants.PLUGIN_ID + ".antProject"; //$NON-NLS-1$
	public static final String IMG_ANT_PROJECT_ERROR = IExternalToolConstants.PLUGIN_ID + ".antProjectError"; //$NON-NLS-1$
	public static final String IMG_ANT_TARGET= IExternalToolConstants.PLUGIN_ID + ".antTarget"; //$NON-NLS-1$
	public static final String IMG_ANT_TARGET_ERROR = IExternalToolConstants.PLUGIN_ID + ".antTargetError"; //$NON-NLS-1$
	public static final String IMG_ANT_TARGET_ELEMENTS= IExternalToolConstants.PLUGIN_ID + ".antTargetElements"; //$NON-NLS-1$
	public static final String IMG_ANT_TARGET_ELEMENT= IExternalToolConstants.PLUGIN_ID + ".antTargetElement"; //$NON-NLS-1$

	public static final String DIALOGSTORE_LASTEXTJAR= IExternalToolConstants.PLUGIN_ID + ".lastextjar"; //$NON-NLS-1$
	public static final String DIALOGSTORE_LASTEXTFILE= IExternalToolConstants.PLUGIN_ID + ".lastextfile"; //$NON-NLS-1$
}
