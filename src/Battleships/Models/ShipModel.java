package Battleships.Models;

import java.util.ArrayList;

public class ShipModel {
	//Variables
	private ShipType shipType;
	private ArrayList<PositionModel> positions = new ArrayList<>();
	private String shipName;



	//Getters
	public ArrayList<PositionModel> getPositions(){
		return this.positions;
	}
	public String getShipName(){
		return this.shipName;
	}
	public ShipType getShipType(){
		return this.shipType;
	}
	public int getLength() {
		return shipType.length;
	}
	public int getOrientation(){
		if(this.getLength() == 1) {
			return 1;
		}
		else {
			if (positions.get(0).getX() == positions.get(1).getX()) {
				return 1;
			}
			else {
				return 0;
			}
		}
	}
	public int getMarkerIndex()
	{
		PositionModel best = this.positions.get(0);
		if(getOrientation() == 0)
		{
			for(PositionModel pos : positions) {
				if(pos.getX() < best.getX())
					best =  pos;
			}
		}
		else
		{
			for(PositionModel pos : positions) {
				if(pos.getY() < best.getY())
					best =  pos;
			}
		}
		return best.index();
	}

	//Constructor
	public ShipModel(ShipType shipType, int shipNumber) {
		this.shipType = shipType;
		this.shipName = shipType.identifier + shipNumber;
	}

	//Methods
	/**
	 * @param pos positionModel that needs to be added to the ArrayList.
	 * Adds a PositionModel to the arrayList
	 * @ensures size of the arraylist has incremented by one
	 */
	public void addPosition(PositionModel pos){
		this.positions.add(pos);
	}
	/**
	 * @return true if all the positions of the shipModel has been guessed
	 */
	public boolean Sunk() {
		for(PositionModel pos : positions) {
			if(!pos.getHasBeenGuessed())
				return false;
		}
		return true;
	}
}
