/**
 * @author Simon Greiderer
 * @author Marc Underrain
 * 
 * Aehnlich wie ClientHandler, man wird 
 * von dem Matchmaking aus verbunden.
 */
package control;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import mysql.MySQLConnect;

public class MatchmakingHandler extends Thread {

	private Socket socket1;
	private Socket socket2;

	private String username1; 
	private String username2; 
	
	private DataInputStream inputStream1;
	private DataOutputStream outputStream1;

	private DataInputStream inputStream2;
	private DataOutputStream outputStream2;

	private final String PREFIX = "MATCHMATCHING HANDLER: ";

	private boolean socket2Sendet = false; 

	
	public MatchmakingHandler(Socket socket1, Socket socket2, String username1, String username2) {
		
		this.username1 = username1; 
		this.username2 = username2; 
		AnsiCode.sendSuccess(PREFIX, "Es wurde ein Match zwischen " + this.username1 + " und " + this.username2 + " gefunden!");
		this.socket1 = socket1;
		this.socket2 = socket2;
		try {
			this.inputStream1 = new DataInputStream(this.socket1.getInputStream());
			this.outputStream1 = new DataOutputStream(this.socket1.getOutputStream());

			this.inputStream2 = new DataInputStream(this.socket2.getInputStream());
			this.outputStream2 = new DataOutputStream(this.socket2.getOutputStream());

			outputStream1.writeUTF("true" + "-" + username2); 
			outputStream2.writeUTF("false" + "-" + username1);
		} catch (IOException e) {
			AnsiCode.sendError(PREFIX, "Streams konnten nicht erstellt werden!");
			e.printStackTrace();
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		String text = "";
		AnsiCode.sendSuccess(PREFIX, "Thread laeuft nun!");
		while (true) {
			if(!socket2Sendet) {
				text = waitForMessage1();
			} else {
				text = waitForMessage2();
			}
			if (text == null) {
				stop();
				break;
			} else {
				AnsiCode.sendSuccess(PREFIX, "Received Message: " + text);
				try {
					checkPrefix(text);
					socket2Sendet = !socket2Sendet; 
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private String waitForMessage1() {
		String message = null;
		try {
			AnsiCode.sendMessage(PREFIX, "Wartet auf Message von Client[1]...");
			message = inputStream1.readUTF();
		} catch (IOException e) {
			if (socket1.isConnected()) {
				AnsiCode.sendError(PREFIX, "Connection Client1 to Server lost!");
			} else if(socket2.isConnected()) {
				AnsiCode.sendError(PREFIX, "Connection Client2 to Server lost!");
			} else {
				AnsiCode.sendError(PREFIX, "Input Stream1 funktioniert nicht!");
			}
			MySQLConnect.updateLoggedIn(username1, 0);
			MySQLConnect.updateLoggedIn(username2, 0);

			try {
				sleep(250);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
		return message;
	}
	
	
	private String waitForMessage2() {
		String message = null;
		try {
			AnsiCode.sendMessage(PREFIX, "Wartet auf Message von Client[2]...");
			message = inputStream2.readUTF();
		} catch (IOException e) {
				endGame();	
			if (socket2.isConnected()) {
				AnsiCode.sendError(PREFIX, "Connection Client2 to Server lost!");
				MySQLConnect.updateLoggedIn(username2, 0);
			} else if(socket1.isConnected()) {
				AnsiCode.sendError(PREFIX, "Connection Client1 to Server lost!");
				MySQLConnect.updateLoggedIn(username1, 0);
			} else {
				AnsiCode.sendError(PREFIX, "Input Stream2 funktioniert nicht!");
			}
			
			try {
				sleep(250);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
		return message;
	}


	private void checkPrefix(String message) throws IOException {
		if (message.startsWith("§m")) {
			message = message.substring(2);
			if(!socket2Sendet) {
				AnsiCode.sendSuccess(PREFIX, message);
				outputStream2.writeUTF(message);
			} else {
				AnsiCode.sendSuccess(PREFIX, message);
				outputStream1.writeUTF(message);
			}
		} else if (message.startsWith("§w")) {
			message = message.substring(2);
			AnsiCode.sendSuccess(PREFIX, "Das Spiel ist vorbei!");
			MySQLConnect.updateUserPoints(message, 10); 
			MySQLConnect.updateUserPoints(message.equals(username1) ? username2 : username1, -10);
			endGame();
		} else if(message.startsWith("§u")) {
			message = message.substring(2);
			AnsiCode.sendSuccess(PREFIX, "Das Spiel ist vorbei!");
			MySQLConnect.updateUserPoints(message, 0); 
			endGame(); 
		} else if(message.startsWith("§d")) {
			message = message.substring(2);
			if(!socket2Sendet) {
				AnsiCode.sendError(PREFIX, "Spieler " + username1 +" hat einen Disconnect!");
				outputStream2.writeUTF("§e");
			} else {
				AnsiCode.sendError(PREFIX, "Spieler " + username2 +" hat einen Disconnect!");
				outputStream1.writeUTF("§e");
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	public void endGame() {
		AnsiCode.sendMessage(PREFIX, "Match wird geschlossen...");
		Matchmaking.getHandlers().remove(username1); 
		Matchmaking.getHandlers().remove(username2);
		
		Thread thread1 = new ClientHandler(socket1, username1);
		thread1.start();
		
		Thread thread2 = new ClientHandler(socket2, username2);
		thread2.start();
		
		this.stop();
	}
}
