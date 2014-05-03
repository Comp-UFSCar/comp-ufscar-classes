/* Copyright 2010 Santiago Ontanon and Ashwin Ram */

package gatech.mmpm.tools.expressionparser;

import gatech.mmpm.ActionParameterType;

/**
 * Class that represents a ternary operator, that is
 * the well-known conditional operator of C/C++ and
 * Java, for example.
 * 
 * @author Pedro Pablo Gomez Martin
 * @date August, 2009
 *
 */
public class TernaryOperatorNode extends ExpressionNode {

	// Externally types should have been tested
	// (conditionalNode to be BOOLEAN, and yesCase and
	// noCase to have the same type).
	public TernaryOperatorNode(ExpressionNode condition,
	                           ExpressionNode yesCase,
	                           ExpressionNode noCase) {
		
		_condition = condition;
		_yesCase = yesCase;
		_noCase = noCase;

	} // Constructor
	
	//-----------------------------------------------------
	
	@Override
	public ActionParameterType getType() {
		
		return _yesCase.getType();
		// Should be the same than _noCase.getType();
		
	} // getType

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

	public ExpressionNode getCondition() {
		
		return _condition;
		
	}
	
	//-----------------------------------------------------

	public ExpressionNode getYesCase() {
		
		return _yesCase;
		
	}
	
	//-----------------------------------------------------
	
	public ExpressionNode getNoCase() {
		
		return _noCase;
		
	}
	
	//-----------------------------------------------------
	
	public String toString() {
		return "(" + _condition + ") ? (" + _yesCase + ") : (" + _noCase + ")";
	}
	
	//-----------------------------------------------------
	//                  Protected fields
	//-----------------------------------------------------

	protected ExpressionNode _condition;
	
	protected ExpressionNode _yesCase;
	
	protected ExpressionNode _noCase;
	
} // TernaryOperatorNode
