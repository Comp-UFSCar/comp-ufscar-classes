add_ultimo(X,[ ],[X]).
add_ultimo(X, [X1|Y],[X1|L]) :- add_ultimo(X,Y,L).
