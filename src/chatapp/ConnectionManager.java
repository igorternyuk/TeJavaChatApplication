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
    
    private ConnectionManager(){}
    
    public void sendData(final int code, final String message){
        this.connections.forEach((Connection con) -> {
            con.sendData(code, message);
        });
    }
    
    public void newConnection(final Connection connection){
        this.connections.forEach(c -> {
            connection.sendData(1, c.getNickname());
        });
        this.connections.add(connection);
    }
}
