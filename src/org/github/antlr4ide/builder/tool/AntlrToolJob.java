package org.github.antlr4ide.builder.tool;

import java.io.IOException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.ui.console.IOConsoleOutputStream;
import org.github.antlr4ide.console.AntlrConsoleFactory;

public class AntlrToolJob extends Job {
	private IFile file;

	      public AntlrToolJob(IFile file) {
		         super("AntlrTool Job");
		         this.setPriority(BUILD);
		         this.file=file;
		}
		public IStatus run(IProgressMonitor monitor) {
	         System.out.println("This is a job ("+file.getName()+")");
	         // Send message to Antlr Tool Console
	         IOConsoleOutputStream toolout = AntlrConsoleFactory.getConsole().newOutputStream();
	         try {
				toolout.write("TOOL - "+file.getName()+" ... \n");
				toolout.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	         return Status.OK_STATUS;
	      }
	   }