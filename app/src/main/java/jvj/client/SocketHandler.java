package jvj.client;

import java.net.Socket;
/**
 * Created by Jay Jay on 12/26/2015.
 * Something
 */
public class SocketHandler {
    private static Socket socket;
    public static synchronized Socket getSocket(){
        return socket;
    }
    public static synchronized void setSocket(Socket socket){
        SocketHandler.socket = socket;
    }
}
