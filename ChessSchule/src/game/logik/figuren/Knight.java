/**
 * @author Simon Greiderer 
 * @author Marc Underrain
 * 
 */
package game.logik.figuren;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import game.logik.Farbe;
import game.logik.board.Board;
import game.logik.board.BoardUtils;
import game.logik.board.Feld;
import game.logik.board.Move;
import game.logik.board.Move.NormalAttackMove;
import game.logik.board.Move.NormalMove;

public class Knight extends Figur {

	// Entfernung der Felder, in die sich der Springer bewegen kann => gilt nicht
	// immer!
	private final static int[] MOEGLICHE_MOVES_XY = { -17, -15, -10, -6, 6, 10, 15, 17 };

	public Knight(final int figurPosition, final Farbe figurFarbe) {
		super (figurPosition, figurFarbe, FigurenTyp.KNIGHT, true);
	}
	public Knight(final int figurPosition, final Farbe figurFarbe, boolean isErsterMove) {
		super (figurPosition, figurFarbe, FigurenTyp.KNIGHT, isErsterMove);
	}

	@Override
	public Collection<Move> berechneLegaleZuege(final Board board) {

		final List<Move> legaleMoves = new ArrayList<Move>();

		/*
		 * Wir gehen alle moeglichen Zuege durch und sehen nach, ob das jeweils
		 * moegliche feld auﬂerhalb des Brettes ist, oder bereits besetzt ist.
		 * 
		 * Falls dies nicht der Fall ist, wird der Wert in eine ArrayList gespeichert
		 * welche auch zurueckgegeben wird
		 */

		for (final int moeglicherMove : MOEGLICHE_MOVES_XY) {
			final int moeglicheKoordinate = this.figurPosition + moeglicherMove;
			
			if (BoardUtils.isVerfuegbareKoordinate(moeglicheKoordinate)) {
				// Ausnahmen:
				if (isAusnahmeErsteReihe(this.figurPosition, moeglicherMove)
						|| isAusnahmeZweiteReihe(this.figurPosition, moeglicherMove)
						|| isAusnahmeSiebteReihe(this.figurPosition, moeglicherMove)
						|| isAusnahmeAchteReihe(this.figurPosition, moeglicherMove)) {
					continue;
				}

				final Feld moeglichesFeld = board.getFeld(moeglicheKoordinate);

				if (!moeglichesFeld.isFeldBesetzt()) {
					legaleMoves.add(new NormalMove(board, this, moeglicheKoordinate));
				} else {
					final Figur figurAufPosition = moeglichesFeld.getFigur();
					final Farbe figurFarbe = figurAufPosition.getFigurenFarbe();

					if (this.figurFarbe != figurFarbe) {
						legaleMoves.add(new NormalAttackMove(board, this, moeglicheKoordinate, figurAufPosition));
					}

				}
			}
		}

		return legaleMoves;
	}

	@Override
	public String toString() {
		return FigurenTyp.KNIGHT.toString(); 
	}
	
	
	// Ausnahmen, wo sich der Springer (nicht) hinbewegen darf:

	private static boolean isAusnahmeErsteReihe(final int currentPosition, final int moeglichePosition) {
		return BoardUtils.ERSTE_SPALTE[currentPosition] && ((moeglichePosition == -17) || (moeglichePosition == -10)
				|| (moeglichePosition == 6) || (moeglichePosition == 15));
	}

	private static boolean isAusnahmeZweiteReihe(final int currentPosition, final int moeglichePosition) {
		return BoardUtils.ZWEITE_SPALTE[currentPosition] && ((moeglichePosition == -10) || (moeglichePosition == 6));
	}

	private static boolean isAusnahmeSiebteReihe(final int currentPosition, final int moeglichePosition) {
		return BoardUtils.SIEBTE_SPALTE[currentPosition] && ((moeglichePosition == -6) || (moeglichePosition == 10));
	}

	private static boolean isAusnahmeAchteReihe(final int currentPosition, final int moeglichePosition) {
		return BoardUtils.ACHTE_SPALTE[currentPosition] && ((moeglichePosition == -15) || (moeglichePosition == -6)
				|| (moeglichePosition == 10) || (moeglichePosition == 17));
	}
	

	@Override
	public Knight moveFigur(final Move move) {
		return new Knight(move.getPositionFeld(), move.getFigur().getFigurenFarbe());
	}
	
	@Override
	public int locationBonus() {
		return this.figurFarbe.knightBonus(this.figurPosition); 
	}

}
