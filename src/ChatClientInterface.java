import java.rmi.*;
import java.util.*;

public interface ChatClientInterface extends Remote { // Удаленный интерфейс клиента
	public void tell (String name) throws RemoteException;
	public String getName () throws RemoteException;
}	