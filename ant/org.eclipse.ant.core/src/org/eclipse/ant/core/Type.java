/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ant.core;


import org.eclipse.ant.internal.core.AntObject;

/**
 * Represents an Ant type.
 * @since 2.1
 */
public class Type extends AntObject {

	/**
	 * Returns the type name
	 * @return Returns a String
	 */
	public String getTypeName() {
		return fName;
	}

	/**
	 * Sets the taskName.
	 * @param taskName The taskName to set
	 */
	public void setTypeName(String taskName) {
		fName = taskName;
	}
}
