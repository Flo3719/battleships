package Battleships;

import javafx.event.Event;

public class JoinBoxViewController implements JoinBoxViewDelegate{
    protected JoinBoxView view;
    private Server server;
    final private int defaultPort = 4242;

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
    public void handleHostClick(String name,String portInput) {
        int port;
        if(portInput.equals("")){
            // Requirement S01
            port = defaultPort;
            view.controller.showMessage("Default Port is being used.");
        }else{
            port = Integer.parseInt(portInput);
        }
        server = new Server(this);
        server.setup(port);
        System.out.println("Hosting on port " + port + " as " + name);
    }

    @Override
    public void handleJoinClick(String name,String ip,String port) {
        //TODO Implement joining of a server
        System.out.println("Joining on address " + ip + ":" + port + " as " + name);
    }

    @Override
    public void showMessage(String message) {
        System.out.println(message);
    }
}
