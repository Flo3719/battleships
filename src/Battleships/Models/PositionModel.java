package Battleships.Models;

import Battleships.Models.ShipModel;

public class PositionModel {
	//TODO make private; create getters and setters as needed
	public int x;
	public int y;
	public ShipModel ship;
	public boolean hasBeenGuessed;
	
	public PositionModel(int x, int y) {
		this.x = x;
		this.y = y;
		this.hasBeenGuessed = false;
		this.ship = null;
	}

	public int index()
	{
		return y * 15 + x;
	}
	
}
