package Battleships.Models;

import java.io.IOException;

import Battleships.Models.Exceptions.ServerNotAvailableException;
import Battleships.Views.MainView;
import javafx.event.Event;

public interface MainViewDelegate {
    public static MainViewDelegate sharedInstance = null;
    public MainView getView();
    public void initialize(MainView view);
    public void handleButtonClick(Event evt);
    public void handleMenuJoinClick();
    //public void handleJoinClick(Event evt);
    //public void setPlayField(int index);
    public void addShips(Board board);
    public void handleStartClick(Event evt) throws IOException, ServerNotAvailableException;
}
