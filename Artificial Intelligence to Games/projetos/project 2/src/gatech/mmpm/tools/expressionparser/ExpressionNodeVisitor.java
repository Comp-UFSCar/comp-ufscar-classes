/* Copyright 2010 Santiago Ontanon and Ashwin Ram */

package gatech.mmpm.tools.expressionparser;

public interface ExpressionNodeVisitor {

	public Object visit(BinaryOperatorNode node);
	
	public Object visit(BooleanLiteralNode node);
	
	public Object visit(IntegerLiteralNode node);
	
	public Object visit(FloatLiteralNode node);
	
	public Object visit(InvocationNode node);
	
	public Object visit(IdentifierAccessNode node);
	
	public Object visit(StringLiteralNode node);

	public Object visit(TernaryOperatorNode node);
	
} // ExpressionNodeVisitor
