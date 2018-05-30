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

/**
 *
 * @author igor
 */
public class Connection extends Thread {
    private Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;

    public Connection(Socket socket) {
        try {
            this.socket = socket;
            this.dis = new DataInputStream(socket.getInputStream());
            this.dos = new DataOutputStream(socket.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(Connection.class.getName()).log(Level.SEVERE,
                    null, ex);
        }
    }
    
    @Override
    public void run(){
        while(true){
            try {
                String message = dis.readUTF();
                ConnectionManager.getInstance().sendMessage(message);
            } catch (IOException ex) {
                Logger.getLogger(Connection.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void sendMessage(final String message){
        try {
            this.dos.writeUTF(message);
        } catch (IOException ex) {
            Logger.getLogger(Connection.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
    }
}
