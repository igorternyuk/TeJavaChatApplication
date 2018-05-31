package chatapp.model.server;

import java.util.ArrayList;

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
            connection.sendData(code, message);
        });
    }
    
    public void newConnection(final ConnectionThread connection){
        this.connections.forEach(conneciton -> {
            connection.sendData(1, conneciton.getNickname());
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
                        con.sendData(3, "" + pos);
                    });
            this.connections.remove(pos);
        }
    }
}
