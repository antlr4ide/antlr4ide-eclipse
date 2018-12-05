package org.github.antlr4ide.editor.preferences;

import java.io.File;
import java.io.FileInputStream;
import java.util.jar.Attributes;
import java.util.jar.JarInputStream;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.ListEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;

public class AntlrPreferencePageTool extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	private FieldEditor fieldStoreParseTree; 
	
	
	public AntlrPreferencePageTool() {
		super(GRID);
//		setPreferenceStore(activator.getDefault().getPreferenceStore());
		setDescription("ANTLR Tool Configuration");
	}
	
	
	@Override
	public void init(IWorkbench workbench) { 
	    setPreferenceStore(PlatformUI.getPreferenceStore());
}

	@Override
	protected void createFieldEditors() {
		// ------------ Original skeleton:
		//v ANTLR Tool 
		//  [ ] Tool enabled
		//        List of available distributions
		//        Version, Tool Jar path
		//        [ ] Version, Tool Jar path
		//        [ ] Version, Tool Jar path
		//v Options
		//    Directory (-o) _______
		//    Library (-lib) _______
		//    [ ] Generate parse tree listener (-listner)
		//    [ ] Generate parse tree visitors (-visitor)
		//    [ ] Delete generated files when clean build is triggered
		//    [ ] Mark generated files as derived
		//    Encoding (-encoding) _______
		//v VM Arguments
		//    ________________________________________
		// -------

		
		addField(new BooleanFieldEditor(AntlrPreferenceConstants.P_TOOL_ENABLED,	"Tool Enabled",	getFieldEditorParent()));
		addField(new AntlrVersionListEditor(AntlrPreferenceConstants.P_TOOL_DISTRIBUTIONS, "Distributions", getFieldEditorParent()));
		addField(new StringFieldEditor(AntlrPreferenceConstants.P_TOOL_OUTDIRECTORY,"Directory (-o)", getFieldEditorParent()));
		addField(new StringFieldEditor(AntlrPreferenceConstants.P_TOOL_LIB, "Library (-lib)", getFieldEditorParent()));
		
		addField(new BooleanFieldEditor(AntlrPreferenceConstants.P_TOOL_GENLISTENER,    "Generate parse tree listener (-listener)",   getFieldEditorParent()));
		addField(new BooleanFieldEditor(AntlrPreferenceConstants.P_TOOL_GENVISITOR,     "Generate parse tree visitors (-visitor)",    getFieldEditorParent()));
		addField(new BooleanFieldEditor(AntlrPreferenceConstants.P_CLEANUPDERIVED,      "Delete generated files when clean build is triggered",	getFieldEditorParent()));
		addField(new BooleanFieldEditor(AntlrPreferenceConstants.P_MARKDERIVED,         "Mark generated files as derived",	getFieldEditorParent()));
		addField(new StringFieldEditor(AntlrPreferenceConstants.P_TOOL_ENCODING,        "Encoding (-encoding)", getFieldEditorParent()));
		
		addField(new StringFieldEditor(AntlrPreferenceConstants.P_TOOL_VMARGS, "VM Arguments", getFieldEditorParent()));
		// initialize fieldStoreParseTree because AntlrVersionListEditor will enable/disable the field
		fieldStoreParseTree=new StringFieldEditor(AntlrPreferenceConstants.P_TOOL_STORE_PARSETREE, "Folder for stored parsetree (if supported)", getFieldEditorParent());
		fieldStoreParseTree.setEnabled(false, getFieldEditorParent()); // only enable if Tool supports serialization of parseTree
		addField(fieldStoreParseTree);
		}
	
	
	
	private class AntlrVersionListEditor extends ListEditor {
		private String del=",";

		public AntlrVersionListEditor(String label, String content, Composite parent) {
			super(label,content,parent);
		}

		@Override
		protected String createList(String[] items) {
			StringBuffer out=new StringBuffer();
			int m=items.length;
			for(int i=0; i<m; i++) { 
				out.append(items[i]);
				if(i+1<m) out.append(del);
			}
			return out.toString();
		}

		@Override
		protected String getNewInputObject() {
			FileDialog fd=new FileDialog(getFieldEditorParent().getShell(),SWT.OPEN);
			
			String [] filterNames = new String [] {"JAR Files"};
			String [] filterExtensions = new String [] {"*.jar"};
			fd.setFilterExtensions(filterExtensions);
			fd.setFilterNames(filterNames);
			fd.setFileName ("antlr*.jar");
			fd.setText("Select Antlr tool jar");
			
			String toolJarName=fd.open();
			if(toolJarName==null) return null; // Error or cancel in file dialog.

			StringBuffer out=new StringBuffer();
			boolean isOk=verifyTooljar(toolJarName,out);
			if(isOk) return out.toString();
			
			return null;
		}

		@Override
		protected String[] parseString(String stringList) {
			return stringList.split(del);
		}
		
		
		private boolean verifyTooljar(String jarfilename, StringBuffer out)  {
			File f=new File(jarfilename);
			String TOOL = "org.antlr.v4.Tool"; // from ToolOptionsProvider.xtend
			try {
				JarInputStream jarjar = new JarInputStream(new FileInputStream(f));
				Attributes attributes=jarjar.getManifest().getMainAttributes(); 
				
				String version = (String) attributes.get(new Attributes.Name("Implementation-Version"));
				String mainClass = (String) attributes.get(new Attributes.Name("Main-Class"));
				jarjar.close();

				if(mainClass.equals(TOOL)) {
				   out.append(version);
 				   out.append(" ");
				   out.append(f.getAbsolutePath());
				   
				   // enable P_TOOL_STORE_PARSETREE field
				   // TODO: Fix sequence: verifyTooljar invoked when distribution is selected, but this can happen before the field is created.
				   // fieldStoreParseTree.setEnabled(verifyTooljarSerializable(f), getFieldEditorParent());
				   
 				   return true;
				}
			} catch (Exception e) {
				System.out.println("Exception caught, but otherwise ignored.");
				e.printStackTrace();
			}
			
			return false;
		}
		
		private boolean verifyTooljarSerializable (File f) {
			// http://www.javassist.org/html/index.html
			// https://stackoverflow.com/questions/11016092/how-to-load-classes-at-runtime-from-a-folder-or-jar
//			JarFile jarFile = new JarFile(pathToJar);
//			Enumeration<JarEntry> e = jarFile.entries();
//
//			URL[] urls = { new URL("jar:file:" + pathToJar+"!/") };
//			URLClassLoader cl = URLClassLoader.newInstance(urls);
//
//			while (e.hasMoreElements()) {
//			    JarEntry je = e.nextElement();
//			    if(je.isDirectory() || !je.getName().endsWith(".class")){
//			        continue;
//			    }
//			    // -6 because of .class
//			    String className = je.getName().substring(0,je.getName().length()-6);
//			    className = className.replace('/', '.');
//			    Class c = cl.loadClass(className);
//
//			}
			
			
			// http://www.java2s.com/Tutorial/Java/0125__Reflection/ReturnstrueifaclassimplementsSerializableandfalseotherwise.htm
			// check if class implements Serializable  return (Serializable.class.isAssignableFrom(c));
			
			// Check if  
			//    org.antlr.v4.runtime.RuleContext.class
			// implements Serializable
			
			return true;
		}
	}

}
