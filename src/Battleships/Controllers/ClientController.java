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

public class ClientController implements Runnable {
	private Socket serverSock;
	private BufferedReader in;
	private BufferedWriter out;
	final MainViewController mainViewController;

	private boolean leader;
	public boolean myTurn;
	private Board board;

	private PlayerModel player;
	public PlayerModel opponent;

	public int Port;
	public String ip;
	
	public int timeBeginningTurn;
	public int timeLeft = 300;
	public boolean gameTimeOver = false;

	public ComputerPlayer computerPlayer;

	private final int COMPUTINGBUFFER = 100;

	public ClientController(Board board, MainViewController mainViewController) {
		this.board = board;
		this.mainViewController = mainViewController;
		this.myTurn = false;

	}

	public MainViewController getMainViewController() {
		return mainViewController;
	}

	public void setPlayer(PlayerModel player){
		this.player = player;
		//TODO add computer tick box in joinBox
		if(player.getName().equals("Computer") || player.getName().equals("Computer2") ){
			this.computerPlayer = new ComputerPlayer();
		}
	}

	public PlayerModel getPlayer(){
		return this.player;
	}

	public void startGame() throws IOException, ServerNotAvailableException {
		out.write(ProtocolMessages.START);
		out.newLine();
		out.flush();
	}

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

	public void createConnection() {
		clearConnection();
		while (serverSock == null) {
			// try to open a Socket to the server
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

	public boolean isConnected() {
		if (serverSock != null) {
			return true;
		} else {
			return false;
		}
	}

	public void clearConnection() {
		serverSock = null;
		in = null;
		out = null;
	}

	public String[] doHandshake() throws ServerNotAvailableException, ProtocolException {
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
						mainViewController.joinBoxView.joinStage.close();
					}
				});
				System.out.println("CLIENT " + this.player.getName() + ": PRESS BUTTON TO START GAME");
				// startGame();
			} else {
				// TODO clean this up
				leader = false;
				MainViewController.sharedInstance.getView().friendNameLabel.setText(handshakeResult[1]);
				MainViewController.sharedInstance.getView().enemyNameLabel.setText(handshakeResult[2]);
				this.opponent = new PlayerModel(handshakeResult[2]);
				System.out.println("CLIENT " + this.player.getName() + " got player names");
				System.out.println("CLIENT " + this.player.getName() + ": WAITING FOR GAME TO START");
//				waitForStartGame();
			}
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServerNotAvailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
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
						mainViewController.joinBoxView.joinStage.close();
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
				ShipType shipType = GetShipTypeByIdentifier(message[1]);
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
			break;
		case ProtocolMessages.MSGRECEIVED:
			System.out.println(message[1] + ": " + message[2] + "  (" + message[3] + ")" );
			break;
		}
	}
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

	public void Attack(String id) throws IOException {
		Button button = (Button)this.mainViewController.view.enemyGrid.getChildren().get(Integer.parseInt(id) + 1);
		button.setOnAction(null);
		out.write(ProtocolMessages.ATTACK + ProtocolMessages.CS + id);
		out.newLine();
		out.flush();
	}

	public ShipType GetShipTypeByIdentifier(String identifier) {
		for (ShipType shipType : ShipType.values()) {
			if (shipType.identifier.equals(identifier))
				return shipType;
		}

		return null;
	}

	public void sendErrorMessage(String errorMessage) throws IOException {
		out.write(ProtocolMessages.ERROR + ProtocolMessages.CS + errorMessage);
		out.newLine();
		out.flush();
		
	}
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
