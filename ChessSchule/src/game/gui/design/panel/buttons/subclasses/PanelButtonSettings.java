/**
 * @author Simon Greiderer 
 * @author Marc Underrain
 * 
 */
package game.gui.design.panel.buttons.subclasses;


import game.data.FileSave;
import game.gui.GUIUtils;
import game.gui.design.StartFrame;
import game.gui.design.panel.buttons.PanelCheckButton;
import game.logik.board.BoardUtils;
import game.sound.PlaySound;

@SuppressWarnings("serial")
public class PanelButtonSettings extends PanelCheckButton {

	private int id;
	
	/**
	 * 
	 * @param path1
	 * @param path2
	 * @param id
	 */
	public PanelButtonSettings(String path1, String path2, int id) {
		super(path1, path2);
		this.id = id;
	}

	// Funktioniert noch nicht gut!
	@SuppressWarnings("static-access")
	@Override
	public void doWhenClicked(boolean active) {
		if (super.getPath1().equals(GUIUtils.DEFAULT_BUTTONS_IMAGE_PATH + "checkButtonUnpressedSettings.png")
				&& id == 1) {
			boolean highlightLegalMoves = active;
			BoardUtils.highlightLegalMoves = highlightLegalMoves;
			FileSave.saveUsersettings(BoardUtils.SETTINGS_FILE, BoardUtils.SETTINGS_LEGALMOVES,
					Boolean.toString(highlightLegalMoves));
		}

		if (super.getPath2().equals(GUIUtils.DEFAULT_BUTTONS_IMAGE_PATH + "DefaultThemePressed.png") && id == 2) {
			BoardUtils.theme = GUIUtils.DEFAULT_THEME_DEFAULT;
			GUIUtils.CURRENT_START_PAGE = "startPage.jpg";
			GUIUtils.CURRENT_START_PAGE_PANEL = "startPagePanel.jpg";
			BoardUtils.DEFAULT_FIGUREN_IMAGE_PATH = "./resources/images/theme1/"; 
			FileSave.saveUsersettings(BoardUtils.SETTINGS_FILE, BoardUtils.SETTINGS_THEME, BoardUtils.theme);
			StartFrame.refreshContentPane(GUIUtils.CURRENT_START_PAGE_PANEL);
			if(BoardUtils.playMusic) {
				PlaySound.end();
				new PlaySound().startPlaying();
			}
		}

		if (super.getPath2().equals(GUIUtils.DEFAULT_BUTTONS_IMAGE_PATH + "JujutsuKaisenThemePressed.png")
				&& id == 3) {
			BoardUtils.theme = GUIUtils.DEFAULT_THEME_JUJUTSU_KAISEN;
			GUIUtils.CURRENT_START_PAGE = "startPageJujutsuKaisen.jpg";
			GUIUtils.CURRENT_START_PAGE_PANEL = "startPagePanelJujutsuKaisen.jpg";
			BoardUtils.DEFAULT_FIGUREN_IMAGE_PATH = "./resources/images/theme2/"; 
			FileSave.saveUsersettings(BoardUtils.SETTINGS_FILE, BoardUtils.SETTINGS_THEME, BoardUtils.theme);
			StartFrame.refreshContentPane(GUIUtils.CURRENT_START_PAGE_PANEL);
			if(BoardUtils.playMusic) {
				PlaySound.end();
				new PlaySound().startPlaying();
			}
		}
		
		if (super.getPath2().equals(GUIUtils.DEFAULT_BUTTONS_IMAGE_PATH + "AOTThemePressed.png")) {
			BoardUtils.theme = GUIUtils.DEFAULT_THEME_AOT;
			GUIUtils.CURRENT_START_PAGE = "startPageAOT.jpg";
			GUIUtils.CURRENT_START_PAGE_PANEL = "startPagePanelAOT.jpg";
			BoardUtils.DEFAULT_FIGUREN_IMAGE_PATH = "./resources/images/theme3/"; 
			FileSave.saveUsersettings(BoardUtils.SETTINGS_FILE, BoardUtils.SETTINGS_THEME, BoardUtils.theme);
			StartFrame.refreshContentPane(GUIUtils.CURRENT_START_PAGE_PANEL);
			if(BoardUtils.playMusic) {
				PlaySound.end();
				new PlaySound().startPlaying();
			}

		}
	}
}