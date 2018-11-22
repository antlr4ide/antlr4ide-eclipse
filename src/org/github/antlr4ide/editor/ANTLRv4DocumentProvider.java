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

public class ANTLRv4DocumentProvider extends FileDocumentProvider {

	private boolean DEBUG=true;
	
	@Deprecated
	private ANTLRv4Editor editor;
	
	private AntlrDocument doc;


	public ANTLRv4DocumentProvider(ANTLRv4Editor antlRv4Editor) { 
		super();
		this.editor=antlRv4Editor;
		System.out.println(">>> ANTLRv4DocumentProvider editor>" + editor + "<");
	}


	@Override
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
		document.addDocumentListener(new AntlrDocument.AntlrDocumentChangedListener(editor));
		this.doc=(AntlrDocument) document;
		
		if (document != null) {
			IDocumentPartitioner partitioner =
				new FastPartitioner(
					new ANTLRv4PartitionScanner(),
					new String[] {
							ANTLRv4PartitionScanner.ANTLRv4_DIVISION
						});
			partitioner.connect(document);
			document.setDocumentPartitioner(partitioner);
			doc.setEditor(this.editor);
			if(document.getLength()>0)
			  this.doc.scan(); // set the initial list of tokens.
		}
		return document;
	}
	

	/**
	 * Ensure the IDocument is based on AntlrDocument
	 */
	@Override
	protected IDocument createEmptyDocument () {
		return new AntlrDocument();
	}
	
	
	public IDocument getDoc() {
		return doc;
	}


}