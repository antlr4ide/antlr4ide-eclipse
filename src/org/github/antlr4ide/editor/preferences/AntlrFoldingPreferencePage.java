package org.github.antlr4ide.editor.preferences;

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

public class AntlrFoldingPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
	private FieldEditor fields[];
	public AntlrFoldingPreferencePage() {
		// System.out.println("AntlrFoldingPreferencePage");
	}
	@Override
	public void init(IWorkbench workbench) { 
		// System.out.println("AntlrFoldingPreferencePage - init " );
		setPreferenceStore(PlatformUI.getPreferenceStore());
		setDescription("ANTLR Folding Configuration");
	}

	@Override
	protected void createFieldEditors() {
		// System.out.println("AntlrFoldingPreferencePage - createFieldEditors " );
		fields= new FieldEditor[] { 
		 new CheckBoxEditor(AntlrToolPreferenceConstants.P_FOLDING_ENABLED, "Enable folding", getFieldEditorParent())
		,new StringLabel(AntlrToolPreferenceConstants.P_FOLDING_LABEL_01, "Initially fold these elements:", getFieldEditorParent())
		,new CheckBoxEditor(AntlrToolPreferenceConstants.P_FOLDING_COMMENTS, "Comments", getFieldEditorParent())
		,new CheckBoxEditor(AntlrToolPreferenceConstants.P_FOLDING_OPTIONS, "Options", getFieldEditorParent())
		,new CheckBoxEditor(AntlrToolPreferenceConstants.P_FOLDING_TOKENS, "Tokens", getFieldEditorParent())
		,new CheckBoxEditor(AntlrToolPreferenceConstants.P_FOLDING_GRAMMAR_ACTION, "Actions", getFieldEditorParent())
		,new CheckBoxEditor(AntlrToolPreferenceConstants.P_FOLDING_PARSER_RULE, "Parser Rules", getFieldEditorParent())
		,new CheckBoxEditor(AntlrToolPreferenceConstants.P_FOLDING_LEXER_RULE, "Lexer Rules", getFieldEditorParent())
		,new CheckBoxEditor(AntlrToolPreferenceConstants.P_FOLDING_LEXER_MODE, "Lexer Modes", getFieldEditorParent())
		,new CheckBoxEditor(AntlrToolPreferenceConstants.P_FOLDING_RULE_ACTION, "Rule actions", getFieldEditorParent())
		};
		
		for(FieldEditor f: fields) { addField(f); }
		
		boolean val=getPreferenceStore().getBoolean(AntlrToolPreferenceConstants.P_FOLDING_ENABLED);
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
