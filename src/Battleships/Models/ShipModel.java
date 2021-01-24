package Battleships.Models;

import java.util.ArrayList;

public class ShipModel {

	public ShipType shipType;
	public ArrayList<PositionModel> positions = new ArrayList<>();
	public String shipName;
	
	public ShipModel(ShipType shipType, int shipNumber) {
		this.shipType = shipType;
		this.shipName = shipType.identifier + shipNumber;
	}
	
	public int getLength() {
		return shipType.length;
	}
	
	public boolean Sunk() {
		for(PositionModel pos : positions) {
			if(!pos.hasBeenGuessed)
				return false;
		}
		return true;
	}
}
