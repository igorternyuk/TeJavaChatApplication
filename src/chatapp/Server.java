/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatapp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author igor
 */
public class Server extends Thread{
    private int port;
    private JFrame frame;
    
    public Server(final int port, final JFrame frame){
        this.port = port;
        this.frame = frame;
    }
    
    @Override
    public void run(){
        ServerSocket serverSocket = null;
        try{
            serverSocket = new ServerSocket(this.port);
            Socket socket = serverSocket.accept();
            JOptionPane.showMessageDialog(this.frame, "Connection successfull",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            
        }
        catch(IOException ex){
            JOptionPane.showMessageDialog(this.frame, "Could not start server: "
                                          + ex.getMessage(), "Failure",
                                          JOptionPane.ERROR_MESSAGE);
        }
        
        try {
            if(serverSocket != null){
                serverSocket.close();
                System.out.println("Server socket closing");
            }
        } catch (IOException ex) {
            Logger.getLogger(WindowServer.class.getName())
                    .log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this.frame, ex.getMessage(), "Error",
                                          JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void onMessageReceived(final String message){
        System.out.println(message);
    }
}
