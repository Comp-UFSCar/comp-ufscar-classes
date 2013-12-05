Grupo:
Bruno Ferreira - 379301
Leonardo Bacchi - 379727
Raul Cioldin - 379468

O trabalho foi escrito e testado em ambiente:
- GNU/Linux  (Arch) x86_64, Kernel version 3.4.3-1
- gcc 4.7.1
- NetBeans 7.1.2
Dependências:
- gtkmm-3.0 (GTK bindings para C++, pacotes de desenvolvimento)
- gtk3-0 (port do GTK)
- pkg-config

Para compilar, adicione ao comando de compilação do gcc o argumento:
`pkg-config --cflags --libs gtkmm-3.0`
(exemplo: gcc -o out.exe *.cpp `pkg-config --cflags --libs gtkmm-3.0`)
Apesar de não testado, o projeto não está atrelado a nenhuma biblioteca específica para Linux,
o gtk tem um port para win32.
O makefile foi autogerado pelo netbeans. 

A arquitetura do programa é assim dividida:

- main.cpp
\ inicialização do loop do GTK3
- img.txt
\ arquivo de configuração, alimentação do dicionário de imagens
-- res
--- \ arquivos .jpg para alimetação do programa
-- src
--- ImgList.cpp ImgList.h
---\ Classe que descreve uma lista (dicionario) de imagens
--- Engine.cpp Engine.h
---\ Classe que inclui a mecânica do jogo, com a interface acoplada

Principais métodos:
ImgList:
add (path, keyword) => adiciona um novo elemento no fim da lista
remove (keyword) => itera sobre a lista para remover um elemento 
								  especificado por keyword
getCount() => retorna o valor da variável estática que conta o número de instâncias, isto é,
						o número de elementos da lista (a variável independe da classe e é inicializada
						no main)
Engine:
newGame() => inicia um novo jogo, "rebobina" o grid se o jogo já estiver em execução
loadImage() => carrega uma imagem aleatória
fillGrid() => povoa a interface com número dinâmico de botões (varia com a dificuldade)
cleanGrid() => método de conveniência, o GTK tira a referência mas não exclui 
						os componentes da memória
