/*
 * Declaracao da gramatica da linguagem de programacao LUA.
 *
 * Data de criacao: 12/07/2013
 * Ultima modificacao: 12/07/2013
 *
 * Grupo:
 * Fabio Gunkel         316520
 * Lucas Oliveira David 407917
 *
 */
 
grammar Luazinha;


@members{
    PilhaDeTabelas pilhaDeTabelas = new PilhaDeTabelas();
}

programa
    :
        { pilhaDeTabelas.empilhar(new TabelaDeSimbolos("global")); }
        trecho
        { pilhaDeTabelas.desempilhar(); }
    ;

trecho
    : (comando ';'?])* (ultimocomando ';'?)?
    ;

bloco
    : trecho
    ;

comando
    :  listavar '=' listaexp 
           
           // adiciona variaveis ao ultimo escopo definido
           // caso ela ainda nao tenha sido declarada
           // em um dos escopos anteriores
           // isso deve vir abaixo de ** '=' listaexp **, para 
           // que erros como j = j +1 sejam identificados
           { for (String nome : $listavar.nomes)
                if (! pilhaDeTabelas.existeSimbolo(nome))
                   pilhaDeTabelas.topo().adicionarSimbolo(nome, "variavel"); }
        |  chamadadefuncao
        |  'do' bloco 'end'
        |  'while' exp 'do' bloco 'end'
        |  'repeat' bloco 'until' exp
        |  'if' exp 'then' bloco ('elseif' exp 'then' bloco)* ('else' bloco)? 'end'
        |  'for'
           
           // empilha escopo de for
           { pilhaDeTabelas.empilhar(new TabelaDeSimbolos("for")); }
           
           NOME // adiciona NOME ao escopo for, caso ele nao esteja def.
           { if (! pilhaDeTabelas.existeSimbolo($NOME.getText()))
                pilhaDeTabelas.topo().adicionarSimbolo($NOME.getText(), "variavel"); }
                
           '=' exp ',' exp (',' exp)? 'do' bloco
           
           { pilhaDeTabelas.desempilhar(); }
           
           'end'
        |  'for'
           
           // empilha escopo de for
           { pilhaDeTabelas.empilhar(new TabelaDeSimbolos("for")); }
           
           listadenomes
           'in' listaexp 
           
           // adiciona variaveis ao escopo for
           // isso deve vir abaixo de ** listaexp **, para 
           // que erros como for j in j +1 sejam identificados
           // e apos ** 'do' bloco ** para que as variaveis antes de 'in'
           // possam ser utilizadas dentro do escopo do for
           { pilhaDeTabelas.topo().adicionarSimbolos($listadenomes.nomes, "variavel"); }
           
           'do' bloco
           { pilhaDeTabelas.desempilhar(); }
           
           'end'
        |  'function' nomedafuncao
        
           // cria novo escopo com o nome da funcao
           { pilhaDeTabelas.empilhar(new TabelaDeSimbolos($nomedafuncao.nome)); 
            
             if ($nomedafuncao.metodo)
                pilhaDeTabelas.topo().adicionarSimbolo("self", "parametro"); }
           corpodafuncao 
           { pilhaDeTabelas.desempilhar(); }
           
        | 'local' 'function' NOME
           
           // cria novo escopo com o nome da funcao
           { pilhaDeTabelas.empilhar(new TabelaDeSimbolos($NOME.getText())); }
           corpodafuncao
           { pilhaDeTabelas.desempilhar(); }
           
        |  'local' listadenomes 
           ('=' listaexp)?
           
           // adiciona variaveis ao ultimo escopo isso deve 
           // vir abaixo de ** ('=' listaexp)? **, para 
           // que erros como j = j +1 sejam identificados
           { pilhaDeTabelas.topo().adicionarSimbolos($listadenomes.nomes, "variavel"); }
        ;

ultimocomando : 'return' (listaexp)? | 'break'
              ;

nomedafuncao returns [ String nome, boolean metodo ]
@init { $metodo = false; }
    : n1=NOME { $nome = $n1.getText(); }
      ('.' n2=NOME { $nome += "." + $n2.getText(); })*
      (':' n3=NOME { $metodo = true; $nome += "." + $n3.getText(); })?
    ;

listavar returns [ List<String> nomes ]
@init { $nomes = new ArrayList<String>(); }
    : v1=var { $nomes.add($v1.nome); }
      (',' v2=var { $nomes.add($v2.nome); } )*
    ;

var returns [ String nome, int linha, int coluna ]
    :  NOME { $nome = $NOME.getText(); $linha = $NOME.line; $coluna = $NOME.pos; } 
    |  expprefixo '[' exp ']'
    |  expprefixo '.' NOME
    ;

listadenomes returns [ List<String> nomes ]
@init{ $nomes = new ArrayList<String>(); }
    : n1=NOME { $nomes.add($n1.getText()); }
      (',' n2=NOME { $nomes.add($n2.getText()); } )*
    ;

listaexp : (exp ',')* exp
         ;

exp :  'nil' | 'false' | 'true' | NUMERO | CADEIA | '...' | funcao | 
       expprefixo2 | construtortabela | exp opbin exp | opunaria exp 
    ;

expprefixo : NOME ( '[' exp ']' | '.' NOME )*
           ;

expprefixo2 : var
              // aqui uma variavel sera utilizada em
              // comparacoes atribuicoes etc. Verifica-se
              // se ela ja foi  amarrada a algum escopo
              { if (! pilhaDeTabelas.existeSimbolo($var.nome))
                Mensagens.erroVariavelNaoExiste($var.linha, $var.coluna, $var.nome); }
            | chamadadefuncao | '(' exp ')'
           ;

chamadadefuncao :  expprefixo args |
                   expprefixo ':' NOME args
                ;

args :  '(' (listaexp)? ')' | construtortabela | CADEIA 
     ;

funcao : 'function' corpodafuncao
       ;

corpodafuncao : '(' (listapar)? ')' bloco 'end'
              ;

listapar : listadenomes (',' '...')? 
           { pilhaDeTabelas.topo().adicionarSimbolos($listadenomes.nomes, "parametro"); }
         | '...'
         ;

construtortabela : '{' (listadecampos)? '}'
                 ;

listadecampos : campo (separadordecampos campo)* (separadordecampos)?
              ;

campo : '[' exp ']' '=' exp | NOME '=' exp | exp
      ;

separadordecampos : ',' | ';'
                  ;

opbin : '+' | '-' | '*' | '/' | '^' | '%' | '..' | '<' | 
        '<=' | '>' | '>=' | '==' | '~=' | 'and' | 'or'
      ;

opunaria : '-' | 'not' | '#'
         ;

NOME	:	('a'..'z' | 'A'..'Z' | '_') ('a'..'z' | 'A'..'Z' | '0'..'9' | '_')*;
CADEIA	:	'\'' ~('\n' | '\r' | '\'')* '\'' | '"' ~('\n' | '\r' | '"')* '"';
NUMERO	:	('0'..'9')+ EXPOENTE? | ('0'..'9')+ '.' ('0'..'9')* EXPOENTE?
		| '.' ('0'..'9')+ EXPOENTE?;
fragment
EXPOENTE	:	('e' | 'E') ( '+' | '-')? ('0'..'9')+;
COMENTARIO
	:	'--' ~('\n' | '\r')* '\r'? '\n' {skip();};
WS	:	(' ' | '\t' | '\r' | '\n') {skip();};
