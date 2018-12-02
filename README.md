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
1. Map of all lexer modes
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

Supported content in the outline view
* Lexer Modes
* Lexer Rules
* Parser Rules


## Working on
### Folding
Folding is implemented using the projection framework based on the article [Folding](https://www.eclipse.org/articles/Article-Folding-in-Eclipse-Text-Editors/folding.html)

The scanner creates the maps for Lexer modes, Lexer rules and Parser rules. The maps has the relevant name as key and a position as value. A position ``org.eclipse.jface.text.Position`` is start offset and length. The positions can be used directly by the projection framework.

For now only initial folding of Parser Rules and Lexer Modes is implemented.

### Preference Pages
Tool preference page has an option to cache the internal antlr parse tree. This require a version of the antlr tool that supports Serialization. If the parse tree is still valid and if enabled the editor will read the parse tree from the cache instead of regenerating the parsetree.
Currently [this fork of Antlr](https://github.com/HSorensen/antlr4/tree/lexerinclude) supports caching Java based parsetrees.

If there is interest to support this for other targets please raise an issue [antlride-eclipse-issue](https://github.com/antlr4ide/antlr4ide-eclipse/issues)

## TODOs
### Preference Pages for hilights
### Semantic validation
### Code generation (Using eclipse Builders)
### Code generation for multiple target languages
### Refactoring
### Code completion
### Incremental Scanning/Parsing 
### Hovering, Quick fixes
### Code definition lookup (ctrl+Mouse)
### Use proper logging infrastructure
### Eclipse Help
### Eclipse Tutorial

# TIPS and TRICKS from 

This section is for various snippets collected while searching for answers

## getting content of active editor

Currently the editor and document providers are cached. There might be a better way

[Content of Editor](https://stackoverflow.com/questions/6661382/get-contents-of-editor)

```java
public String getCurrentEditorContent() {
    final IEditorPart editor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
        .getActiveEditor();
    if (!(editor instanceof ITextEditor)) return null;
    ITextEditor ite = (ITextEditor)editor;
    IDocument doc = ite.getDocumentProvider().getDocument(ite.getEditorInput());
    return doc.get();
}
```


## Right-Click context menu

Searching for adding "folding" to editor context menu.

1. [right-click menu](https://stackoverflow.com/questions/19566844/eclipse-plugin-development-right-click-preferences-menu)
1. [add right-click menu](https://stackoverflow.com/questions/4726328/eclipse-plugin-development-how-to-add-option-in-right-click-menu)
1. [Custumize popup](https://www.oxygenxml.com/doc/versions/18/ug-editor/topics/api_faq_customize_author_popup.html)

