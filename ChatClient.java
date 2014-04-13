import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

import ea.*;

/**
 * @author Niklas Keller <me@kelunik.com>
 * @version 13. April 2014
 */
public class ChatClient implements ServerGefundenReagierbar {
    private String name;
    
    public ChatClient(final String name) {      
        this.name = name;
        ServerSuche.start(this);
    }

    public void serverGefunden(String ip) {
        final Client client = new Client(ip, 15135) {
            public void empfangeString(String message) {
                System.out.println("[" + new Date().toString() + "] " + message);
            }
        };
        
        System.out.println("[" + new Date().toString() + "] " + "Bot: Server verbunden @ " + ip);

        while(true) {
            try {
                client.sendeString(name + ": " + readMessage());
            } catch(Exception e) {
                e.printStackTrace();
            }
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
