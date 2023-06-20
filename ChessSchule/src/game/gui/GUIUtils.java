/**
 * @author Simon Greiderer 
 * @author Marc Underrain
 * 
 * Klasse, welche praktische GUI Utils besitzt, 
 * welche von ueberall aufrufbar sein sollten
 */
package game.gui;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class GUIUtils {
	
	public static final String DEFAULT_BACKGROUND_IMAGE_PATH = "./resources/images/background/"; 
	public static final String DEFAULT_BUTTONS_IMAGE_PATH = "./resources/images/buttons/"; 
	public static final String DEFAULT_INFO_IMAGE_PATH = "./resources/images/info/"; 
	public static final String DEFAULT_THEME = "./resources/images/themes/"; 
	
	public static final String DEFAULT_THEME_DEFAULT = "default"; 
	public static final String DEFAULT_THEME_JUJUTSU_KAISEN = "JujutsuKaisen"; 
	public static final String DEFAULT_THEME_AOT = "AOT"; 

	public static final String FONT_FILE = "./resources/files/ChicagoFLF.ttf"; 
	
	public static String CURRENT_START_PAGE; 
	public static String CURRENT_START_PAGE_PANEL; 

	public static Font font; 
	public static Font fontGross; 
	
	public static boolean isStart1 = true; 
	public static boolean isStart1Online = true; 


	public static void style() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
	}
	
	public static void loadFont() {
		try {
		    font = Font.createFont(Font.TRUETYPE_FONT, new File(FONT_FILE)).deriveFont(23f);
		    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		    ge.registerFont(font);
		    
		    fontGross = Font.createFont(Font.TRUETYPE_FONT, new File(FONT_FILE)).deriveFont(40f);
		    GraphicsEnvironment ge2 = GraphicsEnvironment.getLocalGraphicsEnvironment();
		    ge2.registerFont(fontGross);
		} catch (IOException e) {
		    e.printStackTrace();
		} catch(FontFormatException e) {
		    e.printStackTrace();
		}
	}
	
}
