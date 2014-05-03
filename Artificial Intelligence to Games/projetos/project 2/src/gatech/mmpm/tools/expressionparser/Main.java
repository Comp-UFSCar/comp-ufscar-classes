/* Copyright 2010 Santiago Ontanon and Ashwin Ram */

package gatech.mmpm.tools.expressionparser;

import java.io.*;
import java_cup.runtime.Symbol;


public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//StringReader sr = new StringReader("10 / (2 + 10) *10");
		//StringReader sr = new StringReader("IsCellEmpty(x,y) && ((Distance(GetClosestEntity(x,y,\"TTower\", player), x, y)) <= 5) || (Distance(GetClosestEntity(x,y,\"TBase\", player), x, y)) <= 5))");
/*		
		StringReader sr = new StringReader(
"IsCellEmpty(x,y) && ((Distance(GetClosestEntity(x,y,\"TTower\", player), x, y)) <= 5) || (Distance(GetClosestEntity(x,y,TBase, player), x, y)) <= 5)");
*/
		StringReader sr = new StringReader(
//				"10 / (2 + 10) * 10"
		/*		
		IsCellEmpty(x,y) &&
		( (Distance(GetClosestEntity(x,y,"TTower", player), x, y) <= 5) ||
		  (Distance(GetClosestEntity(x,y,TBase, player), x, y) <= 5))
*/
				
        "IsCellEmpty(x,y) && "+
     	"((Distance(GetClosestEntity(x,y,\"TTower\", player), x, y) <= 5) ||"+
     	 "(Distance(GetClosestEntity(x,y,\"TBase\", player), x, y) <= 5))"
     	);

//		StringReader sr = new StringReader("Distance(GetClosestEntity(\"Tower\", x,y),x,y)<5");
		Parser parser = new Parser(new Lexer(sr));
		try {
			Symbol result;
			result = parser.parse();

			System.out.println(result.value);

			System.out.println("Valid input found.");
		} catch (Exception e) {
			System.out.println("Expression could not be parsed.");
		}
	} // main

} // class Main
