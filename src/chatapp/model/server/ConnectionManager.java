package chatapp.model.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author igor
 */
public class ConnectionManager {
    private static ConnectionManager instance = null;
    private ArrayList<ConnectionThread> connections = new ArrayList<>();
    
    public static synchronized ConnectionManager getInstance(){
        if(instance == null){
            instance = new ConnectionManager();
        }
        return instance;
    }
    
    private ConnectionManager(){}
    
    public void sendData(final int code, final String message){
        this.connections.forEach((ConnectionThread connection) -> {
            try {
                connection.sendData(code, message);
            } catch (IOException ex) {
                Logger.getLogger(ConnectionManager.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
        });
        
    }
    
    public void newConnection(final ConnectionThread connection){
        this.connections.forEach(conneciton -> {
            try {
                connection.sendData(1, conneciton.getNickname());
            } catch (IOException ex) {
                Logger.getLogger(ConnectionManager.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
        });
        this.connections.add(connection);
    }
    
    public void disconnect(final ConnectionThread connection){
        int posToRemove = -1;
        for(int i = 0; i < this.connections.size(); ++i){
            if(this.connections.get(i).equals(connection)){
                posToRemove = i;
                break;
            }
        }
        
        if(posToRemove != -1){
            final int pos = posToRemove; 
            this.connections.stream().filter(con -> !con.equals(connection))
                    .forEach(con -> {
                try {
                    con.sendData(3, "" + pos);
                } catch (IOException ex) {
                    Logger.getLogger(ConnectionManager.class.getName())
                            .log(Level.SEVERE, null, ex);
                }
                    });
            this.connections.remove(pos);
        }
    }
}
