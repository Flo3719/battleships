package Battleships.Tests;

import Battleships.Controllers.GameClientHandler;
import Battleships.Controllers.MainViewController;
import Battleships.Models.ShipModel;
import Battleships.Views.MainView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MainViewControllerTest {
    MainViewController mainViewController;

    @BeforeEach
    public void init(){
        mainViewController = (MainViewController) MainViewController.sharedInstance;
    }

    @Test
    public void testInitialize(){
        MainView view = new MainView();
        String testBoard = "0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,";
        assertEquals(testBoard, mainViewController.getBoard());
        mainViewController.initialize(view);
        assertNotNull(mainViewController.getBoard());
    }
}
