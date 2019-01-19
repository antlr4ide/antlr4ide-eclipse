package org.github.antlr4ide.editor.antlr;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.antlr.parser.antlr4.ANTLRv4Lexer;
import org.antlr.parser.antlr4.ANTLRv4Parser;
import org.antlr.parser.antlr4.ANTLRv4ParserBaseVisitor;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonToken;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.Position;

public class LexerHelper {
	private AntlrGrammarInfo grammarInfo;

	public LexerHelper(AntlrGrammarInfo grammarInfo) {
		
		this.grammarInfo=grammarInfo;
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

    		grammarInfo.getErrorList().add(s);
        }
      };


      
  	public List<Token> scan(File file) {
  		InputStream stream;
		List<Token> out=null;
		
		try {
			stream=new FileInputStream(file);
			out=scan(CharStreams.fromStream(stream));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return out;
	}
	
      
  	public List<Token> scan(IResource resource) {
		IFile file = (IFile) resource;
		List<Token> out=null;
		
		try {
			out=scan(CharStreams.fromStream(file.getContents()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return out;
  	}

  	public List<Token> scan(String content) {
		return scan(CharStreams.fromString(content));
  	}
  	


  	public List<Token> scan(CharStream stream) {
		long pot[] = new long[4];

		pot[0] = System.currentTimeMillis();

		ANTLRv4Lexer lexer = new ANTLRv4Lexer(stream);
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
			 * grammarSpec : DOC_COMMENT* grammarType identifier SEMI prequelConstruct* rules modeSpec* EOF
			 */
			if(ctx.exception==null) {
			emitGrammarName(ctx.identifier().getText());
			emitGrammarType(ctx.grammarType().getText());
			}

			return visitChildren(ctx); // continue the visit
		}

		@Override
		public Void visitDelegateGrammar(ANTLRv4Parser.DelegateGrammarContext ctx) {
			/*
			 * delegateGrammars : IMPORT delegateGrammar (COMMA delegateGrammar)* SEMI
			 * delegateGrammar : identifier ASSIGN identifier | identifier
			 */

			// TODO: Deal with multiple Delegates
			emitDelegate(ctx.identifier(0).getText());

			return visitChildren(ctx); // continue the visit
		}

		@Override
		public Void visitOptionsSpec(ANTLRv4Parser.OptionsSpecContext ctx) {
		/*
		optionsSpec
		   : OPTIONS LBRACE (option SEMI)* RBRACE
		   ;

		option
		   : identifier ASSIGN optionValue
		   ;
		   */
			emitOption(ctx.option(0).getText()); // TODO: Deal with multiple options
			
			return visitChildren(ctx); // continue the visit
		}		
		
		public Void visitAction(ANTLRv4Parser.ActionContext ctx) {
			/*
			 * // Match stuff like @parser::members {int i;}
			 * action
			 *    : AT (actionScopeName COLONCOLON)? identifier actionBlock
			 *    ;
			 *
			 * // Scope names could collide with keywords; allow them as ids for action scopes
			 * actionScopeName
			 *    : identifier
			 *    | LEXER
			 *    | PARSER
			 *    ;
			 * 
			 * actionBlock
			 *    : BEGIN_ACTION ACTION_CONTENT* END_ACTION
			 *    ;
			 */
			
			if(ctx.identifier().getText().equals("header")) emitHeader(ctx.actionBlock().getText());
			
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
		grammarInfo.getParserRules().put(text, position);
	}
	
	public void emitHeader(String header) {
		grammarInfo.setGrammarHeaders(header);		}

	public void emitOption(String option) {
		grammarInfo.setGrammarOptions(option);	
	}

	public void emitDelegate(String delegate) {
		grammarInfo.setGrammarDelegates(delegate);
	}

	private void emitGrammarName(String name) {
		grammarInfo.setGrammarName(name);
	}

	private void emitGrammarType(String type) {
		grammarInfo.setGrammarType(type);
	}
	
	
	private void emitLexerRule(String text, Position position) {
		grammarInfo.getLexerRules().put(text, position);
	}
	private void emitLexerMode(String text, Position position) {
		grammarInfo.getLexerModes().put(text, position);
	}


}
