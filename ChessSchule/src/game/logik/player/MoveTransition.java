/**
 * @author Simon Greiderer 
 * @author Marc Underrain
 * 
 */
package game.logik.player;

import game.logik.board.Board;
import game.logik.board.Move;

public class MoveTransition {

	
	private final Board transitionBoard; 
	private final Move move;
	private final MoveStatus moveStatus;
	
	public MoveTransition(final Board transitionBoard, final Move move, final MoveStatus moveStatus) {
		this.transitionBoard = transitionBoard;
		this.move = move;
		this.moveStatus = moveStatus;
	}

	public MoveStatus getMoveStatus() {
		return this.moveStatus;
	}
	
	public Board getTransitionBoard() {
		return transitionBoard;
	}

	public Move getMove() {
		return move;
	}
	
	
}
