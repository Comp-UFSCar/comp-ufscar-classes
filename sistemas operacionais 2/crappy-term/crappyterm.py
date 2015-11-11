'''
@author: raul
'''

import os, sys
import signal
import threading
import time

# tipos de execucao
class ExecType:
    BACKGROUND, FOREGROUND, WRITEFILE_BG, WRITEFILE_FG = range(4)
    
# processos em execucao
jobList = {}

def insertJobList(pid, cmd):
    jobList[str(pid)] = (cmd, str(os.getuid()), str(os.getgid()))

def removeJobList(pid):
    del jobList[pid]

class JobDaemon(threading.Thread):
    def run(self):
        # preempcao e implicita em python, nao e possivel interferir com a
        # troca de contexto (ocorre espontaneamente)
        while 1:
            for jobPid in jobList.keys():
                # emite um sinal falso pra saber se o processo ainda nao terminou
                try:
                    os.kill(int(jobPid), 0)
                except OSError:
                    # o processo ja morreu
                    removeJobList(jobPid)
            time.sleep(1)

jobd = JobDaemon()
jobd.start()
                    
        
def BuiltInMethods(string):
    if string == 'exit':
        ''' por algum motivo bizarro o id do process group era compartilhado com
        todos os programas abertos aqui, a solucao foi mandar o sigkill um a um
        os childs e por ultimo o processo pai '''
        for childPid in jobList.keys():
            os.kill(int(childPid), signal.SIGKILL)
        os.kill(os.getpid(), signal.SIGKILL)
        #sys.exit(0)
    elif string == 'info':
        print '''GodDamnShell - Raul Cioldin 2012'
==== Execucao em Foreground ===============================
Uso: <comando> <argumentos>;<comando2> <argumentos2> [...]
==== Execucao em Background ===============================
Uso: <comando> <argumentos>&; [...]
==== Redirecionamento pra arquivo =========================
Uso: <comando> <argumentos> > <arquivo>; [...]'''
        return True
    elif string == 'jobs':
        print 'PID  -  Processo  -  UserID  -  GroupID'
        for pid, stats in jobList.iteritems():
            print pid + ':  ' + stats[0] +'   ' + stats[1] + '   ' + stats[2] 
        return True
    else:
        return False
    
def parsing(string):
    # separa os comandos
    rawCmdList = s.split(';')
    
    # separa os argumentos de cada comando
    cmdList = {}
    index = 0
    for i in rawCmdList:
        args = i.split(' ')
        cmdList[index] = args[:]
        
        if args[-1][-1] == '&':
            # tira o e comercial do argumento (porco way, strings sao imutaveis no python)
            cmdList[index][-1] = args[-1].rstrip('&')
            cmdList[index].append(ExecType.BACKGROUND)
        else:
            cmdList[index].append(ExecType.FOREGROUND)

        posSimbolo = 0
        nomeArquivo = ''            
        for c in args:
            if c == '>':
                nomeArquivo = cmdList[index][-2]
                # o nome do arquivo e uma concatenacao do que vem depois do
                # simbolo de maior
                prevType = cmdList[index][-1]
                cmdList[index] = args[:posSimbolo]
                cmdList[index].append(nomeArquivo)
                
                # configura a execucao baseado em estado anterior
                if prevType == ExecType.BACKGROUND:
                    cmdList[index].append(ExecType.WRITEFILE_BG)
                else:
                    cmdList[index].append(ExecType.WRITEFILE_FG)
                break 
            posSimbolo = posSimbolo + 1        
        index = index + 1
    return cmdList

def execute(cmdList):
    for index, args in cmdList.iteritems():
        cmd = args[0]
        if os.path.exists(cmd):
            pass
        elif os.path.exists('/bin/' + cmd):
            cmd = '/bin/' + cmd
        elif os.path.exists('/usr/bin/' + cmd):
            cmd = '/usr/bin/' + cmd
        else:
            raise OSError
        
        mode = args.pop()
        if mode == ExecType.BACKGROUND:
            # cria um subprocesso no background e adiciona a lista de jobs
            pid = os.spawnv(os.P_NOWAIT, cmd, args)
        elif mode == ExecType.WRITEFILE_FG:
            try:
                # separando o caminho do arquivo
                path = args.pop()
                # aproximacao usando pipe
                from subprocess import Popen
                # garantindo que o buffer esta limpo
                sys.stdout.flush()
                pipeFile = Popen(args, stdout = open(path, 'w'))
                pipeFile.wait()
                pid = pipeFile.pid
                sys.stdout = sys.__stdout__
            except:
                print 'Erro no tratamento de arquivo'
                return
        elif mode == ExecType.WRITEFILE_BG:
            try:
                # separando o caminho do arquivo
                path = args.pop()
                # aproximacao usando pipe
                from subprocess import Popen
                # garantindo que o buffer esta limpo
                sys.stdout.flush()
                pid = Popen(args, stdout = open(path, 'w')).pid
                sys.stdout = sys.__stdout__
            except:
                print 'Erro no tratamento de arquivo'
                return
        else:
            # cria um subprocesso no foreground (o comportamento do execv e diferente
            # no C e no Python)
            pid = os.spawnv(os.P_WAIT, cmd, args)
        
        if pid:
            # so faz sentido inserir na joblist os process child
            insertJobList(pid, cmd)    

        
def sigint_handler(signal, frame):
    print '\nCtrl-C pressionado'
    BuiltInMethods('exit')
    
def sigterm_handler(signal, frame):
    print '\nMorto'
    sys.exit(0)

def sighup_handler(signal, frame):
    # ignora o sighup
    pass

def sigchld_handler(signal, frame):
    # nao funciona quando o os.spawnv tem a flag P_WAIT
    # na documentacao o uso de SIGCHLD e desencorajado
    pass
    #(pid, status) = os.waitpid(-1, 0) 
    #removeJobList(pid)

if __name__ == '__main__':
    
    # Conecta handlers para os sinais (poderia ficar fora do main tambem)
    signal.signal(signal.SIGINT, sigint_handler)
    signal.signal(signal.SIGTERM, sigterm_handler)
    signal.signal(signal.SIGHUP, sighup_handler)
    signal.signal(signal.SIGCHLD, sigchld_handler)
    
    while True:
        try:
            s = raw_input('$> ')
        except:
            break
        
        # Reconhecimento de comandos do shell
        if s and not BuiltInMethods(s.strip().lower()):
            # try to run command
            try:
                # recupera um dicionario de comandos
                cmdList = parsing(s)
                execute(cmdList)
        
            except OSError:
                print 'Comando Invalido'
            
    