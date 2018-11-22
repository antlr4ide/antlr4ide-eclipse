package org.github.antlr4ide.editor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.Token;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.jface.text.Position;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;

/**
 * Encapsulation of the needed additions for the antlr parser.
 * @author Henrik Sorensen
 *
 */
public class AntlrDocument extends Document implements IDocument {


	List<Token> antlrTokens = new ArrayList<Token>();
	private Map<String,Position> parserRules=new HashMap<String, Position>();
	private Map<String,Position> lexerRules=new HashMap<String, Position>();
	List<String> errorList = new ArrayList<String>();
	private ANTLRv4Editor editor;
	
	/**
	 * Invoke the antlr parser and lexer for the document.
	 * The ANTLRv4DocumentProvider will invoke this method, and process the errorList as needed. 
	 * Results of the parsing is in 
	 * <li>List<String> errorList format line:position:token.getStartIndex():token.getStopIndex():message</li>
	 * <li>Map<String,Position> parserRules: name and location of parser rules</li>
	 * <li>Map<String,Position> lexerRules: name and location of lexer rules</li>
	 * The parser- and lexerRules maps are used to populate the outline view.
	 */
	public void scan() {
		errorList.clear();
		LexerHelper lexer = new LexerHelper(getParserRules(), getLexerRules(), errorList);
		antlrTokens = (List<org.antlr.v4.runtime.Token>) lexer.scanString(get());
		processErrors(errorList);
	}
	public Map<String,Position> getParserRules() {
		return parserRules;
	}
	public void setParserRules(Map<String,Position> parserRules) {
		this.parserRules = parserRules;
	}
	public Map<String,Position> getLexerRules() {
		return lexerRules;
	}
	public void setLexerRules(Map<String,Position> lexerRules) {
		this.lexerRules = lexerRules;
	}
	public void setEditor(ANTLRv4Editor editor) {
		this.editor=editor;
	}
	
	private void processErrors(List<String>errorList) {
		if(errorList.size()==0) return;
		
		System.out.println("Syntax errors ("+errorList.size() +"):" + errorList);		

		try {
		// set any error markers from the scanner
		IEditorInput input = editor.getEditorInput();
		IFile file = ((IFileEditorInput) input).getFile();
		file.deleteMarkers(IMarker.PROBLEM, false, 1);
		for (String err : errorList) {
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

	public static class AntlrDocumentChangedListener implements IDocumentListener  {
		ANTLRv4Editor editor;

		public AntlrDocumentChangedListener(ANTLRv4Editor antlRv4Editor) {
			this.editor=antlRv4Editor;
		}

		@Override
		public void documentAboutToBeChanged(DocumentEvent event) {
			// nothing
		}

		@Override
		public void documentChanged(DocumentEvent event) {
//			System.out.println("documentChanged " +event);
			AntlrDocument doc=(AntlrDocument)  event.getDocument();
			doc.scan();
	}
	}
	

}
