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

public class King extends Figur {

	private final static int[] MOEGLICHE_MOVES_XY = { -9, -8, -7, -1, 1, 7, 8, 9 };
	private final boolean kurzeRochade;
	private final boolean langeRochade;
	private final boolean isRochiert;

	public King(int figurPosition, Farbe figurFarbe, final boolean kurzeRochade, final boolean langeRochade) {
		super(figurPosition, figurFarbe, FigurenTyp.KING, true);
		this.kurzeRochade = kurzeRochade;
		this.langeRochade = langeRochade;
		this.isRochiert = false;
	}

	public King(int figurPosition, Farbe figurFarbe, boolean isErsterMove, final boolean isRochiert,
			final boolean kurzeRochade, final boolean langeRochade) {
		super(figurPosition, figurFarbe, FigurenTyp.KING, isErsterMove);
		this.kurzeRochade = kurzeRochade;
		this.langeRochade = langeRochade;
		this.isRochiert = isRochiert;

	}

	@Override
	public Collection<Move> berechneLegaleZuege(Board board) {
		final List<Move> legaleMoves = new ArrayList<Move>();

		for (final int moeglicherMove : MOEGLICHE_MOVES_XY) {
			final int moeglicheKoordinate = this.figurPosition + moeglicherMove;

			if (isAusnahmeErsteReihe(this.figurPosition, moeglicherMove)
					|| isAusnahmeAchteReihe(this.figurPosition, moeglicherMove)) {
				continue;
			}

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

				}
			}

		}

		return legaleMoves;
	}

	@Override
	public String toString() {
		return FigurenTyp.KING.toString();
	}

	private static boolean isAusnahmeErsteReihe(final int currentPosition, final int moeglichePosition) {
		return BoardUtils.ERSTE_SPALTE[currentPosition]
				&& ((moeglichePosition == -9) || (moeglichePosition == -1) || (moeglichePosition == 7));
	}

	private static boolean isAusnahmeAchteReihe(final int currentPosition, final int moeglichePosition) {
		return BoardUtils.ACHTE_SPALTE[currentPosition]
				&& ((moeglichePosition == -7) || (moeglichePosition == 1) || (moeglichePosition == 9));
	}

	@Override
	public King moveFigur(final Move move) {
		return new King(move.getPositionFeld(), move.getFigur().getFigurenFarbe(), false, move.isRochadeMove(), false,
				false);
	}

	public boolean isRochiert() {
		return this.isRochiert;
	}

	public boolean isKurzRochiert() {
		return this.kurzeRochade;
	}

	public boolean isLangRochiert() {
		return this.langeRochade;
	}
	
	@Override
	public int locationBonus() {
		return this.figurFarbe.kingBonus(this.figurPosition); 
	}
}
