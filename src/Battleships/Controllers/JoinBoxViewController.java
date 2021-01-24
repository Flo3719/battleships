package Battleships.Controllers;

import Battleships.Models.Board;
import Battleships.Models.JoinBoxViewDelegate;
import Battleships.Models.Exceptions.ServerNotAvailableException;
import Battleships.Views.JoinBoxView;

import java.io.IOException;
import java.net.ProtocolException;

public class JoinBoxViewController implements JoinBoxViewDelegate {
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
        System.out.println("SERVER: Hosting on port " + port + " as " + name);
        Thread serverThread = new Thread(server);
        serverThread.start();
    }
    @Override
    public void handleJoinClick(String name,String ip,String port, Board board, ClientController client){
        int portHost;
        if(port.equals("")){
            // Requirement S01
            portHost = defaultPort;
            view.controller.showMessage("SERVER: Default Port is being used.");
        }else{
            portHost = Integer.parseInt(port);
        }
        //ClientController client = new ClientController(board);
        try{
            client.start(name, ip, portHost);
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (ServerNotAvailableException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showMessage(String message) {
        System.out.println(message);
    }
}
