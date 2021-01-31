package Battleships.Models;

import Battleships.Server.GameClientHandler;

import java.io.IOException;

public class GameModel {
	//Variables
	public static final int NUMBER_PLAYERS = 2;
	private static final int MAXIMUM_GAME_TIME = 5 * 60;
	//TODO make private; create getters/setters as needed
	private boolean hasWinner = false;
	private boolean winnerDueToLostConnection = false;
	private GameClientHandler[] players;
	private int current;

	//Getters
	public int getCurrent() {
		return this.current;
	}
	public GameClientHandler[] getPlayers() {
		return this.players;
	}
	public GameClientHandler getPlayer(int player) {
		return players[player];
	}
	public GameClientHandler getOpponent() {
		if (current == 0)
			return getPlayer(1);
		else
			return getPlayer(0);
	}
	public GameClientHandler getCurrentPlayer() {
		return this.players[this.current];
	}
	public int getMaximumGameTime() {
		return MAXIMUM_GAME_TIME;
	}
	public boolean getHasWinner() {
		return hasWinner;
	}
	public boolean getWinnerDueToLostConnection() {
		return this.winnerDueToLostConnection;
	}
	public void setWinnerDueToLostConnection(boolean winnerDueToLostConnection) {
		this.winnerDueToLostConnection = winnerDueToLostConnection;
	}
	public void setHasWinner(boolean hasWinner) {
		this.hasWinner = hasWinner;
	}

	//Constructors
	/**
	 * Creates a new Game object.
	 * 
	 * @requires s0 and s1 to be non-null
	 * @param s0 the first player
	 * @param s1 the second player
	 */
	public GameModel(GameClientHandler s0, GameClientHandler s1) {
		players = new GameClientHandler[NUMBER_PLAYERS];
		players[0] = s0;
		players[1] = s1;
		current = 0;
	}

	//Methods
	public void switchCurrent() {
		this.current = (this.current == 0) ? 1 : 0;
	}
	public boolean checkIfCleanSweep() {
		boolean won = true;
		for (ShipModel sh : getOpponent().getBoard().getShips()) {
			if (!sh.Sunk()) {
				won = false;
			}
		}
		if (won) {
			this.setHasWinner(true);
		}
		return won;
	}
	public GameClientHandler checkIfWonOnScore() {
		this.setHasWinner(true);
		if (players[0].getScore() > players[1].getScore()) {
			return players[0];
		} 
		else if (players[0].getScore() < players[1].getScore()){
			return players[1];
		}else {
			return CheckWinnerWhenSamePoints();
		}
	}
	public GameClientHandler CheckWinnerWhenSamePoints() {
		int sunkByPlayer1 = 0;
		int sunkByPlayer0 = 0;
		for (ShipModel sh : players[0].getBoard().getShips()) {
			if (sh.Sunk()) {
				sunkByPlayer1++;
			}
		}
		for (ShipModel sh : players[1].getBoard().getShips()) {
			if (sh.Sunk()) {
				sunkByPlayer0++;
			}
		}
		if (sunkByPlayer0 > sunkByPlayer1) {
			return this.players[0];
		} else if (sunkByPlayer0 < sunkByPlayer1) {
			return this.players[1];
		} else {
			return checkWinnerWhenSameAmountBoatsDestroyed();
		}
	}
	private GameClientHandler checkWinnerWhenSameAmountBoatsDestroyed() {
		for (ShipType st : ShipType.values()) {
			int countOpponent = 0;
			int countSelf = 0;
			for (ShipModel sm : getOpponent().getBoard().getShips()) {
				if (sm.getShipType().equals(st) && sm.Sunk()) {
					countOpponent++;
				}
			}
			for (ShipModel sm : getCurrentPlayer().getBoard().getShips()) {
				if (sm.getShipType().equals(st) && sm.Sunk()) {
					countSelf++;
				}
			}
			if (countSelf < countOpponent) {
				return this.getOpponent();
			} else if (countSelf > countOpponent) {
				return getCurrentPlayer();
			}
		}
		return null;
	}
	public synchronized void endGameDueToLostConnection(GameClientHandler gch) {
		try {
			gch.getGame().getModel().setHasWinner(true);
			gch.sendOut(ProtocolMessages.WON + ProtocolMessages.CS + gch.getName());
			gch.shutdown();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void endGameDueToWin() {
    	this.setHasWinner(true);
    	this.getPlayer(0).shutdown();
    	this.getPlayer(1).shutdown();
	}

}
