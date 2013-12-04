
import java.io.IOException;
import java.rmi.*;
import java.rmi.server.*;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Vector;

public class RelogioRMIImpl extends UnicastRemoteObject implements RelogioRMI {

    private static Vector<Object> listaClientes;

    public RelogioRMIImpl() throws RemoteException {
        super();
        listaClientes = new Vector<Object>();
    }

    public synchronized int register(Object novoCliente) throws RemoteException {
        if (novoCliente == null) {
            return RelogioRMI.FAILURE;
        }
        if (!(listaClientes.contains(novoCliente))) {
            listaClientes.addElement(novoCliente);
            System.out.println("Novo cliente registrado");
            return RelogioRMI.SUCCESS;
        } else {
            return RelogioRMI.FAILURE;
        }
    }

    private static void doSync() throws RemoteException {
        System.out.println("Tentando sincronizar...");

        DateFormat formatter = new SimpleDateFormat("HH:mm:ss:SSS");

        // pega primeiro o horario do servidor
        Long timeSum = System.currentTimeMillis();
        System.out.println("Hora atual do Servidor: " + formatter.format(timeSum));

        int cliNr = 1;
        for (Object cli : listaClientes) {
            Long clientTime = ((RelogioRMIClientInterface) cli).getTimeMillis();
            timeSum += clientTime;
            System.out.println("Cliente " + cliNr + ": " + formatter.format(clientTime));
            cliNr++;
        }

        int numClients = listaClientes.size();
        if (numClients == 0) {
            System.out.println("Erro: Nao ha clientes conectados");
            System.out.println("=========================");
            return;
        }

        // tira a media das horas
        Long timeMean = timeSum / (numClients + 1);

        for (Object cli : listaClientes) {
            // manda a nova hora pra todos os clientes
            ((RelogioRMIClientInterface) cli).setNewTime(timeMean);
        }

        updateTime(timeMean);
        System.out.println("Nova hora do servidor: " + formatter.format(timeMean));
        System.out.println("=========================");
    }

    private static void updateTime(Long time) {
        // metodo que atualiza a hora do servidor
        
        DateFormat formatTimeOS = new SimpleDateFormat("hh:mm:ss");
        String newTime = formatTimeOS.format(time);

        // detecta qual sistema operacional pra mudar a hora
        String OS = System.getProperty("os.name").toLowerCase();
        // windows
        if (OS.indexOf("win") >= 0) {
            try {
                Runtime.getRuntime().exec("cmd /C time " + newTime);
            } catch (IOException ex) {
                System.out.println("Erro ao tentar atualizar a hora: " + ex);
            }
        } // unix-based
        else if (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0) {
            try {
                Runtime.getRuntime().exec("date +%T -s \"" + newTime + "\"");
            } catch (IOException ex) {
                System.out.println("Erro ao tentar atualizar a hora: " + ex);
            }
        }
    }

    private static void usage() {
        System.out.println("Uso correto: java RelogioRMIImpl <rmiregistry_port>");
        System.exit(1);
    }

    public static void main(String argv[]) {

        if (argv.length < 1) {
            usage();
        }
        String portNum = argv[0];
        String registryURL;
        try {
            int RMIPortNum = Integer.parseInt(portNum);
            startRegistry(RMIPortNum);
            registryURL = "rmi://localhost:" + portNum + "/RelogioRMI";
            Naming.rebind(registryURL, new RelogioRMIImpl());
            System.out.println("Servidor pronto para receber conexoes.");

            // sincroniza a cada minuto
            while (true) {
                doSync();
                Thread.sleep(15000);
            }

        }// end try
        catch (Exception re) {
            System.out.println("Exception in RelogioRMIImpl.main: " + re);
        } // end catch
    } // end main

    //This method starts a RMI registry on the local host, if
    //it does not already exists at the specified port number.
    private static void startRegistry(int RMIPortNum)
            throws RemoteException {
        try {
            Registry registry = LocateRegistry.getRegistry(RMIPortNum);
            registry.list();
            // This call will throw an exception
            // if the registry does not already exist
        } catch (RemoteException e) {
            // No valid registry at that port.
            Registry registry = LocateRegistry.createRegistry(RMIPortNum);
        }
    } // end startRegistry
}
