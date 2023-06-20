/**
 * @author Simon Greiderer 
 * @author Marc Underrain
 * 
 * Wurde nur in vorherigen Versionen benoetigt, 
 * jedoch weiﬂ ich noch nicht, ob wir es spaeter 
 * vielleicht noch benoetigen
 * 
 */
package game.gui.design.panel.buttons.subclasses;

import javax.swing.SwingUtilities;

import game.data.Client;
import game.gui.GUIUtils;
import game.gui.design.StartFrame;
import game.gui.design.panel.buttons.PanelButton;
import game.gui.design.panel.innerPanel.LoginPanel;

@SuppressWarnings("serial")
public class PanelButtonLogin extends PanelButton {

	
	Client client; 
	/**
	 * 
	 * @param path1
	 * @param path2
	 */
	public PanelButtonLogin(String path1, String path2) {
		super(path1, path2);
	}

	@Override
	public void doWhenClicked() {
		if(!Client.isLoggedIn()) {
			if(super.getPath1().equals(GUIUtils.DEFAULT_BUTTONS_IMAGE_PATH + "LoginSendUnpressed.png")) {
					String username = LoginPanel.getTextField().getText(); 
					String password = new String(LoginPanel.getTextField_1().getPassword()); 
					if(username.isEmpty() || password.isEmpty()) {
						return; 
					}
					
					client = new Client();
					boolean login = false; 
					try {
						login = Client.sendLogin(username, password);
					} catch (NullPointerException e) {
						StartFrame.getPanel().reset("fehler.png"); 
						return;
					}
					
					if(login) {
						Client.setLoggedIn(true);
						Client.setUsername(username);
						StartFrame.getPanel().reset("login.png");
						((StartFrame) SwingUtilities.getWindowAncestor(this)).setAccountPanel();
						((StartFrame) SwingUtilities.getWindowAncestor(this)).removePanel_frame();
						PanelButtonStart.isPanel2Active = !PanelButtonStart.isPanel2Active; 
					}
					return; 
					
			}
			if(super.getPath1().equals(GUIUtils.DEFAULT_BUTTONS_IMAGE_PATH + "RegisterSendUnpressed.png")) {
				if(!Client.isLoggedIn()) {
					String email = LoginPanel.getTextField_2().getText(); 
					String username = LoginPanel.getTextField_3().getText(); 
					String password = new String(LoginPanel.getTextField_4().getPassword()); 
					
					if(email.isEmpty() || username.isEmpty() || password.isEmpty() || email.indexOf('@') == -1) {
						return; 
					}
					client = new Client();
					boolean register = false; 
					try {
						register = Client.sendRegister(email, username, password);
					} catch (NullPointerException e) {
						StartFrame.getPanel().reset("fehler.png"); 
						return;
					}
					if(register) {
						register = Client.sendRegister(email, username, password);
						LoginPanel.getTextField_2().setText("");
						LoginPanel.getTextField_3().setText("");
						LoginPanel.getTextField_4().setText("");
						StartFrame.getPanel().reset("register.png"); 
						return; 
					} else {
						return; 
					}
					
				}
			}
		}
			
	}

	
	
}
