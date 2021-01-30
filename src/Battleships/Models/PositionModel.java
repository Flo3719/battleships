package Battleships.Models;

import Battleships.Models.ShipModel;

public class PositionModel {
	//Variables
	//TODO make private; create getters and setters as needed
	public int x;
	public int y;
	public ShipModel ship;
	public boolean hasBeenGuessed;

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
