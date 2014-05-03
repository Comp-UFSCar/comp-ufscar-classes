/* Copyright 2010 Santiago Ontanon and Ashwin Ram */

package gatech.mmpm.tools.expressionparser;

import gatech.mmpm.ActionParameterType;

public class FloatLiteralNode extends ExpressionNode {

	public FloatLiteralNode(float value) {
		_value = value;
	}
	
	public float getValue() {
		return _value;
	}
	
	public ActionParameterType getType() {

		return ActionParameterType.FLOAT;

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
		return "" + _value;
	}
	
	protected float _value;

} // class FloatLiteralNode
