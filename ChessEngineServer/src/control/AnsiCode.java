/**
 * @author Simon Greiderer
 * @author Marc Underrain
 * 
 * Klasse um farbe Ausgaben in der Konsole zu machen
 * (Funktioniert automatisch auf Linux, nicht auf Windows!)
 * 
 * In eclipse muss man sich noch ANSI Escape im Eclipse Marketspace 
 * installieren
 */

package control;

public class AnsiCode {
	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_BLACK = "\u001B[30m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_YELLOW = "\u001B[33m";
	public static final String ANSI_BLUE = "\u001B[34m";
	public static final String ANSI_PURPLE = "\u001B[35m";
	public static final String ANSI_CYAN = "\u001B[36m";
	public static final String ANSI_WHITE = "\u001B[37m";

	public static void sendError(String PREFIX, String message) {
		System.out.println(ANSI_PURPLE + PREFIX + ANSI_RED + message);
	}
	
	public static void sendMessage(String PREFIX, String message) {
		System.out.println(ANSI_PURPLE + PREFIX + ANSI_YELLOW + message);
	}
	
	public static void sendSuccess(String PREFIX, String message) {
		System.out.println(ANSI_PURPLE + PREFIX + ANSI_GREEN + message);
	}
}
