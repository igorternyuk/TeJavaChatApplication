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
    private String nickname = "";

    public Connection(Socket socket) {
        try {
            this.socket = socket;
            this.dis = new DataInputStream(socket.getInputStream());
            this.dos = new DataOutputStream(socket.getOutputStream());
            //start();
        } catch (IOException ex) {
            Logger.getLogger(Connection.class.getName()).log(Level.SEVERE,
                    null, ex);
        }
    }

    public String getNickname() {
        return nickname;
    }
    
    @Override
    public void run(){
        while(true){
            try {
                int code = dis.readInt();
                String message = dis.readUTF();
                switch(code){
                    case 1:
                        this.nickname = message;
                        break;
                    case 2:
                        message = String.format("<%s>: %s", nickname, message);
                        break;
                    default:
                        break;
                        
                }
                ConnectionManager.getInstance().sendData(code, message);
            } catch (IOException ex) {
                Logger.getLogger(Connection.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void sendData(final int code, final String message){
        try {
            this.dos.writeInt(code);
            this.dos.writeUTF(message);
        } catch (IOException ex) {
            Logger.getLogger(Connection.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
    }
}
