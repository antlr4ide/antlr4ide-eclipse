package org.github.antlr4ide.editor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.rules.IPartitionTokenScanner;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

public class ANTLRv4PartitionScanner implements IPartitionTokenScanner {

	public final static String ANTLRv4_DIVISION   = "__antlrv4_div";
	
	private List<String>tokenData=new ArrayList<>();
	private List<Integer>tokenOffset=new ArrayList<>();
	private List<Integer>tokenLength=new ArrayList<>();
	private int tokenIndex;
	

	
	private void clearTokenLists() {
		tokenData.clear();
		tokenOffset.clear();
		tokenLength.clear();
	}
	
	private void addTokenData(String type, int offset, int length) {
        tokenData.add(type);
    	tokenOffset.add(offset);
    	tokenLength.add(length);
	}
	
	/*
	 * IPartitionTokenScanner methods
	 */
	
	public void setRange(IDocument document, int offset, int length) {
		System.out.println(">>> ANTLRv4PartitionScanner.setRange offset>"+offset+"< length>"+length+"<");
		clearTokenLists();
        addTokenData(ANTLRv4_DIVISION,offset,length);
        tokenIndex=-1;
	}

	
	public void setPartialRange(IDocument document, int offset, int length, String contentType, int partitionOffset)
	{ // rescan source from the "partitionOffset" until end of source to establish partition boundaries for all subsequent boundaries 
		System.out.println(">>> ANTLRv4PartitionScanner.setPartialRange offset>"+offset+"<"
				+ " length>"+length+"<"
			    + " contentType>"+contentType+"<"
			    + " partitionOffset>"+partitionOffset+"<"
			    );
		clearTokenLists();
        addTokenData(ANTLRv4_DIVISION,offset,length);
        tokenIndex=-1;
	}
	
	public IToken nextToken() {
		/*
		 * invoked from 
		 *    FastPartitioner.initialize()
		 *    FastPartitioner.documentChanged2()
		 */
		tokenIndex++;
		IToken token;
		if(tokenIndex<tokenData.size()) 
			  token=new Token(tokenData.get(tokenIndex));
		else
			  token=Token.EOF;
		
		System.out.println(">>> ANTLRv4PartitionScanner.nextToken >"+(token.isOther()?token.getData():(token.isEOF()?"<EOF>":"-"))+"<");
		return token;
	}

	public int getTokenOffset() {
		int offset=tokenOffset.get(tokenIndex);
		System.out.println(">>> ANTLRv4PartitionScanner.getTokenOffset >"+offset+"<");
		return offset;
	}

	public int getTokenLength() {
		int length=tokenLength.get(tokenIndex);
		System.out.println(">>> ANTLRv4PartitionScanner.getTokenLength >"+length+"<");
		return length;
	}
	/*
	 * Done with IPartitionTokenScanner methods
	 */
	


}
