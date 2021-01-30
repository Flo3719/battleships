package Battleships.Server;

import java.io.IOException;

import Battleships.Models.GameModel;

public class GameController {
	public GameModel model;
	private GameTimer gameTimer;
	private Server server;

	public GameController(Server server, GameClientHandler s0, GameClientHandler s1) {
		this.model = new GameModel(s0, s1);
		s0.setGame(this);
		s1.setGame(this);
		this.server = server;
	}

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
		for (GameClientHandler gch : model.players) {
			try {
				gch.sendOut(message);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public GameTimer getTimer() {
		return this.gameTimer;
	}
}
