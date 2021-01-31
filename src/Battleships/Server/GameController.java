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
	public GameController(Server server, GameClientHandler s0, GameClientHandler s1) {
		this.model = new GameModel(s0, s1);
		s0.setGame(this);
		s1.setGame(this);
		this.server = server;
	}

	//Methods
	public void startGame() {
		gameTimer = new GameTimer(server, model.getMaximumGameTime(), this);
		Thread gameTimerThread = new Thread(gameTimer);
		gameTimerThread.start();
		try {
			server.sendTurnIndicator(this);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void sendToGameClients(String message) {
		// TODO Auto-generated method stub
		for (GameClientHandler gch : model.getPlayers()) {
			try {
				gch.sendOut(message);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
