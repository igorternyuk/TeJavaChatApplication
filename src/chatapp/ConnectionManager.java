/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatapp;

import java.util.ArrayList;

/**
 *
 * @author igor
 */
public class ConnectionManager {
    private static ConnectionManager instance = null;
    private ArrayList<Connection> connections = new ArrayList<>();
    
    public static synchronized ConnectionManager getInstance(){
        if(instance == null){
            instance = new ConnectionManager();
        }
        return instance;
    }
    
    private ConnectionManager(){
        
    }
    
    public void sendMessage(final String message){
        this.connections.forEach((con) -> {
            con.sendMessage(message);
        });
    }
    
    public void newConnection(Connection connection){
        this.connections.add(connection);
    }
}
