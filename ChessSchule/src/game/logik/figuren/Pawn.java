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
import game.logik.board.Move;
import game.logik.board.Move.PawnAngriffMove;
import game.logik.board.Move.PawnEnPassantAngriffMove;
import game.logik.board.Move.PawnJump;
import game.logik.board.Move.PawnMove;
import game.logik.board.Move.PawnPromotion;

public class Pawn extends Figur {

	private final static int[] MOEGLICHE_MOVES_XY = { 8, 16, 7, 9 };

	public Pawn(final int figurPosition, final Farbe figurFarbe) {
		super(figurPosition, figurFarbe, FigurenTyp.PAWN, true);
	}

	public Pawn(int figurPosition, Farbe figurFarbe, boolean isErsterMove) {
		super(figurPosition, figurFarbe, FigurenTyp.PAWN, isErsterMove);
	}

	@Override
	public Collection<Move> berechneLegaleZuege(final Board board) {

		final List<Move> legaleMoves = new ArrayList<Move>();

		for (final int moeglicherMove : MOEGLICHE_MOVES_XY) {

			final int moeglicheKoordinate = this.figurPosition
					+ (this.getFigurenFarbe().getDirection() * moeglicherMove);

			if (!BoardUtils.isVerfuegbareKoordinate(moeglicheKoordinate)) {
				continue;
			}
			if (moeglicherMove == 8 && !board.getFeld(moeglicheKoordinate).isFeldBesetzt()) {

				if (this.figurFarbe.isPawnPromotionSquare(moeglicheKoordinate)) {
					legaleMoves.add(new PawnPromotion(new PawnMove(board, this, moeglicheKoordinate)));
				} else {
					legaleMoves.add(new PawnMove(board, this, moeglicheKoordinate));
				}
			} else if (moeglicherMove == 16 && this.isErsterMove()
					&& ((BoardUtils.SEVENTH_RANK[this.figurPosition] && this.getFigurenFarbe().isBlack())
							|| (BoardUtils.SECOND_RANK[this.figurPosition] && this.getFigurenFarbe().isWhite()))) {

				final int hinterMoeglicherKoordinate = this.figurPosition + (this.figurFarbe.getDirection() * 8);
				if (!board.getFeld(hinterMoeglicherKoordinate).isFeldBesetzt()
						&& !board.getFeld(moeglicheKoordinate).isFeldBesetzt()) {
					legaleMoves.add(new PawnJump(board, this, moeglicheKoordinate));
				}
			} else if (moeglicherMove == 7
					&& !((BoardUtils.ACHTE_SPALTE[this.figurPosition] && this.getFigurenFarbe().isWhite())
							|| (BoardUtils.ERSTE_SPALTE[this.figurPosition] && this.getFigurenFarbe().isBlack()))) {
				if (board.getFeld(moeglicheKoordinate).isFeldBesetzt()) {
					final Figur figurAufFeld = board.getFeld(moeglicheKoordinate).getFigur();
					if (this.figurFarbe != figurAufFeld.getFigurenFarbe()) {
						if (this.figurFarbe.isPawnPromotionSquare(moeglicheKoordinate)) {
							legaleMoves.add(new PawnPromotion(
									new PawnAngriffMove(board, this, moeglicheKoordinate, figurAufFeld)));
						} else {
							legaleMoves.add(new PawnAngriffMove(board, this, moeglicheKoordinate, figurAufFeld));
						}
					}
				} else if (board.getEnPassantPawn() != null) {
					if (board.getEnPassantPawn()
							.getFigurPosition() == (this.figurPosition + (this.figurFarbe.getOppositeDirection()))) {
						final Figur figurAufFeld = board.getEnPassantPawn();
						if (this.figurFarbe != figurAufFeld.getFigurenFarbe()) {
							legaleMoves
									.add(new PawnEnPassantAngriffMove(board, this, moeglicheKoordinate, figurAufFeld));
						}
					}
				}

			} else if (moeglicherMove == 9
					&& !((BoardUtils.ERSTE_SPALTE[this.figurPosition] && this.getFigurenFarbe().isWhite())
							|| (BoardUtils.ACHTE_SPALTE[this.figurPosition] && this.getFigurenFarbe().isBlack()))) {
				if (board.getFeld(moeglicheKoordinate).isFeldBesetzt()) {
					final Figur figurAufFeld = board.getFeld(moeglicheKoordinate).getFigur();
					if (this.figurFarbe != figurAufFeld.getFigurenFarbe()) {
						if (this.figurFarbe.isPawnPromotionSquare(moeglicheKoordinate)) {
							legaleMoves.add(new PawnPromotion(
									new PawnAngriffMove(board, this, moeglicheKoordinate, figurAufFeld)));
						} else {
							legaleMoves.add(new PawnAngriffMove(board, this, moeglicheKoordinate, figurAufFeld));
						}
					}
				} else if (board.getEnPassantPawn() != null) {
					if (board.getEnPassantPawn()
							.getFigurPosition() == (this.figurPosition + (this.figurFarbe.getDirection()))) {
						final Figur figurAufFeld = board.getEnPassantPawn();
						if (this.figurFarbe != figurAufFeld.getFigurenFarbe()) {
							if (this.figurFarbe.isPawnPromotionSquare(moeglicheKoordinate)) {
								legaleMoves.add(new PawnPromotion(
										new PawnAngriffMove(board, this, moeglicheKoordinate, figurAufFeld)));
							} else {
								legaleMoves.add(
										new PawnEnPassantAngriffMove(board, this, moeglicheKoordinate, figurAufFeld));
							}
						}
					}
				}
			}
		}

		return legaleMoves;
	}

	@Override
	public String toString() {
		return FigurenTyp.PAWN.toString();
	}

	@Override
	public Pawn moveFigur(final Move move) {
		return new Pawn(move.getPositionFeld(), move.getFigur().getFigurenFarbe());
	}

	// TODO: immer zu Queen, muss ich noch ändern
	public Figur getPromotionFigur() {
		return new Queen(this.figurPosition, this.figurFarbe, false);
	}

	@Override
	public int locationBonus() {
		return this.figurFarbe.pawnBonus(this.figurPosition);
	}
}
