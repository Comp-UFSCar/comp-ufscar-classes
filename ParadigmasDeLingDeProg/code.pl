pai_de(henrique_pai, henrique).
pai_de(henrique_pai, maria).
pai_de(henrique, elizabeth2).
pai_de(henrique, eduardo).
homem(henrique_pai).
homem(henrique).
homem(eduardo).
mulher(catarina).
mulher(elizabeth1).
mulher(maria).
mulher(elizabeth2).
mulher(ana).
mulher(jane).
mae_de(catarina, maria).
mae_de(ana, elizabeth2).
mae_de(jane, eduardo).
mae_de(elizabeth1, henrique).

filho_de(Y, X) :­- pai_de(X,Y), homem(Y).
pai_ou_mae(X, Y) :-­ pai_de(X,Y).
pai_ou_mae(X, Y) :­- mae_de(X,Y).
predecessor(X, Y) :­- pai_ou_mae(X,Y).
predecessor(X, Y) :­- pai_ou_mae(X,Z), predecessor(Z,Y).
