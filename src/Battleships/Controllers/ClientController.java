package Battleships.Controllers;

import Battleships.Models.Board;
import Battleships.Models.PlayerModel;
import Battleships.Models.ProtocolMessages;
import Battleships.Models.ShipType;
import Battleships.Models.Exceptions.ServerNotAvailableException;
import javafx.application.Platform;
import javafx.scene.control.Button;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.ProtocolException;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

/**
 * @author koen
 *
 */
public class ClientController implements Runnable {
	//Variables
	private Socket serverSock;
	private BufferedReader in;
	private BufferedWriter out;
	final MainViewController mainViewController;

	private boolean leader;
	private boolean myTurn;
	private Board board;

	private PlayerModel player;
	private PlayerModel opponent;

	private int Port;
	private String ip;

	private int timeBeginningTurn;
	private int timeLeft = 300;
	private boolean gameTimeOver = false;

	private ComputerPlayer computerPlayer;

	private final int COMPUTINGBUFFER = 100;

	//Constructor
	public ClientController(Board board, MainViewController mainViewController) {
		this.board = board;
		this.mainViewController = mainViewController;
		this.myTurn = false;

	}
	//Getters
	public MainViewController getMainViewController() {
		return mainViewController;
	}
	public boolean getMyTurn() {
		return this.myTurn;
	}
	public PlayerModel getPlayer(){
		return this.player;
	}

	public void setPlayer(PlayerModel player){
		this.player = player;
		if(player.getName().equals("Computer") || player.getName().equals("Computer2") ){
			this.computerPlayer = new ComputerPlayer();
		}
	}
	public void setPort(int port) {
		this.Port = port;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	/**
	 * Assigns a new Playermodel to the opponent variable. Puts the name of the players on the UI
	 * @throws ServerNotAvailableException
	 * @throws ProtocolException
	 * @ensures friendNameLabel != null && enemyNameLabel != null
	 */
	public void getPlayerNames() throws ServerNotAvailableException, ProtocolException {
		String response = readLineFromServer();
		if (response.contains(ProtocolMessages.HELLO) && response.contains(this.player.getName())) {
			System.out.println("CLIENT: " + response);
			this.opponent = new PlayerModel(response.split(ProtocolMessages.CS)[2]);
			MainViewController.sharedInstance.getView().friendNameLabel.setText(response.split(ProtocolMessages.CS)[1]);
			MainViewController.sharedInstance.getView().enemyNameLabel.setText(response.split(ProtocolMessages.CS)[2]);
		} else {
			throw new ProtocolException("CLIENT: server responded with: " + response);
		}
	}
	/**
	 * @requires identfier is one of: C, S, P, B, D
	 * @param identifier one letter identifier of a ship ("C" for a carrier).
	 * @return the corresponding shipType
	 */
	public ShipType getShipTypeByIdentifier(String identifier) {
		for (ShipType shipType : ShipType.values()) {
			if (shipType.identifier.equals(identifier))
				return shipType;
		}

		return null;
	}
	public int getPort() {
		return this.Port;
	}
	public String getIp() {
		return this.ip;
	}
	//Methods
	/**
	 * Writes start message on output-Stream
	 * @throws IOException If IO error occurs
	 */
	public void startGame() throws IOException {
		out.write(ProtocolMessages.START);
		out.newLine();
		out.flush();
	}

	/**
	 * @requires msg != null.
	 * Writes given message on output Stream
	 * @param msg message to be sent
	 * @throws ServerNotAvailableException
	 */
	public synchronized void sendMessage(String msg) throws ServerNotAvailableException {
		if (out != null) {
			try {
				out.write(msg);
				out.newLine();
				out.flush();
			} catch (IOException e) {
				System.out.println(e.getMessage());
				throw new ServerNotAvailableException("Could not write " + "to server.");
			}
		} else {
			throw new ServerNotAvailableException("Could not write " + "to server.");
		}
	}

	/**
	 * Try to open a socket to the server
	 * Opens the In- and OutputStreams if socket has been opened
	 */
	public void createConnection() {
		clearConnection();
		while (serverSock == null) {
			try {
				InetAddress addr = InetAddress.getByName(this.ip);
				System.out.println("CLIENT: Attempting to connect to " + addr + ":" + this.Port + "...");
				serverSock = new Socket(addr, this.Port);
				in = new BufferedReader(new InputStreamReader(serverSock.getInputStream()));
				out = new BufferedWriter(new OutputStreamWriter(serverSock.getOutputStream()));
			} catch (IOException e) {
				System.out.println(
						"CLIENT: ERROR: could not create a socket on " + this.ip + " and port " + this.Port + ".");
			}
		}
		if (serverSock != null) {
			System.out.println("CLIENT: Connected");
		}
	}

	/**
	 * Clears the connection by closing the In- and OutputStreams
	 */
	public void clearConnection() {
		serverSock = null;
		in = null;
		out = null;
	}

	/**
	 * Sends a handshake message to the server and reads the response from the server.
	 * 
	 * @return a String array with the response from the server split on spaces.
	 * @throws ServerNotAvailableException
	 * @throws ProtocolException
	 */
	public String[] doHandshake() throws ProtocolException, ServerNotAvailableException {
		sendMessage(ProtocolMessages.HELLO + ProtocolMessages.CS + this.player.getName());
		String response = readLineFromServer();
		System.out.println("CLIENT: " + response);
		if (response.split(ProtocolMessages.CS).length == 2) {
			System.out.println("CLIENT: Client is leader.");
		}
		if (!response.contains(ProtocolMessages.HELLO)) {
			throw new ProtocolException("CLIENT: Handshake failed. response was: " + response);
		}
		System.out.println(
				"CLIENT: server responded with 'handshake done with " + response.split(ProtocolMessages.CS)[1] + "'");
		return response.split(ProtocolMessages.CS);
	}

	/**
	 * Reads and returns one line from the server.
	 *
	 * @return the line sent by the server.
	 * @throws ServerNotAvailableException if IO errors occur.
	 */
	public String readLineFromServer() throws ServerNotAvailableException {
		if (in != null) {
			try {
				// Read and return answer from Server
				String answer = in.readLine();
				if (answer == null) {
					throw new ServerNotAvailableException("Could not read " + "from server.");
				}
				return answer;
			} catch (IOException e) {
				throw new ServerNotAvailableException("Could not read " + "from server.");
			}
		} else {
			throw new ServerNotAvailableException("Could not read " + "from server.");
		}
	}

	/**
	 * Calls a method to create a connection with the server and send the handshake
	 * Checks if returned handshake message means that a game can start or the ClientController
	 * needs to wait for a new opponent.
	 * 
	 * Continuously listens input and forwards the input to the handleCommand method.
	 */
	@Override
	public void run() {
		createConnection();

		String handshakeResult[];
		try {
			handshakeResult = doHandshake();
			if (handshakeResult.length == 2) {
				leader = true;
				getPlayerNames();

				Platform.runLater(new Runnable() {

					@Override
					public void run() {
						mainViewController.getjoinBoxView().getJoinStage().close();
					}
				});
				System.out.println("CLIENT " + this.player.getName() + ": PRESS BUTTON TO START GAME");
				// startGame();
			} else {
				leader = false;
				MainViewController.sharedInstance.getView().friendNameLabel.setText(handshakeResult[1]);
				MainViewController.sharedInstance.getView().enemyNameLabel.setText(handshakeResult[2]);
				this.opponent = new PlayerModel(handshakeResult[2]);
				System.out.println("CLIENT " + this.player.getName() + " got player names");
				System.out.println("CLIENT " + this.player.getName() + ": WAITING FOR GAME TO START");
//				waitForStartGame();
			}
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (ServerNotAvailableException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		String msg;
		try {
			msg = in.readLine();
			while (msg != null) {
				handleCommand(msg);
				out.newLine();
				out.flush();
				msg = in.readLine();
			}
		} catch (IOException e) {
		}
	}

	/**
	 * handles commands received from the server by changing the UI or sending back
	 * the correct message.
	 * 
	 * @requries msg to be written according to the protocol
	 * @param msg received command from server
	 * @throws IOException if an IO error occur
	 */
	private void handleCommand(String msg) throws IOException {
		String[] message = msg.split(ProtocolMessages.CS);
		switch (message[0]) {
		case ProtocolMessages.TIME:
			this.getMainViewController().view.setTimeLabel(Integer.parseInt(message[1]));
			this.timeLeft = Integer.parseInt(message[1]);
			if (this.timeLeft == 0) {
				this.gameTimeOver = true;
			}
			if (this.timeLeft < this.timeBeginningTurn - ProtocolMessages.TURN_TIME && myTurn) {
				this.sendErrorMessage(ProtocolMessages.ERRORNAMES[7]);
			}
			break;
		case ProtocolMessages.TURN:
			if (player.getName().equals(message[1])) {
				this.timeBeginningTurn = this.timeLeft;
				System.out.println("It is your turn!");
				myTurn = true;
				getMainViewController().view.setTurn("friend");
				if(computerPlayer != null){
					//String attackLocation = computerPlayer.makeNaiveTurn();
					String attackLocation = computerPlayer.makeTurn();
					try {
						TimeUnit.MILLISECONDS.sleep(COMPUTINGBUFFER);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					Attack(attackLocation);
				}
			} else {
				System.out.println("It is the turn of: " + this.opponent.getName());
				myTurn = false;
				getMainViewController().view.setTurn("enemy");
			}
			break;
		case ProtocolMessages.START:
			out.write(ProtocolMessages.BOARD + ProtocolMessages.CS + this.player.getName() + ProtocolMessages.CS
					+ board.toString());
			out.newLine();
			out.flush();

			if (!leader) {
				Platform.runLater(new Runnable() {

					@Override
					public void run() {
						mainViewController.getjoinBoxView().getJoinStage().close();
					}
				});
			}
			break;
		case ProtocolMessages.HIT:
			if (myTurn) {
				Platform.runLater(new Runnable() {

					@Override
					public void run() {
						mainViewController.view.setPlayField("X", "enemy", Integer.parseInt(message[1]),
								"-fx-background-color: orange;");
					}
				});
				if (computerPlayer != null) {
				computerPlayer.setLastHit(message[1]);
				}
				player.incrementScore(1);
				if(computerPlayer != null){
					//String attackLocation = computerPlayer.makeNaiveTurn();
					String attackLocation = computerPlayer.makeTurn();
					try {
						TimeUnit.MILLISECONDS.sleep(COMPUTINGBUFFER);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					Attack(attackLocation);
				}
			}
			else {
				opponent.incrementScore(1);
			}
			displayScoreAfterAttack();
			break;
		case ProtocolMessages.MISS:
			if (myTurn) {
				Platform.runLater(new Runnable() {

					@Override
					public void run() {
						mainViewController.view.setPlayField("O", "enemy", Integer.parseInt(message[1]),
								"-fx-background-color: beige;");
					}
				});
			}
			break;
		case ProtocolMessages.DESTROY:
			if (myTurn) {
				ShipType shipType = getShipTypeByIdentifier(message[1]);
				int orientation = Integer.parseInt(message[3]);
				int index = Integer.parseInt(message[2]);

				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						for (int i = 0; i < shipType.length; i++) {
							if (orientation == 0) {
								mainViewController.view.setPlayField(shipType.identifier, "enemy", index + i,
										shipType.color);
							} else {
								mainViewController.view.setPlayField(shipType.identifier, "enemy", index + i * 15,
										shipType.color);
							}
						}
					}
				});
				player.incrementScore(2);
				if(computerPlayer != null){
					//String attackLocation = computerPlayer.makeNaiveTurn();
					String attackLocation = computerPlayer.makeTurn();
					try {
						TimeUnit.MILLISECONDS.sleep(COMPUTINGBUFFER);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					Attack(attackLocation);
				}
			}
			else {
				opponent.incrementScore(2);
			}
			displayScoreAfterAttack();
			break;
		case ProtocolMessages.WON:
			System.out.println("Winner: " + message[1]);
			boolean walkOver = false;
			if (!this.gameTimeOver && this.player.getScore() != 91 && this.opponent.getScore() != 91) {
				walkOver = true;
			}
			this.mainViewController.view.Alert("Winner: "+ message[1], walkOver);
			//closeConnection();
			break;
		case ProtocolMessages.MSGRECEIVED:
			System.out.println(message[1] + ": " + message[2] + "  (" + message[3] + ")" );
			break;
		}
	}
	/**
	 * updates the score of the player that has made an attack
	 */
	public void displayScoreAfterAttack() {
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				if (myTurn) {
					mainViewController.view.setFriendScoreLabel(player.getScore());
				}
				else {
					mainViewController.view.setEnemyScoreLabel(opponent.getScore());
				}
				
			}
		});
	}

	/**
	 * @ensures button with id: id, has no function after clicking.
	 * writes an attack message (according to the protocol) containing the index of the clicked button to the OutputStream
	 * @param id id of the button that has been clicked by the client
	 * @throws IOException if an IO error occurs
	 */
	public void Attack(String id) throws IOException {
		Button button = (Button)this.mainViewController.view.enemyGrid.getChildren().get(Integer.parseInt(id) + 1);
		button.setOnAction(null);
		//button.setDisable(true);
		out.write(ProtocolMessages.ATTACK + ProtocolMessages.CS + id);
		out.newLine();
		out.flush();
	}

	/**
	 * writes an error message to the OutputStreams
	 * @param errorMessage type of error that has occurred.
	 * @throws IOException if an IO error occur
	 */
	public void sendErrorMessage(String errorMessage) throws IOException {
		out.write(ProtocolMessages.ERROR + ProtocolMessages.CS + errorMessage);
		out.newLine();
		out.flush();
		
	}
	/**
	 * Closes the connection by closing In- and OutputStreams, the socket and the system.
	 */
	public void closeConnection() {
		System.out.println("Closing the connection...");
		try {
			in.close();
			out.close();
			serverSock.close();
			System.exit(0);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
