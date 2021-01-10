package Battleships;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Board {

	public static final int WIDTH = 15;
	public static final int HEIGHT = 10;
	
	public ArrayList<ShipModel> ships = new ArrayList<>();
	public PositionModel[][] positions = new PositionModel[WIDTH][HEIGHT];
	
	public Board() {
		for(int x = 0; x < WIDTH; x++) {
			for(int y = 0; y < HEIGHT; y++) {
				positions[x][y] = new PositionModel(x, y);
			}
		}
		addShips();
	}
	public void addShips() {
		for(int i = 0; i<10; i++) {
			ships.add(new ShipModel(ShipModel.ShipType.PATROL_BOAT));
		}
		for(int i = 0; i<8; i++) {
			ships.add(new ShipModel(ShipModel.ShipType.SUPER_PATROL));
		}
		for(int i = 0; i<5; i++) {
			ships.add(new ShipModel(ShipModel.ShipType.DESTROYER));
		}
		for(int i = 0; i<3; i++) {
			ships.add(new ShipModel(ShipModel.ShipType.BATTLESHIP));
		}
		for(int i = 0; i<2; i++) {
			ships.add(new ShipModel(ShipModel.ShipType.CARRIER));
		}
	}
	public void positionShips() {
		for(ShipModel ship: ships) {
			boolean positioned = false;
			while(!positioned) {
				int randomX = ThreadLocalRandom.current().nextInt(0, WIDTH);
				int randomY = ThreadLocalRandom.current().nextInt(0, HEIGHT);
				if(tryEast(ship, randomX, randomY)){
					placeEast(ship, randomX, randomY);
					positioned = true;
				}
				else if(trySouth(ship, randomX, randomY)) {
					placeSouth(ship, randomX, randomY);
					positioned = true;
				}
				else if(tryWest(ship, randomX, randomY)) {
					placeWest(ship, randomX, randomY);
					positioned = true;
				}
				else if(tryNorth(ship, randomX, randomY)) {
					placeNorth(ship, randomX, randomY);
					positioned = true;
				}
			}
		}
	}
	public boolean isFreeField(int x, int y) {
		if(x < WIDTH && x >= 0 && y < HEIGHT && y >= 0 && positions[x][y].ship == null) {
			return true;
			}
		return false;
	}
	
	private boolean tryEast(ShipModel ship, int randomX, int randomY) {
		//Try East
		for(int i = 0; i < ship.getLength(); i++) {
			if(!isFreeField(randomX + i, randomY)) {
				return false;
			}
		}
		return true;
	}
	
	public void placeEast(ShipModel ship, int randomX, int randomY) {
		for(int i = 0; i < ship.getLength(); i++) {
			PositionModel pos = positions[randomX + i][randomY];
			ship.positions.add(pos);
			pos.ship = ship;
		}
	}
	
	
	private boolean tryWest(ShipModel ship, int randomX, int randomY) {
		for(int i = 0; i < ship.getLength(); i++) {
			if(!isFreeField(randomX - i, randomY)) {
				return false;
			}
		}
		return true;
	}
	
	public void placeWest(ShipModel ship, int randomX, int randomY) {
		for(int i = 0; i < ship.getLength(); i++) {
			PositionModel pos = positions[randomX - i][randomY];
			ship.positions.add(pos);
			pos.ship = ship;
		}
	}
	
	private boolean tryNorth(ShipModel ship, int randomX, int randomY) {
		for(int i = 0; i < ship.getLength(); i++) {
			if(!isFreeField(randomX, randomY + i)) {
				return false;
			}
		}
		return true;
	}
	
	public void placeNorth(ShipModel ship, int randomX, int randomY) {
		for(int i = 0; i < ship.getLength(); i++) {
			PositionModel pos = positions[randomX][randomY + i];
			ship.positions.add(pos);
			pos.ship = ship;
		}
	}
	
	private boolean trySouth(ShipModel ship, int randomX, int randomY) {
		for(int i = 0; i < ship.getLength(); i++) {
			if(!isFreeField(randomX, randomY - i)) {
				return false;
			}
		}
		return true;
	}
	
	public void placeSouth(ShipModel ship, int randomX, int randomY) {
		for(int i = 0; i < ship.getLength(); i++) {
			PositionModel pos = positions[randomX][randomY - i];
			ship.positions.add(pos);
			pos.ship = ship;
		}
	}
}
