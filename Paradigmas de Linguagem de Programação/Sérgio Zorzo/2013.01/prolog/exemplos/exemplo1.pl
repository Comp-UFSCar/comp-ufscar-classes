pertence(X, [X|_]).
pertence(X, [_|Y]) :- pertence(X,Y).
