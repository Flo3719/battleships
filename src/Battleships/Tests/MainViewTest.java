package Battleships.Tests;

import Battleships.Controllers.JoinBoxViewController;
import Battleships.Controllers.MainViewController;
import Battleships.Views.MainView;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MainViewTest {
    @Test
    public void testAddPlayButtons(){
        JoinBoxViewController joinBoxViewController = (JoinBoxViewController) JoinBoxViewController.getSharedInstance();
        MainViewController mainViewController = (MainViewController) MainViewController.sharedInstance;
        mainViewController.initialize(new MainView());
        MainView mainView = mainViewController.getView();
        mainView.addPlayButtons(mainView.friendGrid, "friend");
        assertEquals(3, mainView.friendGrid.getChildren().size());
    }
}
