package Battleships.Tests;
import java.io.IOException;
import java.net.Socket;

import Battleships.Controllers.*;
import Battleships.Models.ProtocolMessages;
import org.junit.jupiter.api.*;

import Battleships.Models.Board;
import Battleships.Views.JoinBoxView;
import Battleships.Views.MainView;

import static org.junit.jupiter.api.Assertions.*;

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
		gch.setGame(new GameController(server, new GameClientHandler(new Socket(), server , "s0"), new GameClientHandler(new Socket(), server , "s1")));
		gch.getGame().startGame();
	}
	
	@Test
	public void testToBoard() {
		String testBoard = "0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,";
		Board board = gch.toBoard(testBoard);
		assertEquals(testBoard, board.toString());
	}

	@Test void testHandleCommand(){
		assertFalse(gch.getBoard() != null);
		String testBoard = "0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,";
		try {
			gch.handleCommand(ProtocolMessages.BOARD + testBoard);
			assertTrue(gch.getBoard().toString() == testBoard);
		} catch (IOException e) {
			fail();
			e.printStackTrace();
		}
	}
}
