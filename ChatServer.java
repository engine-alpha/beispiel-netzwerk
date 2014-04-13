import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import ea.NetzwerkVerbindung;
import ea.Server;

/**
 * @author Niklas Keller <me@kelunik.com>
 * @version 13. April 2014
 */
public class ChatServer {
	private final Server server;
	private final Thread thread;
	private final Thread messageThread;
    
    public ChatServer() {
        server = new Server(15135) {
        	public void empfangeString(String s) {
        		System.out.println("Nachricht: " + s);
        	}
        };
        
        thread = new Thread() {
            public void run() {
                while(!isInterrupted()) {
                    NetzwerkVerbindung verbindung = server.naechsteVerbindungAusgeben();
                    System.out.println("Neuer Client: " + verbindung.getName() + " @ " + verbindung.getRemoteIP());
                }
            }
        };
        
        messageThread = new Thread() {
        	public void run() {
        		while(!isInterrupted()) {
        			try {
        				server.sendeString(readMessage());
        			} catch(Exception e) {
        				e.printStackTrace();
        			}
        		}
        	}
        };
        
        thread.start();
        messageThread.start();
        
        setNetworkVisible(true);
    }
    
    public void setNetworkVisible(boolean visible) {
    	server.netzwerkSichtbarkeit(visible);
    }
    
    public void kill() {
    	thread.interrupt();
    	messageThread.interrupt();
    	
    	try {
			thread.join();
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