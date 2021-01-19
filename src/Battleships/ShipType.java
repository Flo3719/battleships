package Battleships;

public enum ShipType {
	PATROL_BOAT("P", 1),
	SUPER_PATROL("S", 2),
	DESTROYER("D", 3),
	BATTLESHIP("B", 4),
	CARRIER("C", 5);
	
	public final String identifier;
	public final int length;
	
	private ShipType(String identifier, int length) {
		this.identifier = identifier;
		this.length = length;
	}
}
