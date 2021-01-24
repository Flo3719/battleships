package Battleships.Controllers;

import Battleships.Models.Board;
import Battleships.Views.BoardView;

public class BoardController {
	private Board model;
	private BoardView view;
	
	public BoardController(Board model, BoardView view) {
		this.model = model;
		this.view = view;
	}
	
	public void updateView() {
		view.printBoard(model);
	}
	public void initCompBoard() {
		model.positionShips();
	}
}
