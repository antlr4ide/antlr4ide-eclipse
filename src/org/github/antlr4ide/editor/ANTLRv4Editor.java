package org.github.antlr4ide.editor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.IPreferenceChangeListener;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.PreferenceChangeEvent;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.jface.text.source.projection.ProjectionAnnotation;
import org.eclipse.jface.text.source.projection.ProjectionAnnotationModel;
import org.eclipse.jface.text.source.projection.ProjectionSupport;
import org.eclipse.jface.text.source.projection.ProjectionViewer;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.github.antlr4ide.editor.antlr.AntlrDocument;
import org.github.antlr4ide.editor.outliner.AntlrDocOutlineView;
import org.github.antlr4ide.editor.preferences.AntlrPreferenceConstants;


public class ANTLRv4Editor extends TextEditor implements IAdaptable {

	private AntlrDocOutlineView fOutlinePage;
    private ProjectionSupport projectionSupport;


	public ANTLRv4Editor() {
		super();
		setSourceViewerConfiguration(new ANTLRv4Configuration());
		setDocumentProvider(new ANTLRv4DocumentProvider(this));

		// Setup property change listener
		PlatformUI.getPreferenceStore().addPropertyChangeListener(new AntlrFoldingPropertyListener(this));
	}

	@Override
	protected void doSetInput(IEditorInput input) throws CoreException {
		System.out.println("ANTLRv4Editor - doSetInput "+input);
		super.doSetInput(input);
	}

	
//	@Override
//	protected void initializeEditor() {
//		// TODO Auto-generated method stub
//		super.initializeEditor();
//	}
	
	public void dispose() {
		super.dispose();
		if(fOutlinePage!=null) fOutlinePage.dispose();
	}

	/* configure outline adaptor */
	public Object getAdapter(Class required) {
		if (IContentOutlinePage.class.equals(required)) {
			if (fOutlinePage == null) {
				fOutlinePage= new AntlrDocOutlineView(getDocumentProvider(), this);
				if (getEditorInput() != null)
					fOutlinePage.setInput(getEditorInput());
			}
			return fOutlinePage;
		}
		return super.getAdapter(required);
	}



	// Folding: https://www.eclipse.org/articles/Article-Folding-in-Eclipse-Text-Editors/folding.html
	 /* (non-Javadoc)
     * @see org.eclipse.ui.IWorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
     */
    public void createPartControl(Composite parent)
    {   System.out.println("ANTLRv4Editor - createPartControl");

        super.createPartControl(parent);
        ProjectionViewer viewer =(ProjectionViewer)getSourceViewer();
        
        projectionSupport = new ProjectionSupport(viewer,getAnnotationAccess(),getSharedColors());
		projectionSupport.install();
		
		//turn projection mode on
		viewer.doOperation(ProjectionViewer.TOGGLE);
		
		annotationModel = viewer.getProjectionAnnotationModel();
		
		((AntlrDocument) ((ANTLRv4DocumentProvider) getDocumentProvider()).getDoc()).processFolding();
		
    }
	private Annotation[] oldAnnotations;
	private ProjectionAnnotationModel annotationModel;
		
    /* (non-Javadoc)
     * @see org.eclipse.ui.texteditor.AbstractTextEditor#createSourceViewer(org.eclipse.swt.widgets.Composite, org.eclipse.jface.text.source.IVerticalRuler, int)
     */
    protected ISourceViewer createSourceViewer(Composite parent,
            IVerticalRuler ruler, int styles)
    {   System.out.println("ANTLRv4Editor - createSourceViewer");

        ISourceViewer viewer = new ProjectionViewer(parent, ruler, getOverviewRuler(), isOverviewRulerVisible(), styles);

    	// ensure decoration support has been created and configured.
    	getSourceViewerDecorationSupport(viewer);
    	
    	return viewer;
    }


	public void removeFoldingStructure() {
		System.out.println("ANTLRv4Editor - removeFolderStructure - ALL");
		updateFoldingStructure(new ArrayList<>(), false);
	}

	public void removeFoldingStructure(Collection<Position> positions) {
		System.out.println("ANTLRv4Editor - removeFolderStructure - select positions");
		for(Annotation a: oldAnnotations) {
		    if(positions.contains(annotationModel.getPosition(a)))
				annotationModel.removeAnnotation(a);
		}
	}

    public void updateFoldingStructure(Collection<Position> positions, boolean markCollapsed) {
		System.out.println("ANTLRv4Editor - updateFolderStructure - "+positions + " " + markCollapsed);

		Annotation[] annotations = new Annotation[positions.size()];
		
		//this will hold the new annotations along
		//with their corresponding positions
		Map<ProjectionAnnotation,Position> newAnnotations = new HashMap<>();
		
		int i =0;
		for(Position p:positions)
		{
			ProjectionAnnotation annotation = new ProjectionAnnotation();
			newAnnotations.put(annotation,p);
			annotations[i]=annotation;
			if(markCollapsed)
			annotation.markCollapsed();
			i++;
		}
		
		annotationModel.modifyAnnotations(oldAnnotations,newAnnotations,null);
		
		oldAnnotations=annotations;
		
	}	


    /**
     * Receive event when an Editor propery is changed. 
     * Note this class implements both the IPreferenceChangeListener and IPropertyChangeListener
     * because the Eclipse documentation is not entirely clear on the best way to do this.
     * @author HenrikSorensen
     *
     */
	public class AntlrFoldingPropertyListener implements IPreferenceChangeListener, IPropertyChangeListener {
	private ANTLRv4Editor editor;
	public AntlrFoldingPropertyListener(ANTLRv4Editor editor) {
		System.out.println("AntlrFoldingPropertyListener " );
		this.editor=editor;
	}

	@Override
	public void preferenceChange(PreferenceChangeEvent e) {
		System.out.println("AntlrFoldingPropertyListener - preferenceChange " + e.getKey() + " changed from " + e.getOldValue() + " to " + e.getNewValue());
		if (e.getKey().equals(AntlrPreferenceConstants.P_FOLDING_ENABLED)) {
			Boolean val=(Boolean) e.getNewValue();
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent e) {
		System.out.println("AntlrFoldingPropertyListener - propertyChange " + e.getProperty() + " changed from " + e.getOldValue() + " to " + e.getNewValue());
		if (e.getProperty().equals(AntlrPreferenceConstants.P_FOLDING_ENABLED)) {
			Boolean val=(Boolean) e.getNewValue();
			if(val)
			  ((ANTLRv4DocumentProvider) editor.getDocumentProvider()).getDoc().processFolding();
			else
			  editor.removeFoldingStructure();			
		}
		else
		if (e.getProperty().equals(AntlrPreferenceConstants.P_FOLDING_LEXER_MODE)) { 
			Boolean val=(Boolean) e.getNewValue();
			if(val)
				  ((ANTLRv4DocumentProvider) editor.getDocumentProvider()).getDoc().processFolding();
			else
				editor.removeFoldingStructure(((ANTLRv4DocumentProvider) editor.getDocumentProvider()).getDoc().getLexerModes().values());
		}
	}
}

    
	/* --------------------------------------------------------------
	 * SET EDITOR AS READONLY
	 */
	
//	@Override
//	public boolean isEditable() {
//	    return false;
//	}
//
//	@Override
//	public boolean isEditorInputModifiable() {
//	    return false;
//	}
//
//	@Override
//	public boolean isEditorInputReadOnly() {
//	    return true;
//	}
//
//	@Override
//	public boolean isDirty() {
//	    return false;
//	}
//	
	/* --------------------------------------------------------------- */

}
