package org.github.antlr4ide.builder.tool;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jdt.launching.IRuntimeClasspathEntry;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.JavaRuntime;


public class AntlrToolLauncher implements ILaunchConfigurationDelegate {

//    JarFile jf = new JarFile(jar);
//
//    List<URL> urls = new ArrayList<URL>(5);
//    urls.add(jar.toURI().toURL());
//    Manifest mf = jf.getManifest(); // if jar has a class-path in manfist add it's entries
//    if (mf !=
//            null) {
//        String cp =
//                mf.getMainAttributes().getValue("class-path");
//        if (cp !=
//                null) {
//            for (String cpe : cp.split("\\s+")) {
//                File lib =
//                        new File(jar.getParentFile(), cpe);
//                urls.add(lib.toURI().toURL());
//            }
//        }
//    }
//    ClassLoader cl =
//            new URLClassLoader(urls.toArray(new URL[urls.size()]), classLoader);	
	
	private static final String nnaammee = "org.github.Antlr4.tool";  // must match plugin
	protected IVMInstall jre;

	public void StartAction(IVMInstall vm) {
		//super(vm.getName());
		jre = vm;
	}

	public void crxx() throws CoreException {
	
//	Creating a Launch Configuration
//
//	The first step is to create an instance of a launch configuration, used for launching local Java applications.
//
    ILaunchManager manager = DebugPlugin.getDefault().getLaunchManager();
	ILaunchConfigurationType type =  manager.getLaunchConfigurationType(IJavaLaunchConfigurationConstants.ID_JAVA_APPLICATION);
	ILaunchConfiguration[] configurations =  manager.getLaunchConfigurations(type);
	
	   for (int i = 0; i < configurations.length; i++) {
	      ILaunchConfiguration configuration = configurations[i];
	      if (configuration.getName().equals("Antlr4")) {
	         configuration.delete();
	         break;
	      }
	   }
	ILaunchConfigurationWorkingCopy workingCopy = type.newInstance(null, "Antlr4");
	// specify a JRE
	workingCopy.setAttribute(IJavaLaunchConfigurationConstants.ATTR_VM_INSTALL_NAME, jre.getName());
	workingCopy.setAttribute(IJavaLaunchConfigurationConstants.ATTR_VM_INSTALL_TYPE, jre.getVMInstallType().getId());
	
	// specify main type and program arguments
	workingCopy.setAttribute(IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME, "org.apache.catalina.startup.Bootstrap");
	workingCopy.setAttribute(IJavaLaunchConfigurationConstants.ATTR_PROGRAM_ARGUMENTS, "start");
	
	// specify classpath
	File jdkHome = jre.getInstallLocation();
	IPath toolsPath = new Path(jdkHome.getAbsolutePath()).append("lib").append("tools.jar");
	IRuntimeClasspathEntry toolsEntry = JavaRuntime.newArchiveRuntimeClasspathEntry(toolsPath);
	toolsEntry.setClasspathProperty(IRuntimeClasspathEntry.USER_CLASSES);
	
	IPath bootstrapPath = new Path("TOMCAT_HOME").append("bin").append("bootstrap.jar");
	IRuntimeClasspathEntry bootstrapEntry = JavaRuntime.newVariableRuntimeClasspathEntry(bootstrapPath);
	bootstrapEntry.setClasspathProperty(IRuntimeClasspathEntry.USER_CLASSES);
	
	IPath systemLibsPath = new Path(JavaRuntime.JRE_CONTAINER);
	IRuntimeClasspathEntry systemLibsEntry = JavaRuntime.newRuntimeContainerClasspathEntry(systemLibsPath, IRuntimeClasspathEntry.STANDARD_CLASSES);
	
	List classpath = new ArrayList();
	classpath.add(toolsEntry.getMemento());
	classpath.add(bootstrapEntry.getMemento());
	classpath.add(systemLibsEntry.getMemento());
	
	workingCopy.setAttribute(IJavaLaunchConfigurationConstants.ATTR_CLASSPATH, classpath);
	workingCopy.setAttribute(IJavaLaunchConfigurationConstants.ATTR_DEFAULT_CLASSPATH, false);
	
	// specify System properties
	workingCopy.setAttribute(IJavaLaunchConfigurationConstants.ATTR_VM_ARGUMENTS, "-Djava.endorsed.dirs=\"..\\common\\endorsed\" -Dcatalina.base=\"..\" -Dcatalina.home=\"..\" -Djava.io.tmpdir=\"..\\temp\"");
	
	// specify working diretory
	File workingDir = JavaCore.getClasspathVariable("TOMCAT_HOME").append("bin").toFile();
	workingCopy.setAttribute(IJavaLaunchConfigurationConstants.ATTR_WORKING_DIRECTORY, workingDir.getAbsolutePath());
	
	// save and launch
	ILaunchConfiguration configuration = workingCopy.doSave();
//	DebugUITools.launch(configuration, ILaunchManager.RUN_MODE);
	
	
	}
//
	@Override
	public void launch(ILaunchConfiguration arg0, String arg1, ILaunch arg2, IProgressMonitor arg3)
			throws CoreException {
		//arg0.getWorkingCopy();
		System.out.println("AntlrToolLauncher - launch - ILaunchConfiguration " +arg0.getAttributes());
	}

	
}

