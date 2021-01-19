package Battleships;

import java.util.ArrayList;

public class ShipModel {

	public ShipType shipType;
	public ArrayList<PositionModel> positions = new ArrayList<>();
	public String shipName;
	
	public static int counterCBoat = 1;
	public static int counterBBoat = 1;
	public static int counterDBoat = 1;
	public static int counterSBoat = 1;
	public static int counterPBoat = 1;
	
	public ShipModel(ShipType shipType) {
		this.shipType = shipType;
		switch(this.shipType) {
		case CARRIER:
			counterCBoat++;
			this.shipName = shipType.identifier+counterCBoat;
			break;
		case BATTLESHIP:
			counterBBoat++;
			this.shipName = shipType.identifier+counterBBoat;
			break;
		case DESTROYER:
			counterDBoat++;
			this.shipName = shipType.identifier+counterDBoat;
			break;
		case SUPER_PATROL:
			counterSBoat++;
			this.shipName = shipType.identifier+counterSBoat;
			break;
		case PATROL_BOAT:
			counterPBoat++;
			this.shipName = shipType.identifier+counterPBoat;
			break;
		}
			
			
			
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
