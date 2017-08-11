package com.github.antlr4ide.tests;

import java.util.List;

import org.antlr.v4.runtime.Token;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

public class TestAntlr4Lexer{
	@Rule public TestName name = new TestName();
	
	public TestAntlr4Lexer() {	}	
	
	@Test
	public void	Test001() {
		 System.out.println("------> "+name.getMethodName());
		 List<? extends Token> result = LexerHelper.scanString("");
		 Assert.assertNotNull(result);
		 Assert.assertTrue("Expected empty list", result.isEmpty());

		 System.out.println(result);
		 System.out.println("------< "+name.getMethodName());
	}
	
	

	@Test
	public void	Test002() {
		 System.out.println("------> "+name.getMethodName());
		 List<? extends Token> result = LexerHelper.scanString(
				"parser grammar CobolSQL;\r\n" + 
		 		"	//@header {package com.nttdata.imagn.antlr;}\r\n" + 
		 		"	sqlstmt: sqlquery sqlqueryclauses*"
		 		);
		 Assert.assertNotNull(result);
		 Assert.assertFalse("Expected non-empty list", result.isEmpty());

		 System.out.println(result);
		 System.out.println("------< "+name.getMethodName());
	}
	

	@Test
	public void	Test003() {
		 System.out.println("------> "+name.getMethodName());
		 List<? extends Token> result = LexerHelper.scanString(
				"			parser grammar CobolParser;\r\n" + 
				"			@header {\r\n" + 
				"			package com.nttdata.imagn.antlr; \r\n" + 
				"			}\r\n" + 
				"			options { tokenVocab=CobolLexer; } \r\n" + 
				"			\r\n" + 
				"			program: controlcards?\r\n" + 
				"			         identificationDivision\r\n" + 
				"			         environmentDivision?\r\n" + 
				"			         dataDivision?\r\n" + 
				"			         procedureDivision?\r\n" + 
				"			         EOF\r\n" + 
				"			;"
		 		);
		 Assert.assertNotNull(result);
		 Assert.assertFalse("Expected non-empty list", result.isEmpty());

		 System.out.println(result);
		 System.out.println("------< "+name.getMethodName());
	}
	

}
