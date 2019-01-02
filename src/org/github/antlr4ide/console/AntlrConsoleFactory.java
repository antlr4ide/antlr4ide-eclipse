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
		System.out.println("AntlrConsoleFactory - openConsole ");
		
		// TODO Auto-generated method stub
	    IConsoleManager consoleManager = ConsolePlugin.getDefault().getConsoleManager();
	    // check if Antlr Console is already added:
	    IOConsole antlrConsole = getConsole(); 
	    if(antlrConsole==null) {
	    	// add Antlr Console to list
	    	antlrConsole = new IOConsole(ANTLR_CONSOLE, ConsolePlugin.getImageDescriptor(ANTLR_CONSOLE_IMAGE));
	    	consoleManager.addConsoles( new IConsole[] { antlrConsole } );
	    }
	    consoleManager.showConsoleView( antlrConsole );
	    
//	    testConsole(antlrConsole);
	    
	}

//	private void testConsole(IOConsole antlrConsole) {
//		System.out.println("AntlrConsoleFactory - testConsole ");
//		IOConsoleOutputStream out = antlrConsole.newOutputStream();
//		try {
//			out.write("TESTING\n");
//			out.setColor(IANTLRv4ColorConstants.STMTrgb);
//			out.write("TESTING - DIFFERENT COLOR\n");
//			out.flush();
//			out.close();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	
	public IOConsole getConsole() {
		System.out.println("AntlrConsoleFactory - getConsole ");
	    for(IConsole console:ConsolePlugin.getDefault().getConsoleManager().getConsoles()) {
	    	if(console.getName().equals(ANTLR_CONSOLE)) { return (IOConsole) console; }
	    }
	    return null;
	}
}
