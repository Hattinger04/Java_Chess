/**
 * @author Simon Greiderer 
 * @author Marc Underrain
 * 
 */
package game.logik.player.ai;

import game.logik.board.Board;
import game.logik.board.Move;
import game.logik.player.MoveTransition;

public class MiniMax implements MoveStrategy{

	private final BoardEvaluator boardEvaluator; 
	private final int searchDepth; 
	
	/**
	 * 
	 * @param searchDepth
	 */
	public MiniMax(int searchDepth) {
		this.boardEvaluator = new StandardBoardEvauator();
		this.searchDepth = searchDepth; 
	}
	
	
	@Override
	public String toString() {
		return "MiniMax"; 
	}
	
	/**
	 * Methode berechnet alle moeglichen Moves des derzeitigen Spielers aus
	 * und gibt anhand der gegebenen Bewsertungen fuer bestimmte Figuren und Stellungen
	 * ein Ergebnis aus, von dem anschließend das hoechste für weiß und das niedrigste fuer 
	 * schwarz gewaehlt wird. 
	 */
	@Override
	public Move ausfuehren(Board board) {
		
		final long startTime = System.currentTimeMillis(); 
		
		Move bestMove = null; 
		
		int hightestSeenValue = Integer.MIN_VALUE; 
		int lowestSeenValue = Integer.MAX_VALUE; 
		int currentValue; 
		
		System.out.println(toString() + " denkt mit einer Rechentiefe von " + this.searchDepth + " nach.");
		
		int numMoves = board.currentPlayer().getLegalMoves().size(); 
		int moveCounter = 1; 
		/*
		 * Es wird durch alle Moves iteriert ein Move ausgefuehrt
		 * und anschließend nach dem MiniMax Prinzip ein Ergebnis berechnet
		 * 
		 * MiniMax: https://en.wikipedia.org/wiki/Minimax (English)
		 * 			https://de.wikipedia.org/wiki/Minimax-Algorithmus (Deutsch)
		 */
		for(final Move move : board.currentPlayer().getLegalMoves()) {
			final MoveTransition moveTransition = board.currentPlayer().makeMove(move); 
			if(moveTransition.getMoveStatus().isDone()) {		
				currentValue = board.currentPlayer().getFarbe().isWhite() ? min(moveTransition.getTransitionBoard(), this.searchDepth - 1) : max(moveTransition.getTransitionBoard(), this.searchDepth - 1); 					
				System.out.println("Analysiere Move " + moveCounter + "/" + numMoves + " => " + move.toString() + " hat eine Bewertung von: " + currentValue);
				if(board.currentPlayer().getFarbe().isWhite() && currentValue >= hightestSeenValue) {
					hightestSeenValue = currentValue; 
					bestMove = move; 
				} else if(board.currentPlayer().getFarbe().isBlack() && currentValue <= lowestSeenValue) {
					lowestSeenValue = currentValue; 
					bestMove = move; 
				}
			} else {
				System.out.println(toString() + " konnte Move " + moveCounter + "/" + numMoves + " nicht ausführen => " + move.toString());
			}
			moveCounter++; 
		}
		
		final long rechenzeit = System.currentTimeMillis() - startTime; 
		System.out.println("Er benötigte " + rechenzeit + " Sekunden zum Berechnen.");
		
		return bestMove;
	}

	// Ist das Spiel beendet?
    private static boolean isEndGameScenario(final Board board) {
        return board.currentPlayer().isSchachMatt() || board.currentPlayer().isPatt();
    }
    
    /**
     * Werte werden berechnet und anschließend der kleinste, berechnete Wert
     * zurueckgegeben
     * @param board
     * @param depth
     * @return
     */
	public int min(final Board board, final int depth) {
		
		if(depth == 0 || isEndGameScenario(board)) {
			return this.boardEvaluator.evualuate(board, depth); 
		}
		
		int lowestSeenValue = Integer.MAX_VALUE; 
		for(final Move move : board.currentPlayer().getLegalMoves()) {
			final MoveTransition moveTransition = board.currentPlayer().makeMove(move);  
			if(moveTransition.getMoveStatus().isDone()) {
				final int currentValue = max(moveTransition.getTransitionBoard(), depth - 1); 
				if(currentValue <= lowestSeenValue) {
					lowestSeenValue = currentValue; 
				}
			}
		}
		return lowestSeenValue; 
	}
	
	/**
	 * Gleich wie min, doch nur wird der groeßte Wert zurueckgegeben
	 * @param board
	 * @param depth
	 * @return
	 */
	public int max(final Board board, final int depth) {
		if(depth == 0 || isEndGameScenario(board)) {
			return this.boardEvaluator.evualuate(board, depth); 
		}
		
		int highestSeenValue = Integer.MIN_VALUE; 
		for(final Move move : board.currentPlayer().getLegalMoves()) {
			final MoveTransition moveTransition = board.currentPlayer().makeMove(move);  
			if(moveTransition.getMoveStatus().isDone()) {
				final int currentValue = min(moveTransition.getTransitionBoard(), depth - 1); 
				if(currentValue >= highestSeenValue) {
					highestSeenValue = currentValue; 
				}
			}
		}
		return highestSeenValue; 
	}
	
	
}
