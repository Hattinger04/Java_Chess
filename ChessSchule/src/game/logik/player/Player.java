/**
 * @author Simon Greiderer 
 * @author Marc Underrain
 * 
 */
package game.logik.player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


import game.logik.Farbe;
import game.logik.board.Board;
import game.logik.board.Move;
import game.logik.figuren.Figur;
import game.logik.figuren.King;

public abstract class Player {

	protected final Board board;
	protected final King king;
	protected final Collection<Move> legaleMoves;
	protected final boolean isInSchach;

	/**
	 * 
	 * @param board
	 * @param legaleMoves
	 * @param gegnerMoves
	 */
	Player(final Board board, final Collection<Move> legaleMoves, final Collection<Move> gegnerMoves) {
		this.board = board;
		this.king = establishKing();
		this.isInSchach = !Player.berechneAngriffAufFeld(this.king.getFigurPosition(), gegnerMoves).isEmpty();
		legaleMoves.addAll(berechneKingRochade(legaleMoves, gegnerMoves));
		this.legaleMoves = legaleMoves;
	}

	public King getPlayerKing() {
		return this.king;
	}

	public boolean isKurzRochiert() {
		return this.king.isKurzRochiert();
	}

	public boolean isLangRochiert() {
		return this.king.isLangRochiert();
	}

	public Collection<Move> getLegalMoves() {
		return this.legaleMoves;
	}

	/**
	 * @param figurenPosition
	 * @param gegnerMoves
	 * @return
	 * 
	 * Berechne alle Angriffsmove des Spielers
	 */
	protected static Collection<Move> berechneAngriffAufFeld(int figurenPosition, Collection<Move> gegnerMoves) {
		final List<Move> angriffsMoves = new ArrayList<Move>();

		for (final Move move : gegnerMoves) {
			if (figurenPosition == move.getPositionFeld()) {
				angriffsMoves.add(move);
			}
		}
		return angriffsMoves;
	}

	// Bekomme den Koenig aus allen Figuren
	private King establishKing() {

		for (final Figur figur : getAktiveFiguren()) {
			if (figur.getFigurenTyp().isKing()) {
				return (King) figur;
			}
		}
		throw new RuntimeException("Sollte niemals erreicht werden! Es muss ein Koenig vorhanden sein!");
	}

	public abstract Collection<Figur> getAktiveFiguren();

	public boolean isMoveLegal(final Move move) {
		return this.legaleMoves.contains(move);
	}

	public boolean isSchach() {
		return this.isInSchach;
	}

	
	public boolean isSchachMatt() {
		return this.isInSchach && !hasFluchtMove();
	}

	protected boolean hasFluchtMove() {
		for (final Move move : this.legaleMoves) {
			final MoveTransition transition = makeMove(move);
			if (transition.getMoveStatus().isDone()) {
				return true;
			}
		}
		return false;
	}

	public boolean isPatt() {
		return !this.isInSchach && !hasFluchtMove();
	}

	public boolean isRochiert() {
		return false;
	}

	public MoveTransition makeMove(final Move move) {

		if (!isMoveLegal(move)) {
			return new MoveTransition(this.board, move, MoveStatus.ILLEGAL_MOVE);
		}

		final Board transitionBoard = move.ausfuehren();

		final Collection<Move> kingAngriff = Player.berechneAngriffAufFeld(
				transitionBoard.currentPlayer().getGegner().getPlayerKing().getFigurPosition(),
				transitionBoard.currentPlayer().getLegalMoves());

		if (!kingAngriff.isEmpty()) {
			return new MoveTransition(this.board, move, MoveStatus.LEAVES_PLAYER_IN_CHECK);
		}
		return new MoveTransition(transitionBoard, move, MoveStatus.DONE);
	}

	protected boolean koennteRochieren() {
		return !this.isInSchach && !this.king.isRochiert()
				&& (this.king.isKurzRochiert() || this.king.isLangRochiert());
	}

	public abstract Farbe getFarbe();

	public abstract Player getGegner();

	public abstract Collection<Move> berechneKingRochade(Collection<Move> playerLegals,
			Collection<Move> opponentLegals);
}
