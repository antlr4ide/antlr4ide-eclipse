package org.github.antlr4ide.editor;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

public interface IANTLRv4ColorConstants {
	// SWT Color objects
	Color COMMENTrgb = new Color(Display.getCurrent(),128, 0, 0);
	Color STMTrgb    = new Color(Display.getCurrent(),153, 0, 153); // 128, 128, 0);
	Color CONSTrgb   = new Color(Display.getCurrent(),0, 128, 0);
	Color DEFAULTrgb = new Color(Display.getCurrent(),0, 0, 0);
	Color WHITErgb   = new Color(Display.getCurrent(),255, 255, 255);
	
	// Eclipse Text attributes
	TextAttribute CONST       = new TextAttribute(CONSTrgb, null, SWT.BOLD);
	TextAttribute STMT        = new TextAttribute(STMTrgb, null, SWT.ITALIC);
	TextAttribute COMMENT     = new TextAttribute(COMMENTrgb,null,SWT.NORMAL);
	TextAttribute DEFAULT     = new TextAttribute(DEFAULTrgb,null,SWT.NORMAL);
	TextAttribute PARAGRAPH   = new TextAttribute(DEFAULTrgb,null,SWT.BOLD);
	TextAttribute PARSER_RULE = new TextAttribute(WHITErgb,STMTrgb,SWT.BOLD);
	TextAttribute LEXER_RULE  = new TextAttribute(WHITErgb,COMMENTrgb,SWT.BOLD);
}
