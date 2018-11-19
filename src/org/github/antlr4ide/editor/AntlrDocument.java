package org.github.antlr4ide.editor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.Token;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.Position;

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
		if(errorList.size()>0) System.out.println("Syntax errors ("+errorList.size() +"):" + errorList);
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

	
}
