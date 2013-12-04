import java.io.IOException;
import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RelogioRMIClient extends UnicastRemoteObject implements RelogioRMIClientInterface {

    static RelogioRMI server;
    
    public RelogioRMIClient() throws RemoteException{
        super();
    }

//*************************************************************
    public Long getTimeMillis() throws RemoteException {
        return System.currentTimeMillis();
    }

    public void setNewTime(Long time) throws RemoteException {
        Long currentTime = System.currentTimeMillis();
        DateFormat formatter = new SimpleDateFormat("HH:mm:ss:SSS");
        System.out.println("=========================");
        System.out.println("Hora atual do Cliente: " + formatter.format(currentTime));
        System.out.println("Hora recebida: " + formatter.format(time));
        if (time < currentTime) {
            // hora recebida menor do que a atual
            System.out.println("Hora do Cliente adiantada, nao atualiza");
        } else {
            // hora valida
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
            System.out.println("Hora atualizada");
        }
        System.out.println("=========================");
    }

//*************************************************************
    private static void usage() {
        System.out.println("Uso correto: java RelogioRMIClient <host>:<port>");
        System.exit(1);
    }

    public static void main(String argv[]) {
        if (argv.length < 1) {
            usage();
        }
        String url = argv[0];

        try {
            int RMIPort;
            String registryURL = "rmi://" + url + "/RelogioRMI";
      
            server = (RelogioRMI) Naming.lookup(registryURL);
            System.out.println("Lookup completo");


            int s = server.register(new RelogioRMIClient());
            if (s == RelogioRMI.FAILURE) {
                System.out.println("Falhou em registrar o cliente no servidor");
                System.exit(1);
            } else {
                System.out.println("Sucesso em registrar o cliente");
            }

            try {
                // mantem o cliente registrado pra sempre
                while (true) {
                    Thread.sleep(1000);
                }
            } catch (InterruptedException ex) {
                System.out.println("Interrompido");
            }

        } // end try 
        catch (Exception e) {
            System.out.println(
                    "Exception in Client: " + e);
        } // end catch
    } //end main
}