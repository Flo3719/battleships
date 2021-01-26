package Battleships.Tests;
import java.net.Socket;

import org.junit.jupiter.api.*;

import Battleships.Controllers.ClientController;
import Battleships.Controllers.GameClientHandler;
import Battleships.Controllers.JoinBoxViewController;
import Battleships.Controllers.MainViewController;
import Battleships.Controllers.Server;
import Battleships.Models.Board;
import Battleships.Views.JoinBoxView;
import Battleships.Views.MainView;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GameClientHandlerTest {


	GameClientHandler gch;
	@BeforeEach
	public void init() {
		JoinBoxViewController joinBoxViewController = (JoinBoxViewController) JoinBoxViewController.sharedInstance;
		MainViewController mainViewController = (MainViewController) MainViewController.sharedInstance;
		Board board = new Board();
		MainView mainView = new MainView();
		ClientController clientController = new ClientController(board, mainViewController);
		JoinBoxView joinBoxView = new JoinBoxView(clientController, mainView);
		Socket socket = new Socket();
		Server server = new Server(joinBoxViewController);
		gch = new GameClientHandler(socket, server, "localhost");
	}
	
	@Test
	public void testToBoard() {
		String testBoard = "0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,";
		Board board = gch.toBoard(testBoard);
		assertEquals(testBoard, board.toString());
	}


}
