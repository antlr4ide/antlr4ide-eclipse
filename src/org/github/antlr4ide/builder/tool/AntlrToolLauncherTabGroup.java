package org.github.antlr4ide.builder.tool;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTabGroup;
import org.eclipse.debug.ui.CommonTab;
import org.eclipse.debug.ui.EnvironmentTab;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.debug.ui.ILaunchConfigurationTab;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;

public class AntlrToolLauncherTabGroup  extends AbstractLaunchConfigurationTabGroup {

	@Override
	public void createTabs(ILaunchConfigurationDialog dialog, String mode) {
		// TODO Auto-generated method stub
	    AbstractLaunchConfigurationTab _mainTab = new MainTab();
	    EnvironmentTab _environmentTab = new EnvironmentTab();
	    CommonTab _commonTab = new CommonTab();
	    this.setTabs(new ILaunchConfigurationTab[] { _mainTab, _environmentTab, _commonTab });		
	}

	
	  /*
	   * Based on
	   *  com.github.jknack.antlr4ide.ui.launch.MainTab
	   */
	public class MainTab extends AbstractLaunchConfigurationTab {
		  private Text fGrammarText;
		  private Text fArgsText;
		  private Text fVmArgsText;
		  private ModifyListener modifyListener = new ModifyListener() {
			    @Override
			    public void modifyText(final ModifyEvent event) {
			      MainTab.this.updateLaunchConfigurationDialog();
			    }
			  };

		@Override
		public void createControl(Composite parent) {
		    Font _font = parent.getFont();
		    final Composite comp = this.createComposite(parent, _font, 1, 1, GridData.FILL_BOTH);
		    this.createVerticalSpacer(comp, 2);
		    Text _createSection = this.createSection(comp, "Grammar", (SWT.SINGLE | SWT.BORDER), 1, true);
		    this.fGrammarText = _createSection;
		    this.createVerticalSpacer(comp, 8);
		    //TODO: Use the fields from the tool preference page instead of generic text box
		    Text _createSection_1 = this.createSection(comp, "Arguments", ((SWT.MULTI | SWT.WRAP) | SWT.BORDER), 5, false);
		    this.fArgsText = _createSection_1;
		    Text _createSection_2 = this.createSection(comp, "VM Arguments", ((SWT.MULTI | SWT.WRAP) | SWT.BORDER), 5, false);
		    this.fVmArgsText = _createSection_2;
		    this.setControl(comp);
		}

		@Override
		public void setDefaults(ILaunchConfigurationWorkingCopy workingCopy) {
//		    final IFile file = this.getContext();
//		    if (file!=null) {
//		      IPath _fullPath = file.getFullPath();
//		      String _oSString = _fullPath.toOSString();
//		      workingCopy.setAttribute(LaunchConstants.GRAMMAR, _oSString);
//		      ToolOptions _options = this.optionsProvider.options(file);
//		      List<String> _defaults = _options.defaults();
//		      String _join = IterableExtensions.join(_defaults, " ");
//		      workingCopy.setAttribute(LaunchConstants.ARGUMENTS, _join);
//		    }		
		}

		@Override
		public void initializeFrom(ILaunchConfiguration config) {
		    try {
		      this.fGrammarText.setText(config.getAttribute(LaunchConstants.GRAMMAR, ""));
		      this.fArgsText.setText(config.getAttribute(LaunchConstants.ARGUMENTS, ""));
		      this.fVmArgsText.setText(config.getAttribute(LaunchConstants.VM_ARGUMENTS, ""));
	    } catch (CoreException e) {
	    	e.printStackTrace();
	    }
      }

		@Override
		public void performApply(ILaunchConfigurationWorkingCopy workingCopy) {
		    workingCopy.setAttribute(LaunchConstants.GRAMMAR, this.fGrammarText.getText());
		    workingCopy.setAttribute(LaunchConstants.ARGUMENTS, this.fArgsText.getText());
		    workingCopy.setAttribute(LaunchConstants.VM_ARGUMENTS, this.fVmArgsText.getText());
		}

		  @Override
		  public String getName() {
		    return " Tool    ";
		  }
		  
		  /*
		   * HELPER FUNCTIONS
		   * Based on
		   *  com.github.jknack.antlr4ide.ui.launch.MainTab
		   */
		  
		  
		  protected Composite createComposite(final Composite parent, final Font font, final int columns, final int hspan, final int fill) {
			  Composite _composite = new Composite(parent, SWT.NONE);
			  GridLayout _gridLayout = new GridLayout(columns, false);
			  _gridLayout.verticalSpacing=0;
			  GridData _gridData = new GridData(fill);
	          _gridData.horizontalSpan = hspan;
	          _gridData.grabExcessHorizontalSpace = true;
			  _composite.setLayout(_gridLayout);
			  _composite.setLayoutData(_gridData);
			  
			  return _composite;
		  }
		  
		  protected Text createSection(final Composite parent, final String title, final int style, final int rows, final boolean btn) { 
			    final Font font = parent.getFont();
			    final Group group = new Group(parent, SWT.NONE);
			    group.setText(title);
			    GridData gd = new GridData(GridData.FILL_HORIZONTAL);
			    group.setLayoutData(gd);
			    final GridLayout layout = new GridLayout();
			    layout.numColumns = 3;
			    group.setLayout(layout);
			    group.setFont(font);
			    final Text text = new Text(group, style);
			    GridData _gridData = new GridData(GridData.FILL_HORIZONTAL);
			    gd = _gridData;
			    int _lineHeight = text.getLineHeight();
			    int _multiply = (rows * _lineHeight);
			    gd.heightHint = _multiply;
			    text.setLayoutData(gd);
			    text.setFont(font);
			    text.addModifyListener(this.modifyListener);
//			    if (btn) {
//			      final Button varButton = new Button(group, SWT.PUSH);
//			      varButton.setText("Variables...");
//			      VariableButtonListener _variableButtonListener = new VariableButtonListener(text, this);
//			      varButton.addSelectionListener(_variableButtonListener);
//			      GridData _gridData_1 = new GridData(GridData.END);
//			      gd = _gridData_1;
//			      varButton.setLayoutData(gd);
//			      final Button button = new Button(group, SWT.PUSH);
//			      GridData _gridData_2 = new GridData(GridData.END);
//			      gd = _gridData_2;
//			      button.setLayoutData(gd);
//			      button.setText("Browse...");
//			      final Procedure1<IFile> _function = new Procedure1<IFile>() {
//			        @Override
//			        public void apply(final IFile resource) {
//			          IPath _fullPath = resource.getFullPath();
//			          String _oSString = _fullPath.toOSString();
//			          MainTab.this.fGrammarText.setText(_oSString);
//			        }
//			      };
//			      Widgets.chooseGrammar(button, this.workspaceRoot, _function);
//			    }
			    return text;
		  }
	}
	
}
