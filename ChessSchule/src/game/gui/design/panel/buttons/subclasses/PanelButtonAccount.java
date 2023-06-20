package game.gui.design.panel.buttons.subclasses;


import javax.swing.SwingUtilities;

import game.data.Client;
import game.gui.GUIUtils;
import game.gui.design.StartFrame;
import game.gui.design.panel.buttons.PanelButton;

@SuppressWarnings("serial")
public class PanelButtonAccount extends PanelButton {

	public PanelButtonAccount(String path1, String path2) {
		super(path1, path2);
	}

	@Override
	public void doWhenClicked() {
		if(Client.isLoggedIn()) {
			if(super.getPath1().equals(GUIUtils.DEFAULT_BUTTONS_IMAGE_PATH + "LogoutSendUnpressed.png")) {
				Client.setLoggedIn(false);
				Client.sendLogout(Client.getUsername()); 
				Client.setUsername("");
				StartFrame.getPanel().reset("logout.png"); 
				((StartFrame) SwingUtilities.getWindowAncestor(this)).setLoginPanel();
				((StartFrame) SwingUtilities.getWindowAncestor(this)).removePanel_frame();
				PanelButtonStart.isPanel2Active = !PanelButtonStart.isPanel2Active; 
			}
		}
	}

}
