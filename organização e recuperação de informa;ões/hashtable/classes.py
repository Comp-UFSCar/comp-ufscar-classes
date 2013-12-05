#!/usr/bin/env python
# -*- coding: UTF-8 -*-

"""
    ===== Trabalho 1 de ORI =====
    Arquivo: classes.py
    Descrição: representação do dicionário, seus significados e tesauro
    Autores:
    Raul Vieira Cioldin
    Alexandre Rocha
"""

from abc import ABCMeta, abstractmethod
import hash, globais

class Dicionario:

    def __init__(self, caminho=globais.CAMINHO_DICIONARIO):
        self.caminho = caminho
        self.hash = hash.TabelaHash()
        self.hash.carregar_do_arquivo(self.caminho)

    def inserir(self, palavra):
        sucesso, msg = self.hash.insere(palavra)
        if sucesso:
            print "'%s' inserido com sucesso ao dicionário" % palavra
        else:
            print "Não foi possível inserir '%s' ao dicionário: %s" % (palavra, msg)

    def inserir_significado(self, palavra, significado):
        # verifica se a palavra já está inserida no dicionário
        existe = self.hash.busca(palavra)
        if existe and existe[1]:
            # se existe a palavra e o termo
            termo = existe[1]
        else:
            # palavra ainda não existe no dicionário, será inserida junto com o termo
            termo = TermoDicionario()

        inserido = termo.adicionar(significado)
        if not inserido:
             print "Número de termos de '%s' excedido" % palavra
        else:
            sucesso, msg = self.hash.insere(palavra, termo)
            if sucesso:
                print "O significado '%s' foi inserido com sucesso na palavra '%s'" % (significado, palavra)
            else:
                print "Não foi possível inserir no dicionário: %s" % msg

    def buscar(self, palavra):
        plv = self.hash.busca(palavra)
        if plv:
            # se existe a palavra
            # primeira letra em maiúsculo
            palavra = plv[0][0].upper() + plv[0][1:]
            termos = plv[1]
            print "%s:" % palavra
            if termos:
                termos.listar()
            else:
                print "Nenhum termo foi especificado ainda"
        else:
            print "A palavra '%s' não existe no dicionário" % palavra

    def remover(self, palavra):
        sucesso = self.hash.remove(palavra)
        if sucesso:
            print "'%s' foi removido com sucesso do dicionário" % palavra
        else:
            print "Não foi possível remover a palavra '%s'" % palavra

    def salvar(self):
        self.hash.salvar_para_arquivo(self.caminho)

class Tesauro:

    def __init__(self, caminho=globais.CAMINHO_TESAURO):
        self.caminho = caminho
        self.hash = hash.TabelaHash()
        self.hash.carregar_do_arquivo(self.caminho)

    def inserir(self, palavra):
        sucesso, msg = self.hash.insere(palavra)
        if sucesso:
            print "'%s' inserido com sucesso ao tesauro" % palavra
        else:
            print "Não foi possível inserir '%s' ao tesauro: %s" % (palavra, msg)

    def inserir_frase(self, palavra, frase, tipo):
        # verifica se a palavra já está inserida no tesauro
        existe = self.hash.busca(palavra)
        if existe and existe[1]:
            # se existe a palavra e o termo
            termo = existe[1]
        else:
            # palavra ainda não existe no tesauro, será inserida junto com o termo
            termo = TermoTesauro()

        inserido = termo.adicionar(frase, tipo)
        if not inserido:
             print "Número de termos de '%s' excedido" % palavra
        else:
            sucesso, msg = self.hash.insere(palavra, termo)
            if sucesso:
                print "O termo '%s' foi inserido com sucesso na palavra '%s'" % (frase, palavra)
            else:
                print "Não foi possível inserir no tesauro: %s" % msg

    def buscar(self, palavra):
        plv = self.hash.busca(palavra)
        if plv:
            # se existe a palavra
            # primeira letra em maiúsculo
            palavra = plv[0][0].upper() + plv[0][1:]
            termos = plv[1]
            print "%s:" % palavra
            if termos:
                termos.listar()
            else:
                print "Nenhum termo foi especificado ainda"
        else:
            print "A palavra '%s' não existe no tesauro" % palavra

    def remover(self, palavra):
        sucesso = self.hash.remove(palavra)
        if sucesso:
            print "'%s' foi removido com sucesso do tesauro" % palavra
        else:
            print "Não foi possível remover a palavra '%s'" % palavra

    def salvar(self):
        self.hash.salvar_para_arquivo(self.caminho)

class Termo(object):
    """ classe abstrata que representa um termo """
    __metaclass__ = ABCMeta

    @abstractmethod
    def adicionar(self, frase):
        """ Insere uma palavra/frase no termo """

    @abstractmethod
    def listar(self):
        """ Mostra as frases presentes no termo """

class TermoDicionario(Termo):

    def __init__(self):
        self.significados = []

    def adicionar(self, frase):
        # limita para apenas 5 significados
        if len(self.significados) < 5:
            self.significados.append(frase)
            return True
        else:
            # tamanho excedido
            return False

    def listar(self):
        # imprime os significados
        for i in range(len(self.significados)):
            print "(%d) %s" % (i+1, self.significados[i])

class TermoTesauro(Termo):

    def __init__(self):
        self.sinonimos = []
        self.contrastantes = []
        self.relacionados = []

    def adicionar(self, frase, tipo=None):
        """ Não consegui pensar em um jeito melhor de fazer isso (aka: sem mudar a assinatura do método) """
        if tipo not in ['s','c','r']:
            # tipo não especificado
            return False
        else:
            if tipo in 's':
                # sinônimo
                self.sinonimos.append(frase)
            elif tipo in 'c':
                # termo contrastante
                self.contrastantes.append(frase)
            elif tipo in 'r':
                # termo relacionado
                self.relacionados.append(frase)
            return True

    def listar(self):
        i = 1
        # imprime os termos que existem
        if len(self.sinonimos) > 0:
            print "(" + str(i) + ") Sinônimos: " + ", ".join(self.sinonimos) + "."
            i += 1
        if len(self.contrastantes) > 0:
            print "(" + str(i) + ") Termos contrastantes: " + ", ".join(self.contrastantes) + "."
            i += 1
        if len(self.relacionados) > 0:
            print "(" + str(i) + ") Termos relacionados: " + ", ".join(self.relacionados) + "."

