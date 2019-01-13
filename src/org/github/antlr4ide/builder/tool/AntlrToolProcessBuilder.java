package org.github.antlr4ide.builder.tool;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.github.antlr4ide.editor.antlr.AntlrGrammarInfo;

public class AntlrToolProcessBuilder {

    /*
     * This is called from Launch contexts.
     */
    public static ProcessBuilder getProcessBuilder(ILaunchConfiguration config, AntlrGrammarInfo grammarInfo) {
		// Create Launch for file
		List<String> command=new ArrayList<>();
		command.add("java");
		command.add("-jar");
		command.add(AntlrToolConfig.getToolJar());
		command.addAll(AntlrToolConfig.getToolOptions(grammarInfo));
			
		ProcessBuilder toolProcess=new ProcessBuilder(command);
//		Redirect r= Redirect.to(file);
//		toolProcess.redirectOutput(destination)
		
		return toolProcess;
    }
      

    /*
     * Called from Eclipse project contexts, like the project builder.
     */
    public static ProcessBuilder getProcessBuilder(IFile file, AntlrGrammarInfo grammarInfo) {
		// Create Launch for file
		List<String> command=new ArrayList<>();
		command.add("java");
		command.add("-jar");
		command.add(AntlrToolConfig.getToolJar());
		command.addAll(AntlrToolConfig.getToolOptions(grammarInfo));
			
		ProcessBuilder toolProcess=new ProcessBuilder(command);
//		Redirect r= Redirect.to(file);
//		toolProcess.redirectOutput(destination)
		
		return toolProcess;
    }

    
}
