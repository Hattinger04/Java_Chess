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

public class Queen extends Figur {

	private final static int[] MOEGLICHE_MOVES_XY = {-9, -8, -7, -1, 1, 7, 8, 9};
	
	public Queen(int figurPosition, Farbe figurFarbe) {
		super (figurPosition, figurFarbe, FigurenTyp.QUEEN, true);
	}

	public Queen(int figurPosition, Farbe figurFarbe, boolean isErsterMove) {
		super (figurPosition, figurFarbe, FigurenTyp.QUEEN, isErsterMove);
	}

	@Override
	public Collection<Move> berechneLegaleZuege(final Board board) {

		final List<Move> legaleMoves = new ArrayList<Move>();

		for (final int moeglicherMove : MOEGLICHE_MOVES_XY) {

			int moeglicheKoordinate = this.figurPosition;

			while (BoardUtils.isVerfuegbareKoordinate(moeglicheKoordinate)) {

				if (isAusnahmeErsteReihe(moeglicheKoordinate, moeglicherMove)
						|| isAusnahmeAchteReihe(moeglicheKoordinate, moeglicherMove)) {
					break;
				}

				moeglicheKoordinate += moeglicherMove;
				if (BoardUtils.isVerfuegbareKoordinate(moeglicheKoordinate)) {
					final Feld moeglichesFeld = board.getFeld(moeglicheKoordinate);
					if (!moeglichesFeld.isFeldBesetzt()) {
						legaleMoves.add(new NormalMove(board, this, moeglicheKoordinate));
					} else {
						final Figur figurAufPosition = moeglichesFeld.getFigur();
						final Farbe figurFarbe = figurAufPosition.getFigurenFarbe();
						if (this.figurFarbe != figurFarbe) {
							legaleMoves.add(new NormalAttackMove(board, this, moeglicheKoordinate, figurAufPosition));
						}
						break;
					}
				}
			}

		}

		return legaleMoves;
	}

	
	@Override
	public String toString() {
		return FigurenTyp.QUEEN.toString(); 
	}
	
	private static boolean isAusnahmeErsteReihe(final int currentPosition, final int moeglichePosition) {
		return BoardUtils.ERSTE_SPALTE[currentPosition] && ((moeglichePosition == -1) || (moeglichePosition == -9) || (moeglichePosition == 7));
	}

	private static boolean isAusnahmeAchteReihe(final int currentPosition, final int moeglichePosition) {
		return BoardUtils.ACHTE_SPALTE[currentPosition] && ((moeglichePosition == -7) || (moeglichePosition == 1) || (moeglichePosition == 9));
	}


	@Override
	public Queen moveFigur(final Move move) {
		return new Queen(move.getPositionFeld(), move.getFigur().getFigurenFarbe());
	}
	
	@Override
	public int locationBonus() {
		return this.figurFarbe.queenBonus(this.figurPosition); 
	}

}
