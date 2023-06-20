	/**
 * @author Simon Greiderer 
 * @author Marc Underrain
 * 
 * Superklasse Feld, von dem anschlieﬂend geerbt werden kann
 * 
 * Sie besitzt Basisinformation ueber Felder des Schachbretts
 */
package game.logik.board;

import java.util.HashMap;
import java.util.Map;

import game.logik.figuren.Figur;

public abstract class Feld {

	protected final int feldKoordinate;

	private static final Map<Integer, LeeresFeld> LEERE_FELDER = alleLeereFelder();

	/**
	 * 
	 * @param feldKoordinate
	 */
	private Feld(final int feldKoordinate) {
		this.feldKoordinate = feldKoordinate;
	}

	public abstract boolean isFeldBesetzt();

	public abstract Figur getFigur();

	// erstelle alle Felder, welche nicht besetzt sind
	private static Map<Integer, LeeresFeld> alleLeereFelder() {

		final Map<Integer, LeeresFeld> leereFelder = new HashMap<Integer, LeeresFeld>();

		for (int i = 0; i < BoardUtils.ANZAHL_FELDER; i++) {
			leereFelder.put(i, new LeeresFeld(i));
		}

		return leereFelder;
	}

	/*
	 * Der Konstruktor ist private => wenn ein neues Feld erstellt werden soll, soll
	 * diese Methode aufgerufen werden.
	 */
	public static Feld erstelleFeld(final int feldKoordinate, final Figur figur) {
		return figur != null ? new BesetztesFeld(feldKoordinate, figur) : LEERE_FELDER.get(feldKoordinate);

	}

	public static final class LeeresFeld extends Feld {

		private LeeresFeld(final int feldKoordinate) {
			super(feldKoordinate);
		}

		@Override
		public String toString() {
			return "-"; 
		}
		
		@Override
		public boolean isFeldBesetzt() {
			return false;
		}

		@Override
		public Figur getFigur() {
			return null;
		}
	}

	public static final class BesetztesFeld extends Feld {

		private final Figur figurAufFeld;

		private BesetztesFeld(int feldKoordinate, final Figur figurAufFeld) {
			super(feldKoordinate);
			this.figurAufFeld = figurAufFeld;
		}

		@Override
		public String toString() {
			return getFigur().getFigurenFarbe().isBlack() ? getFigur().toString().toLowerCase() : getFigur().toString();  
		}
		
		@Override
		public boolean isFeldBesetzt() {
			return true;
		}

		@Override
		public Figur getFigur() {
			return this.figurAufFeld;
		}

	}

	public int getFeldKoordinate() {
		return feldKoordinate;
	}
}
