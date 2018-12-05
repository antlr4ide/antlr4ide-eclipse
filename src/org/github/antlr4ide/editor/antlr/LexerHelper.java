package org.github.antlr4ide.editor.antlr;

import java.util.List;
import java.util.Map;

import org.antlr.parser.antlr4.ANTLRv4Lexer;
import org.antlr.parser.antlr4.ANTLRv4Parser;
import org.antlr.parser.antlr4.ANTLRv4ParserBaseVisitor;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonToken;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.eclipse.jface.text.Position;

public class LexerHelper {
	private Map<String, Position> parserRules;
	private Map<String, Position> lexerRules;
	private Map<String, Position> lexerModes;
	private List<String> errorList;

	public LexerHelper(Map<String, Position> parserRules, Map<String, Position> lexerRules, List<String> errorList, Map<String, Position> lexerModes) {
		this.parserRules = parserRules;
		this.lexerRules = lexerRules;
		this.lexerModes = lexerModes;
		this.errorList  = errorList;
	}
	
    BaseErrorListener printError = new BaseErrorListener() {
        @Override
        public void syntaxError(final Recognizer<?, ?> recognizer, final Object offendingSymbol,
            final int line, final int position, final String msg,
            final RecognitionException e) {
//        	System.out.println(gname + "::" + startRule + ":" + line + ":" + position + ": " + msg);
        	String s;
    		if(offendingSymbol instanceof CommonToken) {
    			CommonToken token = (CommonToken) offendingSymbol;
    			s= line + ":" + position + ":" + token.getStartIndex() +":"+ token.getStopIndex() +  ": " + msg;
    		}
    		else
    			s= line + ":" + position + "::: " + msg;

        	errorList.add(s);
        }
      };
	
	public List<? extends Token> scanString(String s) {
		long pot[] = new long[4];

		pot[0] = System.currentTimeMillis();

		ANTLRv4Lexer lexer = new ANTLRv4Lexer(CharStreams.fromString(s));
		pot[1] = System.currentTimeMillis();
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		ANTLRv4Parser parser = new ANTLRv4Parser(tokens);
		parser.removeErrorListeners();
		parser.addErrorListener(printError);
		parser.setBuildParseTree(true);
		ParseTree tree = parser.grammarSpec(); 
		System.out.println(tree.toStringTree());
		pot[2] = System.currentTimeMillis();
		ANTLRv4Visitor visitor = new ANTLRv4Visitor();
		visitor.visit(tree);
		pot[3] = System.currentTimeMillis();

//		System.out.println("Elapsed time " + (pot[3] - pot[0]));
//		System.out.println("   lexer     " + (pot[1] - pot[0]));
//		System.out.println("   parser    " + (pot[2] - pot[1]));
//		System.out.println("   visitor   " + (pot[3] - pot[2]));

		return tokens.getTokens();
	}

	public class ANTLRv4Visitor extends ANTLRv4ParserBaseVisitor<Void> {

		@Override
		public Void visitGrammarSpec(ANTLRv4Parser.GrammarSpecContext ctx) {
			/*
			 * grammarSpec : DOC_COMMENT* grammarType identifier SEMI prequelConstruct*
			 * rules modeSpec* EOF
			 */

			String id = ctx.identifier().getText(); // name of imported grammar
//			System.out.println(">>> LexerHelper.ANTLRv4Visitor visitGrammarSpec. Grammar name >" + id + "<");

			return visitChildren(ctx); // continue the visit
		}

		@Override
		public Void visitDelegateGrammar(ANTLRv4Parser.DelegateGrammarContext ctx) {
			/*
			 * delegateGrammars : IMPORT delegateGrammar (COMMA delegateGrammar)* SEMI
			 * delegateGrammar : identifier ASSIGN identifier | identifier
			 */

			String id = ctx.identifier(0).getText(); // name of imported grammar
//			String id2;
//			if (ctx.identifier().size() > 1)
//				id2 = ctx.identifier(1).getText();

//			System.out.println(">>> LexerHelper.ANTLRv4Visitor visitDelegateGrammar. Import name >" + id + "<");

			return visitChildren(ctx); // continue the visit
		}

		@Override
		public Void visitParserRuleSpec(ANTLRv4Parser.ParserRuleSpecContext ctx) {
			// Track this for outline and cross references
			/*
			 * parserRuleSpec : DOC_COMMENT* ruleModifiers? RULE_REF argActionBlock? ruleReturns? throwsSpec? localsSpec? rulePrequel* COLON ruleBlock SEMI exceptionGroup
			 */

//			System.out.println(">>> LexerHelper.ANTLRv4Visitor visitParserRuleSpec. Defining parser rule >" + ctx.RULE_REF().getText() + "<");

			int a=ctx.start.getStartIndex();
			int b=ctx.stop.getStopIndex();  

			Position position = new Position(a,b-a); // mark the whole parserRuleSpec
			
			emitParserRule(ctx.RULE_REF().getText(), position);

			return visitChildren(ctx); // continue the visit
		}

		@Override
		public Void visitLexerRuleSpec(ANTLRv4Parser.LexerRuleSpecContext ctx) {
			// Track this for outline and cross references
			/*
			 * lexerRuleSpec
			 *    : DOC_COMMENT* FRAGMENT? TOKEN_REF COLON lexerRuleBlock SEMI
			 */

//			System.out.println(">>> LexerHelper.ANTLRv4Visitor visitLexerRuleSpec. Defining lexer rule >" + ctx.TOKEN_REF().getText() + "<");
			
			int a=ctx.start.getStartIndex();
			int b=ctx.stop.getStopIndex(); 

			Position position = new Position(a,b-a); // mark the whole lexerRuleSpec

			emitLexerRule(ctx.TOKEN_REF().getText(), position);

			return visitChildren(ctx); // continue the visit
		}

		@Override
		public Void visitModeSpec(ANTLRv4Parser.ModeSpecContext ctx) {
			// Track this for outline and cross references
			/*
			 * modeSpec
			 *    : MODE identifier SEMI lexerRuleSpec*
			 */

			int a=ctx.start.getStartIndex();
			int b=ctx.stop.getStopIndex();  

			Position position = new Position(a,b-a); // mark the whole modeSpec

			emitLexerMode(ctx.identifier().getText(), position);

			return visitChildren(ctx); // continue the visit
		}
		
	}

	private void emitParserRule(String text, Position position) {
		parserRules.put(text, position);
	}
	private void emitLexerRule(String text, Position position) {
		lexerRules.put(text, position);
	}
	private void emitLexerMode(String text, Position position) {
		lexerModes.put(text, position);
	}

}
