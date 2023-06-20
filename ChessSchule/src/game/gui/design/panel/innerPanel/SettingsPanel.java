/**
 * @author Simon Greiderer 
 * @author Marc Underrain
 * 
 */
package game.gui.design.panel.innerPanel;


import game.gui.GUIUtils;
import game.gui.design.panel.ContentPanel;
import game.gui.design.panel.buttons.subclasses.PanelButtonSettings;
import game.logik.board.BoardUtils;

import javax.swing.JLabel;

@SuppressWarnings("serial")
public class SettingsPanel extends ContentPanel {

	private static PanelButtonSettings panel_1, panel_2, panel_3; 
	
	public SettingsPanel(String path) {
		super(path); 
		setBounds(0, 0, 500, 400);
		setLayout(null);
		setOpaque(false);

		JLabel lbl_Settings = new JLabel("Settings");
		lbl_Settings.setFont(GUIUtils.font);
		lbl_Settings.setBounds(180, 15, 120, 26);
		add(lbl_Settings);
		
		JLabel lbl_option1 = new JLabel("Zeige Legale Moves an: ");
		lbl_option1.setFont(GUIUtils.font);
		lbl_option1.setBounds(30, 85, 300, 34);
		add(lbl_option1);
		
		PanelButtonSettings panel = new PanelButtonSettings("checkButtonUnpressedSettings.png", "checkButtonPressedSettings.png", 1);
		if(BoardUtils.highlightLegalMoves) {
			panel.switchImage();
		}
		panel.setBounds(350, 76, 50, 50);
		add(panel);
		
		JLabel lbl_option2 = new JLabel("Waehle dein Theme: ");
		lbl_option2.setFont(GUIUtils.font);
		lbl_option2.setBounds(30, 180, 300, 34);
		add(lbl_option2);
		
		panel_1 = new PanelButtonSettings("DefaultThemePressed.png", "DefaultThemePressed.png",  2);
		if(BoardUtils.theme.equals(GUIUtils.DEFAULT_THEME_DEFAULT)) {
			panel_1.switchImage();
		}
		panel_1.setBounds(23, 244, 75, 75);
		add(panel_1);
		
		panel_2 = new PanelButtonSettings("JujutsuKaisenThemePressed.png", "JujutsuKaisenThemePressed.png", 3);
		if(BoardUtils.theme.equals(GUIUtils.DEFAULT_THEME_JUJUTSU_KAISEN)) {
			panel_2.switchImage();
		}
		panel_2.setBounds(144, 244, 75, 75);
		add(panel_2);
		
		
		panel_3 = new PanelButtonSettings("AOTThemePressed.png", "AOTThemePressed.png", 4);
		panel_3.setBounds(274, 244, 74, 100);
		add(panel_3);
// 
//		PanelButtonSettings panel_1_3 = new PanelButtonSettings("checkButtonUnpressedSettings.png", "checkButtonPressedSettings.png", 5);
//		panel_1_3.setBounds(403, 244, 75, 75);
//		add(panel_1_3);
		
		setVisible(true);
	}
	
	
}