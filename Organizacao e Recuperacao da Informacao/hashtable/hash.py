#!/usr/bin/env python
# -*- coding: UTF-8 -*-

"""
    ===== Trabalho 1 de ORI =====
    Arquivo: main.py
    Descrição: armazenamento, estrutura e métodos hash
    Para tratar as colisões, optei por sondagem quadrática ( http://en.wikipedia.org/wiki/Quadratic_probing )
    Autores:
    Raul Vieira Cioldin
    Alexandre Rocha
"""

import sys
import cPickle as pickle
from os.path import isfile
import globais

class TabelaHash:

    def __init__(self, tamanho=globais.TAMANHO_HASH):
        self.tamanho = tamanho
        self.lista = []

    def insere(self, palavra, termo=None):
        """
        adiciona na tabela a palavra e/ou o termo (se a palavra já existir)
        retorna verdadeiro se a operação foi bem sucedida ou falso caso contrário
        observação: a validação das regras dos termos deve ser feita fora daqui
        """
        # verifica se a palavra já existe
        pos = self.buscaPos(palavra)
        if pos != -1:
            # se a palavra já existe, apenas atualiza o termo (se especificado)
            if termo:
                self.lista[pos][1] = termo
                return (True, None)
            else:
                # tentando reinserir a mesma palavra
                return (False, "palavra já existe")
        else:
            # acha a posição de inserção
            chave = self.funcao_hash(palavra)
            i = 1
            while (self.lista[chave] != 0 and i < self.tamanho):
                # tratamento de colisão (sondagem quadrática)
                chave = (chave + i*i) % self.tamanho
                i += 1
            if i == self.tamanho:
                # tabela cheia
                return (False, "tabela cheia")
            else:
                # nova palavra
                if termo:
                    self.lista[chave] = [palavra, termo]
                else:
                    self.lista[chave] = [palavra, None]
                return (True, None)

    def remove(self, palavra):
        """
        remove uma palavra da tabela, se existir (se não existir retorna falso, verdadeiro caso contrário)
        """
        pos = self.buscaPos(palavra)
        if pos == -1:
            # palavra não existe na tabela
            return False
        else:
            # marca a "exclusão" da palavra
            self.lista[pos] = 0
            return True

    def busca(self, palavra):
        """
        retorna tupla com a palavra e seus termos ou None se não encontrar
        """
        pos = self.buscaPos(palavra)
        if pos == -1:
            return None
        else:
            return self.lista[pos]

    def buscaPos(self, palavra):
        """
        retorna a posição da palavra ou -1 caso não faça parte da tabela
        """
        chave = self.funcao_hash(palavra)
        i = 1
        while (self.lista[chave] != 0 and i < self.tamanho):
            if self.lista[chave][0] == palavra:
                # encontrou
                return chave
            else:
                # tratamento de colisão (sondagem quadrática)
                chave = (chave + i*i) % self.tamanho
                i += 1
        # não encontrou
        return -1

    def funcao_hash(self, palavra):
        """
        peguei a dica da primeira resposta (na ocasião, o algorítmo do Paul Larson que multiplica por 101 em um loop):
        http://stackoverflow.com/questions/98153/whats-the-best-hashing-algorithm-to-use-on-a-stl-string-when-using-hash-map
        """
        hash = 0
        for s in range(len(palavra)):
            hash = hash * 101 + ord(palavra[s])
        return hash % self.tamanho

    def carregar_do_arquivo(self, caminho):
        try:
            if not isfile(caminho):
                # arquivo não existe, então deve ser criado
                arquivo = file(caminho, 'a')
                self.lista = [0]*self.tamanho
            else:
                arquivo = file(caminho, 'rb')
                self.lista = pickle.load(arquivo)

            arquivo.close()

        except IOError:
            print "Erro de acesso ao arquivo '%s'" % caminho
            sys.exit(0)

    def salvar_para_arquivo(self, caminho):
        """
        coloca um objeto python list com a estrutura da tabela no arquivo especificado pelo caminho
        observação: o método "carregar_do_arquivo" é sempre chamado antes (uma vez, na execução da aplicação)
        """
        try:
            arquivo = file(caminho, 'wb')
            pickle.dump(self.lista, arquivo)
            arquivo.close()
        except IOError:
            print "Erro de acesso ao arquivo '%s'" % caminho
            sys.exit(0)
