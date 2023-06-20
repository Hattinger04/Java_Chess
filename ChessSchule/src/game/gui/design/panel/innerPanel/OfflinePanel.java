/**
 * @author Simon Greiderer 
 * @author Marc Underrain
 * 
 */
package game.gui.design.panel.innerPanel;

import game.gui.GUIUtils;
import game.gui.design.panel.ContentPanel;
import game.gui.design.panel.buttons.subclasses.PanelButtonOffline;

import javax.swing.JLabel;

import javax.swing.JComboBox;

@SuppressWarnings("serial")
public class OfflinePanel extends ContentPanel {
	private String[] computerStrenght = {"1", "2", "3", "4", "5", "6", "7", "8"}; 
	private JComboBox<String> comboBox = new JComboBox<String>(computerStrenght);
	
	
	public OfflinePanel(String path) {
		super(path);
		GUIUtils.style();
		setBounds(0, 0, 500, 400);
		
		JLabel lbl_Offline = new JLabel("Offline");
		lbl_Offline.setBounds(180, 15, 130, 24);
		lbl_Offline.setFont(GUIUtils.font);
		add(lbl_Offline);
		
		JLabel lbl_Spielstaerke = new JLabel("Spielstaerke:");
		lbl_Spielstaerke.setFont(GUIUtils.font);
		lbl_Spielstaerke.setBounds(25, 101, 200, 33);
		add(lbl_Spielstaerke);
	
		comboBox.setBounds(220, 101, 184, 33);
		comboBox.setSelectedIndex(3);
		add(comboBox);
		
		PanelButtonOffline panel = new PanelButtonOffline("whiteButton.png", "whiteButton.png");
		panel.setBounds(52, 255, 75, 75);
		add(panel);
		
		PanelButtonOffline panel_1 = new PanelButtonOffline("blackButton.png", "blackButton.png");
		panel_1.setBounds(326, 255, 75, 75);
		add(panel_1);
		
		PanelButtonOffline panel_2 = new PanelButtonOffline("zufallButton.png", "zufallButton.png");
		panel_2.setBounds(184, 245, 85, 85);
		add(panel_2);
	}
	
	public JComboBox<String> getComboBox() {
		return comboBox;
	}
}
