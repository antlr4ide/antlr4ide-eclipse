package org.github.antlr4ide.editor;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.github.antlr4ide.editor.outliner.AntlrDocOutlineView;


public class ANTLRv4Editor extends TextEditor implements IAdaptable {

	private AntlrDocOutlineView fOutlinePage;

	public ANTLRv4Editor() {
		super();
		setSourceViewerConfiguration(new ANTLRv4Configuration(this));
		setDocumentProvider(new ANTLRv4DocumentProvider(this));
	}

		/*
		 * This method is exposing the source viewer configuration. 
		 * This is used by various parts of the editor
		 * - outliner to get to the scanner that contains the map of parserrules and lexerrules
		 */
		public SourceViewerConfiguration getEditorConfiguration() {
			return getSourceViewerConfiguration();
		}
	
	
	
	public void dispose() {
		super.dispose();
		fOutlinePage.dispose();
	}

	/* configure outline adaptor */
	public Object getAdapter(Class required) {
		if (IContentOutlinePage.class.equals(required)) {
			if (fOutlinePage == null) {
				fOutlinePage= new AntlrDocOutlineView(getDocumentProvider(), this);
				if (getEditorInput() != null)
					fOutlinePage.setInput(getEditorInput());
			}
			return fOutlinePage;
		}
		return super.getAdapter(required);
	}
	

	/* --------------------------------------------------------------
	 * SET EDITOR AS READONLY
	 */
	
//	@Override
//	public boolean isEditable() {
//	    return false;
//	}
//
//	@Override
//	public boolean isEditorInputModifiable() {
//	    return false;
//	}
//
//	@Override
//	public boolean isEditorInputReadOnly() {
//	    return true;
//	}
//
//	@Override
//	public boolean isDirty() {
//	    return false;
//	}
//	
	/* --------------------------------------------------------------- */

}
