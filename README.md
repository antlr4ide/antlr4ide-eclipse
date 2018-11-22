# antlr4ide-eclipse
Antlr4 Eclipse based editor

The editor use a the antlr v4 grammar from the antlr.org examples site:
[github.com/antlr/grammars-v4/tree/master/antlrv4](https://github.com/antlr/grammars-v4/tree/master/antlr4)

The translated grammar is used to provide the tokens for the eclipse editor.

## Implementation notices
This editor implements a number of features.

Use the ANTLR parse tree visitor and listeners to extract details from the parsed antlr grammar.

To capture changes to the document a document listener is added in [AntlrDocument](https://github.com/antlr4ide/antlr4ide-eclipse/blob/master/src/org/github/antlr4ide/editor/AntlrDocument.java)

The scanner and parser is invoked from [LexerHelper](https://github.com/antlr4ide/antlr4ide-eclipse/blob/master/src/org/github/antlr4ide/editor/LexerHelper.java) and the visitor [ANTLRv4Visitor](https://github.com/antlr4ide/antlr4ide-eclipse/blob/fbc3fbfacfbb2348c73e01ad1ff7ee4c36472844/src/org/github/antlr4ide/editor/LexerHelper.java#L76) produces 
1. Map of all parser rules
1. Map of all lexer rules
1. List of all error messages

### Antlr error messages
The error messages are added to the file as resource markers.

### Syntax highlighting
The text attributes are provided in [ANTLRv4Scanner](https://github.com/antlr4ide/antlr4ide-eclipse/blob/master/src/org/github/antlr4ide/editor/ANTLRv4Scanner.java) and [IANTLRv4ColorConstants](https://github.com/antlr4ide/antlr4ide-eclipse/blob/master/src/org/github/antlr4ide/editor/IANTLRv4ColorConstants.java). 

For example, for the Lexer keyword FRAGMENT:

```java
Color STMTrgb        = new Color(Display.getCurrent(),153, 0, 153);
TextAttribute STMT   = new TextAttribute(STMTrgb, null, SWT.ITALIC);
IToken stmtTextToken = new org.eclipse.jface.text.rules.Token(IANTLRv4ColorConstants.STMT);
hilite.put(ANTLRv4Lexer.FRAGMENT, stmtTextToken);
```

### Outline view 
The Outline view uses the lexer rules and parser rules maps that the scanner produced. 
The outline content provider [AntlrDocOutlineContentProvider](https://github.com/antlr4ide/antlr4ide-eclipse/blob/master/src/org/github/antlr4ide/editor/outliner/AntlrDocOutlineContentProvider.java) takes the content of the maps and add to the outline content.

 