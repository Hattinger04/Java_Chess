/**
 * @author Simon Greiderer 
 * @author Marc Underrain
 * 
 */
package game.gui.design.panel.buttons.subclasses;

import javax.swing.SwingUtilities;

import game.gui.GUIUtils;
import game.gui.design.StartFrame;
import game.gui.design.panel.buttons.PanelButton;

@SuppressWarnings("serial")
public class PanelButtonStart extends PanelButton {

	// Setzt die Panels wieder auf false, damit nur eines angezeigt wird
	public static boolean isPanel0Active = false; 
	public static boolean isPanel1Active = false; 
	public static boolean isPanel2Active = false; 
	public static boolean isPanel3Active = false; 
	public static boolean isPanel4Active = false; 

	/**
	 * 
	 * @param path1
	 * @param path2
	 */
	public PanelButtonStart(String path1, String path2) {
		super(path1, path2); 			
	}

	
	public void doWhenClicked() {
		if(super.getPath1().equals(GUIUtils.DEFAULT_BUTTONS_IMAGE_PATH + "OnlineUnpressed.png")) {
			if(!isPanel0Active) {
				isPanel1Active = false; 
				isPanel2Active = false; 
				isPanel3Active = false; 
				isPanel4Active = false; 
				((StartFrame) SwingUtilities.getWindowAncestor(this)).setPanel_frameOnline("startPageButtonClick.png"); 
			} else  {
				((StartFrame) SwingUtilities.getWindowAncestor(this)).removePanel_frame();
			}
			isPanel0Active = !isPanel0Active; 
		}
		if(super.getPath1().equals(GUIUtils.DEFAULT_BUTTONS_IMAGE_PATH + "OfflineUnpressed.png")) {
			if(!isPanel1Active) {
				isPanel0Active = false; 
				isPanel2Active = false; 
				isPanel3Active = false; 
				isPanel4Active = false; 
				((StartFrame) SwingUtilities.getWindowAncestor(this)).setPanel_frameOffline("startPageButtonClick.png"); 
			} else  {
				((StartFrame) SwingUtilities.getWindowAncestor(this)).removePanel_frame();
			}
			isPanel1Active = !isPanel1Active; 

		}
		
		if(super.getPath1().equals(GUIUtils.DEFAULT_BUTTONS_IMAGE_PATH + "LoginUnpressed.png")) {
			if(!isPanel2Active) {
				isPanel0Active = false; 
				isPanel1Active = false; 
				isPanel3Active = false;
				isPanel4Active = false; 
				((StartFrame) SwingUtilities.getWindowAncestor(this)).setPanel_frameLogin("startPageButtonClick.png"); 
			} else  {
				((StartFrame) SwingUtilities.getWindowAncestor(this)).removePanel_frame();
			}
			isPanel2Active = !isPanel2Active; 
		} else if(super.getPath1().equals(GUIUtils.DEFAULT_BUTTONS_IMAGE_PATH + "AccountUnpressed.png")) {
			if(!isPanel2Active) {
				isPanel0Active = false; 
				isPanel1Active = false; 
				isPanel3Active = false;
				isPanel4Active = false; 
				((StartFrame) SwingUtilities.getWindowAncestor(this)).setPanel_frameAccount("startPageButtonClick.png"); 
			} else  {
				((StartFrame) SwingUtilities.getWindowAncestor(this)).removePanel_frame();
			}
			isPanel2Active = !isPanel2Active; 
		}
		
		if(super.getPath1().equals(GUIUtils.DEFAULT_BUTTONS_IMAGE_PATH + "SoundUnpressed.png")) {
			if(!isPanel3Active) {
				isPanel0Active = false; 
				isPanel1Active = false; 
				isPanel2Active = false;
				isPanel4Active = false; 
				((StartFrame) SwingUtilities.getWindowAncestor(this)).setPanel_frameSound("startPageButtonClick.png"); 
			} else  {
				((StartFrame) SwingUtilities.getWindowAncestor(this)).removePanel_frame();
			}
			isPanel3Active = !isPanel3Active; 
		}
		
		if(super.getPath1().equals(GUIUtils.DEFAULT_BUTTONS_IMAGE_PATH + "SettingsUnpressed.png")) {
			if(!isPanel4Active) {
				isPanel0Active = false; 
				isPanel1Active = false; 
				isPanel2Active = false;
				isPanel3Active = false;  
				((StartFrame) SwingUtilities.getWindowAncestor(this)).setPanel_frameSettings("startPageButtonClick.png"); 
			} else  {
				((StartFrame) SwingUtilities.getWindowAncestor(this)).removePanel_frame();
			}
			isPanel4Active = !isPanel4Active; 
		}
		
		if(super.getPath1().equals(GUIUtils.DEFAULT_BUTTONS_IMAGE_PATH + "ExitUnpressed.png")) {
			System.exit(0);
		}
	}
}