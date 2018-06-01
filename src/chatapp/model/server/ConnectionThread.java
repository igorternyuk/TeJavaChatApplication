package chatapp.model.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author igor
 */
public class ConnectionThread extends Thread {
    private Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;
    private String nickname = "";

    public ConnectionThread(Socket socket) throws IOException {
        try {
            this.socket = socket;
            this.dis = new DataInputStream(socket.getInputStream());
            this.dos = new DataOutputStream(socket.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(ConnectionThread.class.getName()).log(Level.SEVERE,
                    null, ex);
            throw ex;
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
                        ConnectionManager.getInstance().sendData(code, message);
                        break;
                    case 2:
                        message = String.format("<%s>: %s", nickname, message);
                        ConnectionManager.getInstance().sendData(code, message);
                        break;
                    case 3:
                        ConnectionManager.getInstance().disconnect(this);
                    default:
                        break;
                        
                }
                
            } catch (IOException ex) {
                Logger.getLogger(ConnectionThread.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void sendData(final int code, final String message) throws IOException{
        try {
            this.dos.writeInt(code);
            this.dos.writeUTF(message);
        } catch (IOException ex) {
            Logger.getLogger(ConnectionThread.class.getName())
                    .log(Level.SEVERE, null, ex);
            throw ex;
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.socket);
        hash = 97 * hash + Objects.hashCode(this.dis);
        hash = 97 * hash + Objects.hashCode(this.dos);
        hash = 97 * hash + Objects.hashCode(this.nickname);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        
        final ConnectionThread other = (ConnectionThread) obj;
        return Objects.equals(this.nickname, other.nickname)
               || Objects.equals(this.socket, other.socket)
               || Objects.equals(this.dis, other.dis)
               || Objects.equals(this.dos, other.dos);
    }
}
