package Battleships.Models;

import Battleships.Models.ShipModel;

public class PositionModel {
	//Variables
	//TODO make private; create getters and setters as needed
	private int x;
	private int y;
	private  ShipModel ship;
	private boolean hasBeenGuessed;

	//Getters
	public int getX() {
		return this.x;
	}
	public int getY() {
		return this.y;
	}
	public ShipModel getShip() {
		return this.ship;
	}
	public void setShip(ShipModel shipmodel) {
		this.ship = shipmodel;
	}
	public boolean getHasBeenGuessed() {
		return this.hasBeenGuessed;
	}
	public void setHasBeenGuessed(Boolean guessed) {
		this.hasBeenGuessed = guessed;
	}
	//Constructor
	public PositionModel(int x, int y) {
		this.x = x;
		this.y = y;
		this.hasBeenGuessed = false;
		this.ship = null;
	}
	//Methods
	public int index()
	{
		return y * 15 + x;
	}
	
}
