package game.gui.design.panel.innerPanel;


import game.data.Client;
import game.gui.GUIUtils;
import game.gui.design.panel.ContentPanel;
import game.gui.design.panel.buttons.subclasses.PanelButtonAccount;

import java.awt.Color;

import javax.swing.JLabel;

@SuppressWarnings("serial")
public class AccountPanel extends ContentPanel {

	/**
	 * Create the panel.
	 */
	public AccountPanel(String path) {
		super(path); 
//		if(Client.isLoggedIn()) {
		
			String data = Client.sendAccountDataRequest(Client.getUsername()); 
			String[] datas = data.split("-"); 
		
			setLayout(null);
			setBounds(0, 0, 500, 400);
			setOpaque(false);
			
			PanelButtonAccount panel_login = new PanelButtonAccount("LogoutSendUnpressed.png", "LogoutSendPressed.png");
			panel_login.setBounds(10, 343, 180, 46);
			add(panel_login);
			
			JLabel lbl_name = new JLabel("Accountname:");
			lbl_name.setFont(GUIUtils.font);
			lbl_name.setBounds(21, 38, 208, 46);
			add(lbl_name);
			
			JLabel lbl_namevariable = new JLabel(Client.getUsername());
			lbl_namevariable.setForeground(Color.RED);
			lbl_namevariable.setFont(GUIUtils.font);
			lbl_namevariable.setBounds(250, 38, 240, 46);
			add(lbl_namevariable);
			
			JLabel lbl_points = new JLabel("Punkte:");
			lbl_points.setFont(GUIUtils.font);
			lbl_points.setBounds(21, 116, 208, 46);
			add(lbl_points);
			
			JLabel lbl_pointsvariable = new JLabel(datas[0]);
			lbl_pointsvariable.setForeground(Color.RED);
			lbl_pointsvariable.setFont(GUIUtils.font);
			lbl_pointsvariable.setBounds(250, 116, 240, 46);
			add(lbl_pointsvariable);
			
		
			JLabel lbl_games_variable = new JLabel(datas[1]);
			lbl_games_variable.setForeground(Color.RED);
			lbl_games_variable.setFont(GUIUtils.font);
			lbl_games_variable.setBounds(250, 184, 240, 46);
			add(lbl_games_variable);
			
			JLabel lbl_games = new JLabel("Spiele");
			lbl_games.setFont(GUIUtils.font);
			lbl_games.setBounds(21, 184, 208, 46);
			add(lbl_games);
			
			JLabel lbl_rank = new JLabel("Platzierung:");
			lbl_rank.setFont(GUIUtils.font);
			lbl_rank.setBounds(21, 254, 208, 46);
			add(lbl_rank);
			
			JLabel lbl_rank_variable = new JLabel(datas[2] + ".");
			lbl_rank_variable.setForeground(Color.RED);
			lbl_rank_variable.setFont(GUIUtils.font);
			lbl_rank_variable.setBounds(250, 254, 240, 46);
			add(lbl_rank_variable);

			
			setVisible(true);
//		}
			
	}
}
