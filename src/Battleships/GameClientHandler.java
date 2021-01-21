package Battleships;

import java.io.*;
import java.net.Socket;

public class GameClientHandler implements Runnable {
    /** The socket and In- and OutputStreams */
    private BufferedReader in;
    private BufferedWriter out;
    private Socket sock;

    /** Server */
    private Server server;

    /** Name of this Player */
    private String name = "unnamed";

    @Override
    public void run() {
        String msg;
        try {
            msg = in.readLine();
            while (msg != null) {
                //System.out.println("> [" + player + "] sends: " + msg);
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

    public void sendNameRequest() throws IOException {
        //TODO check protocol
        out.write(ProtocolMessages.GETNAME);
    }

    public String getName(){
        return this.name;
    }

    public void sendOut(String msg) throws IOException {
        out.write(msg);
    }

    public GameClientHandler(Socket sock, Server server) {
        try {
            in = new BufferedReader(
                    new InputStreamReader(sock.getInputStream()));
            out = new BufferedWriter(
                    new OutputStreamWriter(sock.getOutputStream()));
            this.sock = sock;
            this.server = server;
        } catch (IOException e) {
            //shutdown();
        }
    }
    private void handleCommand(String msg) throws IOException {
        String[] message = msg.split(ProtocolMessages.DELIMITER);
        switch(message[0].charAt(0)){
            case ProtocolMessages.HELLO:
                out.write(server.getHello(message[1]));
                break;
            case ProtocolMessages.NAME:
                this.name = message[1];
                break;
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
