import java.rmi.*;
import java.rmi.server.*;

//начало работы сервера
public class StartServer{
	public static void main(String[] args) {
		try {
				//System.setSecurityManager(new RMISecurityManager());
			 	java.rmi.registry.LocateRegistry.createRegistry(1099);	// Занимаем порт ????
			 	
				ChatServerInterface b=new ChatServer();
				Naming.rebind("server", b);								// Запускаем сервер ???
				System.out.println("[System] Chat Server is ready.");
			}catch (Exception e) {
					System.out.println("Chat Server failed: " + e);
			}
	}
}	