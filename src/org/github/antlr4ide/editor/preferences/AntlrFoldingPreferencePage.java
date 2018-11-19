package org.github.antlr4ide.editor.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class AntlrFoldingPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	@Override
	public void init(IWorkbench workbench) {
		setPreferenceStore(workbench.getPreferenceStore());
		setDescription("ANTLR Folding Configuration");
	}

	@Override
	protected void createFieldEditors() {

		addField(new BooleanFieldEditor(AntlrToolPreferenceConstants.P_ENABLED, "Enable folding", getFieldEditorParent()));

		// new Label(parent, SWT.NONE) => [
		// text = "Initially fold these elements:"
		// layoutData = new GridData => [
		// it.verticalAlignment = SWT.BOTTOM
		// verticalSpan = 6
		// ]
		// ]

		addField(new StringLabel(AntlrToolPreferenceConstants.P_LABEL_01, "Initially fold these elements:", getFieldEditorParent()));
		
		addField(new BooleanFieldEditor(AntlrToolPreferenceConstants.P_COMMENTS, "Comments", getFieldEditorParent()));
		addField(new BooleanFieldEditor(AntlrToolPreferenceConstants.P_OPTIONS, "Options", getFieldEditorParent()));
		addField(new BooleanFieldEditor(AntlrToolPreferenceConstants.P_TOKENS, "Tokens", getFieldEditorParent()));
		addField(new BooleanFieldEditor(AntlrToolPreferenceConstants.P_GRAMMAR_ACTION, "Actions", getFieldEditorParent()));
		addField(new BooleanFieldEditor(AntlrToolPreferenceConstants.P_RULE, "Rules", getFieldEditorParent()));
		addField(new BooleanFieldEditor(AntlrToolPreferenceConstants.P_LEXER_RULE, "Lexer Rules", getFieldEditorParent()));
		addField(new BooleanFieldEditor(AntlrToolPreferenceConstants.P_RULE_ACTION, "Rule actions", getFieldEditorParent()));
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
	
}
