package org.github.antlr4ide.editor.preferences;

/**
 * Constant definitions for plug-in preferences
 */
public class AntlrToolPreferenceConstants {
	// Tool Constants from com.github.jknack.antlr4ide.generator.ToolOptions.xtend
	// TODO: BUILD_TOOL_PATH = "antlr4.antlrToolPath"
	// TODO: BUILD_ANTLR_TOOLS = "antlr4.antlrRegisteredTools"
	
	public static final String P_VERSIONLIST   = "antlride2_pref_versionListPreference";
	public static final String P_TOOLENABLED = "antlride2_pref_toolenabled";
	public static final String P_DISTRIBUTIONS = "antlride2_pref_dists"; 
	public static final String P_OUTDIRECTORY = "antlride2_pref_outdir";
	
	public static final String P_LIB = "antlr4.libdirectory";
	public static final String P_GENLISTENER = "antlr4.listener";
	public static final String P_GENVISITOR = "antlr4.visitor";
	public static final String P_ENCODING = "antlr4.encoding";
	public static final String P_VMARGS = "antlr4.vmArgs";

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
	
	public static final String P_FOLDING_LABEL_01       = "folding.label_01";
}
