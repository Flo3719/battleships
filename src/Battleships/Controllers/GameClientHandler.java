 package Battleships.Controllers;

import Battleships.Models.Board;
import Battleships.Models.PositionModel;
import Battleships.Models.ProtocolMessages;
import Battleships.Models.ShipModel;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class GameClientHandler implements Runnable {
    /** The socket and In- and OutputStreams */
    private BufferedReader in;
    private BufferedWriter out;
    private Socket sock;

    /** Server */
    private Server server;

    /** Name of this Player */
    private String name = "unnamed";

    private Board board;
    
    @Override
    public void run() {
        String msg;
        try {
            msg = in.readLine();
            while (msg != null) {
                handleCommand(msg);
                out.newLine();
                out.flush();
                msg = in.readLine();
            }
            //shutdown();
        } catch (IOException e) {
            //shutdown();
        }
    }

    public String getName(){
        return this.name;
    }

    public GameClientHandler(Socket sock, Server server, String name) {
        try {
            in = new BufferedReader(
                    new InputStreamReader(sock.getInputStream()));
            out = new BufferedWriter(
                    new OutputStreamWriter(sock.getOutputStream()));
            this.sock = sock;
            this.server = server;
            this.name = name;
        } catch (IOException e) {
            //shutdown();
        }
    }

    public void sendOut(String message) throws IOException {
        out.write(message);
        out.newLine();
        out.flush();
    }
    public void toBoard(String stringBoard) {
    	Board resultBoard = new Board();
    	resultBoard.addShips();
    	String[] splitArray = stringBoard.split(",");
    	List<String> al = new ArrayList<>();
    	al = Arrays.asList(splitArray);
    	Iterator<String> it = al.iterator();
    	for (int i = 0; i < Board.HEIGHT; i++) {
    		for (int j = 0; j < Board.WIDTH; j++) {
    			String test = it.next();
    			if (!test.equals("0")) {
    				for (ShipModel s : resultBoard.ships) {
    					if (s.shipName.equals(test)) {
    						resultBoard.positions[j][i].ship = s;
    						PositionModel pos = resultBoard.positions[j][i];
    						s.positions.add(pos);
    					}
    				}
    			}
    			
    		}
    	}
    	this.board = resultBoard;
    }


    private void handleCommand(String msg) throws IOException {
        String[] message = msg.split(ProtocolMessages.CS);
        switch(message[0].charAt(0)){
            //case ProtocolMessages.HELLO:
            //    out.write(server.getHello(message[1]));
            //    break;
//            case ProtocolMessages.ACT:
//                out.write(server.doAct(message[1], message[2]));
//                break;
//            case ProtocolMessages.BILL:
//                out.write(server.doBill(message[1], message[2]));
//                break;
//            case ProtocolMessages.IN:
//                out.write(server.doIn(message[1]));
//                break;
//            case ProtocolMessages.OUT:
//                out.write(server.doOut(message[1]));
//                break;
//            case ProtocolMessages.PRINT:
//                out.write(server.doPrint());
//                break;
//            case ProtocolMessages.ROOM:
//                out.write(server.doRoom(message[1]));
//                break;
//            //case ProtocolMessages.HELP:
//            //server.
//            //break;
//            case ProtocolMessages.EXIT:
//                //server.
//                break;
//            default:
//                //server.
        }
    }
//    private void shutdown() {
//        System.out.println("> [" + name + "] Shutting down.");
//        try {
//            in.close();
//            out.close();
//            sock.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        server.removeClient(this);
//    }
}
