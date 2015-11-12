/*
*                      ^(;,;)^
*   "Ph'nglui mglw'nafh Cthulhu R'lyeh wgah'nagl fhtagn"
*
* Alunos: 
*       João Vitor Brandão Moreira  -   407496
*       Thales Eduardo Adair Menato -   407976
*
*/

grammar Lua;

/*   Analisador Léxico   */

/*
*   Todas as palavras que representam alguma funcao/operacao
*   da linguagem LUA
*/

PALAVRAS_RESERVADAS
    : ('and'|'break'|'do'|'else'|'elseif'|'end'|'false'|
       'for'|'function'|'if'|'in'|'local'|'nil'|'not'|'or'|
       'repeat'|'return'|'then'|'true'|'until'|'while')
    ;

/*
*   Simbolos utilizados como operadores ou reservados para
*    comentario, finalizar bloco/funcao, etc
*/
SIMBOLOS_RESERVADOS
    : ('+'|'-'|'*'|'/'|'%'|'^'|'#'|'=='|'~='|'<='|'>='|
       '<'|'>'|'='|'('|')'|'{'|'}'|'['|']'|';'|':'|
       ','|'.'|'..'|'...')
    ;

/*
 * NOME pode ser qualquer cadeia de letras, dígitos, e sublinhados
 * que não comecam com um dígito.
 */
NOME
    :   ('_'|'a'..'z'|'A'..'Z') ('_'|'a'..'z'|'A'..'Z'|'0'..'9')*
    ;

/*
*   Numeros em geral, podendo ser inteiros ou reais
*/
NUMERO
    :   ('0'..'9')('0'..'9')*('.')?('0'..'9')*
    ;

/*
*   Cadeia é um conjunto de letras, números e . dentro de aspas simples
*/
CADEIA
    : ('\'')(' '|'.'|'_'|'a'..'z'|'A'..'Z'|'0'..'9')*('\'')
    ;

/*
*   Tokens que serão descartados
*/
COMENTARIO 
    : '--' ~('\n'|'\r')* '\r'? '\n' {skip();}
    ; 

WS 
    :   ( ' ' |'\t' | '\r' | '\n') {skip();}
    ;


/*   Analisador Sintático   */

/* Utilizar o nome programa para que haja compatibilidade
 * entre os arquivos de teste java fornecidos pelo professor.
 */
programa
    : trecho
    ;

/*
 * Transformacao da gramatica do LUA de BNF estendida para ANTLR 
 */
trecho
    : (comando ';'?)* (ultimocomando ';'?)? ;

bloco
    :   trecho ;

comando
    : listavar '=' listaexp |
      chamadadefuncao |
      'do' bloco 'end' |
      'while' exp 'do' bloco 'end' |
      'repeat' bloco 'until' exp |
      'if' exp 'then' bloco ('elseif' exp 'then' bloco)* ('else' bloco)? 'end' |
      'for' NOME '=' exp ',' exp (',' exp)? 'do' bloco 'end' |
      'for' listadenomes 'in' listaexp 'do' bloco 'end' |
      'function' nomedafuncao corpodafuncao |
      'local' 'function' NOME corpodafuncao |
      'local' listadenomes ('=' listaexp)?
    ;

ultimocomando
    : 'return' listaexp? | 'break' ;

nomedafuncao
    : NOME{TabelaDeSimbolos.adicionarSimbolo($NOME.text, Tipo.FUNCAO);} ('.' NOME)* (':' NOME)? ;//adiciona o nome da função à tabela de símbolos

listavar
    : var{TabelaDeSimbolos.adicionarSimbolo($var.text, Tipo.VARIAVEL);} (',' var{TabelaDeSimbolos.adicionarSimbolo($var.text, Tipo.VARIAVEL);})* ;
    //Adiciona a variável à tabela de símbolos

var
    : NOME 
    | expprefixo '[' exp ']' 
    | expprefixo '.' NOME
    ;
   
listadenomes
    : NOME (',' NOME)* ;

listaexp
    : (exp ',')* exp ;

exp
    : 'nil' 
    | 'false' 
    | 'true ' 
    | NUMERO 
    | CADEIA 
    | '...' 
    | funcao
    | expprefixo
    | expprefixo2
    | construtortabela
    | exp opbin exp
    | opunaria exp
    ;

/*
 * Na gramatica original há recursividade mútua entre regras,
 * resolveremos utilizando uma nova regra e a original deixaremos
 * como expprefixo2
 */
expprefixo
    : NOME ( '[' exp ']' | '.' NOME )*;

expprefixo2
    : var{TabelaDeSimbolos.adicionarSimbolo($var.text, Tipo.VARIAVEL);} //adiciona a variável à tabela de símbolos
    | chamadadefuncao
    | '(' exp ')'
    ;

chamadadefuncao
    : expprefixo args
    | expprefixo ':' NOME args
    ;

args
    : '(' (listaexp)? ')'
    | construtortabela
    | CADEIA
    ;

funcao
    : 'function' corpodafuncao ;

corpodafuncao
    : '(' listapar? ')' bloco 'end' ;

listapar
    : listadenomes (',' '...')?
    | '...' 
    ;

construtortabela
    : '{' listadecampos? '}' ;

listadecampos
    : campo (separadordecampos campo)* separadordecampos? ;

campo
    : '[' exp ']' '=' exp
    | NOME '=' exp
    | exp
    ;

separadordecampos
    : ',' 
    | ';'
    ;

opbin
    : '+' | '-' | '*' | '/' | '^' | '%' | '..' |
      '<' | '<=' | '>' | '>=' | '==' | '~=' | 'and' | 'or'
    ;

opunaria
    : '-' | 'not' | '#'
    ;