package org.github.antlr4ide.editor.outliner;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.github.antlr4ide.editor.ANTLRv4DocumentProvider;
import org.github.antlr4ide.editor.antlr.AntlrDocument;

/**
 * Divides the editor's document into ten segments and provides elements for them.
 */
public class AntlrDocOutlineContentProvider implements ITreeContentProvider {
	private boolean DEBUG= !true;

	private ANTLRv4DocumentProvider fDocumentProvider;
	private AntlrDocument doc;
	
	public AntlrDocOutlineContentProvider(IDocumentProvider fDocumentProvider) {
		this.fDocumentProvider = (ANTLRv4DocumentProvider) fDocumentProvider;
		this.doc=(AntlrDocument) this.fDocumentProvider.getDoc();

	}

	/*
	 * @see IStructuredContentProvider#getElements(Object)
	 * 
	 * When the element is of IFileEditorInput then return the root elements of the outline
	 * Otherwise return array on zero elements
	 */
	public Object[] getElements(Object element) {
		if(DEBUG)
			System.out.println(">>> AntlrDocOutlineOutlineContentProvider.getElements ("+(element==null?"null":element.getClass())+")");
		
		if(element instanceof IFileEditorInput) {
			// Outline Root
			Object ret[]=new Object[3];
			ret[0]=new OutlineRootElement("Parser Rules",0);
			ret[1]=new OutlineRootElement("Lexer Rules",1);
			ret[2]=new OutlineRootElement("Lexer Modes", 2);
			return ret ;
		}
		
		return new Object[0];
	}

	/*
	 * @see ITreeContentProvider#hasChildren(Object)
	 * 
	 * Returns true if the 
	 */
	public boolean hasChildren(Object element) {
		if(DEBUG)
			System.out.println(">>> AntlrDocOutlineOutlineContentProvider.hasChildren ("+(element==null?"null":element.getClass())+")");
		if (element instanceof OutlineRootElement) {
			Integer type= ((OutlineRootElement)element).getType();
			if(type==0) return doc.getParserRules().isEmpty()==false;
			if(type==1) return doc.getLexerRules().isEmpty()==false;
			if(type==2) return doc.getLexerModes().isEmpty()==false;
		}
		return false;
	}

	/*
	 * @see ITreeContentProvider#getParent(Object)
	 */
	public Object getParent(Object element) {
		if(DEBUG)
			System.out.println(">>> AntlrDocOutlineOutlineContentProvider.getParent ("+(element==null?"null":element.getClass())+")");
		return null;
	}

	/*
	 * @see ITreeContentProvider#getChildren(Object)
	 */
	public Object[] getChildren(Object element) {
		if(DEBUG)
			System.out.println(">>> AntlrDocOutlineOutlineContentProvider.getChildren "+(element==null?"null":element.getClass())+")");
		
		if (element instanceof OutlineRootElement) {
			Integer type= ((OutlineRootElement)element).getType();
			if(type==0) return doc.getParserRules().keySet().toArray();
			if(type==1) return doc.getLexerRules().keySet().toArray();
			if(type==2) return doc.getLexerModes().keySet().toArray();
		}
		
		return new Object[0];
	}
	
	
	/*
	 * Helper classes for the outline tree
	 */
	public interface IOutlineRootElement {
		public String toString();
	}
	public class OutlineRootElement implements IOutlineRootElement {
		private String s;
		private Integer type;
		public OutlineRootElement(String s, int type) {this.s=s; this.type=type;}
		public String toString() {return s;}
		public Integer getType() { return type; }
	}
}

