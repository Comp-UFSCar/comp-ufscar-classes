""" T2 ORI UFSCAR 2014

    Alunos:
        Thales Eduardo Adair Menato     407976

    * Operacoes de insercao e busca
    * Tratamento de colisoes atraves de sondagem linear (linear probing)

        Sondagem linear
            funcao da sondagem: rh(i) = i
            sondagens a partir da posicao k: k+1, k+2, k+3...

    * Entrada: numeros inteiros positivos e sem repeticao de chaves

"""
__author__ = 'thales'
from __builtin__ import range
#biblioteca utilizada para gerar numeros aleatorios
import numpy as np

""" Variaveis para configuracao
"""
# debugMode: mudar para True caso queira informacoes das passagens
debugMode = False
# Tamanho da Tabela Hash (utilizar numero primo)
# Lista de numeros primos: https://en.wikipedia.org/wiki/List_of_prime_numbers
# Alguns valores:   13 , 1069 , 10007 , 100103 ,ba 1000003
tamanhoTabela = 13
# Inicializacao da Tabela Hash com False em todas as posicoes
tabelaHash = [False for i in range(tamanhoTabela)]
chavesNaTabela = 0
# Valor maximo do intervalo de numeros aleatorios
valorMaximo = tamanhoTabela * 2


def funcaoHash(chave):
    """ Funcao Hash
        Entrada:    chave (valor inteiro positivo)
                    tabelaHash onde sera realizado a operacao
        Descricao:  Recebe uma chave e faz a insercao na tabela hash
                    verificando primeiramente se a chave eh valida:
                        - ja nao foi inserida (nao permite repeticoes)
                    caso a chave seja invalida, eh gerado uma nova chave
                    aleatoriamente ateh que seja valida.
                    Sendo valida, eh calculado a posicao a qual ela deve
                    ser inserida:
                        h(chave) = chave % tamanhoTabela
                    se ja existe uma chave naquela posicao, temos o
                    tratamento da colisao atraves de sondagem linear
                        posicao += 1
                    ateh que seja encontrado uma posicao vazia
    """
    # Verificacao se chave eh um valor valido
    # Valor gerado da chave ja esta inserido, nao aceita repeticoes
    if chave in tabelaHash:
        # gerar um valor valido para chave
        while chave in tabelaHash:
            chave = np.random.random_integers(1, valorMaximo)
        if debugMode:
            print "Ja existe -- nova chave valida gerada"
    # calculo da posicao h(c) = c % tamanho
    posicao = chave % tamanhoTabela
    # Insercao na tabela Hash
    if tabelaHash[posicao] is False:
        # se a posicao estiver vazia -> disponivel
        tabelaHash[posicao] = chave
        if debugMode:
            print("Chave " + str(chave) +
                  " inserida em " + str(posicao) + " SEM colisao.")
    else:
        # a posicao ja esta ocupada
        # calcula sondagem ate encontrar posicao vazia
        while tabelaHash[posicao] is not False:
            posicao = (posicao + 1) % tamanhoTabela
        tabelaHash[posicao] = chave
        if debugMode:
            print("Chave " + str(chave) +
                  " inserida em " + str(posicao) + " COM colisao.")
    global chavesNaTabela
    chavesNaTabela += 1


def populaTabela(percentagem):
    """ Funcao Popula Tabela
        Entrada:    tabelaHash a ser populada
                    percentagem desejada a ser preenchida
        Descricao:  Enquanto a percentagem de ocupacao da tabela hash
                    for menor que a desejada, inserir nova chave
    """
    global chavesNaTabela
    # A tabela ja foi populada previamente, zerar ela primeramente
    for i in range(tamanhoTabela):
        tabelaHash[i] = False
    chavesNaTabela = 0

    # Realizer insercao ateh que percentagem seja atingida
    while percentagem > ((chavesNaTabela * 100) / tamanhoTabela):
        funcaoHash(np.random.random_integers(1, valorMaximo))
        #print "Atual: " + str(((chavesNaTabela * 100) / tamanhoTabela))


def buscaChave(chave):
    """ Funcao Busca Chave
        Entrada:    chave a ser buscada
                    tabela hash onde a busca sera realizada
        Descricao:  Buscar chave na tabela hash, utilizando a funcao
                    hash como busca.
        Retorno:    Numero de sondagens para aquela busca.
    """
    nroSondagens = 0
    # calculo da posicao h(c) = c % tamanho
    posicao = chave % tamanhoTabela
    # realiza a sondagem enquanto o valor na posicao nao for falso
    while tabelaHash[posicao] is not False:
        if tabelaHash[posicao] is chave:
            # chave encontrada, retornar numero de sondagens
            return nroSondagens
        else:
            # chave nao encontrada, buscar na proxima posicao
            posicao = (posicao + 1) % tamanhoTabela
            nroSondagens += 1
    # a chave nao esta inserida nesta tabelaHash
    return nroSondagens


def calculaSondagem(bemSucedida):
    """ Funcao Calcula Sondagem
        Entrada:    Valor inteiro 0 caso queira sondagem bem sucedida
                    Qualquer outro valor para sondagem mal sucedida
        Descricao:  Sondagem Bem Sucedida
                    *   Realizar uma busca com todas as chaves existentes
                        na tabela e ir armazenando o numero de sondagem
                        realizado em cada uma dessas buscas.
                        Ao final, retornar a media destas sondagens
                    Sondagem Mal Sucedida
                    *   Realizar uma busca 5 vezes maior que a quantidade
                        de elementos presentes na tabela com valores
                        nao presentes na mesma.
                        Armazenar toda a sondagem realizada e ao final
                        retornar a media destas sondagens
        Retorno:    media de sondagens
    """
    sondagem = []
    # Sondagem Bem Sucedida
    if bemSucedida is 0 and chavesNaTabela is not 0:
        for chave in tabelaHash:
            if chave is not False:
                sondagem.append(buscaChave(chave))
        # retorna a soma das sondagens dividida pelo numero de consultas
        # sendo o numero de consultas o tamanho da lista de sondagem
        return float(np.sum(sondagem)) / float(len(sondagem))
    # Sondagem Mal Sucedida
    elif chavesNaTabela is not 0:
        # Realizar a busca em 5 vezes qtd de chaves na tabela
        for i in range(chavesNaTabela * 5):
            # Aleatoriamente buscar uma chave que nao existe na tabela
            chave = np.random.random_integers(1, valorMaximo)
            while chave in tabelaHash:
                chave = np.random.random_integers(1, valorMaximo)
            # Adicionar numero de sondagens na lista
            sondagem.append(buscaChave(chave))
        # Retorna a soma das sondagens dividida pelo numero de consultas
        return float(np.sum(sondagem)) / float(len(sondagem))


# Dicionario utilizado como MENU
def sair():
    print "Ate logo"


def popular_tabela():
    print "\nPopular tabela, digite a percentagem desejada: "
    populaTabela(input())


def buscar_chave():
    print "\nBuscar chave, digite a chave que deseja buscar: "
    print "Sondagens: " + str(buscaChave(input()))


def exibir_tabela():
    print tabelaHash


def calcular_sondagem():
    print "\nCalcular Sondagem: Digite qual tipo quer calcular\n" \
          "\tpara Bem Sucedidade: 0\n" \
          "\tpara Mal Sucedida: 1"
    print calculaSondagem(input())


options = {
    0: sair,
    1: popular_tabela,
    2: buscar_chave,
    3: exibir_tabela,
    4: calcular_sondagem,
}


""" Metodo main
"""
if __name__ == '__main__':

    # Realizar as operacoes de modo 'manual' direto no codigo
    if debugMode:
        print "Debug Mode ativado!\n" \
              "Informacoes adicionais SERAO exibidas no terminal!\n" \
              "Tamanho: " + str(tamanhoTabela)

        print "Tabela Hash vazia:\n\t" + str(tabelaHash)

        # popula a tabela com o valor desejado em percentagem
        populaTabela(75)

        print "Tabela Hash apos ser populada:\n\t" + str(tabelaHash)
        print "Percentagem populada: " + \
            str((chavesNaTabela * 100) / tamanhoTabela)

        # Realiza a busca de um range(n) de chaves geradas aleatoriamente
        for i in range(5):
            buscaChave(np.random.random_integers(1, valorMaximo))

    # Fora do modo debug, utilizar 'menu interativo'
    else:
        op = -1
        while op is not 0:
            print "T2 ORI UFSCAR 2014\n" \
                  "\t1) Popular a tabela\n" \
                  "\t2) Buscar chave\n" \
                  "\t3) Exibir Tabela\n" \
                  "\t4) Calcular Sondagem\n" \
                  "\t0) Sair\n" \
                  "Digite a opcao desejada: "
            op = input()
            options[op]()
