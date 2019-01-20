package org.github.antlr4ide.builder.tool;

/**
 * Launch attributes.
 */
@SuppressWarnings("all")
public interface LaunchConstants {
  /**
   * Specify the grammar path. The path must be relative to the workspace root and must be in the OS format.
   */
  public final static String GRAMMAR = "antlr4.grammar";
  
  /**
   * Specify tool arguments. Arguments are separated by spaces like in a shell console.
   * See http://www.antlr.org/wiki/display/ANTLR4/ANTLR+Tool+Command+Line+Options
   */
  public final static String ARGUMENTS = "antlr4.arguments";
  
  /**
   * Specify VM arguments.
   */
  public final static String VM_ARGUMENTS = "antlr4.vmArguments";
  
  /**
   * Launch ID.
   */
  public final static String LAUNCH_ID = "org.github.Antlr4.tool";
}
