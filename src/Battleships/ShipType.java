package Battleships;

public enum ShipType {
	PATROL_BOAT("P", 1, "-fx-background-color: grey;"),
	SUPER_PATROL("S", 2, "-fx-background-color: green;"),
	DESTROYER("D", 3, "-fx-background-color: red;"),
	BATTLESHIP("B", 4, "-fx-background-color: black;"),
	CARRIER("C", 5, "-fx-background-color: purple;");
	
	public final String identifier;
	public final int length;
	public final String color;
	
	private ShipType(String identifier, int length, String color) {
		this.identifier = identifier;
		this.length = length;
		this.color = color;
	}
}
