package Battleships.Tests;

import Battleships.Controllers.*;
import Battleships.Models.Board;
import Battleships.Models.Exceptions.ServerNotAvailableException;
import Battleships.Models.JoinBoxViewDelegate;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringReader;
import java.net.Socket;

import Battleships.Models.ProtocolMessages;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ClientControllerTest {
    private ClientController clientController;
    private Board board;
    private MainViewController mainViewController;

    @BeforeEach
    public void init(){
        mainViewController = (MainViewController) MainViewController.sharedInstance;
        clientController = new ClientController(mainViewController);
    }
    @Test
    public void testStartGame(){
        BufferedReader out = new BufferedReader(new StringReader(ProtocolMessages.START));
        try {
            clientController.startGame();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ServerNotAvailableException e) {
            e.printStackTrace();
        }
        assertEquals(out, ProtocolMessages.START);

    }

    @Test
    public void testCreateConnection(){
        clientController.createConnection("client 1", "12.34.56.78", 1234);
    }
}
