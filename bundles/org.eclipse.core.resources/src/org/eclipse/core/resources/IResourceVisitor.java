package org.eclipse.core.resources;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved.
 */

import org.eclipse.core.runtime.CoreException;

/** 
 * This interface is implemented by objects that visit resource trees.
 * <p> 
 * Usage:
 * <pre>
 * class Visitor implements IResourceVisitor {
 *    public boolean visit(IResource res) {
 *       // your code here
 *       return true;
 *    }
 * }
 * IResource root = ...;
 * root.accept(new Visitor());
 * </pre>
 * </p> 
 * <p>
 * Clients may implement this interface.
 * </p>
 *
 * @see IResource#accept
 */
public interface IResourceVisitor {
/** 
 * Visits the given resource.
 *
 * @param resource the resource to visit
 * @return <code>true</code> if the resource's members should
 *		be visited; <code>false</code> if they should be skipped
 * @exception CoreException if the visit fails for some reason.
 */
public boolean visit(IResource resource) throws CoreException;
}
