package Battleships;

public class PositionModel {
	public int x;
	public int y;
	public ShipModel ship;
	public boolean hasBeenGuessed;
	public String shipName;
	
	public PositionModel(int x, int y) {
		this.x = x;
		this.y = y;
		this.hasBeenGuessed = false;
		this.ship = null;
	}
}
