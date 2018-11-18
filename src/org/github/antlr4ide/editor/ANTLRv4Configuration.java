package org.github.antlr4ide.editor;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextInputListener;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.editors.text.TextEditor;

public class ANTLRv4Configuration extends SourceViewerConfiguration {
	
//	private XMLDoubleClickStrategy doubleClickStrategy;
	private ANTLRv4Scanner scanner;
	private ANTLRv4Editor editor;


	public ANTLRv4Configuration(ANTLRv4Editor antlRv4Editor) {
		super();
		this.editor=antlRv4Editor;
	}

	@Override
	public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
		return new String[] {
			IDocument.DEFAULT_CONTENT_TYPE,
			ANTLRv4PartitionScanner.ANTLRv4_DIVISION
			};
	}

//	@Override
//	public ITextDoubleClickStrategy getDoubleClickStrategy(
//		ISourceViewer sourceViewer,
//		String contentType) {
//		if (doubleClickStrategy == null)
//			doubleClickStrategy = new XMLDoubleClickStrategy();
//		return doubleClickStrategy;
//	}

	public ANTLRv4Scanner getANTLRv4Scanner() {
		if (scanner == null) {
			scanner = new ANTLRv4Scanner(editor,true);
			scanner.setDefaultReturnToken(new Token(IANTLRv4ColorConstants.DEFAULT));
		}
		return scanner;
	}

	public ANTLRv4Editor getEditor() { return this.editor; }

	@Override
	public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
		PresentationReconciler reconciler = new PresentationReconciler();
		
		DefaultDamagerRepairer dr = new DefaultDamagerRepairer(getANTLRv4Scanner());
		reconciler.setDamager(dr, ANTLRv4PartitionScanner.ANTLRv4_DIVISION);
		reconciler.setRepairer(dr,ANTLRv4PartitionScanner.ANTLRv4_DIVISION);

		return reconciler;
	}
	
}