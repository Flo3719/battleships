package Battleships.Tests;

import Battleships.Controllers.ClientController;
import Battleships.Controllers.JoinBoxViewController;
import Battleships.Controllers.MainViewController;
import Battleships.Controllers.Server;
import Battleships.Models.Board;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class JoinBoxViewControllerTest {
    JoinBoxViewController joinBoxViewController;
    MainViewController mainViewController;

    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @BeforeEach
    public void init(){
        System.setOut(new PrintStream(outputStreamCaptor));
        joinBoxViewController = (JoinBoxViewController) JoinBoxViewController.sharedInstance;
        mainViewController = (MainViewController) MainViewController.sharedInstance;
    }

    @Test
    public void testHandleHostClick(){
        Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
        assertNull(joinBoxViewController.getServer());
        joinBoxViewController.handleHostClick("player", "1234");
        assertNotNull(joinBoxViewController.getServer());
        assertTrue((threadSet.size()+1) == (Thread.getAllStackTraces().keySet().size()));
    }

    @Test
    public void testJoinHostClick(){
        Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
        ClientController client = new ClientController(new Board(), mainViewController);
        assertNull(client.getPlayer());
        joinBoxViewController.handleJoinClick("player","localhost", "1234", new Board(), client);
        assertNotNull(client.getPlayer());
        assertEquals(client.ip, "localhost");
        assertEquals(client.Port, 1234);
        assertTrue((threadSet.size()+1) == (Thread.getAllStackTraces().keySet().size()));
    }

    @Test
    public void testShowMessage(){
        joinBoxViewController.showMessage("Hello World");
        assertEquals("Hello World", outputStreamCaptor.toString()
                .trim());
    }
}
