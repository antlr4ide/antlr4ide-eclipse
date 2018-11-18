package org.github.antlr4ide.editor;

import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.Token;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.text.rules.*;
import org.eclipse.jface.text.*;

public class AntlrScanner implements ITokenScanner  {
	private static final boolean DEBUG_VERBOSE = false;

	protected boolean DEBUG;

	protected AntlrDocument document;
	protected static Map<Integer, IToken> hilite ;  // Users should override this. See ANTLRv4Scanner.java
	
	private IToken fDefaultReturnToken;
	
	private List<Token>antlrTokens;
	private int tokenIndex;
	
	private int setRangeOffset;
	private int setRangeLength;

	private Map<String, Position> parserRules;

	private Map<String, Position> lexerRules;

	public AntlrScanner() {
         this(false);
	}
	
	public AntlrScanner( boolean debug) {
		this.DEBUG=debug;
		if (DEBUG) {
			System.out.println(">>> AntlrScanner debug>" + debug + "<");
		}
		
	}

	public boolean setDebug(boolean debug) {
		boolean oldDebug=DEBUG;
		DEBUG=debug;
		return oldDebug;
	}
	
	/**
	 * Configures the scanner's default return token. This is the token
	 * which is returned when none of the ruleops fired and EOF has not been
	 * reached.
	 *
	 * @param defaultReturnToken the default return token
	 * @since 2.0
	 */
	public void setDefaultReturnToken(IToken defaultReturnToken) {
		Assert.isNotNull(defaultReturnToken.getData());
		fDefaultReturnToken= defaultReturnToken;
	}
	public IToken getDefaultReturnToken() { return fDefaultReturnToken; }
	
	/**
	 * The convention Eclipse seems to use is:
	 *    for all tokens in the range return text attributes
	 *    start offset and lengths can be anywhere within the document, and offset can be a start of line
	 * TODO: setRange should rebuild the parse tree
	 * setRange is invoked _after_ the partition scanner
	 */
	@Override
	public void setRange(IDocument document, int offset, int length) {
		
		if (DEBUG) {
			System.out.println(">>> AntlrScanner.setRange offset>" + offset + "< length>" + length + "<");
		}
		
		if (DEBUG_VERBOSE) {
			//System.out.println(">>> AntlrScanner.setRange offset>" + offset + "< length>" + length + "<");
			Exception ex = new Exception();
			int i = 0;
			for (StackTraceElement ste : ex.getStackTrace()) {
				if(i<20)
				    System.out.println(ste.toString());
				else if (i > 20 && ste.toString().startsWith("org.github")) // only print related
					System.out.println(ste.toString());
				i++;
			}
		}
		
		this.setRangeOffset=offset;
		this.setRangeLength=length;
		
		this.document= (AntlrDocument) document;

		antlrTokens=this.document.antlrTokens;
		tokenIndex=getAntlrTokenIndex(offset, length);
		
	}

	protected IToken getTextAttrToken(Token antlrToken) {
		IToken token=hilite.get(antlrToken.getType());
		
		if(token==null)			
		  token=fDefaultReturnToken; // no attributes defined
		
		return token;
	}
	
	/**
	 * Returns org.eclipse.jface.text.rules.Tokens for color highlighting
	 */
	@Override
	public IToken nextToken() {
		IToken token;

		// only look at tokens from current file
		tokenIndex++;
	
		if (tokenIndex < antlrTokens.size()
		&& isRangeOK(antlrTokens.get(tokenIndex))) {
			token = getTextAttrToken(antlrTokens.get(tokenIndex));
		} else {
			token = org.eclipse.jface.text.rules.Token.EOF;
		}
		
		if(DEBUG_VERBOSE) {
			String documentText="";  // for trace texts
			if(token!=org.eclipse.jface.text.rules.Token.EOF)
			try {
				documentText=((IDocument)document).get(getTokenOffset(),getTokenLength());
			} catch (BadLocationException e) {
				System.out.println(" Exception caught for >"+antlrTokens.get(tokenIndex)+"< "
						+ " offset "+getTokenOffset()
						+ " length "+getTokenLength()
						);
				e.printStackTrace();
			}
		  // TODO: qualify text with actual scanner name  String scannerName?
		  System.out.println(">>> AntlrScanner.nextToken >"+(token.isOther()?((TextAttribute)token.getData()).getForeground():(token.isEOF()?"<EOF>":(token.isUndefined()?"undefined":"-")))+"<"
		   		  +(tokenIndex < antlrTokens.size()?
					  " offset "+getTokenOffset()
					+ " length "+getTokenLength():"")
				    +" >"+documentText+"<");
		}
		return token;
	}

	private boolean isRangeOK(Token token) {
		return isRangeOK(token.getStartIndex());
	}

	private boolean isRangeOK(int index) {
		if(DEBUG_VERBOSE) { 
			System.out.println(">>> AntlrScanner.isRangeOK >"+index
			+","+(index< (setRangeOffset+setRangeLength))
		    +","+(index>=(setRangeOffset))
		     );
		}
		
		if(index< (setRangeOffset+setRangeLength)
		&& index>=(setRangeOffset)
		  )
			return true;
		
		return false;
	}
	
	protected int getAntlrTokenIndex(int offset, int length) {
		int tokenIndex=-1;
		// set token index to match
		for(Token t:antlrTokens) {
		  if(!isRangeOK(t)) tokenIndex++;
		  else break;
		}
		
		if(DEBUG_VERBOSE)
		System.out.println("getAntlrTokenIndex returns >"+tokenIndex+"<");
		
		return tokenIndex;
	}

	@Override
	public int getTokenOffset() {
		int offset=antlrTokens.get(tokenIndex).getStartIndex();
		return offset;
	}

	@Override
	public int getTokenLength() {
		int length=1+antlrTokens.get(tokenIndex).getStopIndex()
				    -antlrTokens.get(tokenIndex).getStartIndex();
		return length;
	}
	/* ---------------- */
	
	public Map<String,Position> getParserRules() { return parserRules; }
	public Map<String,Position> getLexerRules()  { return lexerRules;  }
	
}
