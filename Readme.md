Trabalho
Implementação do Linda e de um servidor Pub-Sub em python.

Projeto
 client.py = Interface do cliente, faz as chamadas no linda de acordo com o input no Shell
 linda.py = Biblioteca do Linda, com as funções básicas (linda_in(), linda_out(), linda_rd() e outras)
 tupplespace.py = Classe do TuppleSpace
 server.py = Servidor, trata as requisições vindas de um objeto da classe Linda. O servidor teve sua implementação baseada no material de Kuntal Chandra
 ip_config.txt = Arquivo para configuração do ip e porta do servidor. Clientes e servidores devem ter o mesmo arquivo ip_config para poderem se conectar. Primeira linha deve conter o ip do servidor, segunda deve conter a porta. O restante é ignorado.
Execução
Rodar o server em uma sessão shell:

> python3 server.py
Rodar o cliente em uma (ou múltiplas) sessão shell:

> python3 client.py
Quando o cliente inicializar, ele solicitará:

Hostname: Nome de usuário desejado (não há login)
Topic: Nome do tópico ao qual deseja se inscrever, se não houver nenhum com esse nome, é criado.