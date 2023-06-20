/**
 * @author Simon Greiderer
 * @author Marc Underrain
 * 
 * Server Klasse mit Main
 */
package control;

import java.io.File;
import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import mysql.MySQLConnect;

public class Server {
	private static ServerSocket serverSocket;
	private int port;
	private Socket socket = null;
	private static final String PREFIX = "SERVER: ";
	private final MySQLConnect mySQL;
	
	private static ArrayList<String> playerlist = new ArrayList<String>();  

	private static File file = new File("./ServerSettings");
	
	public static void main(String[] args) {
		new Server();
	}

	public Server() {
		port = Integer.valueOf(FileSave.readUsersettings(file, "port"));
		connect();
		mySQL = new MySQLConnect();
		mySQL.connect();
		new ServerControl().start();
		new Matchmaking().start();
		while (true) {
			if (hasClientConnected()) {
				clientHandler();
			}
		}
	}

	public void connect() {
		try {
			serverSocket = new ServerSocket(port); 
			AnsiCode.sendSuccess(PREFIX, "Server[" + serverSocket.getInetAddress().getHostAddress() + "] auf Port "
					+ port + " wurde erfolgreich gestartet!");
			Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
				@Override
				public void run() {
					disconnect();
				}
			}));
		} catch (BindException e) {
			AnsiCode.sendError(PREFIX,
					"PORT bereits belegt, bitte ändern sie die ServerSettings Datei, falls sie den Port ändern möchten (auch der Client muss geupdated werden!)!");
			return;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean hasClientConnected() {
		try {
			socket = serverSocket.accept();
			AnsiCode.sendSuccess(PREFIX, "Client " + socket.getInetAddress() + " wurde erfolgreich connected!");
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public void clientHandler() {
		Thread thread = new ClientHandler(this.socket);
		thread.start();
	}

	public static void disconnect() {
		try {
			serverSocket.close();
			AnsiCode.sendSuccess(PREFIX, "Server wurde erfolgreich geschlossen!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static ArrayList<String> getPlayerlist() {
		return playerlist;
	}
	
	public static ServerSocket getServerSocket() {
		return serverSocket;
	}
}
