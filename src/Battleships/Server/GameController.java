package Battleships.Server;

import java.io.IOException;

import Battleships.Models.GameModel;

public class GameController {
	//Variables
	private GameModel model;
	private GameTimer gameTimer;
	private Server server;

	//Getters
	public GameModel getModel() {
		return this.model;
	}
	public GameTimer getTimer() {
		return this.gameTimer;
	}

	//Constructor
	/**
	 * constructs a new GameController, assigns the model to a new GameModel. All participating
	 * GameClientHandlers are assigned this game.
	 * 
	 * @requires s0 != null && s1 != null
	 * 
	 * @param server the connected server on which the game will be played
	 * @param s0 first connected GameClientHandler to participate in this game.
	 * @param s1 second connected GameClientHandler to participate in this game.
	 */
	public GameController(Server server, GameClientHandler s0, GameClientHandler s1) {
		this.model = new GameModel(s0, s1);
		s0.setGame(this);
		s1.setGame(this);
		this.server = server;
	}

	//Methods
	/**
	 * Starts the game, a GameTimer is created on a new thread, this thread is started.
	 * A turn indicator is sent to from the GameClientHandlers to the clients.
	 *
	 */
	public void startGame() {
		gameTimer = new GameTimer(server, model.getMaximumGameTime(), this);
		Thread gameTimerThread = new Thread(gameTimer);
		gameTimerThread.start();
		try {
			server.sendTurnIndicator(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Finds all GameClientHandlers connected to the game, these will send the message over the socket.
	 * @param message message to be sent to the clients
	 */
	public void sendToGameClients(String message) {
		for (GameClientHandler gch : model.getPlayers()) {
			try {
				gch.sendOut(message);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
