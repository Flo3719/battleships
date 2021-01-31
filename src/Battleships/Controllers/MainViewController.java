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
		// TODO: MAYBE change this. the ships should MAYBE not be placed before joining/hosting.Maybe
		// separate setView method again?
		this.board.positionShips();
		this.addShips(this.board);
		// Regarding Requirement C02 (launch it on initialization)
		handleMenuJoinClick();
	}

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

	@Override
	public void handleStartClick(Event evt) throws IOException, ServerNotAvailableException {
		this.clientController.startGame();
	}

	public void handleMenuJoinClick() {
		this.joinBoxView = new JoinBoxView(this.clientController, this.view);
		joinBoxView.display(this.board);
	}

	public void addShips(Board board) {
		for (int y = 0; y < board.HEIGHT; y++) {
			for (int x = 0; x < board.WIDTH; x++) {
				PositionModel pos = board.getPositions()[x][y];
				if (pos.getShip() != null) {
					switch (pos.getShip().getShipType()) {
					case PATROL_BOAT:
						// TODO implement enum for indicators + add to the setPlayField funtion that it
						// changes the color according to indicator/as param
						// TODO add numbers to the indicator P1, C1, C2, etc
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
