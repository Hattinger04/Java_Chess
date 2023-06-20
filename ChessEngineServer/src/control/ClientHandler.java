/**
 * @author Simon Greiderer
 * @author Marc Underrain
 * 
 * Erstellt für jeden Client der sich verbindet, einen Socket 
 * auf der ServerSeite und wartet auf Anfragen jedes Client 
 * (Anz. Clients = Anz. Sockets => jeder Socket ein eigener Thread)
 */
package control;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

import mysql.MySQLConnect;

public class ClientHandler extends Thread {

	private Socket socket;
	private boolean isInMatch;
	private DataInputStream inputStream;
	private DataOutputStream outputStream;
	private final String PREFIX = "CLIENTHANDLER: ";

	private String email;
	private String username;
	private String password;
	private String queue;

	public ClientHandler(Socket socket) {
		AnsiCode.sendMessage(PREFIX, "ClientHandler wird eingerichtet...");
		this.socket = socket;
		try {
			this.inputStream = new DataInputStream(this.socket.getInputStream());
			this.outputStream = new DataOutputStream(this.socket.getOutputStream());
		} catch (IOException e) {
			AnsiCode.sendError(PREFIX, "Streams konnten nicht erstellt werden!");
			e.printStackTrace();
		}
	}
	
	public ClientHandler(Socket socket, String username) {
		this.username = username; 
		AnsiCode.sendMessage(PREFIX, "ClientHandler wird erneut eingerichtet...");
		this.socket = socket;
		try {
			this.inputStream = new DataInputStream(this.socket.getInputStream());
			this.outputStream = new DataOutputStream(this.socket.getOutputStream());
		} catch (IOException e) {
			AnsiCode.sendError(PREFIX, "Streams konnten nicht erstellt werden!");
			e.printStackTrace();
		}
	}

	public boolean isInMatch() {
		return isInMatch;
	}
	
	public void setInMatch(boolean isInMatch) {
		this.isInMatch = isInMatch;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		String text = "";
		while (true) {
			if (!isInMatch()) {
				text = waitForMessage();
				if (text == null) {
					stop();
					break;
				} else {
					AnsiCode.sendSuccess(PREFIX, "Received Message: " + text);
					try {
						checkPrefix(text);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	private String waitForMessage() {
		String message = null;
		try {
			message = inputStream.readUTF();
			System.out.println(message);
		} catch (SocketException e) {
			// Do nothing
		} catch (IOException e) {
			MySQLConnect.updateLoggedIn(username, 0);
			if (!socket.isConnected()) {
				AnsiCode.sendError(PREFIX, "Connection Client to Server lost!");
			}
			try {
				sleep(250);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		} 
		return message;
	}

	@SuppressWarnings("deprecation")
	private void checkPrefix(String message) throws IOException {
		if (message.startsWith("§l")) {
			message = message.substring(2);
			String[] teile = message.split("-");
			username = teile[0];
			password = teile[1];
			Server.getPlayerlist().add(username); 
			if (MySQLConnect.login(username, password)) {
				outputStream.writeBoolean(true);
			} else {
				outputStream.writeBoolean(false);
			}
		} else if (message.startsWith("§r")) {
			message = message.substring(2);
			String[] teile = message.split("-");
			username = teile[0];
			email = teile[1];
			password = teile[2];
			if (MySQLConnect.register(email, username, password)) {
				outputStream.writeBoolean(true);
			} else {
				outputStream.writeBoolean(false);
			}
		} else if (message.startsWith("§q")) {
			message = message.substring(2);
			String[] teile = message.split("-");
			username = teile[0];
			queue = teile[1];
			switch (queue) {
			case "1":
				Matchmaking.addToGameMaking1(this, username, socket);
				this.stop();
				break;
			default:
				break;
			}
		} else if (message.startsWith("§o")) {
			message = message.substring(2);
			MySQLConnect.updateLoggedIn(message, 0); 
			Server.getPlayerlist().remove(username); 
			AnsiCode.sendSuccess(PREFIX, "Spieler " + message + " hat sich erfolgreich ausgeloggt!");
		} else if(message.startsWith("§p")) {
			message = message.substring(2);
			String response = MySQLConnect.selectUserPoints(message) + "-" + MySQLConnect.selectUserGames(message) + "-"+ MySQLConnect.selectUserRank(message); 
			outputStream.writeUTF(response);
		}
	}
}
