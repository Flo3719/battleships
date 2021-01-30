package Battleships.Models;

public enum ShipType {
	//Variables
	PATROL_BOAT(ProtocolMessages.PATROL, 1, "-fx-background-color: grey;"),
	SUPER_PATROL(ProtocolMessages.SUPERPATROL, 2, "-fx-background-color: green;"),
	DESTROYER(ProtocolMessages.DESTROYER, 3, "-fx-background-color: red;"),
	BATTLESHIP(ProtocolMessages.BATTLESHIP, 4, "-fx-background-color: black;"),
	CARRIER(ProtocolMessages.CARRIER, 5, "-fx-background-color: purple;");
	
	public final String identifier;
	public final int length;
	public final String color;

	//Constructor
	private ShipType(String identifier, int length, String color) {
		this.identifier = identifier;
		this.length = length;
		this.color = color;
	}
	
	
}
