package org.github.antlr4ide.editor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.Token;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.text.rules.*;
import org.eclipse.ui.IEditorInput;
import org.eclipse.jface.text.*;
import org.eclipse.ui.IFileEditorInput;

public class AntlrScanner implements ITokenScanner  {
	private static final boolean DEBUG_VERBOSE = false;

	protected boolean DEBUG;
	private int setRangeCount;

	protected IDocument document;
	protected static Map<Integer, IToken> hilite ;
	
	private ANTLRv4Editor editor;
	
	private IToken fDefaultReturnToken;
	
	private Map<String,Position> parserRules=new HashMap<String, Position>();
	private Map<String,Position> lexerRules=new HashMap<String, Position>();
	private List<String> errorList = new ArrayList<String>();
	
	private List<Token>antlrTokens;
	private int tokenIndex;
	
	private int setRangeOffset;
	private int setRangeLength;

	public AntlrScanner() {
         this(null,false);
	}
	
	public AntlrScanner(ANTLRv4Editor editor, boolean debug) {
		this.setRangeCount=-1;
		this.editor=editor;
		this.DEBUG=debug;
		if (DEBUG) {
			System.out.println(">>> AntlrScanner Editor>" +editor + "< debug>" + debug + "<");
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
		setRangeCount++;
		
		if (DEBUG) {
			System.out.println(">>> AntlrScanner.setRange ["+setRangeCount+"] offset>" + offset + "< length>" + length + "<");
		}
		
		if (DEBUG_VERBOSE||DEBUG) {
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
		
		this.document= document;

//		if(offset==0) {
		// TODO: check if all tokens needs to be rescanned
		antlrTokens=getAllAntlrTokens();
		tokenIndex=getAntlrTokenIndex(offset, length);
//		}
//		else {  // prevent rescanning after marker is set
//			antlrTokens=new ArrayList<Token>();
//			tokenIndex=Integer.MAX_VALUE-1;
//		}
		
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
		if(DEBUG_VERBOSE) { 
			System.out.println(">>> AntlrScanner.isRangeOK >"+token.getStartIndex()
			+","+(token.getStartIndex()< (setRangeOffset+setRangeLength))
		     +","+(token.getStartIndex()>=(setRangeOffset))
		     );
		}

		
		if(token.getStartIndex()< (setRangeOffset+setRangeLength)
		&& token.getStartIndex()>=(setRangeOffset)
		  )
			return true;
		
		return false;
	}

	protected List<Token> getAllAntlrTokens() {
		
		//TODO: Check if document has changed
		
		List<Token>antlrTokens=new ArrayList<Token>();

		
		
		try { errorList.clear();
			LexerHelper lexer=new LexerHelper(parserRules,lexerRules,errorList);
		antlrTokens=(List<Token>) lexer.scanString(this.document.get());
		// TODO: Remove markers if error fixed
		if(errorList.size()>0) {
			// TODO: check error positions are within setRange offset+length
//			// annotate resource with errors.
			System.out.println("Syntax errors:" + errorList);
//			//errorlist format  line:position:token.getStartIndex():token.getStopIndex(): message
			String ss[]=errorList.get(0).split(":");
			int line=Integer.parseInt(ss[0]);
			int start=Integer.parseInt(ss[2]);
			int end=Integer.parseInt(ss[3])+1; //token.getStopIndex() is inclusive. char_end is exclusive so add +1
			System.out.println("["+setRangeCount+"]>>>> Setting Marker START <<<<");
			// Note setting the marker on the file, causes eclipse to invoke the scanner for the impacted line
			IEditorInput input = this.editor.getEditorInput();
			IFile file = ((IFileEditorInput) input).getFile();
			
			// check if marker already exists
			int markerset=0;
			IMarker fileMarkers [] = file.findMarkers(IMarker.PROBLEM, false, 1);
			//IMarker implementation org.eclipse.core.internal.resources.Marker mm;
			for(IMarker m: fileMarkers) {
				if(m.getAttribute(IMarker.SEVERITY,-1)==IMarker.SEVERITY_ERROR
				&& m.getAttribute(IMarker.LINE_NUMBER,-1)==line
				&& m.getAttribute(IMarker.CHAR_START,-1)==start
				&& m.getAttribute(IMarker.CHAR_END,-1)==end) {markerset++; break;}
			}
			if (markerset==0) {
			String mrkAttr[]= {IMarker.SEVERITY,IMarker.LINE_NUMBER,IMarker.MESSAGE,IMarker.CHAR_START,IMarker.CHAR_END};
			Object mrkValue[] = {IMarker.SEVERITY_ERROR,line,ss[4],start,end} ;

			IMarker marker = file.createMarker(IMarker.PROBLEM);
			marker.setAttributes(mrkAttr, mrkValue);
//			marker.setAttribute(IMarker.SEVERITY   , IMarker.SEVERITY_ERROR);
//			marker.setAttribute(IMarker.LINE_NUMBER, line);
//			marker.setAttribute(IMarker.MESSAGE    , ss[4]);
//			marker.setAttribute(IMarker.CHAR_START , start);
//			marker.setAttribute(IMarker.CHAR_END   , end);
			}
			System.out.println("["+setRangeCount+"]>>>> Setting Marker DONE  <<<<");
		}
			
		}
		catch (Exception e) {
			System.out.println("ooops ");
			e.printStackTrace();
		}
		
		if (DEBUG_VERBOSE)
			System.out.println("getAllAntlrTokens returns >"+antlrTokens+"<");
		
		return antlrTokens;
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
