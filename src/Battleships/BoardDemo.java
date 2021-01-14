package Battleships;

public class BoardDemo {

	public static void main(String[] args) {
		Board board = new Board();
		BoardView boardView = new BoardView();
		
		BoardController controller = new BoardController(board, boardView);


		
		controller.initCompBoard();
		controller.updateView();

	}
}
