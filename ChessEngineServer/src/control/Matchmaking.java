/**
 * @author Simon Greiderer
 * @author Marc Underrain
 * 
 * Vom ClientHandler aus verbindet man sich zu diesem 
 * Unterserver, welche ein Match zwischen 2 Spielern sucht
 *
 */

package control;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;


public class Matchmaking extends Thread {

	private static HashMap<String, Socket> gameMaking1 = new HashMap<String, Socket>();
	private final static String PREFIX = "MATCHMAKER: ";
	private static HashMap<String, ClientHandler> handlers = new HashMap<String, ClientHandler>(); 
	
	public static void addToGameMaking1(ClientHandler handler, String username, Socket socket) {
		gameMaking1.put(username, socket);
		handlers.put(username, handler); 
		AnsiCode.sendSuccess(PREFIX, "Client hinzugefügt"); 
	}

	public String getRandomID(HashMap<String, Socket> map) {
		Random random = new Random();
		List<String> keys = new ArrayList<String>(map.keySet());
		String randomKey = keys.get(random.nextInt(keys.size()));
		return randomKey; 
	}

	public void setThread(HashMap<String, Socket> map) {
		String username1 = getRandomID(map); 
		String username2 = getRandomID(map); 
		while(username1.equals(username2)) {
			username2 = getRandomID(map); 
		}
		handlers.get(username1).setInMatch(true);
		handlers.get(username2).setInMatch(true);
		MatchmakingHandler handler = new MatchmakingHandler(map.get(username1), map.get(username2), username1, username2); 
		handler.start();
		removeFormHashMap(gameMaking1, username1);
		removeFormHashMap(gameMaking1, username2);
	}
	
	private void removeFormHashMap(HashMap<String, Socket> map, String username) {
		map.remove(username); 
	}
	
	public void run() {
		AnsiCode.sendMessage(PREFIX, "Suche nach Spielern...");
		while(true) {
			try {
				sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(gameMaking1.size() >= 2) {
				AnsiCode.sendMessage(PREFIX, "Es wird nun ein Spiel erstellt: ");
				setThread(gameMaking1);
			}
		}
	}
	
	public static HashMap<String, ClientHandler> getHandlers() {
		return handlers;
	}
}
