package Battleships.Controllers;

import Battleships.Models.Board;

public class ComputerPlayerClient{
	public Board board;
	public GameController game;
	

	public ComputerPlayerClient(Board board, MainViewController mainViewController) {
		this.board = board;
	}

}
