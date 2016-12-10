import javax.swing.*;
import javax.swing.border.*;

import java.awt.*;
import java.awt.event.*;
import java.rmi.Naming;
import java.util.*;

public class ChatUI{
	private ChatClient client;
	private ChatServerInterface server;
	
	JTextArea tx;
	JTextField tf, name;
	JButton connect;
	JList lst;
	JFrame frame;
	  
	public void doConnect(){														// Функция соединения с сервером.
		if(connect.getText().equals("Connect")){
			if(name.getText().length() < 2) {
				JOptionPane.showMessageDialog(frame, "You need to type a name.");	// Если имя пользователя - 1 символ => ошибка.
				return; 
			}
			try{
				client = new ChatClient(name.getText());				// Экземпляр chatclient
				client.setGUI(this);									// присваиваем chatclient этот граф.интерфейс
				server = (ChatServerInterface)Naming.lookup("server");	// Подключение к серверу || Не понятная функция
				server.login(client);									// Добавление пользователя в список "онлайн"
				updateUsers(server.getConnected());						// Узнаем, кто сейчас подключен к серверу.
				connect.setText("Disconnect !");						
			}catch(Exception e){
				e.printStackTrace();
				JOptionPane.showMessageDialog(frame, "ERROR, we wouldn't connect...");
			}
		}else{
			updateUsers(null);				// Если человек отключился, очищаем список подключенных пользователей.
			connect.setText("Connect");
		}
	}
	
	public void sendText(){
		if (connect.getText().equals("Connect")){	// Если клиент не подключен к серверу, он не может отправить сообщение.
			JOptionPane.showMessageDialog(frame, "You need to connect first.");
			return;
		}
		String st = tf.getText();				// Считываем сообщение
		st = "[" + name.getText() + "] " + st;	// Добавляем к строке имя отправителя
		tf.setText("");							// Очищаем поле для ввода сообщения
		try{
			server.publish(st);					// Отправляем сообщение всем подключенным пользователям
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void writeMsg(String st){			// Функция для вывода сообщения на экране клиента
		tx.setText(tx.getText() + "\n" + st);	
	}
	public void updateUsers(Vector v){										// Функция для вывода подключенных 
		DefaultListModel listModel = new DefaultListModel();				// Экземпляр списка по умолчанию
		if(v != null){
			for(int i = 0; i < v.size(); i++){
				try{
					String tmp = ((ChatClientInterface)v.get(i)).getName();	// Узнаем имя клиента
					listModel.addElement(tmp);								// Добавляем в список
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		lst.setModel(listModel);											// Добавляем список в JList
	}
	public static void main(String[] args){
		System.out.println("Hello World !");
		ChatUI c = new ChatUI();
	}
	public ChatUI(){
		frame= new JFrame("Group Chat");
	    JPanel main = new JPanel();
	    JPanel top = new JPanel();
	    JPanel cn = new JPanel();
	    JPanel bottom = new JPanel();
	    tf = new JTextField();
	    name = new JTextField();
	    tx = new JTextArea();
	    connect = new JButton("Connect");
	    JButton bt = new JButton("Send");
	    lst = new JList();        
		// До этого создавались экземпляры граф.элементов
		
	    main.setLayout(new BorderLayout(5,5));        		// Север, Юг, центр, запад, восток
	    top.setLayout(new GridLayout(1,0,5,5));   			// Менеджер компоновки таблицей
	    cn.setLayout(new BorderLayout(5,5));				// Север, Юг, центр, запад, восток
	    bottom.setLayout(new BorderLayout(5,5));			// Север, Юг, центр, запад, восток
		
		// Добавляем верхнюю часть контента
	    top.add(new JLabel("Your name: "));
		top.add(name);    
	    top.add(connect);
	    
		// Центральную и т.д.
		cn.add(new JScrollPane(tx), BorderLayout.CENTER);        
	    cn.add(lst, BorderLayout.EAST);    
	    
		bottom.add(tf, BorderLayout.CENTER);    
	    bottom.add(bt, BorderLayout.EAST);
	    
		main.add(top, BorderLayout.NORTH);
	    main.add(cn, BorderLayout.CENTER);
	    main.add(bottom, BorderLayout.SOUTH);
	    main.setBorder(new EmptyBorder(10, 10, 10, 10) );	// Что делает EmptyBorder ??????????????????????????
	    
		//Events
	    connect.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){ 
				doConnect();   
			}  
		});
	    bt.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){ 
				sendText();   
			}  
		});
	    tf.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){ 
				sendText();   
			}  
		});
	    
	    frame.setContentPane(main);
	    frame.setSize(600,600);
	    frame.setVisible(true);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}
}