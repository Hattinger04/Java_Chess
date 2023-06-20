/**
 * @author Simon Greiderer 
 * @author Marc Underrain
 * 
 * Das erste Frame, welches man sieht und auch die Klasse, 
 * welche in der Main aufgerufen wird
 */
package game.gui.design;


import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import game.data.Client;
import game.gui.GUIUtils;
import game.gui.design.panel.MotionPanel;
import game.gui.design.panel.buttons.subclasses.PanelButtonStart;
import game.gui.design.panel.innerPanel.AccountPanel;
import game.gui.design.panel.innerPanel.FlyPanel;
import game.gui.design.panel.innerPanel.LoginPanel;
import game.gui.design.panel.innerPanel.OfflinePanel;
import game.gui.design.panel.innerPanel.OnlinePanel;
import game.gui.design.panel.innerPanel.SettingsPanel;
import game.gui.design.panel.innerPanel.SoundPanel;
import game.logik.board.BoardUtils;
import game.sound.PlaySound;

@SuppressWarnings("serial")
public class StartFrame extends JFrame {

	private static MotionPanel contentPane;
	
	public static final int X_COORD = 270;
	public static final int Y_COORD = 129;
	public static final int LENGHT= 500;
	public static final int HEIGHT = 400;
	
	private JPanel panel_frame;
	
	private PanelButtonStart loginPanel, accountPanel; 
	
	private static FlyPanel panel; 
	
	/**
	 * Frame wird erstellt und die Panels LINKS auf dem 
	 * Bildschrim erstellt, welche alle einen Mouselistener
	 * besitzen
	 * 
	 * Ein weiteres JPanel wird erstellt, welches jedoch nicht angezeigt
	 * wird. Wenn auf einen Button geclickt wird, wird dieses Panel
	 * entsprechend ueberschrieben
	 * 
	 * Auﬂerdem wird die Settingsdatei ausgelesen und entsprechend
	 * die Musik ab- oder nicht abgespielt werden
	 */
	@SuppressWarnings("static-access")
	public StartFrame() {
		
		setBounds(100, 100, 780, 585);
		setUndecorated(true);
		BoardUtils.readFiles();
		GUIUtils.loadFont();
		
		Runtime.getRuntime().addShutdownHook(new Thread() {
		    public void run() {
				if(Client.isLoggedIn()) {
					Client.sendLogout(Client.getUsername()); 
				}
		    }
		});
		
		if(BoardUtils.theme.equals(GUIUtils.DEFAULT_THEME_JUJUTSU_KAISEN)) {
			GUIUtils.CURRENT_START_PAGE = "startPageJujutsuKaisen.jpg"; 
			GUIUtils.CURRENT_START_PAGE_PANEL = "startPagePanelJujutsuKaisen.jpg";
			contentPane = new MotionPanel(this, GUIUtils.CURRENT_START_PAGE);
			BoardUtils.DEFAULT_FIGUREN_IMAGE_PATH = "./resources/images/theme2/"; 
		} else if(BoardUtils.theme.equals(GUIUtils.DEFAULT_THEME_AOT)) {
			GUIUtils.CURRENT_START_PAGE = "startPageAOT.jpg"; 
			GUIUtils.CURRENT_START_PAGE_PANEL = "startPagePanelAOT.jpg";
			BoardUtils.DEFAULT_FIGUREN_IMAGE_PATH = "./resources/images/theme3/"; 
			contentPane = new MotionPanel(this, GUIUtils.CURRENT_START_PAGE);

		} else {
			GUIUtils.CURRENT_START_PAGE = "startPage.jpg"; 
			GUIUtils.CURRENT_START_PAGE_PANEL = "startPagePanel.jpg"; 
			BoardUtils.DEFAULT_FIGUREN_IMAGE_PATH = "./resources/images/theme1/"; 
			contentPane = new MotionPanel(this, GUIUtils.CURRENT_START_PAGE);
		}
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		PanelButtonStart panel_online = new PanelButtonStart("OnlineUnpressed.png", "OnlinePressed.png");
		panel_online.setBounds(25, 74, 221, 57);
		contentPane.add(panel_online);
		
		PanelButtonStart panel_offline = new PanelButtonStart("OfflineUnpressed.png", "OfflinePressed.png");
		panel_offline.setBounds(25, 164, 221, 57);
		contentPane.add(panel_offline);

		if(!Client.isLoggedIn()) {
			loginPanel = new PanelButtonStart("LoginUnpressed.png", "LoginPressed.png");
			loginPanel.setBounds(25, 253, 221, 57);
			contentPane.add(loginPanel);
		} else {
			accountPanel = new PanelButtonStart("AccountUnpressed.png", "AccountPressed.png");
			accountPanel.setBounds(25, 253, 221, 57);
			contentPane.add(accountPanel);
		}
		
		PanelButtonStart panel_settings = new PanelButtonStart("SettingsUnpressed.png", "SettingsPressed.png");
		panel_settings.setBounds(25, 339, 221, 57);
		contentPane.add(panel_settings);
		
		PanelButtonStart panel_sound = new PanelButtonStart("SoundUnpressed.png", "SoundPressed.png");
		panel_sound.setBounds(25, 422, 221, 57);
		contentPane.add(panel_sound);
		
		PanelButtonStart panel_exit = new PanelButtonStart("ExitUnpressed.png", "ExitPressed.png");
		panel_exit.setBounds(25, 504, 221, 57);
		contentPane.add(panel_exit);
		
		panel = new FlyPanel("servus.png");
		panel.setBounds(638, 46, 150, 50);
		contentPane.add(panel);
		
		if(BoardUtils.playMusic) {
			new PlaySound().startPlaying();
		}
		
		panel_frame = new JPanel(); 
	}
	
	
	public static void refreshContentPane(String path) {
		contentPane.changeImage(path);
	}
	
	public static FlyPanel getPanel() {
		return panel;
	}
	
	public static void setPanel(FlyPanel panel) {
		StartFrame.panel = panel;
		StartFrame.panel.setBounds(638, 46, 150, 50);
		StartFrame.panel.repaint(); 
		contentPane.add(StartFrame.panel); 
	}
	
	public JPanel getPanel_frame() {
		return panel_frame;
	}
	
	public void removePanel_frame() {
		panel_frame.removeAll();	
		panel_frame.setVisible(false);
		contentPane.changeImage(GUIUtils.CURRENT_START_PAGE);
	}
	
	public void setPanel_frameOnline(String path) {
		panel_frame.removeAll();	
		panel_frame = new OnlinePanel(path);
		panel_frame.setBounds(X_COORD, Y_COORD, LENGHT, HEIGHT);
		contentPane.add(panel_frame);
		contentPane.changeImage(GUIUtils.CURRENT_START_PAGE_PANEL);
		panel_frame.setVisible(true);
	}
	
	public void setPanel_frameOffline(String path) {
		panel_frame.removeAll();	
		panel_frame = new OfflinePanel(path);
		panel_frame.setBounds(X_COORD, Y_COORD, LENGHT, HEIGHT);
		contentPane.add(panel_frame);
		contentPane.changeImage(GUIUtils.CURRENT_START_PAGE_PANEL);
		panel_frame.setVisible(true);
	}
	
	public void setPanel_frameLogin(String path) {
		panel_frame.removeAll();	
		panel_frame = new LoginPanel(path);
		panel_frame.setBounds(X_COORD, Y_COORD, LENGHT, HEIGHT);
		contentPane.add(panel_frame);
		contentPane.changeImage(GUIUtils.CURRENT_START_PAGE_PANEL);
		panel_frame.setVisible(true);
	}
	
	public void setPanel_frameAccount(String path) {
		panel_frame.removeAll();	
		panel_frame = new AccountPanel(path);
		panel_frame.setBounds(X_COORD, Y_COORD, LENGHT, HEIGHT);
		contentPane.add(panel_frame);
		contentPane.changeImage(GUIUtils.CURRENT_START_PAGE_PANEL);
		panel_frame.setVisible(true);
	}
	
	public void setPanel_frameSound(String path) {
		panel_frame.removeAll();	
		panel_frame = new SoundPanel(path);
		panel_frame.setBounds(X_COORD, Y_COORD, LENGHT, HEIGHT);
		contentPane.add(panel_frame);
		contentPane.changeImage(GUIUtils.CURRENT_START_PAGE_PANEL);
		panel_frame.setVisible(true);
	}
	
	public void setPanel_frameSettings(String path) {
		panel_frame.removeAll();	
		panel_frame = new SettingsPanel(path);
		panel_frame.setBounds(X_COORD, Y_COORD, LENGHT, HEIGHT);
		contentPane.add(panel_frame);
		contentPane.changeImage(GUIUtils.CURRENT_START_PAGE_PANEL);
		panel_frame.setVisible(true);
	}

	public void setAccountPanel() {
		loginPanel.setVisible(false);
		accountPanel = new PanelButtonStart("AccountUnpressed.png", "AccountPressed.png");
		accountPanel.setBounds(25, 253, 221, 57);
		contentPane.add(accountPanel);
	}
	
	public void setLoginPanel() {
		accountPanel.setVisible(false);
		loginPanel = new PanelButtonStart("LoginUnpressed.png", "LoginPressed.png");
		loginPanel.setBounds(25, 253, 221, 57);
		contentPane.add(loginPanel);

	}
	
}