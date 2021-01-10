package Battleships;

import java.util.ArrayList;

public class ShipModel {
	
	enum ShipType{
		PATROL_BOAT,
		SUPER_PATROL,
		DESTROYER,
		BATTLESHIP,
		CARRIER;
	}
	public static int[] shipLength = {1, 2, 3, 4, 5};
	public ShipType shipType;
	public ArrayList<PositionModel> positions = new ArrayList<>();
	
	public int getLength() {
		return shipLength[shipType.ordinal()];
	}
	
	public ShipModel(ShipType shipType) {
		this.shipType = shipType;
	}
	public boolean Sunk() {
		for(PositionModel pos : positions) {
			if(!pos.hasBeenGuessed)
				return false;
		}
		return true;
		
	}
}
