package Battleships.Tests;

import Battleships.Controllers.JoinBoxViewController;
import Battleships.Server.GameClientHandler;
import Battleships.Server.GameController;
import Battleships.Server.Server;
import org.junit.jupiter.api.Test;

import java.net.Socket;

import static org.junit.jupiter.api.Assertions.assertNull;

public class GameControllerTest {
    @Test
    public void testStartGame(){
        JoinBoxViewController view = (JoinBoxViewController) JoinBoxViewController.getSharedInstance();
        Server server = new Server(view);
        GameClientHandler s0 = new GameClientHandler(new Socket(), server, "s0");
        GameClientHandler s1 = new GameClientHandler(new Socket(), server, "s1");
        GameController gameController = new GameController(server, s0, s1);
        assertNull(gameController.getTimer());
    }
}
