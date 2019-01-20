package org.github.antlr4ide.builder.tool;

import java.io.File;
import java.io.IOException;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate;
import org.eclipse.debug.core.model.IProcess;
import org.github.antlr4ide.editor.antlr.AntlrGrammarInfo;
import org.github.antlr4ide.editor.antlr.LexerHelper;


public class AntlrToolLauncher implements ILaunchConfigurationDelegate {

	@Override
	public void launch(ILaunchConfiguration config, String mode, ILaunch launch, IProgressMonitor progress)
			throws CoreException {
		System.out.println("AntlrToolLauncher - launch - ILaunchConfiguration " +config.getAttributes());
		
		AntlrGrammarInfo info=new AntlrGrammarInfo();
		LexerHelper lexerhelper = new LexerHelper(info);
		String fileName=config.getAttribute(LaunchConstants.GRAMMAR, ""); // name of grammar file
		File file=new File(fileName);
		lexerhelper.scan(file);

		
		progress.worked(10);
		try {
			IProcess process=DebugPlugin.newProcess(launch, AntlrToolProcessBuilder.getProcessBuilder(config,info).start(), "Antlr Tool");			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

