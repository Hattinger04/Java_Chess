package game.gui.design.panel.buttons.subclasses;

import javax.swing.SwingUtilities;

import game.data.Client;
import game.gui.GUIUtils;
import game.gui.brettAllerBretter.GameFrameOnline;
import game.gui.design.panel.buttons.PanelButton;
import game.logik.Farbe;
import game.sound.PlaySound;

@SuppressWarnings("serial")
public class PanelButtonOnline extends PanelButton {

	GameFrameOnline frame;

	public PanelButtonOnline(String path1, String path2) {
		super(path1, path2);
	}

	@Override
	public void doWhenClicked() {

		Client.sendRankedGame(Client.getUsername(), "1");

		if (!GUIUtils.isStart1Online) {
			GameFrameOnline.setGameFrame();
		}
		frame = GameFrameOnline.get();
		GUIUtils.isStart1Online = false;
		PanelButtonStart.isPanel1Active = false;
		PlaySound.pause();
		frame.updateGegner(Client.isStarten() ? Farbe.WHITE : Farbe.BLACK);

		if (super.getPath1().equals(GUIUtils.DEFAULT_BUTTONS_IMAGE_PATH + "RankedBlitz.png")) {

		}
		if (super.getPath1().equals(GUIUtils.DEFAULT_BUTTONS_IMAGE_PATH + "RankedRapid.png")) {

		}
		if (super.getPath1().equals(GUIUtils.DEFAULT_BUTTONS_IMAGE_PATH + "RankedClassic.png")) {

		}

		SwingUtilities.getWindowAncestor(this).dispose();

	}

}
