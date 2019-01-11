package org.github.antlr4ide.builder.tool;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.ui.console.IOConsoleOutputStream;
import org.github.antlr4ide.console.AntlrConsoleFactory;

public class AntlrToolJob extends Job {
	private IFile file;
	private File ioFile; // java.io.File

      public AntlrToolJob(IFile file) {
	         super("AntlrTool Job");
	         this.setPriority(BUILD);
	         this.file=file;
	         this.ioFile=null;
	}
      public AntlrToolJob(File ioFile) {
	         super("AntlrTool Job");
	         this.setPriority(BUILD);
	         this.file=null;
	         this.ioFile=ioFile;
	}
    
    private String getFileName() {
    	if(this.file!=null) return file.getName();
    	return this.ioFile.getName();
    }
      
    public ProcessBuilder getProcessBuilder() {
		// Create Launch for file
		List<String> command=new ArrayList<>();
		command.add("java");
		command.add("-jar");
		command.add(AntlrToolConfig.getToolJar());
		command.addAll(AntlrToolConfig.getToolOptions());
//		command.add("name of grammar");
			
		ProcessBuilder toolProcess=new ProcessBuilder(command);
//		Redirect r= Redirect.to(file);
//		toolProcess.redirectOutput(destination)
		
		return toolProcess;
    	
    }
    
	public IStatus run(IProgressMonitor monitor) {
         System.out.println("This is a job ("+getFileName()+")");
         // Send message to Antlr Tool Console
         IOConsoleOutputStream toolout = AntlrConsoleFactory.getConsole().newOutputStream();
         try {
			toolout.write("TOOL - tbd - "+getFileName()+" ... \n");
			ProcessBuilder toolProcess=getProcessBuilder();
			toolProcess.inheritIO();
			
			toolout.write(toolProcess.command().toString()+"\n");
			
//			Redirect r= Redirect.to(file);
//			toolProcess.redirectOutput(destination)
			toolProcess.start();

			toolout.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
         return Status.OK_STATUS;
      }
   }