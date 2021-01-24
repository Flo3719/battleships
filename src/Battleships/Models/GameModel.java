package Battleships.Models;

import Battleships.Controllers.GameClientHandler;

import java.util.Timer;

public class GameModel {

	public static final int NUMBER_PLAYERS = 2;
    /**
     * The maximum game time in seconds. The maximum game time is 5 minutes.
     */
    private static final int MAXIMUM_GAME_TIME = 5*60;
    private boolean timeOver = false;
    private Timer timer;
    
    /**
     * The players of the game.
     * @invariant the length of the array equals NUMBER_PLAYERS
     * @invariant all array items are never null
     */
    private GameClientHandler[] players;
    

    private Board[] boards;

    /**
     * Index of the current player.
     * @invariant the index is always between 0 and {@link #NUMBER_PLAYERS}
     */
    public int current;

    // -- Constructors -----------------------------------------------

    /**
     * Creates a new Game object.
     * @requires s0 and s1 to be non-null
     * @param s0 the first player
     * @param s1 the second player
     */
    public GameModel(GameClientHandler s0, GameClientHandler s1) {
        players = new GameClientHandler[NUMBER_PLAYERS];
        players[0] = s0;
        players[1] = s1;
        current = 0;
        //timer = new Timer();
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

    public GameClientHandler getPlayer(int player){
        return players[player];
    }
    
    public void setBoard() {
    	boards[0] = players[0].getBoard();
    	boards[1] = players[1].getBoard();
    }

    public int getMaximumGameTime(){
        return MAXIMUM_GAME_TIME;
    }
}
