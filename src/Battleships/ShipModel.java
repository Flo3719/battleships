package Battleships;

import java.util.ArrayList;

public class ShipModel {

	public ShipType shipType;
	public ArrayList<PositionModel> positions = new ArrayList<>();
	
	public int getLength() {
		return shipType.length;
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
