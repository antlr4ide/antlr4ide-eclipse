package com.github.antlr4ide.tests;

import org.antlr.parser.antlr4.ANTLRv4Lexer;
import org.antlr.parser.antlr4.ANTLRv4Parser;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

public class ParserHelper {

	public static String scanString(String s) {

		ANTLRInputStream antlrInputStream = new ANTLRInputStream(s);
		ANTLRv4Lexer lexer = new ANTLRv4Lexer(antlrInputStream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        ANTLRv4Parser parser = new ANTLRv4Parser(tokens);
        
        parser.setBuildParseTree(true);
        ParseTree tree = parser.grammarSpec();
        
		return tree.toStringTree(parser);
	}

}
