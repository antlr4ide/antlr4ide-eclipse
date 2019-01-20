package org.github.antlr4ide.editor.preferences;

import java.util.HashMap;
import java.util.Map;

import org.antlr.parser.antlr4.ANTLRv4Lexer;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;

public class AntlrPreferenceSyntaxHighLighting extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
	private FieldEditor fields[];
	
	private static Map<Integer,String> hiliteElements ;  // default from ANTLRv4Scanner.java
	static {
		   hiliteElements = new HashMap<Integer, String>();
		   // Statements
		   hiliteElements.put(ANTLRv4Lexer.FRAGMENT, "Lexer Fragment");
		   hiliteElements.put(ANTLRv4Lexer.CHANNELS, "Lexer Channels");
		   hiliteElements.put(ANTLRv4Lexer.PARSER,   "Parser");
		   hiliteElements.put(ANTLRv4Lexer.LEXER,    "Lexer");
		   hiliteElements.put(ANTLRv4Lexer.OPTIONS,  "Options");
		   hiliteElements.put(ANTLRv4Lexer.TOKENS,   "Tokens");
		   hiliteElements.put(ANTLRv4Lexer.GRAMMAR,  "Grammar");
		   hiliteElements.put(ANTLRv4Lexer.IMPORT,   "Import");

		   // Constants
		   hiliteElements.put(ANTLRv4Lexer.INT,            "Interger");
		   hiliteElements.put(ANTLRv4Lexer.STRING_LITERAL, "Strings");
		   
		   // Comments
		   hiliteElements.put(ANTLRv4Lexer.DOC_COMMENT,   "Doc Comment");
		   hiliteElements.put(ANTLRv4Lexer.BLOCK_COMMENT, "Block Comment");
		   hiliteElements.put(ANTLRv4Lexer.LINE_COMMENT,  "Line Comment");
		   
		   // References
		   hiliteElements.put(ANTLRv4Lexer.TOKEN_REF, "Token");
		   hiliteElements.put(ANTLRv4Lexer.RULE_REF,  "Rule");

		}
	
	
	
	public AntlrPreferenceSyntaxHighLighting() {
		// System.out.println("AntlrPreferenceSyntaxHighLighting");
	}
	@Override
	public void init(IWorkbench workbench) { 
		// System.out.println("AntlrPreferencePageFolding - init " );
		setPreferenceStore(PlatformUI.getPreferenceStore());
		setDescription("ANTLR Syntax Coloring");
	}

	@Override
	protected void createFieldEditors() {
		// System.out.println("AntlrPreferenceSyntaxHighLighting - createFieldEditors " );
		fields= new FieldEditor[] { 
		 new CheckBoxEditor(AntlrPreferenceConstants.P_SYNTAXCOLOR_ENABLED, "Enable Syntax Coloring", getFieldEditorParent())
/*		,new StringLabel(AntlrPreferenceConstants.P_FOLDING_LABEL_01, "Initially fold these elements:", getFieldEditorParent())
		,new CheckBoxEditor(AntlrPreferenceConstants.P_FOLDING_COMMENTS, "Comments (tbd)", getFieldEditorParent())
		,new CheckBoxEditor(AntlrPreferenceConstants.P_FOLDING_OPTIONS, "Options (tbd)", getFieldEditorParent())
		,new CheckBoxEditor(AntlrPreferenceConstants.P_FOLDING_TOKENS, "Tokens (tbd)", getFieldEditorParent())
		,new CheckBoxEditor(AntlrPreferenceConstants.P_FOLDING_GRAMMAR_ACTION, "Actions (tbd)", getFieldEditorParent())
		,new CheckBoxEditor(AntlrPreferenceConstants.P_FOLDING_PARSER_RULE, "Parser Rules", getFieldEditorParent())
		,new CheckBoxEditor(AntlrPreferenceConstants.P_FOLDING_LEXER_RULE, "Lexer Rules (tbd)", getFieldEditorParent())
		,new CheckBoxEditor(AntlrPreferenceConstants.P_FOLDING_LEXER_MODE, "Lexer Modes", getFieldEditorParent())
		,new CheckBoxEditor(AntlrPreferenceConstants.P_FOLDING_RULE_ACTION, "Rule actions (tbd)", getFieldEditorParent())
*/		};
		
		for(FieldEditor f: fields) { addField(f); }
		
		boolean val=getPreferenceStore().getBoolean(AntlrPreferenceConstants.P_SYNTAXCOLOR_ENABLED);
		updateFields(val);
		
		/*
		 * Add SelectionAdaptor to the main checkbox to control whether sub controls are enabled or not 
		 */
		Button b=(Button) ((CheckBoxEditor) fields[0]).getCheckBox();
		b.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				boolean val=((Button) e.getSource()).getSelection();
				updateFields(val);
			}
		});
	}

	public void updateFields(boolean val) {
		// only enable/disable checkboxes
		for (int i = 2; i < fields.length; i++) {
			((CheckBoxEditor) fields[i]).getCheckBox().setEnabled(val);
		}
	}
	
	public class StringLabel extends StringFieldEditor {
		
	    /**
	     * Creates a string field editor of unlimited width.
	     * Use the method <code>setTextLimit</code> to limit the text.
	     *
	     * @param name the name of the preference this field editor works on
	     * @param labelText the label text of the field editor
	     * @param parent the parent of the field editor's control
	     */
	    public StringLabel(String name, String labelText, Composite parent) {
	        super(name, labelText, 0, parent);
	    }

	    @Override
	    public int getNumberOfControls() { return 1; };
	    
	    @Override
		protected void doFillIntoGrid(Composite parent, int numColumns) {
	        getLabelControl(parent);
	    }
	    
	    @Override
	    protected boolean checkState() { return true; }

	    @Override
		protected void doLoadDefault() {}
		
		@Override
		protected void doLoad() {} 
		
		@Override
		protected void doStore() {}
	    
	}
	
	
	/**
	 * Wrapper class allowing the CheckBox control to be returned.
	 */
	public class CheckBoxEditor extends BooleanFieldEditor {
		private Button theCheckBox;

		public CheckBoxEditor(String propertyName, String labelText, Composite parent) {
			super(propertyName,labelText,parent);
		}

		public Button getCheckBox() { return theCheckBox; }
		
		protected void doFillIntoGrid(Composite parent, int numColumns) {
			super.doFillIntoGrid(parent, numColumns);
			theCheckBox=getChangeControl(parent);
		}
	}
	
}
