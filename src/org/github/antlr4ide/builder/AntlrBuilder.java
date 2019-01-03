package org.github.antlr4ide.builder;

import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.github.antlr4ide.console.AntlrConsoleFactory;

public class AntlrBuilder extends IncrementalProjectBuilder {

	class AntlrDeltaVisitor implements IResourceDeltaVisitor {
		@Override
		public boolean visit(IResourceDelta delta) throws CoreException {
			System.out.println("AntlrBuilder.AntlrDeltaVisitor - visitor - "+delta.getResource().getName()+" ("+toTextDeltaKind(delta.getKind())+")");
			
			IResource resource = delta.getResource();
			switch (delta.getKind()) {
			case IResourceDelta.ADDED:
				// handle added resource
				checkG4(resource);
				break;
			case IResourceDelta.REMOVED:
				// handle removed resource
				break;
			case IResourceDelta.CHANGED:
				// handle changed resource
				checkG4(resource);
				break;
			}
			//return true to continue visiting children.
			return true;
		}
		
		private String toTextDeltaKind(int deltaKind) {
			switch(deltaKind) {
		case IResourceDelta.ADDED:   return "added";
		case IResourceDelta.REMOVED: return "removed";
		case IResourceDelta.CHANGED: return "changed";
			}
			
			return " fix toTextDeltaKind kind: " + deltaKind;
		}
	}

	class AntlrResourceVisitor implements IResourceVisitor {
		public boolean visit(IResource resource) {
			System.out.println("AntlrBuilder.AntlrResourceVisitor - visitor - "+resource.getName());
			checkG4(resource);
			//return true to continue visiting children.
			return true;
		}
	}
	public static final String BUILDER_ID = "antlr4ide-eclipse.antlrBuilder";

	public static final String MARKER_TYPE = "antlr4ide-eclipse.antlrProblem";

//	private void addMarker(IFile file, String message, int lineNumber,
//			int severity) {
//		try {
//			IMarker marker = file.createMarker(MARKER_TYPE);
//			marker.setAttribute(IMarker.MESSAGE, message);
//			marker.setAttribute(IMarker.SEVERITY, severity);
//			if (lineNumber == -1) {
//				lineNumber = 1;
//			}
//			marker.setAttribute(IMarker.LINE_NUMBER, lineNumber);
//		} catch (CoreException e) {
//		}
//	}

	@Override
	protected IProject[] build(int kind, Map<String, String> args, IProgressMonitor monitor)
			throws CoreException {
		System.out.println("AntlrBuilder - build - ("+toTextBuildKind(kind)+") "+args);
		
		if (kind == IncrementalProjectBuilder.FULL_BUILD) {
			fullBuild(monitor);
		} else {
			IResourceDelta delta = getDelta(getProject());
			if (delta == null) {
				fullBuild(monitor);
			} else {
				incrementalBuild(delta, monitor);
			}
		}
		return null;
	}

	private String toTextBuildKind(int buildKind) {
		  switch(buildKind) {
		  case IncrementalProjectBuilder.FULL_BUILD: return "Full";
		  case IncrementalProjectBuilder.AUTO_BUILD: return "Auto";
		  case IncrementalProjectBuilder.CLEAN_BUILD: return "Clean";
		  case IncrementalProjectBuilder.INCREMENTAL_BUILD: return "Incremental";
		  }
		  return " fix toTextBuildKind kind: " + buildKind;
	}
	
	protected void clean(IProgressMonitor monitor) throws CoreException {
		System.out.println("AntlrBuilder - clean ");
		
		// delete markers set and files created
		getProject().deleteMarkers(AntlrBuilder.MARKER_TYPE, true, IResource.DEPTH_INFINITE);
	}

	void checkG4(IResource resource) {
		System.out.println("AntlrBuilder - checkG4 - "+resource.getName()+" "+resource.getFileExtension());
		if (resource instanceof IFile && resource.getFileExtension().equals("g4")) {
			IFile file = (IFile) resource;
			try {
				deleteMarkers(file);
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void deleteMarkers(IFile file) throws CoreException {
		System.out.println("AntlrBuilder - deleteMarkers");
		file.deleteMarkers(AntlrBuilder.MARKER_TYPE, false, IResource.DEPTH_ONE);
	}

	protected void fullBuild(final IProgressMonitor monitor)
			throws CoreException {
		System.out.println("AntlrBuilder - fullBuild");
		getProject().accept(new AntlrResourceVisitor());

	}

	protected void incrementalBuild(IResourceDelta delta,
			IProgressMonitor monitor) throws CoreException {
		// the visitor does the work.
		System.out.println("AntlrBuilder - incrementalBuild");
		delta.accept(new AntlrDeltaVisitor());
	}
}
