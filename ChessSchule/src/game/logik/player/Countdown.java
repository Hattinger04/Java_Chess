/**
 * @author Simon Greiderer 
 * @author Marc Underrain
 * 
 * Wird zurzeit noch nicht verwendet
 */

package game.logik.player;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.Timer;

import game.gui.brettAllerBretter.GameFrameOnline;



public class Countdown implements ActionListener {
	double remaining;
	long lastUpdate;
	long addition; 
	Timer timer; 
	
	private JLabel label1, label2; 
	
	
	public void init(final double remaining, final long addition, JLabel label1, JLabel label2) {
		this.remaining = remaining; // 600 000 sind 10 Minuten.	
		this.addition = addition; 
	
		this.label1 = label1; 
		this.label2 = label2; 

	    timer = new Timer(500, this);
		timer.setInitialDelay(0); 
		
		long sekunden = (long) ((remaining / 1000) % 60);
		long minuten = (long) ((remaining / (1000 * 60)) % 60);

		
		label1.setText(String.valueOf(minuten));
		label2.setText(String.valueOf(sekunden));
	}

	public void start() {
		resume();
	} 
	
	public void stop() {
		System.out.println("Stop");
		pause();
	} 	
	boolean isRunning() {
		return timer.isRunning(); 
	}
	
	void resume() {
		System.out.println("Start");
		lastUpdate = System.currentTimeMillis();
		timer.start(); // Start the timer
	}

	void pause() {
		long now = System.currentTimeMillis();
		remaining -= (now - lastUpdate);
		System.out.println(remaining);
		timer.stop(); // Stop the timer
	}

	void updateDisplay() {
		long now = System.currentTimeMillis(); 
		long elapsed = now - lastUpdate;
		remaining -= elapsed;
		lastUpdate = now;

		long sekunden = (long) ((remaining / 1000) % 60);
		long minuten = (long) ((remaining / (1000 * 60)) % 60);

		
		label1.setText(String.valueOf(minuten));
		label2.setText(String.valueOf(sekunden));
		
        if (remaining < 0) {
			remaining = 0;
			// Auf Lose setzten
			if(label1 == GameFrameOnline.get().getLabel_2_1()) {
				GameFrameOnline.get().setTimeOut(true);	
			}
        }
        
		if (remaining == 0) {
			System.out.println("Zeit abgelaufen");
			timer.stop();
		}
	}
	
	public void setRemaining(long remaining) {
		this.remaining = remaining;
	}
	
	public double getRemaining() {
		return remaining;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		updateDisplay();
	}
}