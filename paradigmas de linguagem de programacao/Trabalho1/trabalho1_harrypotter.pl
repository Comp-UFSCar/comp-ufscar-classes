% Trabalho 1 PLP
% Exercicio 1
%
% Grupo:
% Fabio Gunkel  316520
% Lucas David   407917
% Pedro Vicente 407658
%
% Resposta encontrada:
% ?- resp(A).
% A = [veneno, vinho, veneno, frente, veneno, vinho, volta] .
%
% Data de criacao: 07/06/2013
% Ultima modificacao: 07/06/2013
%

% Encontra uma permutação em uma lista
permutacao([],[]).
permutacao(L,[X|P]) :- apaga(X,L,L1), permutacao(L1,P).
apaga(X,[X|L],L).
apaga(X,[Y|L],[Y|L1]) :-         apaga(X,L,L1).
 
% Encontra o índice de um dado elemento em uma lista
indice(X, [X|_], 0) :- !.
indice(X, [_|L], N) :- indice(X, L, N1), N is N1 + 1.
 
% Encontra o elemento em uma dada posição da lista
elemento(X, [X|_], 0) :- !.
elemento(X, [_|L], N) :- N1 is N - 1, elemento(X, L, N1).
 
% Retorna uma sublista a partir de uma posição fornecida
% Este predicado pode ser útil para implementar a recursividade na primeira
% pista, já que nela você vai precisar testar as posições de toda ocorrência
% de uma garrafa de vinho de urtigas.
sublista(L, 0, L) :- !.
sublista([_|L1], N, L2) :- N1 is N - 1, sublista(L1, N1, L2).

% PREPOSICAO 1
% a garrafa ao lado esquerdo de todo vinho ee veneno
p1(L) :- indice(vinho, L, X),
         Z is X-1,
         elemento(Y, L, Z),
         Y == veneno.

% PREPOSICAO 2
% a primeira e ultima garrafa sao diferentes (vinho veneno ou volta)
% mas nenhuma e frente.
p2(L) :- elemento(X, L, 0),
         elemento(Y, L, 6),
         X \== Y,
         X \== frente,
         Y \== frente.

% PROPOSICAO 3
% a segunda e a quarta garrafas nao matam (nao sao veneno)
% Ordem assumida para construcao:
% grande, gigante, média, anã, pequena, quadrada, arredondada
p3(L) :- elemento(X, L, 1),
         elemento(Y, L, 3),
         X \== veneno,
         Y \== veneno.

% PREPOSICAO 4
% a segunda e a penultima sao iguais (vinho ou veneno)
p4(L) :- elemento(X, L, 1),
    	 elemento(Y, L, 5),
    	 X == Y.

resp(R) :- permutacao([veneno, veneno, veneno, vinho, vinho, frente, volta], R),
        p1(R), p2(R), p3(R), p4(R).
