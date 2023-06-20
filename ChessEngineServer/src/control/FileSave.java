/**
 * @author Simon Greiderer
 * @author Marc Underrain
 * 
 * Daten werden in der ServerSettings Datei abgespeichert
 * 
 */

package control;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class FileSave {

	private static Properties props = new Properties();

	public static void saveUsersettings(final File file, final String name, final String object) {
		try {
			props.setProperty(name, object);
			OutputStream out = new FileOutputStream(file);
			props.store(out, "ServerSettings");
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
			input = props.getProperty(name);
			in.close();
		} catch (Exception e) {
			input = "8888";
			saveUsersettings(file, name, input);
		}
		return input;
	}

	
}
