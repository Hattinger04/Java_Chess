/**
 * @author Simon Greiderer 
 * @author Marc Underrain
 * 
 * Superklasse Figur, von dem anschlieﬂend geerbt werden kann. 
 * 
 * Sie besitzt Basisinformation ueber alle Figuren 
 */
package game.logik.figuren;

import java.util.Collection;

import game.logik.Farbe;
import game.logik.board.Board;
import game.logik.board.Move;

public abstract class Figur {

	protected final FigurenTyp figurenTyp; 
	protected final int figurPosition;
	protected final Farbe figurFarbe;
	protected final boolean isErsterMove;
	private final int cachedHashCode; 

	/**
	 * 
	 * @param figurPosition
	 * @param figurFarbe
	 * @param figurenTyp
	 * @param isErsterMove
	 */
	Figur(final int figurPosition, final Farbe figurFarbe, final FigurenTyp figurenTyp, final boolean isErsterMove) {
		this.figurPosition = figurPosition;
		this.figurFarbe = figurFarbe;
		this.figurenTyp = figurenTyp;

		this.isErsterMove = isErsterMove;
		this.cachedHashCode = computeHashCode(); 
	}
	
	private int computeHashCode() {
		int result = figurenTyp.hashCode(); 
		result = 31 * result + figurFarbe.hashCode();  
		result = 31 * result + figurPosition; 
		result = 31 * result + (isErsterMove ? 1 : 0); 
		return result; 	
	}


	@Override
	public boolean equals(final Object other) {
		if(this == other) {
			return true; 
		}
		if(!(other instanceof Figur)) {
			return false; 
		}
		final Figur andereFigur = (Figur) other; 
		return figurPosition == andereFigur.getFigurPosition() 
							&& figurenTyp == andereFigur.getFigurenTyp() 
							&& figurFarbe == andereFigur.getFigurenFarbe()
							&& isErsterMove == andereFigur.isErsterMove(); 
	}
	// Kein Plan
	@Override
	public int hashCode() {
		return this.cachedHashCode; 
	}

	public int getFigurPosition() {
		return this.figurPosition; 
	}

	public Farbe getFigurenFarbe() {
		return this.figurFarbe;
	}

	public boolean isErsterMove() {
		return this.isErsterMove;
	}
	
	public FigurenTyp getFigurenTyp() {
		return this.figurenTyp; 
	}

	public abstract Collection<Move> berechneLegaleZuege(final Board board);

    public abstract int locationBonus();
	public abstract Figur moveFigur(Move move); 
	
	public int getFigurenWert() {
		return this.figurenTyp.getFigurenWert();
	}
	
	/**
	 * Enum, um zwischen allen Figuren unterscheiden
	 * zu koennen
	 * 
	 * Parameter sind der Figurenwert, welcher bei dem 
	 * Algorithmus gebraucht wird, und das Kuerzel der Figur
	 */
	public enum FigurenTyp {
		
		PAWN(100, "P") {
			@Override
			public boolean isKing() {
				return false;
			}

			@Override
			public boolean isRook() {
				return false;
			}
		}, 
		KNIGHT(300, "N") {
			@Override
			public boolean isKing() {
				return false;
			}

			@Override
			public boolean isRook() {
				return false;
			}
		}, 
		BISHOP(300, "B") {
			@Override
			public boolean isKing() {
				return false;
			}

			@Override
			public boolean isRook() {
				return false;
			}
		}, 
		ROOK(500, "R") {
			@Override
			public boolean isKing() {
				return false;
			}

			@Override
			public boolean isRook() {
				return true;
			}
		},
		QUEEN(900, "Q") {
			@Override
			public boolean isKing() {
				return false;
			}

			@Override
			public boolean isRook() {
				return false;
			}
		},
		KING(100000000, "K") {
			@Override
			public boolean isKing() {
				return true;
			}

			@Override
			public boolean isRook() {
				return false;
			}
		}; 
		
		final String figurenName; 
		final int figurenWert; 
		
		/**
		 * 
		 * @param figurenWert
		 * @param figurenName
		 */
		FigurenTyp(final int figurenWert, final String figurenName) {
			this.figurenName = figurenName; 
			this.figurenWert = figurenWert; 
		}
		
		@Override
		public String toString() {
			return this.figurenName; 
		}
		
		public abstract boolean isKing();

		public abstract boolean isRook();

		public int getFigurenWert() {
			return this.figurenWert;
		} 
	}

	
}
