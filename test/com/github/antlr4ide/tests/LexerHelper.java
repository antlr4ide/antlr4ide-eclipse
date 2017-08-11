package com.github.antlr4ide.tests;

import java.util.List;

import org.antlr.parser.antlr4.ANTLRv4Lexer;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.Token;

public class LexerHelper {
	
	public static  List<? extends Token> scanString(String s) {
		
		ANTLRInputStream antlrInputStream = new ANTLRInputStream(s);
		ANTLRv4Lexer lexer = new ANTLRv4Lexer(antlrInputStream);
//		lexer.get
		return lexer.getAllTokens();
	}

}
