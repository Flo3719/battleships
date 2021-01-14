package Battleships;

import javafx.event.Event;

public interface MainViewDelegate {
    public static MainViewDelegate sharedInstance = null;
    public MainView getView();
    public void setView(MainView view);
    public void handleButtonClick(Event evt);
    public void handleJoinClick(Event evt);
    //public void setPlayField(int index);
}
