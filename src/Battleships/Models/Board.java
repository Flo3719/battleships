package Battleships.Models;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Board {
	//Finals
	public static final int WIDTH = 15;
	public static final int HEIGHT = 10;
	//Variables
	public ArrayList<ShipModel> ships = new ArrayList<>();
	public PositionModel[][] positions = new PositionModel[WIDTH][HEIGHT];
	//Constructor
	public Board() {
		for(int x = 0; x < WIDTH; x++) {
			for(int y = 0; y < HEIGHT; y++) {
				positions[x][y] = new PositionModel(x, y);
			}
		}
		addShips();
	}
	//Getters
	public int getY(int index) {
		int y = index / 15;
		return y;
	}

	public int getX(int index) {
		int rest = index % 15;
		return rest;
	}
	//Methods
	public void addShips() {
		for(int i = 0; i<2; i++) {
			ships.add(new ShipModel(ShipType.CARRIER, i));
		}
		for(int i = 0; i<3; i++) {
			ships.add(new ShipModel(ShipType.BATTLESHIP, i));
		}
		for(int i = 0; i<5; i++) {
			ships.add(new ShipModel(ShipType.DESTROYER, i));
		}
		for(int i = 0; i<8; i++) {
			ships.add(new ShipModel(ShipType.SUPER_PATROL, i));
		}
		for(int i = 0; i<10; i++) {
			ships.add(new ShipModel(ShipType.PATROL_BOAT, i));
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
					if(tryNorth(ship, randomX, randomY)) {
						placeNorth(ship, randomX, randomY);
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
					if(trySouth(ship, randomX, randomY)) {
						placeSouth(ship, randomX, randomY);
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
			ship.addPosition(pos);
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
			ship.addPosition(pos);
			pos.ship = ship;
		}
	}
	
	private boolean trySouth(ShipModel ship, int randomX, int randomY) {
		for(int i = 0; i < ship.getLength(); i++) {
			if(!isFreeField(randomX, randomY + i)) {
				return false;
			}
		}
		return true;
	}
	
	public void placeSouth(ShipModel ship, int randomX, int randomY) {
		for(int i = 0; i < ship.getLength(); i++) {
			PositionModel pos = positions[randomX][randomY + i];
			ship.addPosition(pos);
			pos.ship = ship;
		}
	}
	
	private boolean tryNorth(ShipModel ship, int randomX, int randomY) {
		for(int i = 0; i < ship.getLength(); i++) {
			if(!isFreeField(randomX, randomY - i)) {
				return false;
			}
		}
		return true;
	}
	
	public void placeNorth(ShipModel ship, int randomX, int randomY) {
		for(int i = 0; i < ship.getLength(); i++) {
			PositionModel pos = positions[randomX][randomY - i];
			ship.addPosition(pos);
			pos.ship = ship;
		}
	}
	public String toString() {
		String result = "";
		for(int i = 0; i < HEIGHT; i++) {
			for(int j = 0; j < WIDTH; j++) {
				if(positions[j][i].ship != null) {
					result = result + positions[j][i].ship.getShipName();
				}
				else {
					result = result + "0";
				}
				result = result + ",";
			}
			
		}
		return result;
	}
}
