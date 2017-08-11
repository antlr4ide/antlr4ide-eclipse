package org.github.antlr4ide.editor;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.ui.editors.text.FileDocumentProvider;

public class ANTLRv4DocumentProvider extends FileDocumentProvider {

	private boolean DEBUG=false; //true;

	protected IDocument createDocument(Object element) throws CoreException {
		if (DEBUG) {
			System.out.println(">>> ANTLRv4DocumentProvider.createDocument element>" + element.getClass() + "<");
			Exception ex = new Exception();
			int i = 0;
			for (StackTraceElement ste : ex.getStackTrace()) {
				System.out.println("    createDocument> "+ ste.toString());
				i++;
				if (i > 20)
					break;
			}
		}
		
		IDocument document = super.createDocument(element);
		
		if (document != null) {
			IDocumentPartitioner partitioner =
				new FastPartitioner(
					new ANTLRv4PartitionScanner(),
					new String[] {
							ANTLRv4PartitionScanner.ANTLRv4_DIVISION
						});
			partitioner.connect(document);
			document.setDocumentPartitioner(partitioner);
		}
		return document;
	}
}