package Battleships.Views;

import Battleships.Models.Board;
import Battleships.Models.PositionModel;

public class BoardView {
	public void printBoard(Board board) {
		for(int y = 0; y < Board.HEIGHT; y++) {
			for(int x = 0; x < Board.WIDTH; x++)  {
				PositionModel pos = board.positions[x][y];
				System.out.print("+");
				if(y > 0 && pos.ship != null && board.positions[x][y - 1].ship == pos.ship) {
					System.out.print(" ");
				}
				else {
					System.out.print("-");
				}
				
			}
			System.out.println("+");
			for(int x = 0; x < Board.WIDTH; x++)  {
				PositionModel pos = board.positions[x][y];
				if(x > 0 && pos.ship != null && board.positions[x - 1][y].ship == pos.ship) {
					System.out.print(" ");
				}
				else {
					System.out.print("|");
				}
				if(pos.ship != null) {
					switch (pos.ship.getShipType()) {
					case PATROL_BOAT:
						System.out.print("P");
						break;
					case SUPER_PATROL:
						System.out.print("S");
						break;
					case DESTROYER:
						System.out.print("D");
						break;
					case BATTLESHIP:
						System.out.print("B");
						break;
					case CARRIER:
						System.out.print("C");
						break;
					}
				}
				else {
					System.out.print(" ");
				}
			}
			System.out.println("|");
		}
		for(int x = 0; x < Board.WIDTH; x++)  {
			System.out.print("+");
			System.out.print("-");
		}
		System.out.println("+");
	}

}
