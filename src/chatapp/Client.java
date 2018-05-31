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
    private String nickName;
    private WindowClient wClient;
    private Socket socket;
    private boolean isConnected;
    
    public Client(final int port, final String url, final String nickname,
            WindowClient wClient) {
        this.port = port;
        this.url = url;
        this.nickName = nickname;
        this.wClient = wClient;
    }

    public int getPort() {
        return this.port;
    }

    public String getUrl() {
        return this.url;
    }

    public String getNickName() {
        return this.nickName;
    }
    
    @Override
    public void run(){
       try{
            this.socket = new Socket(this.url, this.port);
            JOptionPane.showMessageDialog(this.wClient, "Successfully connected",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            DataInputStream dis = new DataInputStream(
                        this.socket.getInputStream());
            this.isConnected = this.socket.isConnected();
            sendData(1, this.nickName);
            while(this.isConnected){
                int code = dis.readInt();
                String message = dis.readUTF();
                switch(code){
                    case 1:
                        this.wClient.addNewPerson(message);
                        break;
                    case 2:
                        this.wClient.onMessageReceived(message);
                        break;
                }
                
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this.wClient,
                    "Error: " + ex.getMessage(), "Could not connect to server",
                    JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(WindowClient.class.getName())
                    .log(Level.SEVERE, null, ex);
        } 
    }
    
    public void sendData(final int code, final String message){
        try {
            DataOutputStream dos = new DataOutputStream(
                    this.socket.getOutputStream());
            dos.writeInt(code);
            dos.writeUTF(message);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
