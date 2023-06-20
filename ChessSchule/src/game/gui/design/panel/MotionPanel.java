/**
 * @author Simon Greiderer 
 * @author Marc Underrain
 * 
 * Klasse wird verwendet, um ein Panel durch die Mausbewegung 
 * verschieben zu koennen. 
 * 
 * Auﬂerdem wird auch ein Hintergrundbild gezeichnet
 */
package game.gui.design.panel;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

import game.gui.GUIUtils;

@SuppressWarnings("serial")
public class MotionPanel extends JPanel{
    private Point initialClick;
    private JFrame parent;
    private String path = ""; 
	private Image image; 

    public MotionPanel(final JFrame parent){
    	this.parent = parent;
    	addMouseListener();
    }
    
    public MotionPanel(final JFrame parent, final String path) {
	
    	this.parent = parent; 
    	this.path = GUIUtils.DEFAULT_BACKGROUND_IMAGE_PATH + path; 
    	addMouseListener();
    	
    	try {				
			ImageIcon img = new ImageIcon(this.path);
			image = img.getImage();
		} catch (NullPointerException e) {
			System.out.println("Fehler beim Bild");
		}
    }
    
    public void addMouseListener() {
    	addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                initialClick = e.getPoint();
                getComponentAt(initialClick);
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {

                // get location of Window
                int thisX = parent.getLocation().x;
                int thisY = parent.getLocation().y;

                // Determine how much the mouse moved since the initial click
                int xMoved = e.getX() - initialClick.x;
                int yMoved = e.getY() - initialClick.y;

                // Move window to this position
                int X = thisX + xMoved;
                int Y = thisY + yMoved;
                parent.setLocation(X, Y);
            }
        });	
    }
 
    public void changeImage(String path) {
    	this.path = GUIUtils.DEFAULT_BACKGROUND_IMAGE_PATH + path; 
    	try {				
			ImageIcon img = new ImageIcon(this.path);
			image = img.getImage();
		} catch (NullPointerException e) {
			System.out.println("Fehler beim Bild");
		}
    	repaint(); 
    }
    
    @Override
	protected void paintComponent(Graphics g) {
		if(!path.isEmpty()) {
			super.paintComponent(g);
			g.drawImage(image, 0, 0, this); 
		}
	}
}
