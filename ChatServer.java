import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

import ea.*;

/**
 * @author Niklas Keller <me@kelunik.com>
 * @version 13. April 2014
 */
public class ChatServer implements VerbindungHergestelltReagierbar {
	private final Server server;
    
    public ChatServer(final String name) {
    	this.server = new Server(15135) {
        	public void empfangeString(String message) {
        		System.out.println("[" + new Date().toString() + "] " + message);
        	}
        };
        
        this.server.setBroadcast(true);
        this.server.setVerbindungHergestelltReagierbar(this);
        this.server.netzwerkSichtbarkeit(true);
        
        while(true) {
			try {
				server.sendeString(name + ": " + readMessage());
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
    }
    
    public void verbindungHergestellt(String ip) {
        System.out.println("[" + new Date().toString() + "] Bot: Client verbunden mit IP " + ip);
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