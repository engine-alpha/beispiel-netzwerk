import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import ea.Client;
import ea.DiscoveryListener;
import ea.ServerDiscovery;

/**
 * @author Niklas Keller <me@kelunik.com>
 * @version 13. April 2014
 */
public class ChatClient {
	private Thread messageThread;
	
    public ChatClient() {
        ServerDiscovery.startDiscovery(new DiscoveryListener() {
            public void onServerGefunden(String ip) {
                final Client client = new Client(ip, 15135) {
                    public void empfangeString(String s) {
                        System.out.println("Nachricht: " + s);
                    }
                };
                
                client.sendeString("Verbunden mit " + ip + ":" + 15135);
                
                messageThread = new Thread() {
                	public void run() {
                		while(!isInterrupted()) {
                			try {
                				client.sendeString(readMessage());
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