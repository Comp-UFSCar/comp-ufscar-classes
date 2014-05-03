/* Copyright 2010 Santiago Ontanon and Ashwin Ram */

package gatech.mmpm.tools.expressionparser;

public enum OperatorToken {

	ADD("+"),
	MINUS("-"),
	MULT("*"),
	DIV("/"),
	MOD("%"),
	LT("<"),
	GT(">"),
	LE("<="),
	GE(">="),
	EQ("=="),
	NE("!="),
	NOT("!"), // Unary
//  BITWISE_NOT("~"),	
	AND("&&"),
	OR("||");
	
	OperatorToken(String token) {
		_token = token;
	}
	
	public String getToken() {
		return _token;
	}
	
	public String toString() {
		return _token;
	}
	
	protected String _token;
	
}
