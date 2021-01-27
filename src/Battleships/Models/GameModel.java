package Battleships.Models;

import Battleships.Controllers.GameClientHandler;

import java.io.IOException;
import java.util.Timer;

public class GameModel {

	public static final int NUMBER_PLAYERS = 2;
	/**
	 * The maximum game time in seconds. The maximum game time is 5 minutes.
	 */
	private static final int MAXIMUM_GAME_TIME = 5 * 60;
	private boolean timeOver = false;
	private Timer timer;
	public boolean hasWinner = false;

	/**
	 * The players of the game.
	 * 
	 * @invariant the length of the array equals NUMBER_PLAYERS
	 * @invariant all array items are never null
	 */
	public GameClientHandler[] players;

	private Board[] boards;

	/**
	 * Index of the current player.
	 * 
	 * @invariant the index is always between 0 and {@link #NUMBER_PLAYERS}
	 */
	public int current;

	// -- Constructors -----------------------------------------------

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
		// timer = new Timer();
	}

//    public void askNames(){
//        try {
//            boolean sameNames = true;
//            players[0].sendNameRequest();
//            while(sameNames){
//                players[1].sendNameRequest();
//                if(players[0].getName().equals(players[1].getName())){
//                    players[1].sendOut(String.valueOf(ProtocolMessages.NAMETAKEN));
//                }else{
//                    sameNames = false;
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

	public GameClientHandler getPlayer(int player) {
		return players[player];
	}

	public GameClientHandler GetOpponent() {
		if (current == 0)
			return getPlayer(1);
		else
			return getPlayer(0);
	}
	public GameClientHandler getCurrentPlayer() {
		return this.players[this.current];
	}

	public void switchCurrent() {
		this.current = (this.current == 0) ? 1 : 0;
	}

	public void setBoard() {
		boards[0] = players[0].getBoard();
		boards[1] = players[1].getBoard();
	}

	public int getMaximumGameTime() {
		return MAXIMUM_GAME_TIME;
	}
	public boolean checkIfCleanSweep() {
		boolean won = true;
		for (ShipModel sh : GetOpponent().getBoard().ships) {
			if (!sh.Sunk()) {
				won = false;
			}
		}
		if (won) {
			this.hasWinner = true;
		}
		return won;
	}

	public GameClientHandler checkIfWonOnScore() {
		this.hasWinner = true;
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
		for (ShipModel sh : players[0].getBoard().ships) {
			if (sh.Sunk()) {
				sunkByPlayer1++;
			}
		}
		for (ShipModel sh : players[1].getBoard().ships) {
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
			for (ShipModel sm : GetOpponent().getBoard().ships) {
				if (sm.shipType.equals(st) && sm.Sunk()) {
					countOpponent++;
				}
			}
			for (ShipModel sm : getCurrentPlayer().getBoard().ships) {
				if (sm.shipType.equals(st) && sm.Sunk()) {
					countSelf++;
				}
			}
			if (countSelf < countOpponent) {
				return this.GetOpponent();
			} else if (countSelf > countOpponent) {
				return getCurrentPlayer();
			}
		}
		return null;
	}
	public synchronized void endGameDueToLostConnection(GameClientHandler gch) {
		try {
			gch.getGame().model.hasWinner = true;
			gch.sendOut(ProtocolMessages.WON + ProtocolMessages.CS + gch.getName());
			gch.shutdown();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void endGameDueToWin() {
    	this.hasWinner = true;
    	this.getPlayer(0).shutdown();
    	this.getPlayer(1).shutdown();
	}

}
