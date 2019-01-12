package org.github.antlr4ide.editor.antlr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.text.Position;

public class AntlrGrammarInfo {
	
	// return all the needed meta data from the grammar
	AntlrDocument doc;

	private Map<String,Position> parserRules;
	private Map<String,Position> lexerRules;
	private Map<String,Position> lexerModes;
	private List<String> errorList;

	private String grammarType;

	private String grammarName;

	private String grammarHeaders;

	private String grammarOptions;

	private String grammarDelegates;

	private String grammarTokens;

	private String grammarChannels;

	public AntlrGrammarInfo(AntlrDocument doc) {
		this.doc=doc;
		this.parserRules=new HashMap<String, Position>();
		this.lexerRules=new HashMap<String, Position>();
		this.lexerModes=new HashMap<String, Position>();
		this.errorList = new ArrayList<String>();
		this.grammarType="";
		this.grammarName="";
		this.grammarHeaders="";
		this.grammarOptions="";
		this.grammarDelegates="";
		this.grammarTokens="";
		this.grammarChannels="";
	}

	public Map<String,Position> getParserRules() { return parserRules;}
	public Map<String,Position> getLexerRules()  { return lexerRules; }
	public Map<String, Position> getLexerModes() { return lexerModes; }
	public List<String> getErrorList() { return errorList; }

	public String getGrammarType()     { return grammarType ; }
	public String getGrammarName()     { return grammarName ; }
	public String getGrammarHeaders()  { return grammarHeaders ; }
	public String getGrammarOptions()  { return grammarOptions ; }
	public String getGrammarDelegates(){ return grammarDelegates ; }
	public String getGrammarTokens()   { return grammarTokens; }
	public String getGrammarChannels() { return grammarChannels ; }
	
	public void setGrammarType(String type) { this.grammarType=type; }
	public void setGrammarName(String name) { this.grammarName=name; }
	public void setGrammarHeaders(String headers) { this.grammarHeaders=headers; }
	public void setGrammarOptions(String options) { this.grammarOptions=options; }
	public void setGrammarDelegates(String delegates) { this.grammarDelegates=delegates; }
	public void setGrammarTokens(String tokens) {this.grammarTokens=tokens; }
	public void setGrammarChannels(String channels) { this.grammarChannels=channels; }

}
