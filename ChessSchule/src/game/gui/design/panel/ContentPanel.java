/**
 * @author Simon Greiderer 
 * @author Marc Underrain
 * 
 * Panel, welches normalerweise als Hintergrundpanel
 * verwendet wird, um Hintergrundbilder darzustellen
 */
package game.gui.design.panel;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import game.gui.GUIUtils;

@SuppressWarnings("serial")
public class ContentPanel extends JPanel{

	private Image image; 
	private String path = ""; 
	
	public ContentPanel(final String path) {
		super();
		setOpaque(false);
		this.path = GUIUtils.DEFAULT_BACKGROUND_IMAGE_PATH + path; 	
		try {
			ImageIcon img = new ImageIcon(path);
			image = img.getImage();
		} catch (NullPointerException e) {
			System.out.println("Fehler beim Bild");
		}
		setVisible(true);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		if (!path.isEmpty()) {
	 		super.paintComponent(g);
			g.drawImage(image, 0, 0, this);
		}
	}

}
