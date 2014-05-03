/* Copyright 2010 Santiago Ontanon and Ashwin Ram */

package gatech.mmpm.tools.compiler;

import gatech.mmpm.tools.expressionparser.*;
import gatech.mmpm.tools.parseddomain.*;

import java.util.List;
import java.util.LinkedList;

/**
 * Class to generate the Java code for building the Sensors
 * tree equivalent to a expression tree created while parsing
 * an expression from the domain XML.
 * 
 * While parsing the XML, flex and cups are used to generate
 * an expression tree using the classes in the
 * gatech.mmpm.tools.expressionparser package.
 * MMPM, in the other hand, uses a Sensor hierarchy on runtime
 * to evaluate the state of the game, check conditions and so
 * on. The XML expression compiler must generate java code
 * to create a "sensors tree" that mimics the expression
 * written in the XML file. This class analyzes the expression
 * tree using the gatech.mmpm.tools.expressionparser classes
 * and generate the java code that, when executed, will create
 * a similar expression using the MMPM runtime sensors.
 * 
 * The class is programmed has a visitor over the
 * gatech.mmpm.tools.expressionparser classes. For convenience,
 * the entry point for the compiler is the static method
 * compile() that makes the dirty work of using the visitor
 * pattern.
 * 
 * @author Pedro Pablo Gomez Martin
 * @date September, 2009
 */
public class Builtin2Java implements ExpressionNodeVisitor {

	public Builtin2Java(ParsedDomain domain) {

		_domain = domain;

	} // Constructor
	
	//---------------------------------------------------

	public Object visit(BinaryOperatorNode node) {

		StringBuilder sb = new StringBuilder();
		List<ExpressionNode> operands;
		
		switch (node.getOperator()) {
			case ADD:
				sb.append("new ArithmeticSensor(");
				sb.append(node.getLHS().accept(this));
				sb.append(", ArithmeticSensor.Operator.ADD, ");
				sb.append(node.getRHS().accept(this));
				sb.append(")");
				break;
			case MINUS:
				sb.append("new ArithmeticSensor(");
				sb.append(node.getLHS().accept(this));
				sb.append(", ArithmeticSensor.Operator.MINUS, ");
				sb.append(node.getRHS().accept(this));
				sb.append(")");
				break;
			case MULT:
				sb.append("new ArithmeticSensor(");
				sb.append(node.getLHS().accept(this));
				sb.append(", ArithmeticSensor.Operator.MULT, ");
				sb.append(node.getRHS().accept(this));
				sb.append(")");
				break;
			case DIV:
				sb.append("new ArithmeticSensor(");
				sb.append(node.getLHS().accept(this));
				sb.append(", ArithmeticSensor.Operator.DIV, ");
				sb.append(node.getRHS().accept(this));
				sb.append(")");
				break;
			case MOD:
				sb.append("new ArithmeticSensor(");
				sb.append(node.getLHS().accept(this));
				sb.append(", ArithmeticSensor.Operator.MOD, ");
				sb.append(node.getRHS().accept(this));
				sb.append(")");
				break;
			case LT:
				sb.append("new RelationalCondition(");
				sb.append(node.getLHS().accept(this));
				sb.append(", RelationalCondition.Operator.LESS_THAN, ");
				sb.append(node.getRHS().accept(this));
				sb.append(")");
				break;
			case GT:
				sb.append("new RelationalCondition(");
				sb.append(node.getLHS().accept(this));
				sb.append(", RelationalCondition.Operator.GREATER_THAN, ");
				sb.append(node.getRHS().accept(this));
				sb.append(")");
				break;
			case LE:
				sb.append("new RelationalCondition(");
				sb.append(node.getLHS().accept(this));
				sb.append(", RelationalCondition.Operator.LESS_EQUAL_THAN, ");
				sb.append(node.getRHS().accept(this));
				sb.append(")");
				break;
			case GE:
				sb.append("new RelationalCondition(");
				sb.append(node.getLHS().accept(this));
				sb.append(", RelationalCondition.Operator.GREATER_EQUAL_THAN, ");
				sb.append(node.getRHS().accept(this));
				sb.append(")");
				break;
			case EQ:
				sb.append("new EqualitySensor(");
				sb.append(node.getLHS().accept(this));
				sb.append(", EqualitySensor.Operator.EQUAL_THAN, ");
				sb.append(node.getRHS().accept(this));
				sb.append(")");
				break;
			case NE:
				sb.append("new EqualitySensor(");
				sb.append(node.getLHS().accept(this));
				sb.append(", EqualitySensor.Operator.NOT_EQUAL_THAN, ");
				sb.append(node.getRHS().accept(this));
				sb.append(")");
				break;
			case AND:
				operands = new LinkedList<ExpressionNode>();
				getAll(node, operands);

				sb.append("new AndCondition(");
				for (ExpressionNode o:operands) {
					sb.append(o.accept(this));
					sb.append(", ");
				} // for
				sb.delete(sb.length() - 2, sb.length());
				sb.append(")");
				break;
			case OR:
				operands = new LinkedList<ExpressionNode>();
				getAll(node, operands);

				sb.append("new OrCondition(");
				for (ExpressionNode o:operands) {
					sb.append(o.accept(this));
					sb.append(", ");
				} // for
				sb.delete(sb.length() - 2, sb.length());
				sb.append(")");
				break;
		} // switch

		if (sb.toString().length() == 0)
			throw new RuntimeException("Not programmed yet");
		
		return sb.toString();

	} // visit(BinaryOperatorNode)

	//---------------------------------------------------

	public Object visit(BooleanLiteralNode node) {
		
		// We will use the native MMPM classes.
		if (node.getValue() == true)
			return "new True()";
		else
			return "new False()";
		
	} // visit(BooleanLiteralNode)
	
	//---------------------------------------------------

	public Object visit(IntegerLiteralNode node) {

		// We will use the native MMPM class.
		return "new ConstantInteger(" + node.getValue() + ")";

	} // visit(IntegerLiteralNode)
	
	//---------------------------------------------------

	public Object visit(FloatLiteralNode node) {

		// We will use the native MMPM class.
		return "new ConstantFloat(" + node.getValue() + ")";

	} // visit(FloatLiteralNode)
	
	//---------------------------------------------------

	public Object visit(InvocationNode node) {
		
		// We are assuming that the method name becomes
		// the sensor Java class name.
		// TODO: case?
		StringBuilder sb = new StringBuilder();

		sb.append("new Invocation(new ");
		sb.append(node.getName() + "(), ");

		java.util.Iterator<String> parameterNameIt;
		java.util.Iterator<ExpressionNode> parameterNodeIt;

		parameterNameIt = node.getParametersNames().iterator();
		parameterNodeIt = node.getParameters().iterator(); 
		
		while (parameterNodeIt.hasNext()) {
			if (!parameterNameIt.hasNext())
				// Ooopsss. This should not happen
				// ExpressionBuilder.buildMethodInvocationExpression
				// must be broken...
				throw new RuntimeException("Incoherence in name-value in method invocation");
			// Step forwards...
			String name = parameterNameIt.next();
			if ((name == null) || (name.length() == 0) ||
			    name.contains("\""))
			    throw new RuntimeException("Invalid parameter name? (" + name + ")");
			    
			ExpressionNode param = parameterNodeIt.next();
			sb.append("new Pair<String, Sensor>(\"");
			sb.append(name);
			sb.append("\", ");
			if (param == null)
				sb.append("null");
			else
				sb.append(param.accept(this));
			sb.append(")"); // new Pair
			sb.append(", ");

		}
		
		// Delete the last comma
		sb.delete(sb.length() - 2, sb.length());
		sb.append(")");
		
		return sb.toString();
		
	} // visit(InvocationNode)

	//---------------------------------------------------

	public Object visit(IdentifierAccessNode node) {

		return "new GetContextValue(\""+node.getName()+"\", ActionParameterType." + node.getType() + ")";
		// TODO: value type?

	} // visit(IdentifierAccessNode)
	
	//---------------------------------------------------

	public Object visit(StringLiteralNode node) {

		// We will use the native MMPM class.
		return "new ConstantString(\"" + node.getValue() + "\")";

	} // visit(StringLiteralNode)

	//---------------------------------------------------

	public Object visit(TernaryOperatorNode node) {

		StringBuilder sb = new StringBuilder();

		sb.append("new Conditional(");
		sb.append(node.getCondition().accept(this));
		sb.append(", ");
		sb.append(node.getYesCase().accept(this));
		sb.append(", ");
		sb.append(node.getNoCase().accept(this));
		sb.append(")");
		
		return sb.toString();

	} // visit(TernaryOperatorNode)
	
	//---------------------------------------------------

	public static String compile(ExpressionNode node, ParsedDomain domain) {

		Builtin2Java e2j;
		
		e2j = new Builtin2Java(domain);
		return (String) node.accept(e2j);

	} // compile

	//---------------------------------------------------

	/**
	 * Receives a binary operator node that it is supposed to
	 * be an AND or an OR, and returns in the second parameter
	 * (that must be initialized) all the operands, assuming
	 * that the RHS of the root operator could be again another
	 * AND (or OR). 
	 */
	public static void getAll(BinaryOperatorNode node, List<ExpressionNode> result) {
		result.add(node.getLHS());
		if ((node.getRHS() instanceof BinaryOperatorNode) &&
		    (((BinaryOperatorNode) node.getRHS()).getOperator() == node.getOperator()))
			getAll((BinaryOperatorNode) node.getRHS(), result);
		else
			// Base case
			result.add(node.getRHS());
	} // getAll
	
	//---------------------------------------------------
	
	protected ParsedDomain _domain;
	
} // Builtin2Java
