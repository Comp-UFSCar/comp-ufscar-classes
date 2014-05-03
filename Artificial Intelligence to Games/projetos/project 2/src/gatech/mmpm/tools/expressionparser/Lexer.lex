/*
  ----------------------------------------------
                    USER CODE
  ----------------------------------------------
 */
package gatech.mmpm.tools.expressionparser;

import java_cup.runtime.*;
%%

/*
  ----------------------------------------------
          OPTIONS AND DECLARATION SECTION
  ----------------------------------------------
 */

/* Generated class name */

%class Lexer

/* Indicar funcionamiento autonomo*/
/*%standalone*/

/* 8 bits ASCII input */
%8bit

/*
   Turn line and column counting on. We will be able
   to use yyline() and yycolumn() to know the
   position of the current token
*/
%line
%column

/*
   Switches to CUP compatibility mode to interface with a CUP generated parser.
*/
%cup


/*
   Declarations. %{ ... %} will be copied verbatim into the generated code.
*/

%{

	/**
	 * String buffer where strings are built keeping into account
	 * escape characters.
	 */
	StringBuffer string = new StringBuffer();

	/**
	 * A new java_cup.runtime.Symbol is created, with no attached information.
	 */
	private Symbol symbol(int type){
		return new Symbol(type,yyline,yycolumn);
	}

	/**
	 * A new java_cup.runtime.Symbol is created, with an object
	 * as attached information.
	 */
	private Symbol symbol(int type,Object value){
		return new Symbol(type,yyline,yycolumn,value);
	}
%}

/*
   Macros
*/ 

/* LineTerminator matches Windows/Linux/MAC EOL*/
LineTerminator = \r|\n|\r\n

/* Separator (WhiteSpace) includes line terminator, and classical spaces */
WhiteSpace     = {LineTerminator} | [ \t\f]

/* Input character is anything but line terminator (used for comments) */
InputCharacter = [^\r\n]

/* Comments */
TraditionalComment = "/*" [^*] ~"*/" | "/*" "*" + "/"
EndOfLineComment = "//"{InputCharacter}* {LineTerminator}

Comment = {TraditionalComment} | {EndOfLineComment}

/* Identifier is nearly any valid letter string */
Identifier = [:jletter:] [:jletterdigit:]*

/* Any decimal digit */
Digit = [0-9]

/* Any hex digit */
HexDigit = [a-fA-F0-9]

/* Decimal integer number (010 is an octal number) */
DecIntLiteral = 0 | [1-9]{Digit}*

/* Octal integer number (starting with 0) */
OctIntLiteral = 0{Digit}*

/* Hexadecimal integer number (starting with 0x) */
HexIntLiteral = 0[xX]{HexDigit}+

/* Exponent for floating point numbers. */
Exponent = [Ee][+-]?{Digit}+

/* Boolean literal */
BooleanLiteral = "true" | "false"



/* State while parsing a string (rounded by ".." */
%state STRING


%%
/*
  ----------------------------------------------
                   LEXICAL RULES
  ----------------------------------------------
 */

<YYINITIAL> {

  {DecIntLiteral}   { return symbol(sym.INTEGER_LITERAL, new Integer(Integer.parseInt(yytext()))); }

  {OctIntLiteral}   { return symbol(sym.INTEGER_LITERAL, new Integer(Integer.parseInt(yytext(), 8))); }

  {HexIntLiteral}   { return symbol(sym.INTEGER_LITERAL, new Integer(Integer.parseInt(yytext().substring(2), 16))); }
                      /* substring(2) due to the "0x" prefix, that must be removed */

  {BooleanLiteral}  { return symbol(sym.BOOLEAN_LITERAL, new Boolean(Boolean.parseBoolean(yytext()))); }

  {Digit}*"."{Digit}+({Exponent})? { return symbol(sym.FLOAT_LITERAL, new Float(Float.parseFloat(yytext()))); }
  {Digit}+"."{Digit}*({Exponent})? { return symbol(sym.FLOAT_LITERAL, new Float(Float.parseFloat(yytext()))); }

  /* When a " is found, the lexer state changes to STRING, so escape characters
     are correctly parsed. See <STRING> state below.
   */
  \"                { string.setLength(0); yybegin(STRING); }

  /* Identifiers */
  {Identifier}      { return symbol(sym.IDENTIFIER, yytext()); }


  /* Separators */
  ","               { return symbol(sym.COMMA); }
  "("               { return symbol(sym.LPAR); }
  ")"               { return symbol(sym.RPAR); }
  "\?"               { return symbol(sym.QUESTION); }
  ":"               { return symbol(sym.COLON); }
  "="               { return symbol(sym.EQUAL); }
  /* { and } for coordinates ? */

  
  /* Arithmetic operators */
  "+"               { return symbol(sym.ADD_OP, OperatorToken.ADD); }
  "-"               { return symbol(sym.ADD_OP, OperatorToken.MINUS); }
  
  "*"               { return symbol(sym.MULT_OP, OperatorToken.MULT); }
  "/"               { return symbol(sym.MULT_OP, OperatorToken.DIV); }
  "%"               { return symbol(sym.MULT_OP, OperatorToken.MOD); }

  /* Logical operators */
  "&&"               { return symbol(sym.AND_OP, OperatorToken.AND); }
  "||"               { return symbol(sym.OR_OP, OperatorToken.OR); }

  /* Equality operators */
  "=="               { return symbol(sym.EQ_OP, OperatorToken.EQ); }
  "!="               { return symbol(sym.EQ_OP, OperatorToken.NE); }

  /* Relational operators */
  "<"                { return symbol(sym.RELATIONAL_OP, OperatorToken.LT); }
  "<="               { return symbol(sym.RELATIONAL_OP, OperatorToken.LE); }
  ">"                { return symbol(sym.RELATIONAL_OP, OperatorToken.GT); }
  ">="               { return symbol(sym.RELATIONAL_OP, OperatorToken.GE); }

  /* Unary operators */
  "!"               { return symbol(sym.NOT_OP, OperatorToken.NOT); }

  /* Ignored input */
  
  {WhiteSpace}      { /* Ignore */ }
  {Comment}         { /* Ignore */ }

  /* Anything */
  . { System.err.println("TODO: ilegal token <" + yytext() + " line " + (yyline + 1) + " column " + (yycolumn + 1)); }

}


/* STRING State (while parsing a string, starting with \" */

<STRING> {
  /* Final " found. The string has ended. */
  \" { yybegin(YYINITIAL);
       return symbol(sym.STRING_LITERAL, string.toString());
     }
  /* Any common letter, will be appended to the building string */
  [^\n\r\"\\]+ { string.append( yytext() ); }

  /* "\t" will be converted to the tab character in the string */
  \\t { string.append('\t'); }

  /* "\n" will be converted to the new line character in the string */
  \\n { string.append('\n'); }

  /* "\r" will be converted to the carriadge return character in the string */
  \\r { string.append('\r'); }

  /* "\"" will be converted to the " character in the string */
  \\\" { string.append('\"'); }

  /* "\\" will be converted to the \ character in the string */
  \\ { string.append('\\'); }

}

