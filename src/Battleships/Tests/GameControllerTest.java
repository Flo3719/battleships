package Battleships.Tests;

import Battleships.Controllers.GameClientHandler;
import Battleships.Controllers.GameController;
import Battleships.Controllers.JoinBoxViewController;
import Battleships.Controllers.Server;
import Battleships.Models.JoinBoxViewDelegate;

import java.net.Socket;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameControllerTest {
    Socket sock1 = new Socket();
    Socket sock2 = new Socket();
    public JoinBoxViewDelegate controller;
    JoinBoxViewController view = (JoinBoxViewController)(JoinBoxViewController.sharedInstance);
    Server server = new Server(view);

    GameController gc;

    GameClientHandler s0 = new GameClientHandler(sock1, server, "client 1");
    GameClientHandler s1 = new GameClientHandler(sock2, server, "client 2");

    @BeforeEach
    public void init(){
        gc = new GameController(s0, s1);
    }

    @Test
    public void testConstructor(){
        assertEquals(gc.getModel().getPlayer(0), s0);
        assertEquals(gc.getModel().getPlayer(1), s1);
    }
}
