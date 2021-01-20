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
		for(int i = 0; i<2; i++) {
			ships.add(new ShipModel(ShipType.CARRIER));
		}
		for(int i = 0; i<3; i++) {
			ships.add(new ShipModel(ShipType.BATTLESHIP));
		}
		for(int i = 0; i<5; i++) {
			ships.add(new ShipModel(ShipType.DESTROYER));
		}
		for(int i = 0; i<8; i++) {
			ships.add(new ShipModel(ShipType.SUPER_PATROL));
		}
		for(int i = 0; i<10; i++) {
			ships.add(new ShipModel(ShipType.PATROL_BOAT));
		}
	}
	public void positionShips() {
		for(ShipModel ship: ships) {
			boolean positioned = false;
			while(!positioned) {
				int randomX = ThreadLocalRandom.current().nextInt(0, WIDTH);
				int randomY = ThreadLocalRandom.current().nextInt(0, HEIGHT);
				int randomOrientation = ThreadLocalRandom.current().nextInt(1, 5);
				switch (randomOrientation) {
				case 1:
					if(tryEast(ship, randomX, randomY)){
						placeEast(ship, randomX, randomY);
						positioned = true;
				}
					break;
				case 2:
					if(trySouth(ship, randomX, randomY)) {
						placeSouth(ship, randomX, randomY);
						positioned = true;
				}
					break;
				case 3:
					if(tryWest(ship, randomX, randomY)) {
						placeWest(ship, randomX, randomY);
						positioned = true;
				}
					break;
				case 4:
					if(tryNorth(ship, randomX, randomY)) {
						placeNorth(ship, randomX, randomY);
						positioned = true;
				}
					break;
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
	public int getY(int index) {
		int y = index / 10;
		return y;
	}
	
	public int getX(int index) {
		int rest = index % 10;
		return rest;
	}
	public String toString() {
		String result = "";
		for(int i = 0; i < HEIGHT; i++) {
			for(int j = 0; j < WIDTH; j++) {
				if(positions[j][i].ship != null) {
					result = result + positions[j][i].ship.shipName;
				}
				else {
					result = result + "X";
				}
				result = result + ";";
			}
			
		}
		return result;
	}
}
