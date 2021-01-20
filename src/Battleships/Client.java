package Battleships;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.ProtocolException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;


public class Client {
	private Socket serverSock;
	private BufferedReader in;
	private BufferedWriter out;
	//public HotelClientTUI tui;
	
	private String name;

	/**
	 * Constructs a new HotelClient. Initialises the view.
	 */
	public Client() {
		//this.tui = new HotelClientTUI(this);
	}
	public String getName() {
		return name;
	}
	
	/**
	 * Starts a new HotelClient by creating a connection, followed by the 
	 * HELLO handshake as defined in the protocol. After a successful 
	 * connection and handshake, the view is started. The view asks for 
	 * used input and handles all further calls to methods of this class. 
	 * 
	 * When errors occur, or when the user terminates a server connection, the
	 * user is asked whether a new connection should be made.
	 * @param portHost 
	 * @param ip 
	 * @param name 
	 */
	public void start(String name, String ip, int portHost) {
		createConnection(name, ip, portHost);
		/*try {
			createConnection();
			} catch (ExitProgram exitprogram) {
			exitprogram.printStackTrace();
		}
		try {
			handleHello();
			tui.start();
		} catch (ServerUnavailableException e) {
			e.printStackTrace();
		} catch (ProtocolException e) {
			e.printStackTrace();
		}*/
	}

	/**
	 * Creates a connection to the server. Requests the IP and port to 
	 * connect to at the view (TUI).
	 * 
	 * The method continues to ask for an IP and port and attempts to connect 
	 * until a connection is established or until the user indicates to exit 
	 * the program.
	 * @param portHost 
	 * @param ip 
	 * @param name 
	 * 
	 * @throws ExitProgram if a connection is not established and the user 
	 * 				       indicates to want to exit the program.
	 * @ensures serverSock contains a valid socket connection to a server
	 */
	public void createConnection(String name, String ip, int portHost) {
		clearConnection();
		this.name = name;
		while (serverSock == null) {
			String host = ip;
			int port = portHost;

			// try to open a Socket to the server
			try {
				InetAddress addr = InetAddress.getByName(host);
				System.out.println("Attempting to connect to " + addr + ":" 
					+ port + "...");
				serverSock = new Socket(addr, port);
				in = new BufferedReader(new InputStreamReader(
						serverSock.getInputStream()));
				out = new BufferedWriter(new OutputStreamWriter(
						serverSock.getOutputStream()));
			} catch (IOException e) {
				System.out.println("ERROR: could not create a socket on " 
					+ host + " and port " + port + ".");

				//Do you want to try again? (ask user, to be implemented)
				//if(false) {
					//throw new ExitProgram("User indicated to exit.");
				//}
			}
		}
		if (serverSock != null) {
			System.out.println("Connected");
		}
	}
	
	public boolean isConnected() {
		if(serverSock != null) {
			return true;
		}
		else {
			return false;
		}
	}

	/**
	 * Resets the serverSocket and In- and OutputStreams to null.
	 * 
	 * Always make sure to close current connections via shutdown() 
	 * before calling this method!
	 */
	public void clearConnection() {
		serverSock = null;
		in = null;
		out = null;
	}
}
