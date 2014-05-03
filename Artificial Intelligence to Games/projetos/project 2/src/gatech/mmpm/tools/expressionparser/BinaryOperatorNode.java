/* Copyright 2010 Santiago Ontanon and Ashwin Ram */

package gatech.mmpm.tools.expressionparser;

import gatech.mmpm.ActionParameterType;

public class BinaryOperatorNode extends ExpressionNode {

	BinaryOperatorNode(ExpressionNode lhs, OperatorToken operator, ExpressionNode rhs) {

		this(lhs, operator, rhs, (lhs==null)?null:lhs.getType()); 

	} // Constructor

	//-----------------------------------------------------

	BinaryOperatorNode(ExpressionNode lhs, OperatorToken operator, ExpressionNode rhs, ActionParameterType type) {

		_lhs = lhs;
		_operator = operator;
		_rhs = rhs;
		_type = type;

	} // Constructor
	
	//-----------------------------------------------------

	public ExpressionNode getLHS() {
		return _lhs;
	}

	//-----------------------------------------------------

	public OperatorToken getOperator() {
		return _operator;
	}

	//-----------------------------------------------------

	public ExpressionNode getRHS() {
		return _rhs;
	}
	
	//-----------------------------------------------------
	
	public ActionParameterType getType() {

		return _type;

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
	
	//-----------------------------------------------------

	public String toString() {
		
		return "(" + _lhs + ")" + _operator + "(" + _rhs + ")";
		
	}
	
	//-----------------------------------------------------
	//                  Protected fields
	//-----------------------------------------------------

	protected ExpressionNode _lhs;
	
	protected OperatorToken _operator;
	
	protected ExpressionNode _rhs;

	protected ActionParameterType _type;

} // class BinaryOperatorNode
