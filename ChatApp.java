
public class ChatApp {
	public static void main(String[] args) {
        if(args.length < 2) {
        	throw new IllegalArgumentException("Falscher Befehl. Nutze einen der folgenden Befehle:\n - java -jar netzwerk.jar server \"Dein Name\"\n - java -jar netzwerk.jar client \"Dein Name\"");
        }
        
        if(args[0].equals("client")) {
        	new ChatClient(args[1]);
        }
        
        else if(args[0].equals("server")) {
        	new ChatServer(args[1]);
        }
        
        else {
        	throw new IllegalArgumentException("Das erste Argument muss 'client' oder 'server' sein!");
        }
    }
}