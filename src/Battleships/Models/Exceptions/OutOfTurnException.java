package Battleships.Models.Exceptions;

public class OutOfTurnException extends Exception {
	public OutOfTurnException() {
		super("It is not your turn...");
	}
}
