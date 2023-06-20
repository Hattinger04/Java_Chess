/**
 * @author Simon Greiderer 
 * @author Marc Underrain
 * 
 */
package game.logik.player.ai;

import game.logik.board.Board;
import game.logik.board.Move;

public interface MoveStrategy {
	Move ausfuehren(Board board); 	
}
