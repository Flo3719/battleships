package Battleships.Server;

import Battleships.Models.Board;
import Battleships.Models.PositionModel;
import Battleships.Models.ProtocolMessages;
import Battleships.Models.ShipModel;

import java.io.*;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class GameClientHandler implements Runnable {
	//Variables
	/** The socket and In- and OutputStreams */
	private BufferedReader in;
	private BufferedWriter out;
	private Socket sock;

	/** The connected Server */
	private Server server;

	/** Name of this Player */
	private String name = "unnamed";
	
	/** Whether Player is the leader of the game*/
	private boolean leader;
	
	/** Board of this Player */
	private Board board;
	
	/** Score of this Player */
	private int score;

	/** Score for a hit or destroy*/
	private final int DESTROY_SCORE = 2;
	private final int HIT_SCORE = 1;
	
	/** Game of this Player */
	private GameController game;

	//Getters
	public String getName() {
		return this.name;
	}
	public Board getBoard() {
		return this.board;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public int getScore() {
		return this.score;
	}
	public GameController getGame() {
		return this.game;
	}

	//Setters
	public void setGame(GameController game) {
		this.game = game;
	}
	public void setLeader(boolean value) {
		leader = value;
	}

	//Methods
	/**
	 * Constructs a new GameClientHandler. Opens the In- and OutputStreams.
	 * 
	 * @param sock The client socket
	 * @param srv  The connected server
	 * @param name The name of this ClientHandler
	 */
	public GameClientHandler(Socket sock, Server server, String name) {
		try {
			in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
			this.sock = sock;
			this.server = server;
			this.name = name;
		} catch (IOException e) {
			shutdown();
		}
	}
	
	/**
	 * Continuously listens to client input and forwards the input to the
	 * handleCommand method.
	 */
	@Override
	public void run() {
		String msg;
		try {
			msg = in.readLine();
			while (msg != null) {
				handleCommand(msg);
				out.newLine();
				out.flush();
				msg = in.readLine();
			}
			//shutdown();
		} catch (IOException e) {
			if (!this.game.getModel().getWinnerDueToLostConnection()) {
				shutdown();
			}
		}
	}	
	/**
	 * Sends the message over the connected socket
	 * @param message message that needs to be sent
	 * @throws IOException
	 */
	public void sendOut(String message) throws IOException {
		out.write(message);
		out.newLine();
		out.flush();
	}
	
	/**
	 * @param stringBoard Board represented in a string
	 * @return the board given by a string, as a type of Board
	 * 
	 * @requires stringBoard.length() == 150
	 */
	public Board toBoard(String stringBoard) {
		Board resultBoard = new Board();
		//resultBoard.addShips();
		String[] splitArray = stringBoard.split(",");
		List<String> al = new ArrayList<>();
		al = Arrays.asList(splitArray);
		Iterator<String> it = al.iterator();
		for (int i = 0; i < Board.HEIGHT; i++) {
			for (int j = 0; j < Board.WIDTH; j++) {
				String test = it.next();
				if (!test.equals("0")) {
					for (ShipModel s : resultBoard.getShips()) {
						if (s.getShipName().equals(test)) {
							resultBoard.getPositions()[j][i].setShip(s);;
							PositionModel pos = resultBoard.getPositions()[j][i];
							s.addPosition(pos);
							break;
						}
					}
				}

			}
		}
		return resultBoard;
	}
	/**
	 * Handles commands received from the client by calling the according 
	 * methods or sending the appropriate messages to the client.
	 * 
	 * @param msg command from client
	 * @throws IOException if an IO errors occur.
	 * 
	 * @requires msg has used Protocol formatting
	 */
	public void handleCommand(String msg) throws IOException {
        String[] message = msg.split(ProtocolMessages.CS);
        switch(message[0]){
            case ProtocolMessages.BOARD:
            	  this.board = toBoard(message[2]);
            	  break;
            case ProtocolMessages.START:
				server.sendStart(this.game);
				this.game.startGame();
                break;
            case ProtocolMessages.ATTACK:
            	int index =Integer.parseInt(message[1]);
                GameClientHandler opponent = this.game.getModel().getOpponent();
                Board opBoard = opponent.board;
                PositionModel position = opBoard.getPositions()[opBoard.getX(index)][opBoard.getY(index)];
                position.setHasBeenGuessed(true); 
                if(position.getShip() == null)
                {
                	game.sendToGameClients(ProtocolMessages.MISS + ProtocolMessages.CS + index + ProtocolMessages.CS + opponent.getName());
                	this.game.getModel().switchCurrent();
                	server.sendTurnIndicator(game);
                }
                else
                {
                	if(position.getShip().Sunk())
                	{
                		this.score = this.score + DESTROY_SCORE;
                		game.sendToGameClients(ProtocolMessages.DESTROY + ProtocolMessages.CS + position.getShip().getShipType().identifier + ProtocolMessages.CS + position.getShip().getMarkerIndex() + ProtocolMessages.CS + position.getShip().getOrientation() + ProtocolMessages.CS + opponent.getName());
                		if(this.game.getModel().checkIfCleanSweep()) {
                			game.sendToGameClients(ProtocolMessages.WON + ProtocolMessages.CS + this.name);
                			this.game.getModel().endGameDueToWin();
                		}
                	}
                	else
                	{
                		this.score = this.score + HIT_SCORE;
                    	game.sendToGameClients(ProtocolMessages.HIT + ProtocolMessages.CS + index + ProtocolMessages.CS + opponent.getName());
                	}
                }
                break;
            case ProtocolMessages.ERROR:
                handleErrorCommand(message[1]);
                break;
            case ProtocolMessages.MSGSEND:
            	Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            	String chatMessage = ProtocolMessages.MSGRECEIVED + ProtocolMessages.CS + message[1] + ProtocolMessages.CS + message[2] + ProtocolMessages.CS + timestamp;
            	this.game.sendToGameClients(chatMessage);
        }
    }
	
	/**
	 * Shut down the connection to this client by closing the socket and 
	 * the In- and OutputStreams.
	 * 
	 * If the game of the client has not have a winner yet, an automatic winner is appointed and
	 * and the game is ended.
	 */
    public void shutdown() {
        System.out.println("> [" + name + "] Shutting down.");
        this.game.getTimer().terminate();
        if (!this.game.getModel().getHasWinner()) {
        	this.game.getModel().setWinnerDueToLostConnection(true);
        	GameClientHandler automaticWinner;
        	if (this.game.getModel().getPlayer(0).equals(this)) {
        		automaticWinner = this.game.getModel().getPlayer(1);
        	}
        	else {
        		automaticWinner = this.game.getModel().getPlayer(0);
        	}
        	this.game.getModel().endGameDueToLostConnection(automaticWinner);
        }
        try {
            server.getClients().remove(this);
            in.close();
            out.close();
            sock.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //TODO why this method? never used?
    public void closeConnection() {
    	try {
    		in.close();
    		out.close();
    		sock.close();
    	} catch (IOException e) {
    		System.out.println("System closed");
    	}
    	
    }
    
	/**
	 * Handles all the messages received, that contain an error message.
	 * 
	 * @param ErrorMessage The type of error that has been sent.
	 * @throws IOException if IO error occurs.
	 */
	private void handleErrorCommand(String ErrorMessage) throws IOException {
		switch(ErrorMessage) {
		case "InvalidIndex":
			server.sendTurnIndicator(this.game);
			break;
		case "TimeOver":
        	this.game.getModel().switchCurrent();
        	server.sendTurnIndicator(game);
        	break;
		}
	}
}
