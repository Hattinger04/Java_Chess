/**
 * @author Simon Greiderer 
 * @author Marc Underrain
 * 
 * In dieser Klasse wird dem Algorithmus seine 
 * Werte gegeben
 * 
 */
package game.logik.player.ai;

import game.logik.board.Board;
import game.logik.figuren.Figur;
import game.logik.figuren.Figur.FigurenTyp;
import game.logik.player.Player;

public final class StandardBoardEvauator implements BoardEvaluator {

	private static final int SCHACH_BONUS = 50; 
	private static final int SCHACHMATT_BONUS = 10000; // Bester Zug, kann man sich als unendlich vorstellen
	private static final int DEPTH_BONUS = 100; 
	private static final int ROCHIERT_BONUS = 65; 
	private final static int TWO_BISHOPS_BONUS = 25;
	
	@Override
	public int evualuate(Board board, int depth) {
		return scorePlayer(board, board.whitePlayer(), depth) - scorePlayer(board, board.blackPlayer(), depth);
	}

	private int scorePlayer(Board board, Player player, int depth) {
		return figurenWert(player) + mobility(player) + schachmatt(player, depth) + rochiert(player) + schach(player);
	}

	private static int rochiert(Player player) {
		return player.isRochiert() ? ROCHIERT_BONUS : 0;
	}

	private static int schachmatt(Player player, int depth) {
		return player.getGegner().isSchachMatt() ? SCHACHMATT_BONUS * depthBonus(depth) : schach(player);
	}

	/**
	 * @param depth
	 * @return
	 * 
	 * Ein Zug in 5 Zuegen mit einer Bewertung von x ist besser
	 * als ein Zug in 2 mit einer Bewertung von x
	 * 
	 */
	private static int depthBonus(int depth) {
		return depth == 0 ? 1 : DEPTH_BONUS * depth;
	}

	private static int schach(Player player) {
		return player.getGegner().isSchach() ? SCHACH_BONUS : 0;
	}

	
	private static int mobility(Player player) {
		return player.getLegalMoves().size();
	}

	private static int figurenWert(final Player player) {
		int figurenWertScore = 0; 
		int anzBishop = 0; 
		for(final Figur figur : player.getAktiveFiguren()) {
			figurenWertScore += figur.getFigurenWert() + figur.locationBonus(); 
			if(figur.getFigurenTyp() == FigurenTyp.BISHOP) {
				anzBishop++; 
			}
		}
		return figurenWertScore + (anzBishop == 2 ? TWO_BISHOPS_BONUS : 0); 
	}
	
}
