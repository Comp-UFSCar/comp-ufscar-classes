/* Copyright 2010 Santiago Ontanon and Ashwin Ram */

package gatech.mmpm.tools.expressionparser;

import gatech.mmpm.ActionParameterType;

public class AssignmentNode extends ExpressionNode {

	public AssignmentNode(String id, ExpressionNode rhs) {
		_id = id;
		_rhs = rhs;
	}
	
	public Object accept(ExpressionNodeVisitor visitor) {
		// This expression type cannot accept visitors
		// (they are hard-code compiled).
		throw new RuntimeException("Visitor not accepted in AssignmentNode");
	}

	public ActionParameterType getType() {
		return _rhs.getType();
	}

	public String getIdentifier() {
		return _id;
	}
	
	public ExpressionNode getRHS() {
		return _rhs;
	}
	
	protected String _id;
	
	protected ExpressionNode _rhs;
	
} // AssignmentNode