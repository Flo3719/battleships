package Battleships.Models;

import Battleships.Views.JoinBoxView;

public interface JoinBoxViewDelegate {
    public static JoinBoxViewDelegate sharedInstance = null;
    public JoinBoxView getView();
    public void setView(JoinBoxView view);
    public void handleHostClick(String name,String port);
    public void handleJoinClick(String name,String ip,String port, Board board);
    public void showMessage(String message);
}
