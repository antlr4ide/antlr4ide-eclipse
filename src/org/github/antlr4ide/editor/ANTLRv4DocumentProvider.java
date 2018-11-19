package org.github.antlr4ide.editor;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.editors.text.FileDocumentProvider;
import org.eclipse.ui.editors.text.TextEditor;

public class ANTLRv4DocumentProvider extends FileDocumentProvider {

	private boolean DEBUG=true;
	
	@Deprecated
	private ANTLRv4Editor editor;
	
	private IDocument doc;


	public ANTLRv4DocumentProvider(ANTLRv4Editor antlRv4Editor) { 
		super();
		this.editor=antlRv4Editor;
		System.out.println(">>> ANTLRv4DocumentProvider editor>" + editor + "<");
	}


	protected IDocument createDocument(Object element) throws CoreException {
		if (DEBUG) {
			System.out.println(">>> ANTLRv4DocumentProvider.createDocument element>" + element.getClass() + "<");
//			Exception ex = new Exception();
//			int i = 0;
//			for (StackTraceElement ste : ex.getStackTrace()) {
//				System.out.println("    createDocument> "+ ste.toString());
//				i++;
//				if (i > 20)
//					break;
//			}
		}
		
		IDocument document = super.createDocument(element);
		document.addDocumentListener(new MyDocumentListener(editor));
		
		if (document != null) {
			IDocumentPartitioner partitioner =
				new FastPartitioner(
					new ANTLRv4PartitionScanner(),
					new String[] {
							ANTLRv4PartitionScanner.ANTLRv4_DIVISION
						});
			partitioner.connect(document);
			document.setDocumentPartitioner(partitioner);
			((AntlrDocument)document).scan(); // set the initial list of tokens.
		}
		this.doc=document;
		return document;
	}
	
	
	protected IDocument createEmptyDocument () {
		return new AntlrDocument();
	}
	
	
	public IDocument getDoc() {
		return doc;
	}


	// TODO: Move to AntlrScanner
	public class MyDocumentListener implements IDocumentListener  {
		TextEditor editor;

		public MyDocumentListener(ANTLRv4Editor antlRv4Editor) {
			this.editor=antlRv4Editor;
		}

		@Override
		public void documentAboutToBeChanged(DocumentEvent event) {
			// nothing
		}

		@Override
		public void documentChanged(DocumentEvent event) {
//			System.out.println("documentChanged " +event);
			try {
			AntlrDocument doc=(AntlrDocument)  event.getDocument();
			doc.scan();

			// set any error markers from the scanner
			IEditorInput input = this.editor.getEditorInput();
			IFile file = ((IFileEditorInput) input).getFile();
			file.deleteMarkers(IMarker.PROBLEM, false, 1);
			for (String err : doc.errorList) {
				// annotate resource with errors.
				// errorlist format line:position:token.getStartIndex():token.getStopIndex():message
				String ss[] = err.split(":");
				int line = Integer.parseInt(ss[0]);
				int start = Integer.parseInt(ss[2]);
				int end = Integer.parseInt(ss[3]) + 1; // token.getStopIndex() is inclusive. char_end is exclusive so add +1

				String mrkAttr[] = { IMarker.SEVERITY, IMarker.LINE_NUMBER, IMarker.MESSAGE, IMarker.CHAR_START, IMarker.CHAR_END };
				Object mrkValue[] = { IMarker.SEVERITY_ERROR, line, ss[4], start, end };
				IMarker marker = file.createMarker(IMarker.PROBLEM);
				marker.setAttributes(mrkAttr, mrkValue);
			}
		}
		 catch (CoreException e) {
			e.printStackTrace();
		}
		
	}

	}
	
}