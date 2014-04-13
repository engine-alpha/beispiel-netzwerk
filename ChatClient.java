import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

import ea.*;

/**
 * @author Niklas Keller <me@kelunik.com>
 * @version 13. April 2014
 */
public class ChatClient {
	private Thread messageThread;
	
    public ChatClient(final String name) {    	
        ServerDiscovery.startDiscovery(new ConnectListener() {
            public void onConnect(String ip) {
            	final Client client = new Client(ip, 15135) {
                    public void empfangeString(String message) {
                    	System.out.printf("[%s] %s\n", new Date().toString(), message);
                    }
                };
                
                System.out.printf("[%s] %s: %s\n", new Date().toString(), "Bot", "Server verbunden: " + ip);
                
                messageThread = new Thread() {
                	public void run() {
                		while(!isInterrupted()) {
                			try {
                				client.sendeString(name + ": " + readMessage());
                			} catch(Exception e) {
                				e.printStackTrace();
                			}
                		}
                	}
                };
                
                messageThread.start();
            }
        });
    }
    
    public void kill() {
    	if(messageThread == null) {
    		return;
    	}
    	
    	messageThread.interrupt();
    	
    	try {
			messageThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
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
