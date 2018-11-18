package org.github.antlr4ide.editor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.Token;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.Position;

public class AntlrDocument extends Document implements IDocument {


	List<Token> antlrTokens = new ArrayList<Token>();
	Map<String,Position> parserRules=new HashMap<String, Position>();
	Map<String,Position> lexerRules=new HashMap<String, Position>();
	List<String> errorList = new ArrayList<String>();
	public void scan() {
		errorList.clear();
		LexerHelper lexer = new LexerHelper(parserRules, lexerRules, errorList);
		antlrTokens = (List<org.antlr.v4.runtime.Token>) lexer.scanString(get());
		if(errorList.size()>0) System.out.println("Syntax errors ("+errorList.size() +"):" + errorList);
	}

	
}
