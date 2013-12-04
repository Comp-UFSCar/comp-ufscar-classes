import java.rmi.*;

public interface RelogioRMI extends Remote 
{   
  public static final String NAME = "RelogioRMI";
  public static final int FAILURE = -1;
  public static final int SUCCESS = 0;
	
  public int register(Object novoCliente) throws RemoteException;

}