package org.github.antlr4ide.console;

import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleFactory;
import org.eclipse.ui.console.IOConsole;

public class AntlrConsoleFactory implements IConsoleFactory {
	public final static String ANTLR_CONSOLE = "Antlr Tool";  // Must match attribute in plugin.xml
	public final static String ANTLR_CONSOLE_IMAGE = "console.png";  // Must match attribute in plugin.xml
	@Override
	public void openConsole() {
		System.out.println("AntlrConsoleFactory - openConsole ");
		
	    // check if Antlr Console is already added:
	    IOConsole antlrConsole = getConsole(); 
	    ConsolePlugin.getDefault().getConsoleManager().showConsoleView( antlrConsole );
	}

	public static IOConsole getConsole() {
		System.out.println("AntlrConsoleFactory - getConsole ");
	    for(IConsole console:ConsolePlugin.getDefault().getConsoleManager().getConsoles()) {
	    	if(console.getName().equals(ANTLR_CONSOLE)) { return (IOConsole) console; }
	    }
	    // If not found add Antlr Console to list
    	IOConsole antlrConsole = new IOConsole(ANTLR_CONSOLE, ConsolePlugin.getImageDescriptor(ANTLR_CONSOLE_IMAGE));
    	ConsolePlugin.getDefault().getConsoleManager().addConsoles( new IConsole[] { antlrConsole } );
	    
//	    showWelcome(antlrConsole);
	    
	    return antlrConsole;
	}

//	private void showWelcome(IOConsole antlrConsole) {
//	System.out.println("AntlrConsoleFactory - testConsole ");
//	IOConsoleOutputStream out = antlrConsole.newOutputStream();
//	try {
//		out.write("TESTING\n");
//		out.setColor(IANTLRv4ColorConstants.STMTrgb);
//		out.write("TESTING - DIFFERENT COLOR\n");
//		out.flush();
//		out.close();
//	} catch (IOException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	}
//}

}
