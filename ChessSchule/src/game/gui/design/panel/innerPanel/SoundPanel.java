/**
 * @author Simon Greiderer 
 * @author Marc Underrain
 * 
 */
package game.gui.design.panel.innerPanel;

import game.gui.GUIUtils;
import game.gui.design.panel.ContentPanel;
import game.gui.design.panel.buttons.subclasses.PanelButtonSound;
import game.logik.board.BoardUtils;
import game.sound.PlaySound;


import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

@SuppressWarnings("serial")
public class SoundPanel extends ContentPanel {

	private JSlider slider_1, slider_2; 	
	
	public SoundPanel(String path) {
		super(path);
		GUIUtils.style();
		setBounds(0, 0, 500, 400);
		setLayout(null);
		setOpaque(false);

		JLabel lbl_Sound = new JLabel("Sound");
		lbl_Sound.setFont(GUIUtils.font);
		lbl_Sound.setBounds(180, 15, 100, 27);
		add(lbl_Sound);

		JLabel lbl_Active = new JLabel("Sound: ");
		lbl_Active.setFont(GUIUtils.font);
		lbl_Active.setBounds(30, 83, 250, 27);
		add(lbl_Active);

		PanelButtonSound panel = new PanelButtonSound("checkButtonUnpressedSettings.png",
				"checkButtonPressedSettings.png", 1);
		if (BoardUtils.playMusic) {
			panel.switchImage();
		}
		panel.setBounds(303, 67, 55, 55);
		add(panel);

		JLabel lbl_Active_1 = new JLabel("Soundeffekte: ");
		lbl_Active_1.setFont(GUIUtils.font);
		lbl_Active_1.setBounds(30, 261, 250, 27);
		add(lbl_Active_1);

		PanelButtonSound panel_1 = new PanelButtonSound("checkButtonUnpressedSettings.png",
				"checkButtonPressedSettings.png", 2);
		if (BoardUtils.playSoundEffects) {
			panel_1.switchImage();
			
		}
		panel_1.setBounds(303, 252, 55, 55);
		add(panel_1);

		slider_1 = new JSlider();
		slider_1.setBounds(225, 150, 236, 26);
		slider_1.setValue((int) Math.round(BoardUtils.volumePlaylist * 100));
		slider_1.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {				
				BoardUtils.volumePlaylist = Double.valueOf(slider_1.getValue()) / 100; 
				PlaySound.setVolumePlaylist(slider_1.getValue());
				repaint();
			}
		});
		slider_1.setFocusable(false);
		slider_1.setOpaque(false);
		add(slider_1);

		JLabel lbl_volume1 = new JLabel("Lautstaerke:");
		lbl_volume1.setFont(GUIUtils.font);
		lbl_volume1.setBounds(30, 150, 167, 27);
		add(lbl_volume1);

		JLabel lbl_volume2 = new JLabel("Lautstaerke: ");
		lbl_volume2.setFont(GUIUtils.font);
		lbl_volume2.setBounds(30, 330, 167, 27);
		add(lbl_volume2);

		slider_2 = new JSlider();
		slider_2.setBounds(225, 330, 236, 26);
		slider_2.setValue((int) Math.round(BoardUtils.volumeIngameSounds * 100));
		slider_2.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				BoardUtils.volumeIngameSounds = Double.valueOf(slider_2.getValue()) / 100; 
				PlaySound.setVolumeIngameSounds(slider_2.getValue());
				repaint();
			}
		});
		slider_2.setFocusable(false);
		slider_2.setOpaque(false);
		add(slider_2);
	}
	
	public JSlider getSlider_1() {
		return slider_1;
	}
	
	public JSlider getSlider_2() {
		return slider_2;
	}
}
