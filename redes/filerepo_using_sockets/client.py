#!/usr/bin/env python
# -*- coding: UTF-8 -*-

"""
    ===== Implementação de p2p filesharing =====
    Arquivo: client.py
    Descrição: Clientes podem requisitar ou enviar arquivos para os supernós
    Autores:
    Raul Vieira Cioldin
    Gabriel Lucius Pomin
    Samuel Carlos Machado Rodrigues
    Edgar Fardin
"""

import socket
import sys
import argparse
import cPickle as pickle
from os.path import isfile

BUFFER_SIZE = 4096

class Client:
    """
    Implementação de um cliente
    """
    def __init__(self, args):
        self.args = args
        # configurando a conexão com o supernó
        self.service_addr = (self.args['hostname'], self.args['port'])
        self.service_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

    def connect(self):
        """ estabele conexão com o supernó """
        try:
            self.service_socket.connect(self.service_addr)
            print "Cliente conectado ao superno (%s:%d) pela porta %d" % \
                  (self.service_addr[0], self.service_addr[1], self.service_socket.getsockname()[1])
        except:
            # Conexão com o supernó falhou
            print "Nao foi possivel estabelecer conexao com (%s:%s)" % self.service_addr
            sys.exit(0)

    def redirect(self, addr):
        """ redireciona a conexão para o endereço especificado """

        # reciclando o socket
        self.service_socket.close()
        self.service_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

        # conectando ao novo supernó
        print "Tentando conectar a (%s:%s)" % addr
        try:
            self.service_socket.connect(addr)
            self.service_addr = self.service_socket.getpeername()
        except:
            print "Não foi possivel estabelecer conexao com (%s:%s)" % addr
            sys.exit(0)

    def disconnect(self):
        # termina a conexão imediatamente ignorando se o socket está sendo usado, diferente do close()
        self.service_socket.shutdown(socket.SHUT_RDWR)
        print "Desconectado."

    def execute(self):
        self.connect()
        try:
            if self.args['download']:
                # download de um arquivo
                self.do_download(self.args['download'])
            elif self.args['upload']:
                # upload de um arquivo
                self.do_upload(self.args['upload'])
            elif self.args['list']:
                # listagem de arquivos
                self.do_listing()
        except KeyboardInterrupt:
            self.disconnect()

    def do_download(self, filename):
        """ faz requisição de download no supernó, informando o nome do arquivo """
        try:
            # verifica existência do arquivo
            if isfile(filename):
                raise BaseException

            # verifica se:
            # o supernó tem posse do arquivo (resp = 'ok')
            # caso negativo: verifica se outro nó na rede tem posse do arquivo (resp = 're')
            # caso negativo: o arquivo não está na rede (resp = 'no')
            self.service_socket.send('d' + filename)
            resp = self.service_socket.recv(2)

            if resp == 'ok':
                print "Recebendo %s de (%s:%s)" % (filename, self.service_addr[0], self.service_addr[1])
                file = open(filename, 'wb')
                while 1:
                    file_data = self.service_socket.recv(BUFFER_SIZE)
                    if not file_data:
                        break
                    file.write(file_data)
                file.close()

            elif resp == 're':
                print "%s não encontrado em (%s:%s), redirecionando..." % (filename, self.service_addr[0],
                                                                           self.service_addr[1])
                redir_addr = self.service_socket.recv(BUFFER_SIZE).split(':')
                redir_addr = (redir_addr[0], int(redir_addr[1]))
                self.redirect(redir_addr)
                self.do_download(filename)

            elif resp == 'no':
                print "%s nao esta disponivel na rede no momento." % filename

            else:
                print "Erro ao interpretar mensagem de (%s:%s)" % (self.service_addr)


        except IOError:
            print "Erro de acesso ao arquivo %s" % filename
        except:
            # Arquivo já existe
            print "%s já existe no cliente" % filename

    def test_file_read_access(self, filename):
        file = open(filename, 'rb')
        file.close()

    def do_upload(self, filename):
        """ faz requisição de upload no supernó, informando o nome do arquivo """
        # verifica se o arquivo existe no cliente
        try:
            self.test_file_read_access(filename)
        except IOError:
            print "Erro de acesso ao arquivo %s" % filename
            self.disconnect()
            sys.exit(0)

        self.service_socket.send('u' + filename)
        # verifica se o arquivo já existe
        resp = self.service_socket.recv(2)
        if resp == 'ok':
            # arquivo novo
            print "Transmitindo %s a (%s:%d)" % (filename, self.service_socket.getpeername()[0],
                                                 self.service_socket.getpeername()[1])
            try:
                file = open(filename, 'rb')
                file_data = file.read()
                file.close()
                self.service_socket.sendall(file_data)
                print "Sucesso!"
            except (IOError):
                print "Erro de acesso ao arquivo %s" % filename
        else:
            print "Arquivo já existe em (%s:%s)" % self.service_socket.getpeername()

    def do_listing(self):
        """ faz requisição de listagem no supernó """
        self.service_socket.send('l')
        list_dump = ''
        # adquire o dump do objeto via socket
        while 1:
            chunk = self.service_socket.recv(BUFFER_SIZE)
            if not chunk:
                break
            list_dump += chunk
        # de-serializa o objeto usando pickle
        file_list = pickle.loads(list_dump)

        if len(file_list) > 0:
            print "Lista de arquivos disponiveis em (%s:%s):" % self.service_socket.getpeername()

            for filename in file_list:
                print filename
        else:
            print "Não existem arquivos disponíveis em (%s:%s)" % self.service_socket.getpeername()


def parse_args():
    """
    faz validação básica da linha de comando e oferece instruções de como
    o programa deve ser executado
    """
    parser = argparse.ArgumentParser(
            description='''Script cliente no acesso a uma rede overlay (via supernos) para transferencia de arquivos''',
            formatter_class=argparse.ArgumentDefaultsHelpFormatter,)
    parser.add_argument('hostname',  action='store',  help='endereco (IPv4) do superno' )
    parser.add_argument('port',  action='store', type=int,  help='porta no superno que atende ao servico' )

    # opções mutuamente exclusivas
    group = parser.add_mutually_exclusive_group(required=True)
    group.add_argument('-l', '--list', action='store_true', help='listagem de arquivos disponiveis no superno')
    group.add_argument('-d', '--download', action='store', help='especifica um arquivo para download')
    group.add_argument('-u', '--upload', action='store', help='especifica um arquivo para upload')
    args = parser.parse_args()
    return vars(args)

if __name__ == "__main__":
    args = parse_args()
    c = Client(args)
    c.execute()