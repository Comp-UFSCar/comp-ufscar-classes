#!/usr/bin/env python
# -*- coding: UTF-8 -*-

"""
    ===== Implementação de p2p filesharing =====
    Arquivo: supernode.py
    Descrição: Supernós armazenam arquivos e oferecem serviço de requisição e envio
    Autores:
    Raul Vieira Cioldin
    Gabriel Lucius Pomin
    Samuel Carlos Machado Rodrigues
    Edgar Fardin
"""

import socket, select, sys, thread
from os import listdir
from os.path import isfile, join, split
from time import sleep
import cPickle as pickle

BUFFER_SIZE = 4096
DEFAULT_INDEX_HOST = 'localhost'
DEFAULT_INDEX_PORT = 6110
DEFAULT_FILEROOT = '/home/raul/estudos/redes/p2p_filesharing/repositorio01/'

class Node:
    """
    Implementação de um supernó
    """
    def __init__(self, index_host=DEFAULT_INDEX_HOST, index_port=DEFAULT_INDEX_PORT, fileroot=DEFAULT_FILEROOT):
        self.fileroot = fileroot
        # constrói uma lista de arquivos a partir de um arquivo com os nomes
        self.file_list = self.populate_from_path(self.fileroot)
        # configurando a conexão com o indexador de supernós
        self.index_addr = (index_host, index_port)
        # socket para se manter sempre conectado ao serviço de indexação
        self.index_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        # socket para receber conexões
        self.node_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        # reusar o mesmo socket para outras conexões
        self.node_socket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
        # porta para escuta, ainda desconhecida
        self.listen_port = None
        # lista com as conexões ativas na porta de escuta
        self.listen_conns = []
        # variável de controle para as threads
        self.closing = False
        self.syncing = False

    def populate_from_path(self, filepath):
        """ retorna os arquivos de um dado diretório (apenas o nome) """
        try:
            return [ f for f in listdir(filepath) if isfile(join(filepath,f)) ]
        except OSError:
            # não existem arquivos no diretório
            return []

    def serve_forever(self):
        #try:
        self.node_socket.bind(('', 0))
        self.node_socket.listen(10)
        self.listen_port = self.node_socket.getsockname()[1]
        print "Recebendo conexões na porta %i" % self.listen_port

        # transmitir a porta em que esse supernó está recebendo conexões ao indexador
        self.connect_to_indexer()

        # thread que auxilia na distribuição dos arquivos na rede overlay
        thread.start_new_thread(self.sync_files_thread, ())

        # lista de conexões na porta de escuta
        self.listen_conns.append(self.node_socket)
        try:
            while 1:
                # pega a lista de sockets que podem ser lidos
                read_sockets,write_sockets,error_sockets = select.select(self.listen_conns,[],[])
                for sock in read_sockets:
                    # nova conexão
                    if sock == self.node_socket:
                        sockfd, addr = self.node_socket.accept()
                        self.listen_conns.append(sockfd)
                        #print "Novo cliente ou superno conectado (%s, %s)" % addr
                    else:
                        # mensagem de um cliente ou supernó
                        try:
                            data = sock.recv(BUFFER_SIZE)
                            if len(data) != 0:
                                self.handle_data(data, sock)
                        except(KeyboardInterrupt):
                            # para que a interrupcao do teclado nao seja tratada no próximo except
                            raise
                        except(ValueError):
                            print "Mensagem fora das especificações recebida de (%s, %s)" % addr

            # se saiu do loop, a conexão foi fechada por alguma razão
            self.node_socket.close()
        except(KeyboardInterrupt):
            # interrupção do teclado
            print "Fechando servico do superno"
            self.cleanup()
            sys.exit(0)

    def cleanup(self):
        # fechar todos os sockets exceto o que ainda vai ser usado para sincronizar
        closing_conns = self.listen_conns[1:]
        for conn in closing_conns:
            conn.close()
        # remove este supernó da tabela hash
        self.index_socket.send('r' + str(self.listen_port))
        # controle: termina a thread de sincronização
        self.closing = True
        print "Sincronizando arquivos..."
        # aguarda a thread de sincronização terminar, se necessário
        while self.syncing:
            sleep(0.25)
        # garante a execução de uma última sincronização para distribuir os arquivos que ficaram sem supernó
        self.sync_files()
        print "Fechando o serviço..."
        # remove o nó do serviço de indexação
        self.index_socket.send('d')
        self.index_socket.close()

    def handle_data(self, data, socket):
        """ trata as requisições dos clientes ou supernos conectados """
        opr = data[0]
        if opr == 'd':
            # download: cliente informa que deseja fazer download de um arquivo
            filename = data[1:]
            # verifica se o arquivo está no repositório
            if filename in self.file_list:
                # está no repositorio
                self.handle_local_download(socket, filename)
            else:
                # não está no repositorio
                self.handle_remote_download(socket, filename)

        elif opr == 'u':
            # upload: cliente informa que deseja enviar um arquivo
            filename = data[1:]
            self.handle_upload(socket, filename)

        elif opr == 'l':
            # list: cliente requisita lista de arquivos do superno
            self.handle_list_request(socket)

        else:
            # mensagem não interpretada pelo serviço de indexação
            raise ValueError

    def handle_list_request(self, socket):
        """ envia a lista de arquivos disponíveis (como um objeto) """
        print "Enviando lista de arquivos a (%s:%s)" % socket.getpeername()
        # fazendo dump do objeto (serializando) para bytes
        list_dump = pickle.dumps(self.file_list, -1)
        socket.sendall(list_dump)
        # atendeu à requisição, fecha a conexão
        self.close_conn(socket)

    def handle_local_download(self, socket, filename):
        """ envia um arquivo diretamente para o cliente """
        filepath = join(self.fileroot, filename)
        # avisa o cliente do envio
        socket.send('ok')
        try:
            file = open(filepath, 'rb')
            file_data = file.read()
            file.close()
            socket.sendall(file_data)
        except (IOError):
            print "Erro de acesso ao arquivo %s" % filepath

        self.close_conn(socket)

    def handle_remote_download(self, socket, filename):
        """ adquire a localização do arquivo no indexador e redireciona o cliente """
        # recebe a localização do nó onde o arquivo está ou deveria estar
        self.index_socket.send('q' + filename)
        node_addr = self.index_socket.recv(BUFFER_SIZE)
        host, port = node_addr.split(':')
        # verifica se não se trata do próprio nó
        if not self.is_localhost(host, port):
            # avisa o cliente do redirecionamento
            socket.send('re')
            # envia o endereço do supernó responsável
            socket.send(node_addr)
        else:
            # arquivo não existe na rede
            socket.send('no')

        self.close_conn(socket)


    def handle_upload(self, socket, filename):
        """ recebe um arquivo de um cliente ou supernó """
        # se estiver "sujo" com diretórios, extrai só o nome do arquivo
        filename = split(filename)[-1]
        filepath = join(self.fileroot, filename)
        try:
            if isfile(filepath):
                # arquivo já existe
                raise BaseException
            # informa que pode receber
            socket.send('ok')
            print "Recebendo %s de (%s:%s)" % (filename, socket.getpeername()[0], socket.getpeername()[1])
            file = open(filepath, 'wb')
            while 1:
                file_data = socket.recv(BUFFER_SIZE)
                if not file_data:
                    break
                file.write(file_data)
            file.close()
            self.file_list.append(filename)
        except IOError:
            print "Erro de acesso ao arquivo %s" % filepath
        except:
            # informa o requisitante que o arquivo já existe
            socket.send('no')

        self.close_conn(socket)

    def close_conn(self, socket):
        self.listen_conns.remove(socket)
        socket.close()

    def connect_to_indexer(self):
        # No contexto deste método:
        # listen_port = porta que o supernó usa para receber conexões
        # port = porta que o supernó usa para se conectar ao serviço de indexação
        try:
            self.index_socket.connect(self.index_addr)
            port = self.index_socket.getsockname()[1]
            print "Superno conectado ao indexador (%s, %s) pela porta " % self.index_addr + str(port)
            self.index_socket.send('a' + str(self.listen_port))
        except:
            # Conexão com o serviço de indexação falhou
            print "Não foi possível estabelecer conexão com (%s, %s)" % self.index_addr
            sys.exit(0)

    def sync_files_thread(self):
        """
        Se necessário, distribui os arquivos do supernó pela rede
        """
        # para evitar colisão de mensagens no socket do indexador durante o cleanup
        self.syncing = True
        while 1:
            if self.closing:
                self.syncing = False
                break
            # delay de verificação (10 segundos)
            sleep(5)
            try:
                self.sync_files()
            except:
                # um dos lados da conexão foi fechado durante a sincronização
                print sys.exc_info()

    def sync_files(self):
        """
        Faz a varredura da lista e distribuição de arquivos
        Separei esse método para que possa ser executado uma última vez (cleanup) sem a condicional da thread
        """
        for filename in self.file_list:
            self.index_socket.send('q' + filename)
            # recebe a localização do nó onde o arquivo está ou deveria estar
            node_addr = self.index_socket.recv(BUFFER_SIZE)
            if node_addr == 'empty':
                # não existem nós disponíveis na tabela hash
                break
            host, port = node_addr.split(':')
            # verifica se não se trata do próprio nó
            if not self.is_localhost(host, port):
                new_conn = socket.socket()
                new_conn.connect((host, int(port)))
                new_conn.send('u' + filename)
                resp = new_conn.recv(2)
                if resp == 'ok':
                    # é necessário disseminar o arquivo
                    print "[Balanceamento] Retransmitindo %s a (%s:%s)" % (filename, host, port)
                    filepath = join(self.fileroot, filename)
                    try:
                        file = open(filepath, 'rb')
                        file_data = file.read()
                        file.close()
                        new_conn.sendall(file_data)
                        new_conn.close()
                    except (IOError):
                        print "Erro de acesso ao arquivo %s" % filepath

    def is_localhost(self, host, port):
        """ retorna verdadeiro se a tupla hostname e porta for o próprio nó """
        if ( (host, int(port)) == self.node_socket.getsockname()):
            return True
        if host == "127.0.0.1":
            # endereços equivalentes
            return self.is_localhost('0.0.0.0', port)
        return False

if __name__ == "__main__":
    # tratando argumentos
    if len(sys.argv) == 2:
        path = sys.argv[1]
        s = Node(fileroot=path)
    elif len(sys.argv) == 3:
        host = sys.argv[1]
        port = int(sys.argv[2])
        s = Node(index_host=host, index_port=port)
    elif len(sys.argv) == 4:
        host = sys.argv[1]
        port = int(sys.argv[2])
        path = sys.argv[3]
        s = Node(host, port, path)
    else:
        s = Node()
    # inicia o serviço
    s.serve_forever()