/* Copyright 2010 Santiago Ontanon and Ashwin Ram */

package gatech.mmpm.tools.expressionparser;

import gatech.mmpm.ActionParameterType;

import java.util.List;
import java.util.Iterator;
import java.util.Dictionary;
import java.util.Set;

/**
 * Class used during the syntactical analysis to build the expressions.
 * 
 * It is quite tight to the analysis, and does, in some sense, "contextual
 * tests" of the production rules.
 * 
 * Keep in mind that this class in completely static (we have not found
 * a way to provide an instance to the CUP generated parser). The main
 * problem of this fact is that the class <em>needs</em> to store information
 * so it has static attributes. This means that this class <em>is not
 * thread safe</em> and only a parser should be done at the same time.
 * 
 * @author Pedro Pablo Gomez Martin
 * @date August, 2009
 */
public class ExpressionBuilder {

	// TODO: automatic casting!
	// boolean is the same than float (fuzzy boolean).
	
	/**
	 * Builds an assignment expression with the id name and the
	 * right value.
	 * 
	 * @param id Identifier
	 * @param rhs Expression.
	 * @return The final expression.
	 * @throws ExpressionBuilderException
	 */
	public static ExpressionNode buildAssignment(String id, ExpressionNode rhs) throws ExpressionBuilderException {
		return new AssignmentNode(id, rhs);
	} // buildAssignment
	
	/**
	 * Builds an arithmetic binary expression with two expressions and an
	 * arithmetic operator.
	 * 
	 * Both expressions must have the same type, and that type must
	 * allows the operand.
	 * 
	 * @param lhs
	 * @param op
	 * @param rhs
	 * @return
	 * @throws ExpressionBuilderException
	 */
	public static ExpressionNode buildArithmeticBinaryExpression(ExpressionNode lhs, OperatorToken op, ExpressionNode rhs) throws ExpressionBuilderException {

		// TODO: more elaborated type control and automatic casts?
		ActionParameterType lhsType;
		ActionParameterType rhsType;
		
		lhsType = lhs.getType();
		rhsType = lhs.getType();

		if ((((op == OperatorToken.ADD) ||
		      (op == OperatorToken.MINUS) ||
		      (op == OperatorToken.MOD)) &&
		      (!additionValid(lhsType, rhsType))) ||
		    (((op == OperatorToken.MULT) ||
		      (op == OperatorToken.DIV)) &&
		      (!multiplicationValid(lhsType, rhsType)))
		   ) {
			throw new ExpressionBuilderException("Type mismatch in '" + op + "' operator.");
		}
		
		//Getting the type
		ActionParameterType type = null;
		if(lhs.getType() == ActionParameterType.COORDINATE || 
				lhs.getType() == ActionParameterType.COORDINATE )
			type = ActionParameterType.COORDINATE;
		else if(lhs.getType() == ActionParameterType.STRING || 
				lhs.getType() == ActionParameterType.STRING )
			type = ActionParameterType.STRING;
		else if(lhs.getType() == ActionParameterType.PLAYER || 
				lhs.getType() == ActionParameterType.PLAYER )
			type = ActionParameterType.PLAYER;
		else if(lhs.getType() == ActionParameterType.FLOAT || 
				lhs.getType() == ActionParameterType.FLOAT )
			type = ActionParameterType.FLOAT;
		else if(lhs.getType() == ActionParameterType.INTEGER || 
				lhs.getType() == ActionParameterType.INTEGER )
			type = ActionParameterType.INTEGER;
		
		

		return new BinaryOperatorNode(lhs, op, rhs, type);

	} // buildArithmeticBinaryExpression

	//-----------------------------------------------------
	
	/**
	 * Builds a relational expression with two expressions and an
	 * relational operator.
	 * 
	 * Both expressions must have the same type, and that type must
	 * allows the operand.
	 * 
	 * @param lhs
	 * @param op
	 * @param rhs
	 * @return
	 * @throws ExpressionBuilderException
	 */
	public static ExpressionNode buildRelationalExpression(ExpressionNode lhs, OperatorToken op, ExpressionNode rhs) throws ExpressionBuilderException {

		ActionParameterType lhsType;
		ActionParameterType rhsType;
		
		lhsType = lhs.getType();
		rhsType = lhs.getType();

		if (!relationalValid(lhsType, rhsType)) {
			throw new ExpressionBuilderException("Type mismatch in '" + op + "' operator.");
		}

		// The relational expression is valid.
		return new BinaryOperatorNode(lhs, op, rhs, ActionParameterType.BOOLEAN);

	} // buildRelationalExpression

	//-----------------------------------------------------
	
	/**
	 * Builds a equality expression with two expressions and an
	 * equality (<tt>==</tt> or <tt>!=</tt>) operator.
	 * 
	 * Both expressions must have the same type, and that type must
	 * allows the operand.
	 * 
	 * @param lhs
	 * @param op
	 * @param rhs
	 * @return
	 * @throws ExpressionBuilderException
	 */
	public static ExpressionNode buildEqualityExpression(ExpressionNode lhs, OperatorToken op, ExpressionNode rhs) throws ExpressionBuilderException {

		ActionParameterType lhsType;
		ActionParameterType rhsType;
		
		lhsType = lhs.getType();
		rhsType = lhs.getType();

		if (!equalityValid(lhsType, rhsType)) {
			throw new ExpressionBuilderException("Type mismatch in '" + op + "' operator.");
		}

		// The equality expression is valid.
		return new BinaryOperatorNode(lhs, op, rhs, ActionParameterType.BOOLEAN);

	} // buildEqualityExpression

	//-----------------------------------------------------
	
	/**
	 * Builds a logical expression with two expressions and an
	 * equality (<tt&&</tt> or <tt>||</tt>) operator.
	 * 
	 * Both expressions must be boolean.
	 * 
	 * @param lhs
	 * @param op
	 * @param rhs
	 * @return
	 * @throws ExpressionBuilderException
	 */
	public static ExpressionNode buildLogicalExpression(ExpressionNode lhs, OperatorToken op, ExpressionNode rhs) throws ExpressionBuilderException {

		ActionParameterType lhsType;
		ActionParameterType rhsType;
		
		lhsType = lhs.getType();
		rhsType = lhs.getType();

		if ((lhsType != ActionParameterType.BOOLEAN) ||
		    (lhsType != rhsType)) {
			throw new ExpressionBuilderException("Type mismatch in '" + op + "' operator.");
		}

		return new BinaryOperatorNode(lhs, op, rhs, ActionParameterType.BOOLEAN);

	} // buildLogicalExpression

	//-----------------------------------------------------

	public static ExpressionNode buildConditionalExpression(ExpressionNode condition, ExpressionNode yesCase, ExpressionNode noCase) throws ExpressionBuilderException {

		if (condition.getType() != ActionParameterType.BOOLEAN)
			throw new ExpressionBuilderException("First expression in ternary operator must be boolean.");
		
		if (yesCase.getType() != noCase.getType())
			// TODO: automatic cast!!
			throw new ExpressionBuilderException("Yes and No cases must have the same type in ternary operator.");

		return new TernaryOperatorNode(condition, yesCase, noCase);
		
	} // buildConditionalExpression
	
	//-----------------------------------------------------
	
	public static ExpressionNode buildMethodInvocationExpression(String name, java.util.List<ExpressionNode> params) throws ExpressionBuilderException {

		SymbolTableEntry entry;

		entry = symbolTable.getEntry(name);
		if (entry == null) {
			throw new ExpressionBuilderException("'" + name + "' function does not exist.");
		} // entry
		
		if (entry.getEntryType() != SymbolTable.EntryType.METHOD) {
			throw new ExpressionBuilderException("'" + name + "' is not a function name.");
		}

		SymbolTableMethodEntry methodEntry;
		methodEntry = (SymbolTableMethodEntry) entry;
	
		ActionParameterType returnedType;
		returnedType = methodEntry.getDatatype();

		List<SymbolTableEntry> parameterTypes;
		parameterTypes = methodEntry.getParameters();

		List<ExpressionNode> finalParameters;
		finalParameters = new java.util.LinkedList<ExpressionNode>();
		List<String> formalParameterNames;
		formalParameterNames = new java.util.LinkedList<String>();
		
		Iterator<ExpressionNode> realParameters;
		Iterator<SymbolTableEntry> formalParameters;
		
		formalParameters = parameterTypes.iterator();
		realParameters = params.iterator();
		
		int paramCount = 1;
		while (formalParameters.hasNext()) {
			SymbolTableEntry formalParameter;
			ExpressionNode realParameter;
			formalParameter = formalParameters.next();
			if (realParameters.hasNext())
				realParameter = realParameters.next();
			else
				realParameter = null;

			formalParameterNames.add(formalParameter.getName());
			
			// TODO: if realParameter == null
			// ("default" value), maybe we should test if
			// that makes sense...
			if ((realParameter != null) &&
			    (formalParameter.getDatatype() != realParameter.getType())) {
				// TODO: automatic datatype conversion should be considered
				// here!.
				throw new ExpressionBuilderException("Incompatible data type in parameter " +
				                                     paramCount + " in invocation to '" + name +
				                                     "' function (" + formalParameter.getDatatype() +
				                                     " expected).");
			}
			finalParameters.add(realParameter);
			
			++paramCount;
		}

		// TODO: provide formal names to the InvocationNode?
		return new InvocationNode(name, returnedType, formalParameterNames, finalParameters);

	} // buildMethodInvocationExpression
	
	//-----------------------------------------------------
	
	public static ExpressionNode buildIdentificatorExpression(String name) throws ExpressionBuilderException {

		SymbolTableEntry entry;
		
		entry = symbolTable.getEntry(name);
		if (entry == null) {
			throw new ExpressionBuilderException("'" + name + "' identifier does not exist.");
		} // entry

		if ((entry.getEntryType() != SymbolTable.EntryType.PARAMETER) &&
		    (entry.getEntryType() != SymbolTable.EntryType.GLOBAL_VAR)) {
			throw new ExpressionBuilderException("'" + name + "' should be a variable/parameter name.");
		}

		return new IdentifierAccessNode(name, entry.getDatatype());

	} // buildIdentificatorExpression
	
	//-----------------------------------------------------

	/**
	 * Symbol table used in the class.
	 */
	public static SymbolTable symbolTable;

	//-----------------------------------------------------
	//          Automatic casting information
	//-----------------------------------------------------

	/**
	 * It returns true if two mmpm types can be
	 * compared with <tt>==</tt> of <tt>!=</tt>.
	 * 
	 * @param lhs Left hand side
	 * @param rhs Right hand side
	 * @return True if <tt>==</tt> and <tt>!=</tt> can
	 * be aplied to these mmpm types.
	 */
	protected static boolean equalityValid(ActionParameterType lhs, ActionParameterType rhs) {
		Set<ActionParameterType> types;
		types = _equality.get(lhs);
		return types.contains(rhs);
	}

	/**
	 * It returns true if two mmpm types can be
	 * compared with a relational operator (<tt>&lt</tt>,
	 * <tt>&gt;</tt>, ...).
	 * 
	 * @param lhs Left hand side
	 * @param rhs Right hand side
	 * @return True if a relational operator can
	 * be aplied to these mmpm types.
	 */
	protected static boolean relationalValid(ActionParameterType lhs, ActionParameterType rhs) {
		Set<ActionParameterType> types;
		types = _comparable.get(lhs);
		return types.contains(rhs);
	}

	/**
	 * It returns true if two mmpm types can be
	 * added or substracted.
	 * 
	 * @param lhs Left hand side
	 * @param rhs Right hand side
	 * @return True if <tt>+</tt> or <tt>-</tt> can berelational operator can
	 * * applied to these mmpm types.
	 */
	protected static boolean additionValid(ActionParameterType lhs, ActionParameterType rhs) {
		Set<ActionParameterType> types;
		types = _addable.get(lhs);
		return types.contains(rhs);
	}

	/**
	 * It returns true if two mmpm types can be
	 * multiplied or divided.
	 * 
	 * @param lhs Left hand side
	 * @param rhs Right hand side
	 * @return True if <tt>+</tt> or <tt>-</tt> can berelational operator can
	 * * applied to these mmpm types.
	 */
	protected static boolean multiplicationValid(ActionParameterType lhs, ActionParameterType rhs) {
		Set<ActionParameterType> types;
		types = _multiplicable.get(lhs);
		return types.contains(rhs);
	}

	/**
	 * Hash table that stores whether a type can be
	 * compared with equality operators (<tt>==</tt> and
	 * <tt>!=</tt>) with other types.
	 * It is initialized in the static initializer.
	 */
	protected static Dictionary<ActionParameterType,Set<ActionParameterType>> _equality;

	/**
	 * Hash table that stores whether a type can be
	 * compared with relational operators (<tt>&lt;</tt>,
	 * <tt>&gt;</tt>, ...) with other types.
	 * It is initialized in the static initializer.
	 */
	protected static Dictionary<ActionParameterType,Set<ActionParameterType>> _comparable;

	/**
	 * Hash table that stores whether a type can be
	 * added (or substracted) with other types.
	 * It is initialized in the static initializer.
	 */
	protected static Dictionary<ActionParameterType,Set<ActionParameterType>> _addable; 

	/**
	 * Hash table that stores whether a type can be
	 * multiplied (or divided) with other types.
	 * It is initialized in the static initializer.
	 */
	protected static Dictionary<ActionParameterType,Set<ActionParameterType>> _multiplicable;
	
	static {
		Set<ActionParameterType> types;
		//----------------------------
		// == and !=
		//----------------------------
		_equality = new java.util.Hashtable<ActionParameterType, Set<ActionParameterType>>();
		// ENTITY_TYPE
		types = java.util.EnumSet.of(ActionParameterType.ENTITY_TYPE);
		_equality.put(ActionParameterType.ENTITY_TYPE, types);
		
		// ENTITY_ID
		types = java.util.EnumSet.of(ActionParameterType.ENTITY_ID);
		_equality.put(ActionParameterType.ENTITY_ID, types);
		
		// PLAYER
		types = java.util.EnumSet.of(ActionParameterType.PLAYER);
		_equality.put(ActionParameterType.PLAYER, types);
		
		// COORDINATE
		types = java.util.EnumSet.of(ActionParameterType.COORDINATE);
		_equality.put(ActionParameterType.COORDINATE, types);
		
		// DIRECTION
		types = java.util.EnumSet.of(ActionParameterType.DIRECTION);
		_equality.put(ActionParameterType.DIRECTION, types);
		
		// INTEGER
		types = java.util.EnumSet.of(ActionParameterType.INTEGER,
		                             ActionParameterType.FLOAT);
		_equality.put(ActionParameterType.INTEGER, types);
		
		// STRING
		types = java.util.EnumSet.of(ActionParameterType.STRING);
		_equality.put(ActionParameterType.STRING, types);
		
		// BOOLEAN
		types = java.util.EnumSet.of(ActionParameterType.BOOLEAN);
		_equality.put(ActionParameterType.BOOLEAN, types);
		
		// FLOAT
		types = java.util.EnumSet.of(ActionParameterType.INTEGER,
                                     ActionParameterType.FLOAT);
		_equality.put(ActionParameterType.FLOAT, types);
		
		//----------------------------
		// <, <=, ...
		//----------------------------
		_comparable = new java.util.Hashtable<ActionParameterType, Set<ActionParameterType>>();
		// ENTITY_TYPE
		types = java.util.EnumSet.noneOf(ActionParameterType.class);
		_comparable.put(ActionParameterType.ENTITY_TYPE, types);
		
		// ENTITY_ID
		types = java.util.EnumSet.noneOf(ActionParameterType.class);
		_comparable.put(ActionParameterType.ENTITY_ID, types);
		
		// PLAYER
		types = java.util.EnumSet.noneOf(ActionParameterType.class);
		_comparable.put(ActionParameterType.PLAYER, types);
		
		// COORDINATE
		types = java.util.EnumSet.noneOf(ActionParameterType.class);
		_comparable.put(ActionParameterType.COORDINATE, types);
		
		// DIRECTION
		types = java.util.EnumSet.noneOf(ActionParameterType.class);
		_comparable.put(ActionParameterType.DIRECTION, types);
		
		// INTEGER
		types = java.util.EnumSet.of(ActionParameterType.INTEGER,
                ActionParameterType.FLOAT);
		_comparable.put(ActionParameterType.INTEGER, types);
		
		// STRING
		types = java.util.EnumSet.of(ActionParameterType.STRING);
		_comparable.put(ActionParameterType.STRING, types);

		// BOOLEAN
		types = java.util.EnumSet.noneOf(ActionParameterType.class);
		_comparable.put(ActionParameterType.BOOLEAN, types);
		
		// FLOAT
		types = java.util.EnumSet.of(ActionParameterType.INTEGER,
                ActionParameterType.FLOAT);
		_comparable.put(ActionParameterType.FLOAT, types);
		
		//----------------------------
		// + and -
		//----------------------------
		_addable = new java.util.Hashtable<ActionParameterType, Set<ActionParameterType>>();
		// ENTITY_TYPE
		types = java.util.EnumSet.noneOf(ActionParameterType.class);
		_addable.put(ActionParameterType.ENTITY_TYPE, types);
		
		// ENTITY_ID
		types = java.util.EnumSet.noneOf(ActionParameterType.class);
		_addable.put(ActionParameterType.ENTITY_ID, types);
		
		// PLAYER
		types = java.util.EnumSet.noneOf(ActionParameterType.class);
		_addable.put(ActionParameterType.PLAYER, types);
		
		// COORDINATE
		types = java.util.EnumSet.of(ActionParameterType.COORDINATE);
		_addable.put(ActionParameterType.COORDINATE, types);
		
		// DIRECTION
		types = java.util.EnumSet.noneOf(ActionParameterType.class);
		_addable.put(ActionParameterType.DIRECTION, types);
		
		// INTEGER
		types = java.util.EnumSet.of(ActionParameterType.INTEGER,
		                             ActionParameterType.FLOAT);
		_addable.put(ActionParameterType.INTEGER, types);
		
		// STRING
		types = java.util.EnumSet.noneOf(ActionParameterType.class);
		_addable.put(ActionParameterType.STRING, types);
		
		// BOOLEAN
		types = java.util.EnumSet.of(ActionParameterType.BOOLEAN);
		_addable.put(ActionParameterType.BOOLEAN, types);
		
		// FLOAT
		types = java.util.EnumSet.of(ActionParameterType.INTEGER,
                                     ActionParameterType.FLOAT);
		_addable.put(ActionParameterType.FLOAT, types);

		//----------------------------
		// * and /
		//----------------------------
		
		_multiplicable = new java.util.Hashtable<ActionParameterType, Set<ActionParameterType>>();
		// ENTITY_TYPE
		types = java.util.EnumSet.noneOf(ActionParameterType.class);
		_multiplicable.put(ActionParameterType.ENTITY_TYPE, types);
		
		// ENTITY_ID
		types = java.util.EnumSet.noneOf(ActionParameterType.class);
		_multiplicable.put(ActionParameterType.ENTITY_ID, types);
		
		// PLAYER
		types = java.util.EnumSet.noneOf(ActionParameterType.class);
		_multiplicable.put(ActionParameterType.PLAYER, types);
		
		// COORDINATE
		types = java.util.EnumSet.noneOf(ActionParameterType.class);
		_multiplicable.put(ActionParameterType.COORDINATE, types);
		
		// DIRECTION
		types = java.util.EnumSet.noneOf(ActionParameterType.class);
		_multiplicable.put(ActionParameterType.DIRECTION, types);
		
		// INTEGER
		types = java.util.EnumSet.of(ActionParameterType.INTEGER,
		                             ActionParameterType.FLOAT,
		                             ActionParameterType.DIRECTION,
		                             ActionParameterType.COORDINATE);
		_multiplicable.put(ActionParameterType.INTEGER, types);
		
		// STRING
		types = java.util.EnumSet.noneOf(ActionParameterType.class);
		_multiplicable.put(ActionParameterType.STRING, types);
		
		// BOOLEAN
		types = java.util.EnumSet.of(ActionParameterType.BOOLEAN);
		_multiplicable.put(ActionParameterType.BOOLEAN, types);
		
		// FLOAT
		types = java.util.EnumSet.of(ActionParameterType.INTEGER,
                ActionParameterType.FLOAT,
                ActionParameterType.DIRECTION,
                ActionParameterType.COORDINATE);
		_multiplicable.put(ActionParameterType.FLOAT, types);

	} // static initializer
	
} // class ExpressionBuilder
