/**
 * @author Simon Greiderer
 * @author Marc Underrain
 * 
 * Klasse für Konsoleneingaben ueber Scanner
 */
package control;

import java.util.ArrayList;
import java.util.Scanner;

import mysql.MySQLConnect;

public class ServerControl extends Thread {

	private String PREFIX = "SERVER-CONTROL: ";
	private Scanner scanner;

	public ServerControl() {
		scanner = new Scanner(System.in);
		AnsiCode.sendSuccess(PREFIX, "Es koennen Befehle eingegeben werden!");
	}

	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		String text = "";
		while (true) {
			text = scanner.nextLine();
			if (text == null) {
				stop();
				break;
			} else {
				checkMessage(text);
				scanner.reset();
			}
		}
	}

	private void checkMessage(String message) {
		if (message.equals("exit") || message.equals("exit()") || message.equals("stop") || message.equals("stop()")) {
			AnsiCode.sendSuccess(PREFIX, "Du hast den Server geschlossen!");
			System.exit(0);
		} else if (message.equals("playerlist") || message.equals("pl")) {
			AnsiCode.sendSuccess(PREFIX, "Anzahl Spieler: " + Server.getPlayerlist().size());
			for (int i = 0; i < Server.getPlayerlist().size(); i++) {
				AnsiCode.sendMessage(PREFIX, "Spieler " + i + 1 + ": " + Server.getPlayerlist().get(i));
			}
		} else if (message.equals("allplayer") || (message.equals("allPlayer"))) {
			ArrayList<String> list = MySQLConnect.selectAllPlayer();
			AnsiCode.sendSuccess(PREFIX, "Anzahl Spieler: " + list.size());
			for (String s : list) {
				AnsiCode.sendMessage(PREFIX, s);
			}
		} else if (message.equals("banlist")) {
			ArrayList<String> list = MySQLConnect.selectAllBannedPlayer();
			AnsiCode.sendSuccess(PREFIX, "Anzahl gebannter Spieler: " + list.size());
			for (String s : list) {
				AnsiCode.sendMessage(PREFIX, s);
			}
		} else if (message.startsWith("ban ")) {
			message = message.substring(4);
			MySQLConnect.updateBanned(message, 1);
		} else if (message.startsWith("unban ")) {
			message = message.substring(6);
			MySQLConnect.updateBanned(message, 0);
		}
	}
}