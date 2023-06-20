/**
 * @author Simon Greiderer 
 * @author Marc Underrain
 * 
 */
package game.gui.design.panel.innerPanel;


import game.data.Client;
import game.gui.GUIUtils;
import game.gui.design.panel.ContentPanel;
import game.gui.design.panel.buttons.subclasses.PanelButtonOnline;

import javax.swing.JLabel;

@SuppressWarnings("serial")
public class OnlinePanel extends ContentPanel {

	/**
	 * Create the panel.
	 */
	public OnlinePanel(String path) {
		super(path); 
		setBounds(0, 0, 500, 400);
		setOpaque(false);
		setLayout(null);

		JLabel lbl_Online = new JLabel("Online");
		lbl_Online.setFont(GUIUtils.font);
		lbl_Online.setBounds(180, 15, 120, 50);
		add(lbl_Online);
		

		if(Client.isLoggedIn()) {
			
			PanelButtonOnline panel_ranked1 = new PanelButtonOnline("RankedBlitz.png", "RankedBlitz.png");
			panel_ranked1.setBounds(23, 175, 120, 120);
			add(panel_ranked1);
			
			PanelButtonOnline panel_ranked2 = new PanelButtonOnline("RankedRapid.png", "RankedRapid.png");
			panel_ranked2.setBounds(179, 175, 120, 120);
			add(panel_ranked2);
			
			PanelButtonOnline panel_ranked3 = new PanelButtonOnline("RankedClassic.png", "RankedClassic.png");
			panel_ranked3.setBounds(335, 175, 120, 120);
			add(panel_ranked3);
			
			JLabel lbl_bullet = new JLabel("Blitz");
			lbl_bullet.setFont(GUIUtils.font);
			lbl_bullet.setBounds(68, 110, 98, 26);
			add(lbl_bullet);
			
			JLabel lbl_rapid = new JLabel("Rapid");
			lbl_rapid.setFont(GUIUtils.font);
			lbl_rapid.setBounds(207, 110, 98, 26);
			add(lbl_rapid);
			
			JLabel lbl_classical = new JLabel("Classical");
			lbl_classical.setFont(GUIUtils.font);
			lbl_classical.setToolTipText("");
			lbl_classical.setBounds(362, 110, 98, 26);
			add(lbl_classical);
		} else {
			JLabel lbl_Meldung = new JLabel("Du bist nicht eingeloggt");
			lbl_Meldung.setFont(GUIUtils.font);
			lbl_Meldung.setBounds(20, 80, 460, 100);
			add(lbl_Meldung);
			
			JLabel lblMeldung2 = new JLabel("Du musst dich einloggen");
			lblMeldung2.setFont(GUIUtils.font);
			lblMeldung2.setBounds(20, 180, 460, 80);
			add(lblMeldung2);

			
			JLabel lblMeldung3 = new JLabel("um Online Funktionen zu verwenden.");
			lblMeldung3.setFont(GUIUtils.font);
			lblMeldung3.setBounds(20, 280, 460, 80);
			add(lblMeldung3);

		}
	}
}
