package org.github.antlr4ide.console;

import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleFactory;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.IOConsole;

public class AntlrConsoleFactory implements IConsoleFactory {
	public final static String ANTLR_CONSOLE = "Antlr Tool";  // Must match attribute in plugin.xml
	public final static String ANTLR_CONSOLE_IMAGE = "console.png";  // Must match attribute in plugin.xml
	@Override
	public void openConsole() {
		// TODO Auto-generated method stub
	    IConsoleManager consoleManager = ConsolePlugin.getDefault().getConsoleManager();
	    // check if Antlr Console is already added:
	    boolean found=false;
	    IOConsole antlrConsole = null;
	    for(IConsole console:consoleManager.getConsoles()) {
	    	if(console.getName().equals(ANTLR_CONSOLE)) { found=true; antlrConsole= (IOConsole) console; break; }
	    }
	    if(!found) {
	    	// add Antlr Console to list
	    	antlrConsole = new IOConsole(ANTLR_CONSOLE, ConsolePlugin.getImageDescriptor(ANTLR_CONSOLE_IMAGE));
	    	consoleManager.addConsoles( new IConsole[] { antlrConsole } );
	    }
	    consoleManager.showConsoleView( antlrConsole );		
	}

}
