import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

import ea.*;

/**
 * @author Niklas Keller <me@kelunik.com>
 * @version 13. April 2014
 */
public class ChatServer {
	private final Server server;
	private final Thread messageThread;
    
    public ChatServer(final String name) {
    	this.server = new Server(15135) {
        	public void empfangeString(String message) {
        		System.out.printf("[%s] %s\n", new Date().toString(), message);
        	}
        };
        
        this.server.setBroadcast(true);
        
        this.server.setOnConnectListener(new ConnectListener() {
        	public void onConnect(String ip) {
                System.out.printf("[%s] %s: %s\n", new Date().toString(), "Bot", "Client verbunden: " + ip);
            }
        });
        
        this.messageThread = new Thread() {
        	public void run() {
        		while(!isInterrupted()) {
        			try {
        				server.sendeString(name + ": " + readMessage());
        			} catch(Exception e) {
        				e.printStackTrace();
        			}
        		}
        	}
        };
        
        this.messageThread.start();
        this.setNetworkVisible(true);
    }
    
    public void setNetworkVisible(boolean visible) {
    	server.netzwerkSichtbarkeit(visible);
    }
    
    public void kill() {
    	setNetworkVisible(false);
    	
    	messageThread.interrupt();
    	
    	try {
			messageThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    	
    	server.beendeVerbindung();
    }
    
    // http://stackoverflow.com/questions/4203646/system-console-returns-null
    public String readMessage() throws IOException {
    	if (System.console() != null) {
            return System.console().readLine();
        }
    	
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        return reader.readLine();
    }
}