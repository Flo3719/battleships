package Battleships.Models;

import Battleships.Models.Board;

public class PlayerModel {
	// -- Instance variables -----------------------------------------

    private String name;
    private Board board;
    private int score;

    // -- Constructors -----------------------------------------------

    /**
     * Creates a new Player object.
     * @param name the name of this player
     * @requires name is not null
     * @ensures the Name of this player will be name
     * @ensures the Board of this player will be randomly filled using {@link Board#generateBoats()}
     * @throws PlayerGenerationException if a problem occurs generating the
     * data for this user, mainly the board.
     */
    public PlayerModel(String name) //throws PlayerGenerationException
    {
        this.name = name;
        this.board = new Board();
        /*try {
			this.board.generateBoats();
		} catch (BoatGenerationException e) {
			throw new PlayerGenerationException(name);
		}*/
    }

    // -- Queries ----------------------------------------------------

    /**
     * Returns the name of the player.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the board of the player.
     */
    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board){
        this.board = board;
    }

    /**
     * Returns the score of the player.
     */
    public int getScore() {
    	return score;
    }
}
