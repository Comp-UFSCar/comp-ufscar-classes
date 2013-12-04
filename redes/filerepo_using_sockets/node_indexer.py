#!/usr/bin/env python
# -*- coding: UTF-8 -*-

"""
    ===== Implementação de p2p filesharing =====
    Arquivo: node_indexer.py
    Descrição: Realiza a indexação de supernós no anel hash e trata requisições dos supernós
    Autores:
    Raul Vieira Cioldin
    Gabriel Lucius Pomin
    Samuel Carlos Machado Rodrigues
    Edgar Fardin
"""

import socket, select, sys
from hash_ring import HashRing

# Porta padrão do serviço
PORT = 6110
# Tamanho padrão do buffer
BUFFER_SIZE = 4096

class NodeIndexer:
    """
    Recebe conexões de supernós e auxilia na estruturação de uma rede overlay
    """

    def __init__(self, port):
        self.connList = []
        self.port = port
        # socket vai usar tcp
        self.server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        # reusar o mesmo socket para outras conexões
        self.server_socket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
        # classe que vai fazer consistent hashing dos supernós
        self.ring = HashRing()

    def serve_forever(self):
        # configura a escuta no localhost, porta especificada
        self.server_socket.bind(('', self.port))
        self.server_socket.listen(10)

        self.connList.append(self.server_socket)

        print "Iniciando o servico na porta " + str(self.port)

        try:
            while 1:
                # pega a lista de sockets que podem ser lidos
                read_sockets,write_sockets,error_sockets = select.select(self.connList,[],[])

                for sock in read_sockets:
                    # nova conexão
                    if sock == self.server_socket:
                        sockfd, addr = self.server_socket.accept()
                        self.connList.append(sockfd)
                        print "Novo superno conectado (%s:%s)" % addr
                    else:
                        # mensagem de um supernó
                        try:
                            data = sock.recv(BUFFER_SIZE)
                            if len(data) != 0:
                                self.handle_data(data, sock)
                        except(KeyboardInterrupt):
                            # para que a interrupcao do teclado nao seja tratada no próximo except
                            raise
                        except(ValueError):
                            print "Mensagem fora das especificações recebida de (%s:%s)" % addr
                            print data

            # se saiu do loop, a conexão foi fechada por alguma razão
            self.server_socket.close()
        except(KeyboardInterrupt):
            # interrupção do teclado
            print "Fechando servico"
            sys.exit(0)

    def handle_data(self, data, socket):
        opr = data[0]
        if opr == 'a':
            # append: informa a porta que o supernó está recebendo conexões
            port = int(data[1:])
            self.handle_add(socket, port)
        elif opr == 'r':
            # remove: supernó avisa que vai desconectar
            port = int(data[1:])
            self.handle_remove(socket, port)
        elif opr == 'd':
            # supernó foi desconectado
            self.handle_disconnect(socket)
        elif opr == 'q':
            # query: requisição de um supernó pelo supernó responsável pelo arquivo descrito por 'data'
            if self.ring.is_empty():
                response = 'empty'
            else:
                response = "%s:%s" % self.ring.get_node(data)
            socket.send(response)
        else:
            # mensagem não interpretada pelo serviço de indexação
            raise ValueError

    def handle_add(self, socket, port):
        """ adiciona o supernó no anel hash """
        addr = (socket.getpeername()[0], port)
        self.ring.add_node(addr)

    def handle_remove(self, socket, port):
        """ retira o supernó da indexação """
        addr = (socket.getpeername()[0], port)
        print "Superno desconectando: (%s, %s)" % socket.getpeername()
        self.ring.remove_node(addr)

    def handle_disconnect(self, socket):
        """ remove a conexão para o supernó """
        socket.close()
        self.connList.remove(socket)

if __name__ == "__main__":
    if len(sys.argv) == 2:
        port = sys.argv[1]
        ni = NodeIndexer(port)
    else:
        ni = NodeIndexer(PORT)
    ni.serve_forever()