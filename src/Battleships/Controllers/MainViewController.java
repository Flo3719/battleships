package Battleships.Controllers;

import Battleships.Models.MainViewDelegate;

import java.io.IOException;

import Battleships.Models.Board;
import Battleships.Models.PositionModel;
import Battleships.Models.ProtocolMessages;
import Battleships.Models.Exceptions.OutOfTurnException;
import Battleships.Models.Exceptions.ServerNotAvailableException;
import Battleships.Views.JoinBoxView;
import Battleships.Views.MainView;
import javafx.event.Event;
import javafx.scene.control.Control;

public class MainViewController implements MainViewDelegate {
	//Variables
	protected MainView view;
	private Board board;
	private ClientController clientController;
	private JoinBoxView joinBoxView;

	//Singleton
	public static MainViewDelegate sharedInstance = new MainViewController();

	//Constructor
	private MainViewController() {
		this.board = new Board();
		this.clientController = new ClientController(board, this);
	}

/*	 To Manipulate the game implement "MainViewController.sharedInstance" for
	 example by creating an instance variable
	 of type "MainViewDelegate" called "controller" and assign it to it. (For
	 example like in "MainView.java")
	 To make manipulations that directly involve the view, use the .view attribute
	 of the sharedInstance.*/

	//Getters
	@Override
	public MainView getView() {
		return this.view;
	}
	public JoinBoxView getjoinBoxView() {
		return this.joinBoxView;
	}
	private int getIndex(int col, int row) {
		return row * 15 + col;
	}
	public Board getBoard(){
		return this.board;
	}

	//Methods
	@Override
	public void initialize(MainView view) {
		this.view = view;
		this.board.positionShips();
		this.addShips(this.board);
		// Regarding Requirement C02 (launch it on initialization)
		handleMenuJoinClick();
	}

	/**
	 * If it is the turn of the client that clicked, checks the index of the button
	 * calls the Attack method of the clientController.
	 * @ensures only client whose turn it is can attack.
	 */
	@Override
	public void handleButtonClick(Event evt) throws IOException {
		if (this.clientController.getMyTurn()) {
			String id = ((Control) evt.getSource()).getId();
			System.out.println(id);
			String button[];
			button = id.split("PlayButton");
			int index = Integer.parseInt(button[1]);
			if (button[0].equals("enemy")) {
				System.out.println("enemy field attacked");
				clientController.Attack(button[1]);
			} else {
				clientController.sendErrorMessage(ProtocolMessages.ERRORNAMES[5]);
			}
		} else {
			System.out.println(ProtocolMessages.OUT_OF_TURN);
		}
	}

	/**
	 * calls the start game method as soon as the button is clicked
	 */
	@Override
	public void handleStartClick(Event evt) throws IOException, ServerNotAvailableException {
		this.clientController.startGame();
	}

	/**
	 * Displays the joinBox when the join button has been clicked
	 */
	public void handleMenuJoinClick() {
		this.joinBoxView = new JoinBoxView(this.clientController, this.view);
		joinBoxView.display(this.board);
	}

	/**
	 * Goes through all coordinates of the board, updates the UI if the position is occupied by a ship.
	 */
	public void addShips(Board board) {
		for (int y = 0; y < board.HEIGHT; y++) {
			for (int x = 0; x < board.WIDTH; x++) {
				PositionModel pos = board.getPositions()[x][y];
				if (pos.getShip() != null) {
					switch (pos.getShip().getShipType()) {
					case PATROL_BOAT:
						this.view.setPlayField(pos.getShip().getShipName(), "friend", getIndex(x, y), pos.getShip().getShipType().color);
						break;
					case SUPER_PATROL:
						this.view.setPlayField(pos.getShip().getShipName(), "friend", getIndex(x, y), pos.getShip().getShipType().color);
						break;
					case DESTROYER:
						this.view.setPlayField(pos.getShip().getShipName(), "friend", getIndex(x, y), pos.getShip().getShipType().color);
						break;
					case BATTLESHIP:
						this.view.setPlayField(pos.getShip().getShipName(), "friend", getIndex(x, y), pos.getShip().getShipType().color);
						break;
					case CARRIER:
						this.view.setPlayField(pos.getShip().getShipName(), "friend", getIndex(x, y), pos.getShip().getShipType().color);
						break;
					}
				}
			}
		}
	}

	@Override
	public void handleGiveUpClick() {
		clientController.closeConnection();
		System.exit(0);
	}
}
