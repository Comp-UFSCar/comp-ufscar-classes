/* Copyright 2010 Santiago Ontanon and Ashwin Ram */

package gatech.mmpm.tools.expressionparser;

import gatech.mmpm.ActionParameterType;

abstract public class ExpressionNode {

	public abstract ActionParameterType getType();

	public abstract Object accept(ExpressionNodeVisitor visitor);
	
} // class ExpressionNode
