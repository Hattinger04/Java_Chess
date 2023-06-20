/**
 * @author Simon Greiderer 
 * @author Marc Underrain
 * 
 * Musik Klasse, welche ueber JavaFX laeuft
 */
package game.sound;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.Queue;

import game.data.FileSave;
import game.gui.GUIUtils;
import game.logik.board.BoardUtils;
import javafx.application.Application;
import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

public class PlaySound extends Application {

	private static Queue<Path> musicList = new LinkedList<>();

	public static final String DEFAULT_SOUND_LOCATION = "./resources/sounds/";
	public static String DEFAULT_PLAYLIST_LOCATION = DEFAULT_SOUND_LOCATION + "playlistRocky/";
	public static final String DEFAULT_INGAME_SOUND_LOCATION = DEFAULT_SOUND_LOCATION + "ingameSounds/";

	
	private static boolean hasStarted = false;
	private static boolean isPlaying = false;
	private static boolean isPlayingWin = false;
	private static MediaPlayer mediaPlayer;
	private static MediaPlayer mediaPlayerIngameSounds;
	
	private static double volumePlaylist = BoardUtils.volumePlaylist; 
	private static double volumeIngameSounds = BoardUtils.volumeIngameSounds; 


	@Override
	public void start(Stage primaryStage) throws Exception {
	}

	/**
	 * Es wird ein Verzeichniss nach allen mp3 Dateien ausgelesen.
	 */
	public static void startPlaying() {
		@SuppressWarnings("unused")
		final JFXPanel fxPanel = new JFXPanel(); // Muss erstellt werden, wird aber nicht verwendet
		if(BoardUtils.theme.equals(GUIUtils.DEFAULT_THEME_JUJUTSU_KAISEN)) {
			DEFAULT_PLAYLIST_LOCATION = DEFAULT_SOUND_LOCATION + "playlistJujutsuKaisen/";
		} else if(BoardUtils.theme.equals(GUIUtils.DEFAULT_THEME_AOT)) {
			DEFAULT_PLAYLIST_LOCATION = DEFAULT_SOUND_LOCATION + "playlistAOT/";
		} else {
			DEFAULT_PLAYLIST_LOCATION = DEFAULT_SOUND_LOCATION + "playlistRocky/";
		}
		try {
			Files.find(Paths.get(DEFAULT_PLAYLIST_LOCATION), 100,
					(p, a) -> p.toString().toLowerCase().endsWith(".mp3")).forEach(musicList::add);
			playMusic();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void playIngameSound(String path) {
		@SuppressWarnings("unused")
		final JFXPanel fxPanel = new JFXPanel();
		mediaPlayerIngameSounds = new MediaPlayer(new Media(new File(DEFAULT_INGAME_SOUND_LOCATION + path).toURI().toString()));
		mediaPlayerIngameSounds.play();
		mediaPlayerIngameSounds.setVolume(volumeIngameSounds);
	}
	
	/**
	 * Es wird ein MediaPlayer erstellt, welches alle Lieder mit dem vorher
	 * berechneten Verzeichniss abspielt.
	 */
	private static void playMusic() {
		if (musicList.peek() == null) {
			startPlaying();
			return;
		}
		mediaPlayer = new MediaPlayer(new Media(musicList.poll().toUri().toString()));
		mediaPlayer.setOnReady(() -> {
			mediaPlayer.play();
			mediaPlayer.setVolume(volumePlaylist);

			mediaPlayer.setOnEndOfMedia(() -> {
				mediaPlayer.dispose();
				playMusic();
			});
		});
		hasStarted = true;
		isPlaying = true;
	}

	/**
	 * Die Musik wird komplett gestoppt
	 */
	public void stop() {
		if (mediaPlayer != null) {
			mediaPlayer.stop();
			mediaPlayer.dispose();
			isPlaying = false;
		}
	}

	public static void end() {
		if (mediaPlayer != null) {
			mediaPlayer.stop();
			mediaPlayer.dispose();
			musicList = new LinkedList<>(); 
			isPlaying = false;
		}
	}

		
	
	/**
	 * Die Musik wird nur pausiert
	 */
	public static void pause() {
		if (mediaPlayer != null) {
			mediaPlayer.pause();
			isPlaying = false;
		}
	}
	/**
	 * Die Musik wird weitergespielt
	 */
	public static void resume() {
		if (mediaPlayer != null) {
			mediaPlayer.play();
			isPlaying = true;
		}
	}
	
	
	public static void playSound() {
		if (mediaPlayerIngameSounds != null) {
			mediaPlayerIngameSounds.play();
		}
	}

	/**
	 * Der Musik kann eine bestimmte Lautstärke gegeben werden.
	 */
	public static void setVolumePlaylist(double volume) {
		PlaySound.volumePlaylist = volume / 100; 
		FileSave.saveUsersettings(BoardUtils.SETTINGS_FILE, BoardUtils.SETTINGS_VOLUMEPLAYLIST, Double.toString(volumePlaylist));
		if (mediaPlayer != null) {
			mediaPlayer.setVolume(volumePlaylist);
		}
	}
	public static void setVolumeIngameSounds(double volume) {
		PlaySound.volumeIngameSounds = volume / 100; 
		FileSave.saveUsersettings(BoardUtils.SETTINGS_FILE, BoardUtils.SETTINGS_VOLUMEINGAMESOUNDS, Double.toString(volumeIngameSounds));
		if (mediaPlayerIngameSounds != null) {
			mediaPlayerIngameSounds.setVolume(volumeIngameSounds);
		}
	}
	public static boolean hasStarted() {
		return hasStarted;
	}

	public static boolean isPlaying() {
		return isPlaying;
	}

	public static boolean isPlayingWin() {
		return isPlayingWin;
	}

	public static void stopIngameSound() {
		if(mediaPlayerIngameSounds != null) {	
			mediaPlayerIngameSounds.stop();
		}
	}

}