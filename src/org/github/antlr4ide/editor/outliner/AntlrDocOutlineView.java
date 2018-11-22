package org.github.antlr4ide.editor.outliner;

import org.eclipse.jface.text.*;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;
import org.github.antlr4ide.editor.ANTLRv4DocumentProvider;
import org.github.antlr4ide.editor.AntlrDocument;

/**
 * This sample class demonstrates how to plug-in a new workbench view. The view
 * shows data obtained from the model. The sample creates a dummy model on the
 * fly, but a real implementation would connect to the model available either in
 * this or another plug-in (e.g. the workspace). The view is connected to the
 * model using a content provider.
 * <p>
 * The view uses a label provider to define how model objects should be
 * presented in the view. Each view can present the same model objects using
 * different labels and icons, if needed. Alternatively, a single label provider
 * can be shared between views in order to ensure that objects of the same type
 * are presented in the same way everywhere.
 * <p>
 * see http://alvinalexander.com/java/jwarehouse/eclipse/org.eclipse.ui.examples.javaeditor/Eclipse-Java-Editor-Example/org/eclipse/ui/examples/javaeditor/JavaContentOutlinePage.java.shtml
 * see http://www.eclipse.org/articles/Article-TreeViewer/TreeViewerArticle.htm
 */

public class AntlrDocOutlineView extends ContentOutlinePage {
	private boolean DEBUG = !true; // debug outline view

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "org.github.antlr4ide.editor.outliner.AntlrDocOutlineView";

	/**
	 * A segment element.
	 */
	protected static class Segment {
		public String name;
		public Position position;

		public Segment(String name, Position position) {
			this.name = name;
			this.position = position;
		}

		public String toString() {
			return name;
		}
	}

	protected Object fInput;
	protected ANTLRv4DocumentProvider fDocumentProvider;
	protected ITextEditor fTextEditor;
	protected AntlrDocument doc;

	/**
	 * Creates a content outline page using the given provider and the given editor.
	 * 
	 * @param provider
	 *            the document provider
	 * @param editor
	 *            the editor
	 */
	public AntlrDocOutlineView(IDocumentProvider provider, ITextEditor editor) {
		super();
		if (DEBUG)
			System.out.println(">>> AntlrDocOutlineView (" + provider.getClass() + ", " + editor.getClass() + ")");
		fDocumentProvider = (ANTLRv4DocumentProvider)provider;
		fTextEditor = editor;
		
		doc=(AntlrDocument) fDocumentProvider.getDoc();
		
	}

	/*
	 * (non-Javadoc) Method declared on ContentOutlinePage
	 */
	@Override
	public void createControl(Composite parent) {
//		if (DEBUG)
//			System.out.println(">>> AntlrDocOutlineView.createControl (" + parent.getClass() + ")");

		super.createControl(parent);

		TreeViewer viewer = getTreeViewer();
		viewer.setContentProvider(new AntlrDocOutlineContentProvider(fDocumentProvider));
		viewer.setLabelProvider(new LabelProvider());
		viewer.setComparator(new ViewerComparator()); // Keep outline sorted

		if (fInput != null)
			viewer.setInput(fInput);
	}

	/*
	 * (non-Javadoc) Method declared on ContentOutlinePage
	 */
	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		super.selectionChanged(event);

		IStructuredSelection selection = (IStructuredSelection) event.getSelection();

		if (selection.isEmpty())
			fTextEditor.resetHighlightRange();
		else {
			Position pos = null;
			if (selection.getFirstElement() instanceof String) {
				pos = doc.getParserRules().get(selection.getFirstElement());
				if (pos == null)
					pos = doc.getLexerRules().get(selection.getFirstElement());
			}

			if (pos != null) {
				try {
					fTextEditor.setHighlightRange(pos.getOffset(), pos.getLength(), true);
				} catch (IllegalArgumentException x) {
					fTextEditor.resetHighlightRange();
				}
			}
		}
	}

	/**
	 * Sets the input of the outline page
	 * Initially invoked from ANTRLRv4Editor.java.
	 * 
	 * @param input
	 *            the input of this outline page
	 */
	public void setInput(Object input) {
		// System.out.println(">>> OutlineView.setInput
		// ("+(input==null?"null":input.getClass())+")");
		fInput = input;
		update();
	}

	/**
	 * Updates the outline page.
	 */
	public void update() {
		// System.out.println(">>> OutlineView.update");
		TreeViewer viewer = getTreeViewer();

		if (viewer != null) {
			Control control = viewer.getControl();
			if (control != null && !control.isDisposed()) {
				control.setRedraw(false);
				viewer.setInput(fInput);
				viewer.expandAll();
				control.setRedraw(true);
			}
		}
	}

}