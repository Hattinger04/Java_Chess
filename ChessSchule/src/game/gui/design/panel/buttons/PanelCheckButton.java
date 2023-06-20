/**
 * @author Simon Greiderer 
 * @author Marc Underrain
 * 
 * Oberklasse fuer alle Buttons; es wird ein Hintergrundbild 
 * gezeichnet und ein ActionListener fuer alle erstellt, welcher 
 * beim Click eine abstrakte Methode aufruft, welche jede Subklasse
 * ueberschreiben muss. 
 */
package game.gui.design.panel.buttons;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import game.gui.GUIUtils;

@SuppressWarnings("serial")
public abstract class PanelCheckButton extends JPanel{
	private String path1;
	private String path2;
	private Image image1; 
	private Image image2; 
	private boolean clicked = false; 

	
	public PanelCheckButton(String path1, String path2) {
		super(); 	
		this.path1 = GUIUtils.DEFAULT_BUTTONS_IMAGE_PATH + path1; 
		this.path2 = GUIUtils.DEFAULT_BUTTONS_IMAGE_PATH + path2; 
		setOpaque(false);
		try {
			ImageIcon img1 = new ImageIcon(this.path1);
			image1 = img1.getImage();

			ImageIcon img2 = new ImageIcon(this.path2);
			image2 = img2.getImage();

		} catch (NullPointerException e) {
			System.out.println("Fehler beim Bild");
		} 
		
		addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {}
			
			@Override
			public void mousePressed(MouseEvent e) {
				clicked = !clicked;
				repaint();
				doWhenClicked(clicked); 
			}
			
			@Override
			public void mouseExited(MouseEvent e) {}
			
			@Override
			public void mouseEntered(MouseEvent e) {}
			
			@Override
			public void mouseClicked(MouseEvent e) {

			}
		});
		setVisible(true);
	}
	
	public abstract void doWhenClicked(boolean active); 
	
	public String getPath1() {
		return path1;
	}
	
	public String getPath2() {
		return path2;
	}
	
	
	public void switchImage() {
		clicked = !clicked;
		repaint(); 
	}
	
	public boolean isClicked() {
		return clicked;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(!clicked) {
			g.drawImage(image1, 0, 0, this);	
		} else {
			g.drawImage(image2, 0, 0, this);	
		}
	}
}
