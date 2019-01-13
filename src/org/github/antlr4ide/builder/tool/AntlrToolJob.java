package org.github.antlr4ide.builder.tool;

import java.io.IOException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.IOConsoleOutputStream;
import org.github.antlr4ide.console.AntlrConsoleFactory;
import org.github.antlr4ide.editor.ANTLRv4DocumentProvider;
import org.github.antlr4ide.editor.ANTLRv4Editor;
import org.github.antlr4ide.editor.antlr.AntlrGrammarInfo;

public class AntlrToolJob extends Job {
	private IFile file;
	private AntlrGrammarInfo grammarInfo;

      public AntlrToolJob(IFile file, AntlrGrammarInfo info) {
	         super("AntlrTool Job");
	         this.setPriority(BUILD);
	         this.file=file;
	         this.grammarInfo=info;
	         
	         // get meta info from the grammar
	         // - @header { package ... }
	         // - options { tokensVocab=... }
	         // - import ...
	         System.out.println("AntlrToolJob - file "+file.getName()+ " workbench windows count " + PlatformUI.getWorkbench().getWorkbenchWindowCount());
			 System.out.println("ANTLR Document found parser"
			 		+ " type "+info.getGrammarType()
			 		+ " name " + info.getGrammarName()
			 		+ " options " + info.getGrammarOptions()
			 		+ " header " + info.getGrammarHeaders()
			 		);
         }
      
	public IStatus run(IProgressMonitor monitor) {
         System.out.println("This is a job ("+file.getName()+")");
         // Send message to Antlr Tool Console
         IOConsoleOutputStream toolout = AntlrConsoleFactory.getConsole().newOutputStream();
         try {
			toolout.write("TOOL - tbd - "+file.getName()+" ... \n");
			ProcessBuilder toolProcess=AntlrToolProcessBuilder.getProcessBuilder(this.file,this.grammarInfo);
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