soma :- write('Digite uma lista de numeros: '),
	read(Lista),
	soma(Lista, Resultado),
	soma(Resultado, Resultado, Dobro),
	write('O dobro da soma dos elemtnos da lista e = '),
	write(Dobro),
	nl.
soma([], 0).
soma([Elem|Cauda], S) :-
	soma(Cauda, S1),
	S is S1 + Elem.

soma(X, Y, Z) :-
	Z is X + Y.
