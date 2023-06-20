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

public class WhitePlayer extends Player {

	/**
	 * 
	 * @param board
	 * @param legaleWeisseMoves
	 * @param legaleSchwarzeMoves
	 */
	public WhitePlayer(Board board, Collection<Move> legaleWeisseMoves, Collection<Move> legaleSchwarzeMoves) {
		super(board, legaleWeisseMoves, legaleSchwarzeMoves); 

	}

	@Override
	public Collection<Figur> getAktiveFiguren() {
		return this.board.getWeisseFiguren();
	}

	@Override
	public Farbe getFarbe() {
		return Farbe.WHITE;
	}

	@Override
	public Player getGegner() {
		return this.board.blackPlayer();
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
			if(!this.board.getFeld(61).isFeldBesetzt() && !this.board.getFeld(62).isFeldBesetzt()) {
				final Feld rookFeld = this.board.getFeld(63); 
				if(rookFeld.isFeldBesetzt() && rookFeld.getFigur().isErsterMove()) {
					if(Player.berechneAngriffAufFeld(61, opponentLegals).isEmpty() 
							&& Player.berechneAngriffAufFeld(62, opponentLegals).isEmpty() 
							&& rookFeld.getFigur().getFigurenTyp().isRook()) {
						kingRochade.add(new KurzeRochadeMove(this.board, this.king, 62, (Rook) rookFeld.getFigur(), rookFeld.getFeldKoordinate(), 61)); 
					}
				}
			}
		
			if(!this.board.getFeld(59).isFeldBesetzt() && !this.board.getFeld(58).isFeldBesetzt() && !this.board.getFeld(57).isFeldBesetzt()) {
				final Feld rookFeld = this.board.getFeld(56); 
				if(rookFeld.isFeldBesetzt() && rookFeld.getFigur().isErsterMove() 
						&& Player.berechneAngriffAufFeld(58, opponentLegals).isEmpty()
						&& Player.berechneAngriffAufFeld(59, opponentLegals).isEmpty() 
						&& rookFeld.getFigur().getFigurenTyp().isRook()) {
					kingRochade.add(new LangeRochadeMove(this.board, this.king, 58, (Rook)rookFeld.getFigur(), rookFeld.getFeldKoordinate(), 59)); 
				}
			}
		}
		
		return kingRochade;
	}

}
