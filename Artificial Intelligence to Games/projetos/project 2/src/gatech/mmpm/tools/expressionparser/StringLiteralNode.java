/* Copyright 2010 Santiago Ontanon and Ashwin Ram */

package gatech.mmpm.tools.expressionparser;

import gatech.mmpm.ActionParameterType;

public class StringLiteralNode extends ExpressionNode {

	public StringLiteralNode(String value) {

		_value = value;

	}
	
	public String getValue() {
		
		return _value;
		
	}
	
	public ActionParameterType getType() {

		return ActionParameterType.STRING;

	}

	//-----------------------------------------------------

	/**
	 * Accept a visitor, and invokes the <tt>visit</tt>
	 * method on it depending on the current node type.
	 * 
	 * @param visitor Visitor to accept.
	 * @return Visitor returned value.
	 */
	public Object accept(ExpressionNodeVisitor visitor) {
		
		return visitor.visit(this);
		
	} // accept
	
	public String toString() {
		return _value;
	}
	
	protected String _value;

} // class StringLiteralNode
