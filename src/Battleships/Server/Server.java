package Battleships.Server;

import Battleships.Controllers.JoinBoxViewController;
import Battleships.Models.ProtocolMessages;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server implements Runnable {
	//Variables
	//Connection
	private ServerSocket ssock;
	private BufferedReader in;
	private BufferedWriter out;

	//Clients
	private List<GameClientHandler> clients;
	private GameClientHandler waitingClient;

	//View
	private JoinBoxViewController view;

	//Getters
	public List<GameClientHandler> getClients() {
		return clients;
	}

	//Constructor
	public Server(JoinBoxViewController view) {
		this.view = view;
	}

	//Methods
	/**
	 * Continuously waits for a socket willing to connect. Opens the In- and OutputStreams if a socket is accepted.
	 * Does the handshake with the client, calls for a new GameClientHandler and possibly start a game with 2 GameClientHandlers.
	 */
	@Override
	public void run() {
		boolean openNewSocket = true;
		clients = new ArrayList<>();
		while (openNewSocket) {
			try {
				while (true) {
					Socket sock = ssock.accept();

					in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
					out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));

					String msg = in.readLine();
					String name = msg.split(ProtocolMessages.CS)[1];
					if (nameAvailable(name)) {
						if (waitingClient != null) {
							handshakeToAll(handshakeMessage(waitingClient.getName(), name));
							out.write(handshakeMessage(name, waitingClient.getName()));
						} else {
							out.write(handshakeMessage(name));
						}
						out.newLine();
						out.flush();

						System.out.println("SERVER: NAME AVAILABLE");
						view.showMessage("SERVER: New client " + name + " connected!");
						GameClientHandler handler = new GameClientHandler(sock, this, name);
						clients.add(handler);
						System.out.println("SERVER: " + clients.size());
						new Thread(handler).start();
						if (waitingClient != null) {
							System.out.println(
									"SERVER: Player " + name + " is matched with player " + waitingClient.getName());
							GameController game = new GameController(this, waitingClient,
									clients.get(clients.size() - 1));
							waitingClient = null;
						} else {
							System.out.println("SERVER: Player " + name + " is waiting for a partner to match");
							waitingClient = clients.get(clients.size() - 1);
							waitingClient.setLeader(true);
						}
					} else {
						System.out.println("SERVER: NAME TAKEN");
					}
				}
			} catch (Exception e) {
				openNewSocket = false;
			}
		}
	}
	/**
	 * Sets up a server socket on the given port number.
	 * 
	 * @param port Port to which the server is listening
	 */
	public void setup(int port) {
		ssock = null;
		while (ssock == null) {
			// try to open a new ServerSocket
			try {
				view.showMessage("SERVER: Attempting to open a socket at 127.0.0.1 " + "on port " + port + "...");
				ssock = new ServerSocket(port, 0, InetAddress.getByName("127.0.0.1"));
				view.showMessage("SERVER: Server started at port " + port);
			} catch (IOException e) {
				view.showMessage("ERROR: could not create a socket on " + "127.0.0.1" + " and port " + port + ".");
			}
		}
	}
	/**
	 * calls all GameClientHandlers to send out a given message.
	 * @param message to be sent 
	 */
	public void handshakeToAll(String message) {
		for (GameClientHandler gch : clients) {
			try {
				gch.sendOut(message);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * calls the SendToGameClients method with a time update (Message according to the protocol).
	 * 
	 * @param seconds seconds left in the game
	 * @param gameController Controller of the game of which the Time update must be sent.
	 */
	public void sendTimeUpdate(int seconds, GameController gameController) {
		String message = ProtocolMessages.TIME + ProtocolMessages.CS + seconds;
		SendTogameClients(message, gameController);
	}
	/**
	 * calls the SendToGameClients method with a turn indicator (Message according to the protocol).
	 * 
	 * @param gameController controller of the game of which the turn indicator must be sent.
	 * @throws IOException if IO error occurs
	 */
	public void sendTurnIndicator(GameController game) throws IOException {
		String message = ProtocolMessages.TURN + ProtocolMessages.CS
				+ game.getModel().getPlayer(game.getModel().getCurrent()).getName();
		SendTogameClients(message, game);
	}
	/**
	 * Calls all the GameClientHandlers connected to the game to send a message over their sockets.
	 * @param msg message that needs to be sent
	 * @param gameController Controller of the game that needs a to send a message to their clients.
	 */
	public void SendTogameClients(String msg, GameController gameController) {
		for (GameClientHandler gch : gameController.getModel().getPlayers()) {
			try {
				gch.sendOut(msg);
			} catch (IOException e) {
				System.out.println("Could not send it...");
			}
		}
	}
	/**
	 * @param name name of the newly connected GameClientHandler
	 * @return the handshake message according to the protocol
	 */
	public String handshakeMessage(String name) {
		return ProtocolMessages.HELLO + ProtocolMessages.CS + name;
	}
	/**
	 * @param name1 name of the GameClientHandler waiting for a game to start.
	 * @param name2 name of the newly connected GameClientHandler
	 * @return the handshake message according to the protocol
	 */
	public String handshakeMessage(String name1, String name2) {
		return ProtocolMessages.HELLO + ProtocolMessages.CS + name1 + " " + name2;
	}
	/**
	 * checks if a name is not already used by a different GameClientHandler
	 * 
	 * @requires name != null
	 * @param name name that needs to be checked
	 * @return true if name is not used already.
	 */
	public boolean nameAvailable(String name) {
		for (GameClientHandler gch : clients) {
			if (gch.getName().equals(name)) {
				return false;
			}
		}
		return true;
	}
	/**
	 * calls the GameClientHandlers connected to the GameController to send a message over their
	 * sockets to indicate that the game has started.
	 * 
	 * @requires game != null
	 * @param game gameController of the game of which the start message must be sent.
	 * @throws IOException if IO error occurs
	 */
	public void sendStart(GameController game) throws IOException {
		for (GameClientHandler gch : clients) {
			if (gch.getGame().equals(game)) {
				gch.sendOut(ProtocolMessages.START + ProtocolMessages.CS + "0");
			}
		}
	}
}
