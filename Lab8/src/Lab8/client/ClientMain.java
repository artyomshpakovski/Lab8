package Lab8.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;
import java.util.TreeMap;

import Lab8.Message;
import Lab8.MessageCheckMail;
import Lab8.MessageCheckMailResult;
import Lab8.MessageConnect;
import Lab8.MessageConnectResult;
import Lab8.MessageDisconnect;
import Lab8.MessageLetter;
import Lab8.MessageResult;
import Lab8.MessageUser;
import Lab8.MessageUserResult;
import Lab8.Protocol;
import Lab8.client.ClientMain.Session;

// jhjhjhjhj
public class ClientMain {
	// arguments: userNic userFullName [host]
	public static void main(String[] args)  {
		if (args.length < 2 || args.length > 3) 
		{
			System.err.println(	"Invalid number of arguments\n" + "Use: nic name [host]" );
			waitKeyToStop();
			return;
		}
		try ( Socket sock = ( args.length == 2 ? 
				new Socket( InetAddress.getLocalHost(), Protocol.PORT ):
				new Socket( args[2], Protocol.PORT ) )) { 		
			System.err.println("initialized");
			session(sock, args[0], args[1] );
		} catch ( Exception e) {
			System.err.println(e);
		} finally {
			System.err.println("bye...");
		}
	}
	
	static void waitKeyToStop() {
		System.err.println("Press a key to stop...");
		try {
			System.in.read();
		} catch (IOException e) {
		}
	}
	
	static class Session {
		boolean connected = false;
		String userNic = null;
		String userName = null;
		Session( String nic, String name )
{
			userNic = nic;
			userName = name;
		}
	}
	static void session(Socket s, String nic, String name) 
	{
		try ( Scanner in = new Scanner(System.in);
			  ObjectInputStream is = new ObjectInputStream(s.getInputStream());
			  ObjectOutputStream os = new ObjectOutputStream(s.getOutputStream())) {
			Session ses = new Session(nic, name);
			if ( openSession( ses, is, os, in )) { 
				try {
					while (true) {
						Message msg = getCommand(ses, in);
						if (! processCommand(ses, msg, is, os)) {
							break;
						}
						else {
						}
					}			
				} finally {
					closeSession(ses, os);
				}
			}
		} catch ( Exception e) {
			System.err.println(e);
		}
	}
	
	static boolean openSession(Session ses, ObjectInputStream is, ObjectOutputStream os, Scanner in) 
			throws IOException, ClassNotFoundException {
		os.writeObject( new MessageConnect(ses.userNic, ses.userName));
		MessageConnectResult msg = (MessageConnectResult) is.readObject();
		if (msg.Error()== false ) {
			System.err.println("connected");
			ses.connected = true;
			return true;
		}
		System.err.println("Unable to connect: "+ msg.getErrorMessage());
		System.err.println("Press <Enter> to continue...");
		if( in.hasNextLine())
			in.nextLine();
		return false;
	}
	
	static void closeSession(Session ses, ObjectOutputStream os) throws IOException {
		if ( ses.connected ) {
			ses.connected = false;
			os.writeObject(new MessageDisconnect());
		}
	}

	static Message getCommand(Session ses, Scanner in) {	
		while (true) {
			printPrompt();
			if (in.hasNextLine()== false)
				break;
			String str = in.nextLine();
			byte cmd = translateCmd(str);
			switch ( cmd ) {
				case -1:
					return null;
				case Protocol.CMD_ORDER:
					return inputLetter(in);
				
				default: 
					System.err.println("Unknow command!");
					continue;
			}
		}
		return null;
	}
	
	static MessageLetter inputLetter(Scanner in) 
	{
		String ord, letter;
		System.out.print("Enter your order: ");
		ord = in.nextLine();
		System.out.print("Enter your address: ");
		letter = in.nextLine();
		return new MessageLetter(ord, letter);
	}
	
	static TreeMap<String,Byte> commands = new TreeMap<String,Byte>();
	static {
		commands.put("q", new Byte((byte) -1));
		commands.put("quit", new Byte((byte) -1));
		commands.put("o", new Byte(Protocol.CMD_ORDER));
		commands.put("order", new Byte(Protocol.CMD_ORDER));
	}
	
	static byte translateCmd(String str) 
	{
		// returns -1-quit, 0-invalid cmd, Protocol.CMD_XXX
		str = str.trim();
		Byte r = commands.get(str);
		return (r == null ? 0 : r.byteValue());
	}
	
	static void printPrompt() 
	{
		System.out.println();
		System.out.println( "            Menu: \n\n"
				+ "  Starters:\n"
				+ "       Garlic Bread          2.50$\n"
				+ "       Soup of the day       4.99$\n"
				+ "       Olives                3.99$\n"
	        	+ "       Prawn salad           4.99$\n"
	        	+ "       Mozarella salad       4.50$\n\n"
                + "  Main Courses:\n"
				+ "       Margherita pizza      7.99$\n"
				+ "       Roast chicken salad   9.50$\n"
				+ "       Lasagne and salad     11.99\n"
				+ "       Fish and chips        8.75$\n\n"
				+ "  Desserts:\n"
				+ "       Ice cream             3.99$\n"
				+ "       Cheesecake            4.50$\n"
				+ "       Fruit trifle          3.99$\n"
				+ "       Ice cream             2.99$\n\n"
				+ "  Drinks:\n"
				+ "       Red/white wine        5.99$\n"
				+ "       Beer                  3.25$\n"
				+ "       Cola                  2.75$\n"
				+ "       Lemonade              3.99$\n"
				+ "       Champagne             6.50$\n"
				+ "       Orange juice          2.99$\n"
				+ "       Apple juice           2.99$\n");
		System.out.print("(q)uit/(o)rder >");
		System.out.flush();
	}
	
	static boolean processCommand(Session ses, Message msg, 
			                      ObjectInputStream is, ObjectOutputStream os) 
            throws IOException, ClassNotFoundException {
		if ( msg != null )
		{
			System.err.println("MSG IS NOT NULL");
			os.writeObject(msg);
			MessageResult res = (MessageResult) is.readObject();
			System.err.println("line 198 is working");
			if ( res.Error()) {
				System.err.println(res.getErrorMessage());
				System.err.println("CCCCCCCC resgetID got");
			} else {
				System.out.println("Accepted...");
				}
			System.err.println("RETURN TRUE IS WORKING");
			return true;
		}
		return false;	
	}
}
