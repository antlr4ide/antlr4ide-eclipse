package org.github.antlr4ide.editor.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
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
		System.out.println("AntlrFoldingPreferencePage");
	}
	@Override
	public void init(IWorkbench workbench) { 
		System.out.println("AntlrFoldingPreferencePage - init " );
		setPreferenceStore(PlatformUI.getPreferenceStore());
		setDescription("ANTLR Folding Configuration");
//		PlatformUI.getPreferenceStore().addPropertyChangeListener(new AntlrFoldingPropertyChangeListener());
	}

	@Override
	protected void createFieldEditors() {
		System.out.println("AntlrFoldingPreferencePage - createFieldEditors " );
		fields= new FieldEditor[] { 
		 new MyBooleanFieldEditor(AntlrToolPreferenceConstants.P_FOLDING_ENABLED, "Enable folding", getFieldEditorParent())
		,new StringLabel(AntlrToolPreferenceConstants.P_FOLDING_LABEL_01, "Initially fold these elements:", getFieldEditorParent())
		,new MyBooleanFieldEditor(AntlrToolPreferenceConstants.P_FOLDING_COMMENTS, "Comments", getFieldEditorParent())
		,new MyBooleanFieldEditor(AntlrToolPreferenceConstants.P_FOLDING_OPTIONS, "Options", getFieldEditorParent())
		,new MyBooleanFieldEditor(AntlrToolPreferenceConstants.P_FOLDING_TOKENS, "Tokens", getFieldEditorParent())
		,new MyBooleanFieldEditor(AntlrToolPreferenceConstants.P_FOLDING_GRAMMAR_ACTION, "Actions", getFieldEditorParent())
		,new MyBooleanFieldEditor(AntlrToolPreferenceConstants.P_FOLDING_PARSER_RULE, "Parser Rules", getFieldEditorParent())
		,new MyBooleanFieldEditor(AntlrToolPreferenceConstants.P_FOLDING_LEXER_RULE, "Lexer Rules", getFieldEditorParent())
		,new MyBooleanFieldEditor(AntlrToolPreferenceConstants.P_FOLDING_RULE_ACTION, "Rule actions", getFieldEditorParent())
		};
		
		for(FieldEditor f: fields) { addField(f); }
		
		/*
		 * Add SelectionAdaptor to the main checkbox to control whether sub controls are enabled or not 
		 */
		Button b=(Button) ((MyBooleanFieldEditor) fields[0]).getCheckBox();
		b.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				Button b=(Button) e.getSource();
				boolean val=b.getSelection();
//				System.out.println("AntlrFoldingPreferencePage - createFieldEditors - widgetSelected "+e.toString() + " source "+e.getSource()+" selection "+ b.getSelection());
				for (int i = 2; i < fields.length; i++) {
					((MyBooleanFieldEditor) fields[i]).getCheckBox().setEnabled(val);
				}
			}
		});
	}

	public class AntlrFoldingPropertyChangeListener implements IPropertyChangeListener {
		public AntlrFoldingPropertyChangeListener() {
			System.out.println("AntlrFoldingPropertyChangeListener " );
		}
		@Override
		public void propertyChange(PropertyChangeEvent e) {
			Composite fieldEditorParent = getFieldEditorParent();
			System.out.println("AntlrFoldingPropertyChangeListener - PropertyChange " + e.getProperty() + " changed from " + e.getOldValue() + " to " + e.getNewValue());
			if (e.getProperty().equals(AntlrToolPreferenceConstants.P_FOLDING_ENABLED)) {
				Boolean val=(Boolean) e.getNewValue();
					// enable/disable all the fields
					for (int i = 2; i < fields.length; i++) {
						fields[i].setEnabled(val, fieldEditorParent);
						fields[i].getLabelControl(fieldEditorParent).setEnabled(val);
					}
			}
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
	public class MyBooleanFieldEditor extends BooleanFieldEditor {
		private Button theCheckBox;

		public MyBooleanFieldEditor(String propertyName, String labelText, Composite parent) {
			super(propertyName,labelText,parent);
		}

		public Button getCheckBox() { return theCheckBox; }
		
		protected void doFillIntoGrid(Composite parent, int numColumns) {
			super.doFillIntoGrid(parent, numColumns);
			theCheckBox=getChangeControl(parent);
		}
	}
	
}
