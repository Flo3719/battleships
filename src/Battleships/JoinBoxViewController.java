package Battleships;

import javafx.event.Event;

public class JoinBoxViewController implements JoinBoxViewDelegate{
    protected JoinBoxView view;

    //Singleton
    public static JoinBoxViewDelegate sharedInstance = new JoinBoxViewController();
    private JoinBoxViewController(){}

    @Override
    public JoinBoxView getView() {
        return this.view;
    }

    @Override
    public void setView(JoinBoxView view) {
        this.view = view;
    }

    @Override
    public void handleHostClick(String name,String port) {
        //TODO Implement hosting of a server
        System.out.println("Hosting on port " + port + " as " + name);
    }

    @Override
    public void handleJoinClick(String name,String ip,String port) {
        //TODO Implement joining of a server
        System.out.println("Joining on address " + ip + ":" + port + " as " + name);
    }
}
