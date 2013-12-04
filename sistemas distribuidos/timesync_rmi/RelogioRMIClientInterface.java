import java.rmi.*;

public interface RelogioRMIClientInterface extends Remote {

    public static final String NAME = "RelogioRMIClientInterface";

    public Long getTimeMillis() throws RemoteException;
    
    public void setNewTime(Long time) throws RemoteException;
    
} 