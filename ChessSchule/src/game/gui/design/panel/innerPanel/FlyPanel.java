/**
 * @author Simon Greiderer 
 * @author Marc Underrain
 * 
 * Panel, welches einfliegt und bei bestimmten+
 * Events triggert
 */
package game.gui.design.panel.innerPanel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.Timer;

import game.gui.GUIUtils;

public class FlyPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3771488981592429558L;
	private SlideContainer slideContainer = new SlideContainer();
	private String path;

	public FlyPanel(String path) {
		super();
		setOpaque(false);
		this.path = GUIUtils.DEFAULT_INFO_IMAGE_PATH + path;
		setLayout(new BorderLayout());
		add(slideContainer, BorderLayout.CENTER);

		JLabel helloLabel = new JLabel();
		helloLabel.setIcon(new ImageIcon(this.path));
		slideContainer.add(helloLabel);
	}

	
	public void reset(String path) {
		this.path = GUIUtils.DEFAULT_INFO_IMAGE_PATH + path;
		JLabel helloLabel = new JLabel();
		helloLabel.setIcon(new ImageIcon(this.path));
		slideContainer.add(helloLabel);
	}
}

class SlideContainer extends JLayeredPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4937478130011058123L;
	private static final int PREF_W = 180;
	private static final int PREF_H = 50;
	private static final int SLIDE_IN_DELAY = 12;
	private static final int SLIDE_OUT_DELAY = 12;
	private static final int BETWEEN_DELAY = 750;
	protected static final int DELTA_X = 2;
	Component oldComponent;

	public SlideContainer() {
		setLayout(null);
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(PREF_W, PREF_H);
	}

	@Override
	public Component add(Component comp) {
		Component[] comps = getComponents();
		if (comps.length > 0) {
			oldComponent = comps[0];
		}
		if (oldComponent == comp) {
			return super.add(comp);
		}
		if (oldComponent != null) {
			putLayer((JComponent) oldComponent, JLayeredPane.DEFAULT_LAYER);
		}
		Component returnResult = super.add(comp);
		putLayer((JComponent) comp, JLayeredPane.DRAG_LAYER);
		comp.setSize(getPreferredSize());
		comp.setVisible(true);
		comp.setLocation(getPreferredSize().width, 0);
		slideFromRight(comp, oldComponent);
		return returnResult;
	}

	// Panel fliegt rein
	private void slideFromRight(final Component comp, final Component oldComponent2) {
		new Timer(SLIDE_IN_DELAY, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent aEvt) {
				int x = comp.getX();
				if (x <= 0) {
					slideBack(comp, oldComponent2);
					comp.setLocation(0, 0);
					putLayer((JComponent) comp, JLayeredPane.DEFAULT_LAYER);
					if (oldComponent2 != null) {
						remove(oldComponent2);
					}
					((Timer) aEvt.getSource()).stop();
				} else {
					x -= DELTA_X;
					comp.setLocation(x, 0);
				}
				repaint();
			}
		}).start();
	}


	// Panel wartet bestimmte Zeit und fliegt dann nach rechts raus
	private void slideBack(final Component comp, final Component oldComponent2) {
	    long Time0 = System.currentTimeMillis();

		new Timer(SLIDE_OUT_DELAY, new ActionListener() {
		    long Time1 = 0;
		    long runTime = 0;
			@Override
			public void actionPerformed(ActionEvent aEvt) {
				if(runTime > BETWEEN_DELAY) {
					int x = comp.getX();
					if (x >= 150) {
						comp.setLocation(150, 50);
						putLayer((JComponent) comp, JLayeredPane.DEFAULT_LAYER);
						if (oldComponent2 != null) {
							remove(oldComponent2);
						}
						((Timer) aEvt.getSource()).stop();
					} else {
						x += DELTA_X;
						comp.setLocation(x, 0);
					}
					repaint();
				} else {
			        Time1 = System.currentTimeMillis();
			        runTime = Time1 - Time0;
				}
			}
		}).start();
	}
}
