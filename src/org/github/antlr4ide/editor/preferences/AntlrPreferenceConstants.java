package org.github.antlr4ide.editor.preferences;

/**
 * Constant definitions for plug-in preferences
 */
public class AntlrPreferenceConstants {
	// Tool Constants from com.github.jknack.antlr4ide.generator.ToolOptions.xtend
	// TODO: BUILD_TOOL_PATH = "antlr4.antlrToolPath"
	// TODO: BUILD_ANTLR_TOOLS = "antlr4.antlrRegisteredTools"
	
	public static final String P_TOOL_VERSIONLIST   = "antlride2_pref_versionListPreference";
	public static final String P_TOOL_ENABLED = "antlride2_pref_toolenabled";
	public static final String P_TOOL_DISTRIBUTIONS = "antlride2_pref_dists"; 
	public static final String P_TOOL_OUTDIRECTORY = "antlride2_pref_outdir";
	
	public static final String P_TOOL_LIB = "antlr4.libdirectory";
	public static final String P_TOOL_GENLISTENER = "antlr4.listener";
	public static final String P_TOOL_GENVISITOR = "antlr4.visitor";
	public static final String P_TOOL_ENCODING = "antlr4.encoding";
	public static final String P_TOOL_VMARGS = "antlr4.vmArgs";
	public static final String P_TOOL_STORE_PARSETREE = "antlr4.storeparsetree";

	// From com.github.jknack.antlr4ide.ui.preferences.BuilderConfigurationBlock
	// From org.eclipse.xtext.builder.org.eclipse.xtext.builder
	// EclipseOutputConfigurationProvider.OUTPUT_CLEANUP_DERIVED
	// EclipseOutputConfigurationProvider.OUTPUT_DERIVED
	// 	  public static final String OUTPUT_CLEANUP_DERIVED = "cleanupDerived";
	//    public static final String OUTPUT_DERIVED = "derived";
	public static final String P_CLEANUPDERIVED = "antlride2_pref_deletegen";
	public static final String P_MARKDERIVED = "antlride2_pref_markderived";

	
	// Folding Constants from com.github.jknack.antlr4ide.ui.folding.Antlr4FoldingPreferenceStoreInitializer
	public static final String P_FOLDING_ENABLED        = "folding.enabled";
	public static final String P_FOLDING_OPTIONS        = "folding.options";
	public static final String P_FOLDING_TOKENS         = "folding.tokens";
	public static final String P_FOLDING_COMMENTS       = "folding.comments";
	public static final String P_FOLDING_GRAMMAR_ACTION = "folding.grammarAction";
	public static final String P_FOLDING_RULE_ACTION    = "folding.ruleAction";
	public static final String P_FOLDING_PARSER_RULE    = "folding.rule";
	public static final String P_FOLDING_LEXER_RULE     = "folding.lexerRule";
	public static final String P_FOLDING_LEXER_MODE     = "folding.lexerMode";
	
	public static final String P_FOLDING_LABEL_01       = "folding.label_01";
}
