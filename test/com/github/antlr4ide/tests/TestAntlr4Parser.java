package com.github.antlr4ide.tests;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

public class TestAntlr4Parser {
	@Rule public TestName name = new TestName();
	
	public TestAntlr4Parser() {	}	
	
	@Test
	public void	Test001() {
		 System.out.println("------> "+name.getMethodName());
		 String result = ParserHelper.scanString(""); 
		 Assert.assertNotNull(result);
		 Assert.assertEquals("Parsing empty string", "(grammarSpec grammarType identifier <missing SEMI> rules <EOF>)",result);

		 System.out.println(result);
		 System.out.println("------< "+name.getMethodName());
	}
	
	

	@Test
	public void	Test002() {
		 System.out.println("------> "+name.getMethodName());
		 String result = ParserHelper.scanString(
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
		 String result = ParserHelper.scanString(
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
