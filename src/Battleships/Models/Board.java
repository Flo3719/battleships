package Battleships.Models;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Board {
	//Finals
	public static final int WIDTH = 15;
	public static final int HEIGHT = 10;
	//Variables
	private ArrayList<ShipModel> ships = new ArrayList<>();
	private PositionModel[][] positions = new PositionModel[WIDTH][HEIGHT];
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
	public ArrayList<ShipModel> getShips(){
		return this.ships;
	}
	public PositionModel[][] getPositions(){
		return this.positions;
	}
	//Methods
	/**
	 * Adds all the ships to the arrayList
	 * @ensures ships.size() == 28
	 */
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
	/**
	 * Chooses for each ship a random position on the board and a random orientation.
	 * @ensures all ships have a position
	 */
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
	/**
	 * @param x x-coordinate
	 * @param y y-coordinate
	 * @return true if it is a position on the board without a ship on it.
	 */
	public boolean isFreeField(int x, int y) {
		if(x < WIDTH && x >= 0 && y < HEIGHT && y >= 0 && positions[x][y].getShip() == null) {
			return true;
			}
		return false;
	}
	
	/**
	 * @param ship model of the ship that needs to be placed
	 * @param randomX random x-coordinate
	 * @param randomY random y-coordinate
	 * @return true if the ship can be placed from point (x, y) eastwards on.
	 */
	private boolean tryEast(ShipModel ship, int randomX, int randomY) {
		for(int i = 0; i < ship.getLength(); i++) {
			if(!isFreeField(randomX + i, randomY)) {
				return false;
			}
		}
		return true;
	}
	/**
	 * places the ship, starting at point (x, y), pointing east.
	 * assigns the positions to that ship, and the ship to those positions
	 * @param ship model of the ship that needs to be placed
	 * @param randomX random x-coordinate
	 * @param randomY random y-coordinate
	 */
	public void placeEast(ShipModel ship, int randomX, int randomY) {
		for(int i = 0; i < ship.getLength(); i++) {
			PositionModel pos = positions[randomX + i][randomY];
			ship.addPosition(pos);
			pos.setShip(ship);
		}
	}
	
	/**
	 * @param ship model of the ship that needs to be placed
	 * @param randomX random x-coordinate
	 * @param randomY random y-coordinate
	 * @return true if the ship can be placed from point (x, y) westwards on.
	 */
	private boolean tryWest(ShipModel ship, int randomX, int randomY) {
		for(int i = 0; i < ship.getLength(); i++) {
			if(!isFreeField(randomX - i, randomY)) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * places the ship, starting at point (x, y), pointing west
	 * assigns the positions to that ship, and the ship to those positions
	 * @param ship model of the ship that needs to be placed
	 * @param randomX random x-coordinate
	 * @param randomY random y-coordinate
	 */
	public void placeWest(ShipModel ship, int randomX, int randomY) {
		for(int i = 0; i < ship.getLength(); i++) {
			PositionModel pos = positions[randomX - i][randomY];
			ship.addPosition(pos);
			pos.setShip(ship);
		}
	}
	/**
	 * @param ship model of the ship that needs to be placed
	 * @param randomX random x-coordinate
	 * @param randomY random y-coordinate
	 * @return true if the ship can be placed from point (x, y) southwards on.
	 */
	private boolean trySouth(ShipModel ship, int randomX, int randomY) {
		for(int i = 0; i < ship.getLength(); i++) {
			if(!isFreeField(randomX, randomY + i)) {
				return false;
			}
		}
		return true;
	}
	/**
	 * places the ship, starting at point (x, y), pointing South
	 * assigns the positions to that ship, and the ship to those positions
	 * @param ship model of the ship that needs to be placed
	 * @param randomX random x-coordinate
	 * @param randomY random y-coordinate
	 */
	public void placeSouth(ShipModel ship, int randomX, int randomY) {
		for(int i = 0; i < ship.getLength(); i++) {
			PositionModel pos = positions[randomX][randomY + i];
			ship.addPosition(pos);
			pos.setShip(ship);
		}
	}
	/**
	 * @param ship model of the ship that needs to be placed
	 * @param randomX random x-coordinate
	 * @param randomY random y-coordinate
	 * @return true if the ship can be placed from point (x, y) northwards on.
	 */
	private boolean tryNorth(ShipModel ship, int randomX, int randomY) {
		for(int i = 0; i < ship.getLength(); i++) {
			if(!isFreeField(randomX, randomY - i)) {
				return false;
			}
		}
		return true;
	}
	/**
	 * places the ship, starting at point (x, y), pointing north
	 * assigns the positions to that ship, and the ship to those positions
	 * @param ship model of the ship that needs to be placed
	 * @param randomX random x-coordinate
	 * @param randomY random y-coordinate
	 * 
	 */
	public void placeNorth(ShipModel ship, int randomX, int randomY) {
		for(int i = 0; i < ship.getLength(); i++) {
			PositionModel pos = positions[randomX][randomY - i];
			ship.addPosition(pos);
			pos.setShip(ship);
		}
	}
	/**
	 * Creates a string out of a board. Adds a 0 to the String if the position is empty,
	 * else the name of the ship is added.
	 * 
	 */
	public String toString() {
		String result = "";
		for(int i = 0; i < HEIGHT; i++) {
			for(int j = 0; j < WIDTH; j++) {
				if(positions[j][i].getShip() != null) {
					result = result + positions[j][i].getShip().getShipName();
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
