#Problemas Cap-1 pag 15

# (3)
# Inserção dos dados dentro da variável erroImpressao
erroImpressao = c(8,11,8,12,14,13,11,14,14,5,6,10,14,19,6,12,7,5,8,8,10,16,10,12,12,8,11,6,7,12,7,10,14,5,12,7,9,12,11,9,14,8,14,8,12,10,12,22,7,15)
hist(erroImpressao) #representação usando um histograma
stem(erroImpressao) #representação usando ramo-e-folhas

# (5)
# Inserção dos dados dentro da variável mediaGeometrica
mediaGeometrica = c(3.67,1.82,3.73,4.10,4.30,1.28,8.14,2.43,4.17,5.36,3.96,6.54,5.84,7.35,3.63,2.93,2.82,8.45,5.28,5.41,7.77,4.65,1.88,2.12,4.26,2.78,5.54,0.90,5.09,4.07)
hist(mediaGeometrica) #representação usando um histograma
stem(mediaGeometrica,scale=1,width=80,atom=1e-08) #representação usando ramo-e-folhas