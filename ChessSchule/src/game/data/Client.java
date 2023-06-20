/**
 * @author Simon Greiderer
 * @author Marc Underrain
 * 
 * Client Klasse, welche eine Connection zu dem Server
 * aufbaut
 */
package game.data;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client {

	private static boolean loggedIn = false;
	private static final String PREFIX = "CLIENT: ";
	private static DataOutputStream outputStream;
	private static DataInputStream inputStream;
	private static Socket socket = null;

	private static boolean starten = false;
	private static String username;
	private static String gegnername;

	private static String ip;
	private static int port;

	public Client() {
		connect();
	}

	public void connect() {
		try {
			socket = new Socket(ip, port);
			System.out.println(PREFIX + "Erfolgreich mit dem Server verbunden!");
			try {
				inputStream = new DataInputStream(socket.getInputStream());
				outputStream = new DataOutputStream(socket.getOutputStream());
			} catch (IOException e) {
				if (socket.isConnected()) {
					socket.close();
				}
			}
		} catch (Exception e) {
			// Do nothing
		}
	}

	public static String getUsername() {
		return username;
	}

	public static void setUsername(String username) {
		Client.username = username;
	}

	public static String getGegnername() {
		return gegnername;
	}

	public static void setGegnername(String gegnername) {
		Client.gegnername = gegnername;
	}

	public static boolean isLoggedIn() {
		return loggedIn;
	}

	public static void setLoggedIn(boolean loggedIn) {
		Client.loggedIn = loggedIn;
	}

	public static boolean isStarten() {
		return starten;
	}

	public static void setStarten(boolean starten) {
		Client.starten = starten;
	}

	public static String getIp() {
		return ip;
	}

	public static void setIp(String ip) {
		Client.ip = ip;
	}

	public static int getPort() {
		return port;
	}

	public static void setPort(int port) {
		Client.port = port;
	}

	public static String waitForMessage() {
		String temp = null;
		try {
			temp = inputStream.readUTF();
			System.out.println(PREFIX + "Nachricht vom Server: " + temp);
			return temp;
		} catch (IOException e) {
			return null;
		}
	}

	public static boolean waitForAnswer() {
		boolean temp = false;
		try {
			temp = inputStream.readBoolean();
			System.out.println(PREFIX + "Nachricht vom Server: " + temp);
			return temp;
		} catch (IOException e) {
			return false;
		}
	}

	public static boolean sendMessage(String prefix, String msg) {
		try {
			outputStream.writeUTF(prefix + msg);
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	public static boolean sendLogin(String username, String password) {
		try {
			outputStream.writeUTF("§l" + username + "-" + password);
		} catch (IOException e) {
			return false;
		}
		boolean response = waitForAnswer();
		return response;
	}

	public static boolean sendRegister(String email, String username, String password) {
		try {
			outputStream.writeUTF("§r" + email + "-" + username + "-" + password);
		} catch (IOException e) {
			return false;
		}
		boolean response = waitForAnswer();
		return response;
	}

	public static String sendAccountDataRequest(String username) {
		try {
			outputStream.writeUTF("§p" + username);
		} catch (IOException e) {
			return null;
		}
		String response = waitForMessage();
		return response;
	}

	public static boolean sendMove(String move) {
		try {
			outputStream.writeUTF("§m" + move.toString());
			System.out.println(PREFIX + "Move " + move + " wurde gesendet!");
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	public static boolean sendWin(String username) {
		try {
			outputStream.writeUTF("§w" + username);
			System.out.println(PREFIX + "Spieler " + username + " hat gewonnen!");
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	public static boolean sendDisconnectionWin(String username) {
		try {
			outputStream.writeUTF("§z" + username);
			System.out.println(PREFIX + "Spieler " + username + " hat gewonnen!");
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	public static boolean sendDraw(String username) {
		try {
			outputStream.writeUTF("§u" + username);
			System.out.println(PREFIX + "Spieler " + username + " hat ein Remis erziehlt!");
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	public static boolean sendLose(String username) {
		try {
			outputStream.writeUTF("§v" + username);
			System.out.println(PREFIX + "Spieler " + username + " hat verloren!");
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	public static boolean sendDisconnect(String username) {
		try {
			outputStream.writeUTF("§d" + username);
			System.out.println(PREFIX + "Spieler " + username + " hat die Verbindung verloren!");
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	public static boolean sendLogout(String username) {
		try {
			outputStream.writeUTF("§o" + username);
			System.out.println(PREFIX + "Spieler " + username + " hat sich ausgeloggt!");
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	public static boolean sendRankedGame(String username, String queue) {
		try {
			outputStream.writeUTF("§q" + username + "-" + queue);
			String response = waitForMessage();
			String[] message = response.split("-");
			gegnername = message[1];
			sendMessage("§x", "Aus der Schleife werfen");
			if (message[0].equals("true")) {
				starten = true;
			} else {
				try {
					starten = false;
					Thread.sleep(250);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			return Boolean.valueOf(message[0]);
		} catch (IOException e) {
			return false;
		}
	}

	public static void disconnect() {
		try {
			if (socket != null && socket.isConnected()) {
				socket.close();
				System.out.println(PREFIX + "Wurde erfolgreich vom Server disconnected!");
			}
		} catch (Exception e) {
			if (socket == null || !socket.isConnected()) {
				System.out.println(PREFIX + "Fehler beim Disconnect!");
			} else {
				System.out.println(PREFIX + "Fehler beim Disconnect!");
			}

		}
	}

}
