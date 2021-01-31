package Battleships.Server;

import java.util.concurrent.TimeUnit;

import Battleships.Models.ProtocolMessages;

public class GameTimer implements Runnable {
	//Variables
	private volatile boolean running = true;
	private int gameTime;
	private Server server;
	private GameController gameController;

	//Constructor
	public GameTimer(Server server, int gameTime, GameController gameController) {
		this.gameTime = gameTime;
		this.server = server;
		this.gameController = gameController;
	}

	//Methods
	@Override
	public void run() {
		while (running) {
			for (int i = gameTime; i > -1; i--) {
				try {
					if (!this.gameController.getModel().getHasWinner()) {
						TimeUnit.SECONDS.sleep(1);
						server.sendTimeUpdate(i, gameController);
						if (i == 0) {
							GameClientHandler winner = gameController.getModel().checkIfWonOnScore();
							String msg = ProtocolMessages.WON + ProtocolMessages.CS + winner.getName();
							server.SendTogameClients(msg, gameController);
							this.gameController.getModel().endGameDueToWin();
						}
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
		}
	}
	public void terminate() {
		this.running = false;
	}
}
