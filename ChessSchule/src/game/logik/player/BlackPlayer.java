/**
 * @author Simon Greiderer 
 * @author Marc Underrain
 * 
 */
package game.logik.player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import game.logik.Farbe;
import game.logik.board.Board;
import game.logik.board.Feld;
import game.logik.board.Move;
import game.logik.board.Move.KurzeRochadeMove;
import game.logik.board.Move.LangeRochadeMove;
import game.logik.figuren.Figur;
import game.logik.figuren.Rook;

public class BlackPlayer extends Player {

	public BlackPlayer(Board board, Collection<Move> legaleWeisseMoves, Collection<Move> legaleSchwarzeMoves) {
		super(board, legaleSchwarzeMoves, legaleWeisseMoves); 
	}

	@Override
	public Collection<Figur> getAktiveFiguren() {
		return this.board.getSchwarzeFiguren(); 
	}

	@Override
	public Farbe getFarbe() {
		return Farbe.BLACK;
	}

	@Override
	public Player getGegner() {
		return this.board.whitePlayer();
	}

	/**
	 * Berechne, ob der Koenig rochieren kann
	 */
	@Override
	public Collection<Move> berechneKingRochade(Collection<Move> playerLegals, Collection<Move> opponentLegals) {

		final List<Move> kingRochade = new ArrayList<Move>(); 
		
		if(!koennteRochieren()) {
			return Collections.emptyList(); 
		}
		
		if(this.getPlayerKing().isErsterMove() && !this.isSchach()) {
			if(!this.board.getFeld(5).isFeldBesetzt() && !this.board.getFeld(6).isFeldBesetzt()) {
				final Feld rookFeld = this.board.getFeld(7); 
				if(rookFeld.isFeldBesetzt() && rookFeld.getFigur().isErsterMove()) {
					if(Player.berechneAngriffAufFeld(5, opponentLegals).isEmpty() 
							&& Player.berechneAngriffAufFeld(6, opponentLegals).isEmpty() 
							&& rookFeld.getFigur().getFigurenTyp().isRook()) {
						kingRochade.add(new KurzeRochadeMove(this.board, this.king, 6, (Rook) rookFeld.getFigur(), rookFeld.getFeldKoordinate(), 5)); 
					}
				}
			}
		
			if(!this.board.getFeld(1).isFeldBesetzt() && !this.board.getFeld(2).isFeldBesetzt() && !this.board.getFeld(3).isFeldBesetzt()) {
				final Feld rookFeld = this.board.getFeld(0); 
				if(rookFeld.isFeldBesetzt() && rookFeld.getFigur().isErsterMove() 
						&& Player.berechneAngriffAufFeld(2, opponentLegals).isEmpty() 
						&& Player.berechneAngriffAufFeld(3, opponentLegals).isEmpty() 
						&& rookFeld.getFigur().getFigurenTyp().isRook()) {
					kingRochade.add(new LangeRochadeMove(this.board, this.king, 2, (Rook) rookFeld.getFigur(), rookFeld.getFeldKoordinate(), 3)); 
				}
			}
		}
		
		return kingRochade; 
	}

}
