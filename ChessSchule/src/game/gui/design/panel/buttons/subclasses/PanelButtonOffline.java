/**
 * @author Simon Greiderer 
 * @author Marc Underrain
 * 
 */
package game.gui.design.panel.buttons.subclasses;

import javax.swing.SwingUtilities;

import game.gui.GUIUtils;
import game.gui.brettAllerBretter.GameFrame;
import game.gui.design.StartFrame;
import game.gui.design.panel.buttons.PanelButton;
import game.gui.design.panel.innerPanel.OfflinePanel;
import game.logik.Farbe;
import game.sound.PlaySound;

@SuppressWarnings("serial")
public class PanelButtonOffline extends PanelButton {

	GameFrame frame; 
	
	/**
	 * 
	 * @param path1
	 * @param path2
	 */
	public PanelButtonOffline(String path1, String path2) {
		super(path1, path2);
	}

	@Override
	public void doWhenClicked() {
		OfflinePanel panel = (OfflinePanel) ((StartFrame) SwingUtilities.getWindowAncestor(this)).getPanel_frame(); 
		int value = Integer.valueOf((String) panel.getComboBox().getSelectedItem());  
	
		if(!GUIUtils.isStart1) {
			GameFrame.setGameFrame();
		}
		frame = GameFrame.get();	
		GUIUtils.isStart1 = false;
		PanelButtonStart.isPanel1Active = false;
		PlaySound.pause();
		if(super.getPath1().equals(GUIUtils.DEFAULT_BUTTONS_IMAGE_PATH + "whiteButton.png")) {
			frame.setKiValues(Farbe.WHITE, value);
		}
		if(super.getPath1().equals(GUIUtils.DEFAULT_BUTTONS_IMAGE_PATH + "blackButton.png")) {
			frame.setKiValues(Farbe.BLACK, value);
		}
		if(super.getPath1().equals(GUIUtils.DEFAULT_BUTTONS_IMAGE_PATH + "zufallButton.png")) {
			boolean random = (Math.random() <= 0.5) ? true : false;
			frame.setKiValues((random ? Farbe.WHITE : Farbe.BLACK), value);
		}
		SwingUtilities.getWindowAncestor(this).dispose();	
	}
	
}
