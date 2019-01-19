package org.github.antlr4ide.builder.tool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.ui.console.IOConsoleOutputStream;
import org.github.antlr4ide.builder.AntlrBuilder;
import org.github.antlr4ide.console.AntlrConsoleFactory;
import org.github.antlr4ide.editor.antlr.AntlrGrammarInfo;

public class AntlrToolJob extends Job {
	private IFile file;
	private AntlrGrammarInfo grammarInfo;

      public AntlrToolJob(IFile file, AntlrGrammarInfo info) {
	         super("AntlrTool Job");
	         this.setPriority(BUILD);
	         this.file=file;
	         this.grammarInfo=info;
         }
      
	public IStatus run(IProgressMonitor monitor) {
         System.out.println("This is a job ("+file.getName()+")");
         // Send message to Antlr Tool Console
         IOConsoleOutputStream toolout = AntlrConsoleFactory.getConsole().newOutputStream();
         try {
			toolout.write("TOOL - tbd - "+file.getName()+" ... \n");
			ProcessBuilder toolProcess=AntlrToolProcessBuilder.getProcessBuilder(this.file,this.grammarInfo);
			File fileStdOut= File.createTempFile("antlrtool-stdout-",".tmp");
			File fileStdErr= File.createTempFile("antlrtool-stderr-",".tmp");
			toolProcess.redirectOutput(fileStdOut);
			toolProcess.redirectError(fileStdErr);
			toolout.write("Working directory "+toolProcess.directory().toString()+"\n");
			toolout.write("Tool command: "+toolProcess.command().toString()+"\n");
			
			Process p=toolProcess.start();
			
			int rc=p.waitFor();

			toolout.write("Tool command completed with return code : "+rc+"\n");

			//copyFileToConsole(toolout, fileStdOut);
			//copyFileToConsole(toolout, fileStdErr);
			if(rc==1)
				processGrammarErrors(toolout, fileStdErr);
			
			fileStdErr.delete();
			fileStdOut.delete();
			
			if(rc==0)
			  getGrammarDependents(toolout, toolProcess);
			
			
			toolout.close();
			
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
         return Status.OK_STATUS;
      }
	
//	private void copyFileToConsole(IOConsoleOutputStream console, File file  ) throws IOException {
//		FileReader fr=new FileReader(file);
//		BufferedReader br = new BufferedReader(fr);
//		String line = br.readLine();
//		while (line != null) {
//		   console.write(line);
//		   console.write("\n");
//		   line = br.readLine();
//		}
//		fr.close();
//		br.close();
//	}
	
	
	private void processGrammarErrors(IOConsoleOutputStream console, File fileStdErr) throws IOException {
		/*
		 * Format of messages from Antlr Tool:
		 * error(114): ANTLRv4Parser.g4:46:18: cannot find tokens file .\ANTLRv4Lexer.tokens
		 * warning(125): ANTLRv4Parser.g4:50:5: implicit definition of token DOC_COMMENT in parser
		 * warning(125): ANTLRv4Parser.g4:50:41: implicit definition of token SEMI in parser
		 * warning(125): ANTLRv4Parser.g4:54:6: implicit definition of token LEXER in parser
		 * warning(125): ANTLRv4Parser.g4:54:12: implicit definition of token GRAMMAR in parser
		 * error(5):  directory not found: ../generated/com/csat/antlr
		 * 
		 */
		FileReader fr=new FileReader(fileStdErr);
		BufferedReader br = new BufferedReader(fr);
		String line = br.readLine();
		while (line != null) {
		   console.write(line);
		   console.write("\n");
		   
		   addMarker(this.file,line);
		   
		   line = br.readLine();
		}
		fr.close();
		br.close();
		
	}
	
	
	private void addMarker(IFile file, String msgLine) {
		try { //TODO: Locate file in a active editor to make sure the editor is updated.
			
			// errorlist format >error(no): file:line:col: message<
			String ss[] = msgLine.split(":");

			//int start = Integer.parseInt(ss[3]);
			//int end = Integer.parseInt(ss[3]) + 1; // token.getStopIndex() is inclusive. char_end is exclusive so add +1
			int severity; 
			     if(ss[0].startsWith("warn"))  severity=IMarker.SEVERITY_WARNING;
			else if(ss[0].startsWith("error")) severity=IMarker.SEVERITY_ERROR;
			else return; // skip if not warn or error 
			
			int line; 
			String msg;
			try {
			line = Integer.parseInt(ss[2]);
			msg= ss[0]+":"+ss[4];
			} catch (NumberFormatException e ) {
				line=1;
				msg= msgLine;
			}
			

			String mrkAttr[] = { IMarker.SEVERITY, IMarker.LINE_NUMBER, IMarker.MESSAGE};
			Object mrkValue[] = { severity, line, msg };
			IMarker marker = file.createMarker(AntlrBuilder.MARKER_TYPE);
			marker.setAttributes(mrkAttr, mrkValue);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}
	
	private List<String> getGrammarDependents(IOConsoleOutputStream console, ProcessBuilder pb) throws InterruptedException, IOException {
		// execute the tool with the -depend option
		List <String> commands=pb.command();
		int i = commands.size();
		commands.add(i-1,"-depend");  // insert -depend option into commands
		
		File fileStdOut= File.createTempFile("antlrtool-stdout-",".tmp"); //("tool-stdout.temp.001"); //TODO Capture -depends output
		pb.redirectOutput(fileStdOut);
		int rc=pb.start().waitFor();

		if(rc==0) return processToolStdout(fileStdOut);
		fileStdOut.delete();
		
		return null;

	}
	
	private List<String> processToolStdout(File file) throws IOException {
		// if -depend is specified as parameter the fileStdout will contain the list of files generated
		
/*
 * ..\generated\org\antlr\parser\antlr4\ANTLRv4Lexer.java : ANTLRv4Lexer.g4
 * ..\generated\org\antlr\parser\antlr4\ANTLRv4Lexer.tokens : ANTLRv4Lexer.g4
 * ..\generated\org\antlr\parser\antlr4\ANTLRv4LexerListener.java : ANTLRv4Lexer.g4
 * ..\generated\org\antlr\parser\antlr4\ANTLRv4LexerBaseListener.java : ANTLRv4Lexer.g4
 * ..\generated\org\antlr\parser\antlr4\ANTLRv4LexerVisitor.java : ANTLRv4Lexer.g4
 * ..\generated\org\antlr\parser\antlr4\ANTLRv4LexerBaseVisitor.java : ANTLRv4Lexer.g4
 * 
 * 
 * ANTLRv4Parser.g4: ..\generated\org\antlr\parser\antlr4\ANTLRv4Lexer.tokens
 * ..\generated\org\antlr\parser\antlr4\ANTLRv4Parser.java : ANTLRv4Parser.g4
 * ..\generated\org\antlr\parser\antlr4\ANTLRv4Parser.tokens : ANTLRv4Parser.g4
 * ..\generated\org\antlr\parser\antlr4\ANTLRv4ParserListener.java : ANTLRv4Parser.g4
 * ..\generated\org\antlr\parser\antlr4\ANTLRv4ParserBaseListener.java : ANTLRv4Parser.g4
 * ..\generated\org\antlr\parser\antlr4\ANTLRv4ParserVisitor.java : ANTLRv4Parser.g4
 * ..\generated\org\antlr\parser\antlr4\ANTLRv4ParserBaseVisitor.java : ANTLRv4Parser.g4
 * 	
 */
		
		List<String> out=new ArrayList<>();
		FileReader fr=new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
		String line = br.readLine();
		while (line != null) {
		   out.add(line);
		   line = br.readLine();
		}
		fr.close();
		br.close();
		
		return out;
	}
   }