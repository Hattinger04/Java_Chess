/**
 * @author Simon Greiderer 
 * @author Marc Underrain
 * 
 * Klasse mit Hilfsmethoden fuer das Schachbrett
 * 
 * Auch lokale File Informationen werden (zurzeit noch)
 * hier gespeichert
 */
package game.logik.board;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import game.data.Client;
import game.data.FileSave;

public class BoardUtils {

	// Von dieser Klasse soll nie ein Objekt erstellt werden!
	private BoardUtils() {
		throw new RuntimeException("Du kannst mich nicht erstellen!");
	}

	public static boolean highlightLegalMoves;
	public static boolean playMusic;
	public static boolean playSoundEffects;
	public static double volumePlaylist;
	public static double volumeIngameSounds;
	public static String theme;

	public static String DEFAULT_FIGUREN_IMAGE_PATH = "./resources/images/theme1/"; 
	public static final File SETTINGS_FILE = new File("./resources/files/settings");

	public static final String SETTINGS_LEGALMOVES = "LegalMoves";
	public static final String SETTINGS_PLAYMUSIC = "PlayMusic";
	public static final String SETTINGS_PLAYSOUNDEFFECTS = "PlaySoundEffects";
	public static final String SETTINGS_VOLUMEPLAYLIST = "VolumePlaylist";
	public static final String SETTINGS_VOLUMEINGAMESOUNDS = "VolumeIngameSounds";
	public static final String SETTINGS_THEME = "Theme";
	
	public static final String IP = "IP"; 
	public static final String PORT = "Port"; 

	public static final boolean[] ERSTE_SPALTE = initColumn(0);
	public static final boolean[] ZWEITE_SPALTE = initColumn(1);
	public static final boolean[] SIEBTE_SPALTE = initColumn(6);
	public static final boolean[] ACHTE_SPALTE = initColumn(7);

	public static final boolean[] EIGHT_RANK = initRow(0);
	public static final boolean[] SEVENTH_RANK = initRow(8);
	public static final boolean[] SIXTH_RANK = initRow(16);
	public static final boolean[] FIFTH_RANK = initRow(24);
	public static final boolean[] FOUTH_RANK = initRow(32);
	public static final boolean[] THRID_RANK = initRow(40);
	public static final boolean[] SECOND_RANK = initRow(48);
	public static final boolean[] FIRST_RANK = initRow(56);

	public static final String[] ALGEBRETIC_NOTATION = initializeAlgebreticNotation();
	public static final Map<String, Integer> POSITION_TO_KOORDINATE = initializePositionToCoordinateMap();

	public static final int ANZAHL_FELDER = 64;
	public static final int ANZAHL_FELDER_PRO_REIHE = 8;

	// Lese Files aus
	public static void readFiles() {
		try {
			highlightLegalMoves = Boolean
					.valueOf(FileSave.readUsersettings(BoardUtils.SETTINGS_FILE, SETTINGS_LEGALMOVES));
			playMusic = Boolean
					.valueOf(FileSave.readUsersettings(BoardUtils.SETTINGS_FILE, SETTINGS_PLAYMUSIC));
			playSoundEffects = Boolean.valueOf(
					FileSave.readUsersettings(BoardUtils.SETTINGS_FILE, SETTINGS_PLAYSOUNDEFFECTS));

			volumePlaylist = Double.valueOf(
					FileSave.readUsersettings(BoardUtils.SETTINGS_FILE, SETTINGS_VOLUMEPLAYLIST));

			volumeIngameSounds = Double.valueOf(
					FileSave.readUsersettings(BoardUtils.SETTINGS_FILE, SETTINGS_VOLUMEINGAMESOUNDS));

			theme = FileSave.readUsersettings(BoardUtils.SETTINGS_FILE, SETTINGS_THEME);

			Client.setIp(FileSave.readUsersettings(BoardUtils.SETTINGS_FILE, IP));
			Client.setPort(Integer.valueOf(FileSave.readUsersettings(BoardUtils.SETTINGS_FILE, PORT)));
		} catch (NullPointerException e) {
			System.out.println("Manche Dateien konnten nicht ausgelesen werden");
		}
	}

	public static boolean isVerfuegbareKoordinate(final int koordinate) {
		return koordinate >= 0 && koordinate < ANZAHL_FELDER;
	}

	private static Map<String, Integer> initializePositionToCoordinateMap() {

		final Map<String, Integer> positionToCoordinate = new HashMap<String, Integer>();

		for (int i = 0; i < ANZAHL_FELDER; i++) {
			positionToCoordinate.put(ALGEBRETIC_NOTATION[i], i);
		}

		return positionToCoordinate;
	}

	// Schachbrett als String Array
	private static String[] initializeAlgebreticNotation() {
		return new String[] { "a8", "b8", "c8", "d8", "e8", "f8", "g8", "h8", "a7", "b7", "c7", "d7", "e7", "f7", "g7",
				"h7", "a6", "b6", "c6", "d6", "e6", "f6", "g6", "h6", "a5", "b5", "c5", "d5", "e5", "f5", "g5", "h5",
				"a4", "b4", "c4", "d4", "e4", "f4", "g4", "h4", "a3", "b3", "c3", "d3", "e3", "f3", "g3", "h3", "a2",
				"b2", "c2", "d2", "e2", "f2", "g2", "h2", "a1", "b1", "c1", "d1", "e1", "f1", "g1", "h1" };
	}

	private static boolean[] initRow(int rowNumber) {

		final boolean[] spalte = new boolean[ANZAHL_FELDER];
		do {
			spalte[rowNumber] = true;
			rowNumber++;
		} while (rowNumber % ANZAHL_FELDER_PRO_REIHE != 0);
		return spalte;
	}

	private static boolean[] initColumn(int columnNumber) {
		final boolean[] reihe = new boolean[ANZAHL_FELDER];
		do {
			reihe[columnNumber] = true;
			columnNumber += ANZAHL_FELDER_PRO_REIHE;
		} while (columnNumber < ANZAHL_FELDER);
		return reihe;
	}

	public static int getKoordinateAtPosition(String position) {
		return POSITION_TO_KOORDINATE.get(position);
	}

	public static String getPositionAtKoordinate(int koordinate) {
		return ALGEBRETIC_NOTATION[koordinate];
	}
}
