package Battleships.Server;

import java.util.concurrent.TimeUnit;

import Battleships.Models.ProtocolMessages;

public class GameTimer implements Runnable {
	private volatile boolean running = true;
	private int gameTime;
	private Server server;
	private GameController gameController;

	@Override
	public void run() {
		while (running) {
			for (int i = gameTime; i > -1; i--) {
				try {
					if (!this.gameController.model.hasWinner) {
						TimeUnit.SECONDS.sleep(1);
						server.sendTimeUpdate(i, gameController);
						if (i == 0) {
							GameClientHandler winner = gameController.model.checkIfWonOnScore();
							String msg = ProtocolMessages.WON + ProtocolMessages.CS + winner.getName();
							server.SendTogameClients(msg, gameController);
							this.gameController.model.endGameDueToWin();
						}
					}

//                System.out.println("TIMER: " + i);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
		}
	}

	public GameTimer(Server server, int gameTime, GameController gameController) {
		this.gameTime = gameTime;
		this.server = server;
		this.gameController = gameController;
	}

	public void terminate() {
		this.running = false;
		
	}
}
