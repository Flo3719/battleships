package Battleships.Controllers;

import Battleships.Models.Board;
import Battleships.Models.JoinBoxViewDelegate;
import Battleships.Models.PlayerModel;
import Battleships.Server.Server;
import Battleships.Views.JoinBoxView;

public class JoinBoxViewController implements JoinBoxViewDelegate {
	protected JoinBoxView view;
	private Server server;
	final private int defaultPort = 4242;

	// Singleton
	private static JoinBoxViewDelegate sharedInstance = new JoinBoxViewController();

	private JoinBoxViewController() {
	}
	public static JoinBoxViewDelegate getSharedInstance() {
		return JoinBoxViewController.sharedInstance;
	}

	@Override
	public void setView(JoinBoxView view) {
		this.view = view;
	}

	/**
	 * @requires name != null
	 * 
	 * @param name name written in text field
	 * @param portInput integer written in text field
	 * 
	 * Starts a server with the given name and port.
	 * if portInput == null, a defaultPort is used
	 * Server is created on a different thread
	 */
	@Override
	public void handleHostClick(String name, String portInput) {
		int port;
		if (portInput.equals("")) {
			// Requirement S01
			port = defaultPort;
			view.getController().showMessage("Default Port is being used.");
		} else {
			port = Integer.parseInt(portInput);
		}
		server = new Server(this);
		server.setup(port);
		System.out.println("SERVER: Hosting on port " + port + " as " + name);
		Thread serverThread = new Thread(server);
		serverThread.start();
	}

	/**
	 * @requires name != null
	 * @param name name written in text field
	 * @param ip ip written in text field
	 * @param port portnumber written in text field
	 * @param board board that was created after the program was started.
	 * 
	 * Client is running on a different thread
	 */
	@Override
	public void handleJoinClick(String name, String ip, String port, Board board, ClientController client) {
		int portHost;
		if (port.equals("")) {
			// Requirement S01
			portHost = defaultPort;
			view.getController().showMessage("SERVER: Default Port is being used.");
		} else {
			portHost = Integer.parseInt(port);
		}
		client.setPlayer(new PlayerModel(name));
		client.setIp(ip);
		client.setPort(portHost);
		Thread thread = new Thread(client);
		thread.start();
	}

	@Override
	public void showMessage(String message) {
		System.out.println(message);
	}

	public Server getServer() {
		return this.server;
	}
}
