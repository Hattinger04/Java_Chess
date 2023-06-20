/**
 * @author Simon Greiderer 
 * @author Marc Underrain
 * 
 */
package game.logik.player.ai;

import game.logik.board.Board;

public interface BoardEvaluator {

	int evualuate(Board board, int depth); 
	
}
