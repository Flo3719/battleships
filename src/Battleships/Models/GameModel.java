package Battleships.Models;

import Battleships.Server.GameClientHandler;

import java.io.IOException;

public class GameModel {
	//Variables
	public static final int NUMBER_PLAYERS = 2;
	private static final int MAXIMUM_GAME_TIME = 5 * 60;
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
	/**
	 * Changes the current. 
	 * @ensures previous current is not the same as new current.
	 */
	public void switchCurrent() {
		this.current = (this.current == 0) ? 1 : 0;
	}
	
	/**
	 * @return true if all boats of the opponent has been hit completely
	 */
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
	/**
	 * @return GameclientHandler that has the most points, if scores are equal
	 * CheckWinnerWhenSamePoints is called.
	 * 
	 * @ensures result != null
	 */
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
	/**
	 * @return GameClientHandler that made the most boats of the opponent sunk.
	 * if the amount is equal, checkWinnerWhenSameAmountBoatsDestroyed is called.
	 * 
	 * @ensures result != null
	 */
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
	/**
	 * Goes over each shipType, starting with the biggest. If there is a difference between
	 * the amount of these boats the players have sunk, the GameClientHandler with the most boats of this shiptype
	 * destroyed will be returned. Else, the next shiptype will be checked.
	 * @return GameClientHandler who has destroyed the most boats with the biggest length.
	 */
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
	/**
	 * Assigns a winner to the game
	 * @param gch GameClientHandler that has won the game
	 * calls the GameClientHandler to write a win message on the OutputStream
	 * Shuts down the GameClientHandler
	 */
	public synchronized void endGameDueToLostConnection(GameClientHandler gch) {
		try {
			gch.getGame().getModel().setHasWinner(true);
			gch.sendOut(ProtocolMessages.WON + ProtocolMessages.CS + gch.getName());
			gch.shutdown();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * assigns a winner to the game
	 * Shuts down both GameClientHandlers
	 */
	public void endGameDueToWin() {
    	this.setHasWinner(true);
    	this.getPlayer(0).shutdown();
    	this.getPlayer(1).shutdown();
	}

}
