package Battleships;

import java.util.Timer;

public class Game {

	public static final int NUMBER_PLAYERS = 2;
    /**
     * The maximum game time in milliseconds. The maximum game time is 5 minutes.
     */
    private static final int MAXIMUM_GAME_TIME = 5*60*1000;
    private boolean timeOver = false;
    private Timer timer;
    
    /**
     * The players of the game.
     * @invariant the length of the array equals NUMBER_PLAYERS
     * @invariant all array items are never null
     */
    private Player[] players;

    /**
     * Index of the current player.
     * @invariant the index is always between 0 and {@link #NUMBER_PLAYERS}
     */
    private int current;

    // -- Constructors -----------------------------------------------

    /**
     * Creates a new Game object.
     * @requires s0 and s1 to be non-null
     * @param s0 the first player
     * @param s1 the second player
     */
    public Game(Player s0, Player s1) {
        players = new Player[NUMBER_PLAYERS];
        players[0] = s0;
        players[1] = s1;
        current = 0;
        timer = new Timer();
    }
}
