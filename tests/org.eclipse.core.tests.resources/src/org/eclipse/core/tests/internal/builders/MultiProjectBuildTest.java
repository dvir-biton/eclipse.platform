package org.eclipse.core.tests.internal.builders;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved.
 */

import java.util.*;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.tests.resources.TestPerformer;


/**
 * This class tests builds that span multiple projects.  Project builders
 * can specify what other projects they are interested in receiving deltas for,
 * and they should only be receiving deltas for exactly those projects.
 */
public class MultiProjectBuildTest extends AbstractBuilderTest {
	//various resource handles
	private IProject project1;
	private IProject project2;
	private IProject project3;
	private IProject project4;
	private IFile file1;
	private IFile file2;
	private IFile file3;
	private IFile file4;
public MultiProjectBuildTest() {
	super(null);
}
/**
 * Public constructor required for test harness.
 */
public MultiProjectBuildTest(String name) {
	super(name);
}
/**
 * Returns an array of interesting project combinations.
 */
protected Object[] interestingProjects() {
	//mix things up, because requests from one run affect results in the next.
	return new Object[] {
		new IProject[] {},		
		new IProject[] {project3},
		new IProject[] {project1},
		new IProject[] {project1, project2, project3},
		new IProject[] {project2},
		new IProject[] {project3},
		new IProject[] {project4},
		new IProject[] {project1, project2},
		new IProject[] {project1, project3},
		new IProject[] {project3},
		new IProject[] {project2, project3},
		new IProject[] {project1, project2, project3},
		new IProject[] {project1, project2, project4},
		new IProject[] {project1},
		new IProject[] {project1, project3, project4},
		new IProject[] {project1, project2},
		new IProject[] {project2, project3, project4},
		new IProject[] {project3, project4},
		new IProject[] {project1, project2, project3, project4},
	};
}

public static Test suite() {
	return new TestSuite(MultiProjectBuildTest.class);
}
/**
 * Modifies any files in the given projects, all in a single operation
 */
protected void dirty(final IProject[] projects) throws CoreException {
	getWorkspace().run(new IWorkspaceRunnable() {
		public void run(IProgressMonitor monitor) throws CoreException {
			for (int i = 0; i < projects.length; i++) {
				IResource[] members = projects[i].members();
				for (int j = 0; j < members.length; j++) {
					if (members[j].getType() == IResource.FILE) {
						((IFile)members[j]).setContents(getRandomContents(), true, true, null);
					}
				}
			}
		}
	}, getMonitor());
}
/**
 * Returns an array reversed.
 */
Object[] reverse(Object[] input) {
	if (input == null) return null;
	int len = input.length;
	Object[] output = new Object[len];
	for (int i = 0; i < len; i++) {
		output[len-i-1] = input[i];
	}
	return output;
}

/*
 * @see TestCase#setUp()
 */
protected void setUp() throws Exception {
	super.setUp();
	setAutoBuilding(true);
	IWorkspaceRoot root = getWorkspace().getRoot();
	project1 = root.getProject("Project1");
	project2 = root.getProject("Project2");
	project3 = root.getProject("Project3");
	project4 = root.getProject("Project4");
	file1 = project1.getFile("File1");
	file2 = project2.getFile("File2");
	file3 = project3.getFile("File3");
	file4 = project4.getFile("File4");
	IResource[] resources = {project1, project2, project3, project4, file1, file2, file3, file4};
	ensureExistsInWorkspace(resources, true);
}

/*
 * @see TestCase#tearDown()
 */
protected void tearDown() throws Exception {
	super.tearDown();
	getWorkspace().getRoot().delete(true, getMonitor());
}
/**
 * In this test, only project1 has a builder, but it is interested in deltas from the other projects.
 * We vary the set of projects that are changed, and the set of projects we request deltas for.
 */
public void testDeltas() {
	//add builder and do an initial build to get the instance
	try {
		addBuilder(project1, DeltaVerifierBuilder.BUILDER_NAME);
		project1.build(IncrementalProjectBuilder.FULL_BUILD, getMonitor());
	} catch (CoreException e) {
		fail("1.0", e);
	}
	final DeltaVerifierBuilder builder = DeltaVerifierBuilder.getInstance();
	assertTrue("1.1", builder != null);
	//always check deltas for all projects
	final IProject[] allProjects = new IProject[] {project1, project2, project3, project4};
	builder.checkDeltas(allProjects);

	//hold onto the set of requested projects here
	final IProject[][] previousRequest = new IProject[][] {new IProject[] {project1}};
	//hold onto projects that have been modified since the last time the builder was run.
	final HashSet previouslyModified = new HashSet();
	new TestPerformer("testDeltas") {
		public Object[] interestingOldState(Object[] args) throws Exception {return null;}
		public Object invokeMethod(Object[] args, int count) throws Exception {
			//set requests for next build
			IProject[] requested = (IProject[])args[0];
			IProject[] toModify = (IProject[])args[1];
			builder.reset();
			builder.requestDeltas(requested);
			//do the build
			dirty(toModify);
			Object result = previousRequest[0];
			if (builder.wasBuilt()) {
				//if the builder ran, update previous request
				previousRequest[0] = requested;
				previouslyModified.clear();
			} else {
				previouslyModified.addAll(Arrays.asList(toModify));
			}
			return result;
		}
		public boolean shouldFail(Object[] args, int count) {return false;}
		public boolean wasSuccess(Object[] args, Object result, Object[] oldState) throws Exception {
			HashSet requested = new HashSet(Arrays.asList((IProject[])result));
			HashSet modified = new HashSet(Arrays.asList((IProject[])args[1]));
			modified.addAll(previouslyModified);
			HashSet obtained = new HashSet();
			if (!builder.getReceivedDeltas().isEmpty())
				obtained.addAll(builder.getReceivedDeltas());
			ArrayList emptyDeltas = builder.getEmptyDeltas();
			
			//the builder's project is implicitly requested
			requested.add(builder.getProject());
				
			for (int i = 0; i < allProjects.length; i++) {
				IProject project = allProjects[i];
				boolean wasObtained = obtained.contains(project);
				boolean wasRequested = requested.contains(project);
				boolean wasModified = modified.contains(project);
				boolean wasEmpty = emptyDeltas.contains(project);
				if (wasObtained) {
					//every delta we obtained should have been requested and (modified or empty)
					if (!wasRequested || !(wasModified || wasEmpty))
						return false;
				} else {
					//if delta was not obtained, then must be unchanged or not requested
					if (wasRequested && wasModified) return false;
				}
			}
			return true;
		}
	}.performTest(new Object[][] {interestingProjects(), reverse(interestingProjects())});
}
/**
 * Test for Bug #5102.  Never reproduced but interesting little test, worth keeping around
 */
public void testPR() throws Exception {
	//create a project with a RefreshLocalJavaFileBuilder and a SortBuilder on the classpath
	IProject project = getWorkspace().getRoot().getProject("P1");
	project.create(null);
	project.open(null);
	IProjectDescription desc = project.getDescription();
	ICommand one = desc.newCommand();
	one.setBuilderName(RefreshLocalJavaFileBuilder.BUILDER_NAME);
	ICommand two = desc.newCommand();
	two.setBuilderName(SortBuilder.BUILDER_NAME);
	desc.setBuildSpec(new ICommand[] {one, two});
	project.setDescription(desc, null);
	
	//do a full build
	project.build(IncrementalProjectBuilder.FULL_BUILD, null);

	//do an incremental build by creating a file
	IFile file = project.getFile("Foo");
	file.create(getRandomContents(), true, getMonitor());
	
}

}

