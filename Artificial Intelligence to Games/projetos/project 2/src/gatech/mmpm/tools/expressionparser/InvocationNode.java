/* Copyright 2010 Santiago Ontanon and Ashwin Ram */

package gatech.mmpm.tools.expressionparser;

import java.util.List;

import gatech.mmpm.ActionParameterType;

public class InvocationNode extends ExpressionNode {


	// parameter list is not cloned, so it should NOT be
	// changed externally (the object becomes the owner of
	// the list).
	public InvocationNode(String name, ActionParameterType resultType, 
	                      List<String> parametersNames, List<ExpressionNode> parameters) {

		_name = name;
		_resultType = resultType;
		_parametersNames = parametersNames;
		_parameters = parameters;
		
	} // Constructor

	//-----------------------------------------------------
	
	public ActionParameterType getType() {
		
		return _resultType;
		
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

	public String getName() {
		return _name;
	}
	
	//-----------------------------------------------------
	
	// List should not be modified externally.
	public List<String> getParametersNames() {
		return _parametersNames;
	}
	
	//-----------------------------------------------------
	
	// List should not be modified externally.
	public List<ExpressionNode> getParameters() {
		return _parameters;
	}
	
	//-----------------------------------------------------
	
	public String toString() {
		
		StringBuffer sb = new StringBuffer(_name);
		sb.append("(");
		for (ExpressionNode p:_parameters) {
			sb.append(p);
			sb.append(",");
		} // for
		
		// The last comma must be removed.
		sb.deleteCharAt(sb.length() - 1);
		
		sb.append(")");
		
		return sb.toString();
		
	} // toString
	
	//-----------------------------------------------------
	
	protected String _name;
	
	protected ActionParameterType _resultType;
	
	protected List<String> _parametersNames;

	protected List<ExpressionNode> _parameters;
	
} // InvocationNode 
