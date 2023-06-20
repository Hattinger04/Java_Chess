/**
 * @author Simon Greiderer 
 * @author Marc Underrain
 * 
 */
package game.gui.design.panel.buttons.subclasses;

import game.data.FileSave;
import game.gui.GUIUtils;
import game.gui.design.panel.buttons.PanelCheckButton;
import game.logik.board.BoardUtils;
import game.sound.PlaySound;

@SuppressWarnings("serial")
public class PanelButtonSound extends PanelCheckButton{

	private int id; 
	
	/**
	 * 
	 * @param path1
	 * @param path2
	 * @param id
	 */
	public PanelButtonSound(String path1, String path2, int id) {
		super(path1, path2);
		this.id = id; 
	}
	
	@SuppressWarnings("static-access")
	@Override
	public void doWhenClicked(boolean active) {
		if(super.getPath1().equals(GUIUtils.DEFAULT_BUTTONS_IMAGE_PATH + "checkButtonUnpressedSettings.png") && id == 1) {
			boolean playMusic = active; 
        	BoardUtils.playMusic = playMusic;
        	FileSave.saveUsersettings(BoardUtils.SETTINGS_FILE, BoardUtils.SETTINGS_PLAYMUSIC, Boolean.toString(playMusic));
        	if(playMusic) {
        		if(PlaySound.hasStarted() && !PlaySound.isPlaying()) {
        			PlaySound.resume();
        		} else if(!PlaySound.hasStarted()) {
        			new PlaySound().startPlaying();
        		}
        	} else {
        		if(PlaySound.hasStarted() && PlaySound.isPlaying()) {
        			PlaySound.pause();
        		}
        	}
		}
		if(super.getPath1().equals(GUIUtils.DEFAULT_BUTTONS_IMAGE_PATH + "checkButtonUnpressedSettings.png") && id == 2) {
			boolean playMusic = active; 
        	BoardUtils.playSoundEffects = playMusic;
        	FileSave.saveUsersettings(BoardUtils.SETTINGS_FILE, BoardUtils.SETTINGS_PLAYSOUNDEFFECTS, Boolean.toString(playMusic));
		}
	}
}
