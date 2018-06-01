package chatapp.model.client;

import chatapp.gui.WindowClient;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author igor
 */
public class Client {
    private int port;
    private String url;
    private String nickName;
    private Consumer<String> onNewPersonAdded;
    private Consumer<String> onMessageReceived;
    private Consumer<Integer> onPersonDisconnected;
    private Socket socket;
    private ClientThread clientThread = new ClientThread();
    
    public Client(final int port, final String url, final String nickname,
                  Consumer<String> onNewPersonAdded,
                  Consumer<String> onMessageReceived,
                  Consumer<Integer> onPersonDisconnected) {
        this.port = port;
        this.url = url;
        this.nickName = nickname;
        this.onNewPersonAdded = onNewPersonAdded;
        this.onMessageReceived = onMessageReceived;
        this.onPersonDisconnected = onPersonDisconnected;
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
    
    public void connect(){
        clientThread.start();
    }
    
    public void disconnect(){
        clientThread.interrupt();
    }
    
    private class ClientThread extends Thread {
        @Override
        public void run(){
           try{
                socket = new Socket(url, port);
                DataInputStream dis = new DataInputStream(
                            socket.getInputStream());
                sendData(1, nickName);
                while(true){
                    int code = dis.readInt();
                    String message = dis.readUTF();
                    switch(code){
                        case 1:
                            onNewPersonAdded.accept(message);
                            break;
                        case 2:
                            onMessageReceived.accept(message);
                            break;
                        case 3:
                            try {
                                int pos = Integer.parseInt(message);
                                onPersonDisconnected.accept(pos);
                            }
                            catch(NumberFormatException ex){
                                Logger.getLogger(Client.class.getName())
                                      .log(Level.SEVERE, null, ex);
                            }
                    }

                }
            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName())
                        .log(Level.SEVERE, null, ex);
            } 
        }    
    }
    
    
    public void sendData(final int code, final String message) throws IOException{
        if(this.socket != null){
            try{
                DataOutputStream dos
                        = new DataOutputStream(this.socket.getOutputStream());
                dos.writeInt(code);
                dos.writeUTF(message);
            }
            catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE,
                        null, ex);
                throw ex;
            }
        }
    }
}
