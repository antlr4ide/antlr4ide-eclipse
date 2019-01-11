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


public class AntlrToolLauncher implements ILaunchConfigurationDelegate {

	@Override
	public void launch(ILaunchConfiguration config, String mode, ILaunch launch, IProgressMonitor progress)
			throws CoreException {
		//arg0.getWorkingCopy();
		System.out.println("AntlrToolLauncher - launch - ILaunchConfiguration " +config.getAttributes());
		File file = new File (config.getAttribute(LaunchConstants.GRAMMAR, "")); 
		
		AntlrToolJob job=new AntlrToolJob(file);
		progress.worked(10);
		try {
			IProcess process=DebugPlugin.newProcess(launch, job.getProcessBuilder().start(), "Antlr Tool");			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

