package org.github.antlr4ide.editor;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.jface.text.source.projection.ProjectionAnnotation;
import org.eclipse.jface.text.source.projection.ProjectionAnnotationModel;
import org.eclipse.jface.text.source.projection.ProjectionSupport;
import org.eclipse.jface.text.source.projection.ProjectionViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.github.antlr4ide.editor.outliner.AntlrDocOutlineView;
import org.github.antlr4ide.editor.preferences.AntlrToolPreferenceConstants;


public class ANTLRv4Editor extends TextEditor implements IAdaptable {

	private AntlrDocOutlineView fOutlinePage;
    private ProjectionSupport projectionSupport;


	public ANTLRv4Editor() {
		super();
		setSourceViewerConfiguration(new ANTLRv4Configuration());
		setDocumentProvider(new ANTLRv4DocumentProvider(this));
		
		//TODO ADD public void addPropertyChangeListener(IPropertyChangeListener listener);
		IPreferenceStore xx = PlatformUI.getPreferenceStore();
        System.out.println("ANTLRv4Editor - Preference "+AntlrToolPreferenceConstants.P_FOLDING_ENABLED+":"+xx.getBoolean(AntlrToolPreferenceConstants.P_FOLDING_ENABLED));
	}

	
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
		
    }
	private Annotation[] oldAnnotations;
	private ProjectionAnnotationModel annotationModel;
	
	public void updateFoldingStructure(List<Position> positions)
	{   System.out.println("ANTLRv4Editor - updateFoldingStructure");
	    if(annotationModel==null) return; // too early
		if(positions.size()==0) return;   // empty list
		Annotation[] annotations = new Annotation[positions.size()];
		
		//this will hold the new annotations along
		//with their corresponding positions
		Map<ProjectionAnnotation,Position> newAnnotations = new HashMap<>();
		
		for(int i =0;i<positions.size();i++)
		{
			ProjectionAnnotation annotation = new ProjectionAnnotation();
			
			newAnnotations.put(annotation,positions.get(i));
			
			annotations[i]=annotation;
		}
		
		annotationModel.modifyAnnotations(oldAnnotations,newAnnotations,null);
		
		oldAnnotations=annotations;
	}
	
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



    public void updateFoldingStructure(Collection<Position> positions) {
	    if(annotationModel==null) return; // too early
    	if(positions.size()==0) return;
    	
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
			i++;
		}
		
		annotationModel.modifyAnnotations(oldAnnotations,newAnnotations,null);
		
		oldAnnotations=annotations;
		
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
