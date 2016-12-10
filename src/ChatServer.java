import java.rmi.*;
import java.rmi.server.*;
import java.util.*;

//Удаленный сервер(реализация)
public class ChatServer extends UnicastRemoteObject implements ChatServerInterface {
	private Vector v = new Vector();
	public ChatServer() throws RemoteException { }
	
	public boolean login(ChatClientInterface a) throws RemoteException {
		System.out.println(a.getName() + " got connected...");		// Выводим в консоли название пользователя, который подключился.
		a.tell("Your connection is successful.");					// Сообщение для пользователя, который подключился.	
		publish(a.getName() + " joined.");							// Сообщение для всех остальных пользователей.	
		v.add(a);													// Добавляем пользователя в список пользователей.
		return true;
	}
	
	public void publish(String s) throws RemoteException {
		System.out.println(s);
		for (int i = 0; i < v.size(); i++){
			try{
				ChatClientInterface tmp = (ChatClientInterface)v.get(i);// Используем удаленный интерфейс выбранного клиента 
				tmp.tell(s);											// Вывод сообщения у клиента
			}catch(Exception e){
				println("Problem with connection: \n" + e);
				// Проблемы с подключением клиента. Не подключен.
			}
		}
	}
	public Vector getConnected() throws RemoteException{
		return v;	// Возвращаем список подсоединенных клиентов
	}
}	