/* Copyright 2010 Santiago Ontanon and Ashwin Ram */

package gatech.mmpm.tools.expressionparser;

import java.io.StringReader;

import java_cup.runtime.Symbol;

/**
 * Entry point for the expression parser.
 * 
 * This is necessary because Jflex doesn't seem to generate
 * the Lexer as a public class, so lexer object can only
 * be created from this package.
 * 
 * @author Pedro Pablo Gomez Martin
 * @date August, 2009
 */
public class ExpressionParser {

	/**
	 * Parse an expression. This method is <em>not</em> thread safe. Only one of
	 * them can be called.
	 * 
	 * @param expression
	 * @param symbolTable
	 * @return
	 */
	public static ExpressionNode parse(String expression, SymbolTable symbolTable) {

		synchronized(_semaphore) {
			StringReader sr = new StringReader(expression);

			// Static attributes... that is... a refined way to
			// call global variables...
			ExpressionBuilder.symbolTable = symbolTable;

			Parser parser = new Parser(new Lexer(sr));
			Symbol result;

			try {
				result = parser.parse();
			} catch (Exception e) {
				System.out.println("Expression '" + expression + "' could not be parsed.");
				e.printStackTrace();
				return null;
			}

			if (!(result.value instanceof ExpressionNode)) {
				throw new RuntimeException("Internal error: parser is inconsistent!! (expected ExpressionNode, get " + result.value.getClass().getName() + ")");
			}

			return (ExpressionNode) result.value;

		} // synchronized(_semaphore)

	} // parse
	
	protected static Object _semaphore = new Object();
	
} // class ExpressionParser
