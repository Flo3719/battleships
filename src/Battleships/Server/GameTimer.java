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
	/**
	 * Constructs a new GameTimer
	 * 
	 * @param server the connected server
	 * @param gameTime duration of a game
	 * @param gameController Controller of the game
	 */
	public GameTimer(Server server, int gameTime, GameController gameController) {
		this.gameTime = gameTime;
		this.server = server;
		this.gameController = gameController;
	}

	//Methods
	/**
	 * gives the GameClientHandlers a time update of the game every second.
	 */
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
	
	/**
	 * Terminates the GameTimer running on a thread.
	 * @requires game to be over
	 */
	public void terminate() {
		this.running = false;
	}
}
