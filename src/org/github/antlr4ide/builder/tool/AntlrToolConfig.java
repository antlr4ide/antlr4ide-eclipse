package org.github.antlr4ide.builder.tool;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.PlatformUI;
import org.github.antlr4ide.editor.antlr.AntlrGrammarInfo;
import org.github.antlr4ide.editor.preferences.AntlrPreferenceConstants;

public class AntlrToolConfig {
	
	public static String getToolJar() {
		// TODO return version ?
		IPreferenceStore ps = PlatformUI.getPreferenceStore();
		
		// format of AntlrPreferenceConstants.P_TOOL_DISTRIBUTIONS
		// see AntlrPreferencePageTool.verifyTooljar
		// ver path, ver path ...
		String toolDistribution=ps.getString(AntlrPreferenceConstants.P_TOOL_DISTRIBUTIONS);
		String toolJars[] =toolDistribution.split(","); 
		String toolJar[] = toolJars[0].split(" ");
		
		return toolJar[1];
	}
	
	// set output directory to include the package location
	public static List<String> getOutputdir(AntlrGrammarInfo grammarInfo) {
		String pdir=grammarInfo.getGrammarHeaderPackageAsPath();
		List<String> out = getStringParm("-o",AntlrPreferenceConstants.P_TOOL_OUTDIRECTORY);
		
		if (!pdir.isEmpty())
		{
			String dir=out.get(1);
			if (dir.endsWith("/")) out.set(1, dir+pdir);
			else out.set(1, dir+"/"+pdir);
		}
		
	   return out;	
	}
	
	// if grammar contain option tokenVocab=lexername include -lib packagedir
	public static List<String> getLibdir(AntlrGrammarInfo grammarInfo) {
		// If preferences are set use it
		List<String> out = getStringParm("-lib",AntlrPreferenceConstants.P_TOOL_LIB);
		if (out.size()>0) return out;
		
		// TODO: Fix assumption that lexer and grammer share same package
		String tokenVocab=grammarInfo.getGrammarOptionsTokenVocab();
		if(tokenVocab.equals("")) return out; // is empty list if P_TOOL_LIB is not set
		
		out = getOutputdir(grammarInfo);
		out.set(0, "-lib"); // set option lib
		
		return out;
	}
	
	
	public static List<String> getToolOptions(AntlrGrammarInfo grammarInfo) {
		ArrayList<String> out=new ArrayList<>();
		
		
		// TODO: Deal with user supplied launch parameters, project parameters, individual file parameters
		
		
//		ANTLR Parser Generator  Version 4.7.1
//		 -o ___              specify output directory where all output is generated
//		 -lib ___            specify location of grammars, tokens files
//		 -atn                generate rule augmented transition network diagrams
//		 -encoding ___       specify grammar file encoding; e.g., euc-jp
//		 -message-format ___ specify output style for messages in antlr, gnu, vs2005
//		 -long-messages      show exception details when available for errors and warnings
//		 -listener           generate parse tree listener (default)
//		 -no-listener        don't generate parse tree listener
//		 -visitor            generate parse tree visitor
//		 -no-visitor         don't generate parse tree visitor (default)
//		 -package ___        specify a package/namespace for the generated code
//		 -depend             generate file dependencies
//		 -D<option>=value    set/override a grammar-level option
//		 -Werror             treat warnings as errors
//		 -XdbgST             launch StringTemplate visualizer on generated code
//		 -XdbgSTWait         wait for STViz to close before continuing
//		 -Xforce-atn         use the ATN simulator for all predictions
//		 -Xlog               dump lots of logging info to antlr-timestamp.log
//		 -Xexact-output-dir  all output goes into -o dir regardless of paths/package		
		
		out.addAll(getOutputdir(grammarInfo));
		out.addAll(getBooleanParm("-listener","-no-listener",AntlrPreferenceConstants.P_TOOL_GENLISTENER));
		out.addAll(getBooleanParm("-visitor","-no-visitor",AntlrPreferenceConstants.P_TOOL_GENVISITOR));
		out.addAll(getStringParm("-encoding",AntlrPreferenceConstants.P_TOOL_ENCODING));
		out.addAll(getLibdir(grammarInfo));
		
		//out.addAll(getString("-package",grammarInfo.getGrammarHeaderPackage())); 
		
		return out;
	}

	private static List<String>getStringParm(String parm, String name) {
		IPreferenceStore ps = PlatformUI.getPreferenceStore();
		String value = ps.getString(name);

		ArrayList<String> out=new ArrayList<>();
		if (value!=null && !value.trim().isEmpty()) {
			out.add(parm);
			out.add(value);
		}
		
		return out;
	};

	private static List<String>getString(String parm, String value) {
		ArrayList<String> out=new ArrayList<>();
		if (value!=null && !value.trim().isEmpty()) {
			out.add(parm);
			out.add(value);
		}
		return out;
	};

	
	private static List<String>getBooleanParm(String parmTrue, String parmFalse, String name) {
		IPreferenceStore ps = PlatformUI.getPreferenceStore();
		Boolean value = ps.getBoolean(name);
		ArrayList<String> out=new ArrayList<>();  
		if (value!=null) {
			if(value) out.add(parmTrue);
			else out.add(parmFalse);
		}
		
		return out;
	};
	
	
}
