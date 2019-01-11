package org.github.antlr4ide.builder.tool;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.PlatformUI;
import org.github.antlr4ide.editor.preferences.AntlrPreferenceConstants;

public class AntlrToolConfig {
	
	public static String getToolJar() {
		// TODO use AntlrPreferenceConstants.P_TOOL_DISTRIBUTIONS
		return "C:\\Users\\HenrikSorensen\\git\\antlr4\\tool\\target\\antlr4-4.7.2-SNAPSHOT-complete.jar";
	}
	
	public static List<String> getToolOptions() {
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
		
		out.addAll(getStringParm("-o",AntlrPreferenceConstants.P_TOOL_OUTDIRECTORY));
		out.addAll(getBooleanParm("-listener","-no-listener",AntlrPreferenceConstants.P_TOOL_GENLISTENER));
		out.addAll(getBooleanParm("-visitor","-no-visitor",AntlrPreferenceConstants.P_TOOL_GENVISITOR));
		out.addAll(getStringParm("-encoding",AntlrPreferenceConstants.P_TOOL_ENCODING));
		out.addAll(getStringParm("-lib",AntlrPreferenceConstants.P_TOOL_LIB));
		
		//TODO -package, depends on grammar file itself.
		
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
