package game.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import game.logik.board.BoardUtils;

public class FileSave {

	private static Properties props = new Properties();

	public static void saveUsersettings(final File file, final String name, final String object) {
		try {
			props.setProperty(name, object);
			OutputStream out = new FileOutputStream(file);
			props.store(out, "Usersettings");
			out.close();
		} catch (Exception e) {
			System.out.println("Konnte nicht abgespeichert werden!");
		}
	}

	public static String readUsersettings(final File file, final String name) {
		String input = null;
		try {
			InputStream in = new FileInputStream(file); 
			props.load(in);
			if(props.getProperty(name) == null) {
				if(name.equals(BoardUtils.SETTINGS_THEME)) {
					saveUsersettings(file, name, "default");
				} else if(name.equals(BoardUtils.IP)) {
					saveUsersettings(file, name, "localhost");
				} else if(name.equals(BoardUtils.PORT)) {
					saveUsersettings(file, name, "8888");
				} else {
					saveUsersettings(file, name, "0");
				}
			}
			input = props.getProperty(name);
			in.close();
		} catch (Exception e) {
			System.out.println("Konnte nicht ausgelesen werden!");
		}
		return input;
	}
}
