package org.github.antlr4ide.builder.tool;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
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
		try {
			command.add(config.getAttribute(LaunchConstants.GRAMMAR, "")); // name of grammar
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		
		ProcessBuilder toolProcess=new ProcessBuilder(command);
		
		// TBD: For launch Set working directory to path of grammar file
		IWorkspace workspace = ResourcesPlugin.getWorkspace(); 
		File workspaceDirectory = workspace.getRoot().getLocation().toFile();
		toolProcess.directory(workspaceDirectory);
//		Redirect r= Redirect.to(file);
//		toolProcess.redirectOutput(destination)
		
		return toolProcess;
    }
      

    /*
     * Called from Eclipse project contexts, like the project builder.
     * Paths
     * - No output path: output is same as location of grammar file
     * - output path: output path is root. If package, output path with have package as directory
     */
    public static ProcessBuilder getProcessBuilder(IFile file, AntlrGrammarInfo grammarInfo) {
		// Create Launch for file
		List<String> command=new ArrayList<>();
		command.add("java");
		command.add("-jar");
		command.add(AntlrToolConfig.getToolJar());
		command.addAll(AntlrToolConfig.getToolOptions(grammarInfo));
//		command.add(file.getProjectRelativePath().toOSString()); // name of grammar relative to project root
		command.add(file.getName()); // name of grammar relative to project root
		
		ProcessBuilder toolProcess=new ProcessBuilder(command);
		// Set working directory to project path of grammar file
		File workDirectory = file.getLocation().toFile().getParentFile();  
//		File workDirectory = file.getProject().getLocation().toFile();
		toolProcess.directory(workDirectory);
//		Redirect r= Redirect.to(file);
//		toolProcess.redirectOutput(destination)
		
		return toolProcess;
    }

    
}
