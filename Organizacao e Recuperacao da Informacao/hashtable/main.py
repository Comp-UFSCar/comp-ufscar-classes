#!/usr/bin/env python
# -*- coding: UTF-8 -*-

"""
    ===== Trabalho 1 de ORI =====
    Arquivo: main.py
    Descrição: script que roda a aplicação e faz interface com o usuário
    Autores:
    Raul Vieira Cioldin
    Alexandre Rocha
"""
import sys

from classes import Dicionario, Tesauro

class Aplicacao:
    def __init__(self):
        self.dicionario = Dicionario()
        self.tesauro = Tesauro()

    def executar(self):
        try:
            while 1:
                self.menu()
        except (KeyboardInterrupt, EOFError):
            # Interrupção pelo usuário (ctrl+c)
            self.encerrar()

    def menu(self):
        """ Menu inicial da aplicação """
        while 1:
            print "===== T1 ORI ========"
            print "Selecione a operação:"
            print "(1) Dicionário"
            print "(2) Tesauro"
            print "(3) Sair"
            try:
                opcao = int(raw_input("Operação: "))
                if opcao in [1,2,3]:
                    break
                else:
                    raise ValueError
            except ValueError:
                print "Operação não reconhecida"
        if opcao == 1:
            # Dicionário
            self.menu_dicionario()
        elif opcao == 2:
            # Tesauro
            self.menu_tesauro()
        else:
            # Sair
            self.encerrar()

    def menu_dicionario(self):
        """ Menu das operações sob o dicionário """
        while 1:
            while 1:
                print "===== Dicionário ====="
                print "Selecione a operação:"
                print "(1) Buscar uma palavra"
                print "(2) Adicionar uma palavra"
                print "(3) Adicionar um significado para uma palavra"
                print "(4) Remover uma palavra"
                print "(5) Menu anterior"
                try:
                    opcao = int(raw_input("Operação: "))
                    if opcao in [1,2,3,4,5]:
                        break
                    else:
                        raise ValueError
                except ValueError:
                    print "Operação não reconhecida"
            if opcao == 1:
                # Buscar
                print "Digite a palavra que deseja pesquisar"
                palavra = raw_input("Palavra: ")
                self.dicionario.buscar(palavra)
            elif opcao == 2:
                # Adicionar palavra
                print "Digite a palavra que deseja adicionar"
                palavra = raw_input("Palavra: ")
                self.dicionario.inserir(palavra)
            elif opcao == 3:
                # Adicionar significado
                print "Digite a palavra a qual se deseja adicionar um significado"
                palavra = raw_input("Palavra: ")
                print "Digite o significado"
                significado = raw_input("Significado: ")
                self.dicionario.inserir_significado(palavra, significado)
            elif opcao == 4:
                # Remover palavra
                print "Digite a palavra que deseja remover"
                palavra = raw_input("Palavra: ")
                self.dicionario.remover(palavra)
            else:
                break
            raw_input("Pressione <ENTER> para continuar...")

    def menu_tesauro(self):
        """ Menu das operações sob o tesauro """
        while 1:
            while 1:
                print "===== Tesauro ========"
                print "Selecione a operação:"
                print "(1) Buscar uma palavra"
                print "(2) Adicionar uma palavra"
                print "(3) Adicionar um termo para uma palavra"
                print "(4) Remover uma palavra"
                print "(5) Menu anterior"
                try:
                    opcao = int(raw_input("Operação: "))
                    if opcao in [1,2,3,4,5]:
                        break
                    else:
                        raise ValueError
                except ValueError:
                    print "Operação não reconhecida"
            if opcao == 1:
                # Buscar
                print "Digite a palavra que deseja pesquisar"
                palavra = raw_input("Palavra: ")
                self.tesauro.buscar(palavra)
            elif opcao == 2:
                # Adicionar palavra
                print "Digite a palavra que deseja adicionar"
                palavra = raw_input("Palavra: ")
                self.tesauro.inserir(palavra)
            elif opcao == 3:
                # Adicionar termo
                self.menu_tesauro_termo()
            elif opcao == 4:
                # Remover palavra
                print "Digite a palavra que deseja remover"
                palavra = raw_input("Palavra: ")
                self.tesauro.remover(palavra)
            else:
                break
            raw_input("Pressione <ENTER> para continuar...")

    def menu_tesauro_termo(self):
        """ Menu de inserção de termo ao tesauro """
        print "Digite a palavra a qual se deseja adicionar um termo"
        palavra = raw_input("Palavra: ")

        while 1:
            print "Tipo do termo que será inserido:"
            print "(1) Sinônimo"
            print "(2) Termo contrastante"
            print "(3) Termo relacionado"
            try:
                opcao = int(raw_input("Tipo: "))
                if opcao in [1,2,3]:
                    break
                else:
                    raise ValueError
            except ValueError:
                print "Tipo não reconhecido"

        print "Digite o termo (palavra ou frase)"
        termo = raw_input("Termo: ")

        if opcao == 1:
            # Sinônimo
            self.tesauro.inserir_frase(palavra, termo, 's')
        elif opcao == 2:
            # Termo contrastante
            self.tesauro.inserir_frase(palavra, termo, 'c')
        else:
            # Termo relacionado
            self.tesauro.inserir_frase(palavra, termo, 'r')

    def encerrar(self):
        """ Rotina de cleanup da aplicação """
        print "\nEncerrando a aplicação..."
        self.dicionario.salvar()
        self.tesauro.salvar()
        sys.exit(1)

if __name__ == "__main__":
    app = Aplicacao()
    app.executar()