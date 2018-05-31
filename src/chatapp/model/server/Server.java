package chatapp.model.server;

import chatapp.gui.WindowServer;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author igor
 */
public class Server {
    private int port;
    private ServerThread serverThread = new ServerThread();
    
    public Server(final int port){
        this.port = port;
    }
    
    public void startServer(){
        this.serverThread.start();
    }
    
    public void stopServer(){
        this.serverThread.interrupt();
    }
    
    private class ServerThread extends Thread{
        @Override
            public void run(){
            try(ServerSocket serverSocket = new ServerSocket(port)) {
                while(true){
                    Socket socket = serverSocket.accept();
                    ConnectionThread connection = new ConnectionThread(socket);
                    ConnectionManager.getInstance().newConnection(connection);
                    connection.start();
                }
            }
            catch(IOException ex){
                Logger.getLogger(WindowServer.class.getName())
                        .log(Level.SEVERE, null, ex);
            }    
        }
    }
}
