/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatapp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author igor
 */
public class Client extends Thread {
    private int port;
    private String url;
    private WindowClient wClient;
    private Socket socket;
    private boolean isConnected;
    
    public Client(int port, String url, WindowClient wClient) {
        this.port = port;
        this.url = url;
        this.wClient = wClient;
    }
    
    @Override
    public void run(){
       try{
            this.socket = new Socket(this.url, this.port);
            JOptionPane.showMessageDialog(this.wClient, "Successfully connected ",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            DataInputStream dis = new DataInputStream(
                        this.socket.getInputStream());
            this.isConnected = this.socket.isConnected();
            while(this.isConnected){
                String message = dis.readUTF();
                this.wClient.onMessageReceived(message);
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this.wClient,
                    "Error: " + ex.getMessage(), "Could not connect to server",
                    JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(WindowClient.class.getName())
                    .log(Level.SEVERE, null, ex);
        } 
    }
    
    public void sendMessage(final String message){
        try {
            DataOutputStream dos = new DataOutputStream(
                    this.socket.getOutputStream());
            dos.writeUTF(message);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
