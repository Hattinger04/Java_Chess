/**
 * @author Simon Greiderer 
 * @author Marc Underrain
 * 
 * Superklasse Move, von der anschlieﬂend geerbt werden kann
 * 
 * Sie besitzt Basisinformation ueber einen Move eines Spielers
 */
package game.logik.board;

import game.logik.board.Board.Builder;
import game.logik.figuren.Figur;
import game.logik.figuren.Pawn;
import game.logik.figuren.Rook;

public abstract class Move {

	protected final Board board;
	protected final Figur figur;
	protected final int positionFeld;
	protected final boolean isErsterMove; 
	
	public static final Move NULL_MOVE = new NullMove(); 

	/**
	 * 
	 * @param board
	 * @param figur
	 * @param positionFeld
	 */
	private Move(Board board, Figur figur, int positionFeld) {
		super();
		this.board = board;
		this.figur = figur;
		this.positionFeld = positionFeld;
		this.isErsterMove = figur.isErsterMove(); 
	}

	/**
	 * 
	 * @param board
	 * @param positionFeld
	 */
	public Move(Board board, int positionFeld) {
		this.board = board;
		this.positionFeld = positionFeld;
		this.figur = null; 
		this.isErsterMove = false; 
	}
	
	@Override
	public int hashCode() {
		final int prime = 31; 
		int result = 1; 
		
		result = prime * result + this.positionFeld; 
		result = prime * result + this.figur.hashCode(); 
		result = prime * result + this.figur.getFigurPosition(); 
		return result; 
	}
	
	@Override
	public boolean equals(Object other) {
		if(this == other) {
			return true; 
		}
		
		if(!(other instanceof Move)) {
			return false; 
		}
		
		final Move otherMove = (Move) other; 
		return  getCurrentPosition() == otherMove.getCurrentPosition() && 
				getPositionFeld() == otherMove.getPositionFeld() 
				&& this.getFigur().equals(otherMove.getFigur()); 
	}
	
	public int getCurrentPosition() {
		return this.getFigur().getFigurPosition(); 
	}
	
	public Figur getFigur() {
		return figur; 
	}
	
	public boolean isAngriff() {
		return false;  
	}
	
	public boolean isRochadeMove() {
		return false; 
	}
	
	public Figur getAngegriffeneFigur() {
		return null; 
	}
		

	public Board getBoard() {
		return this.board; 
	}
	
	
	public Board ausfuehren() {
		
		final Board.Builder builder = new Builder(); 
		
		for(final Figur figur : this.board.currentPlayer().getAktiveFiguren()) {
			if(!this.figur.equals(figur)) {
				builder.setFigur(figur); 
			}
		}
		for(final Figur figur : this.board.currentPlayer().getGegner().getAktiveFiguren()) {
			builder.setFigur(figur); 
		}
		builder.setFigur(this.figur.moveFigur(this)); 
		builder.setMoveMaker(this.board.currentPlayer().getGegner().getFarbe()); 
		
		return builder.build();
	}

	
	public int getPositionFeld() {
		return this.positionFeld; 
	}

	/**
	 * In diesen Klassen wird zwischen verschiedenen Movearten unterschieden: 
	 * 		NormalMove
	 * 		NormalAngriffsMove
	 * 		PawnMove
	 * 		PawnAngriffsMove
	 * 		PawnEnPassantAngriffsMove
	 * 		PawnPromotion
	 * 		PawnJump
	 * 		RochadeMove
	 * 		KurzeRochadeMove
	 * 		LangeRochadeMove
	 * 	
	 * 		ansonsten NullMove
	 */
	
	
	public static final class NormalMove extends Move {
		public NormalMove(Board board, Figur figur, int positionFeld) {
			super(board, figur, positionFeld);
		}
		
		@Override
		public boolean equals(Object other) {
			return this == other || other instanceof NormalMove && super.equals(other); 
		}
		
		@Override
		public String toString() {
			return figur.getFigurenTyp().toString() + BoardUtils.getPositionAtKoordinate(this.getPositionFeld()); 
		}
	}


	
	
	public static class AngriffsMove extends Move {
		final Figur angegriffeneFigur; 	
		public AngriffsMove(Board board, Figur figur, int positionFeld, Figur angegriffeneFigur) {
			super(board, figur, positionFeld);
			this.angegriffeneFigur = angegriffeneFigur; 
		}
		
		@Override
		public int hashCode() {
			return this.angegriffeneFigur.hashCode() + super.hashCode(); 
		}
		
		@Override
		public boolean equals(Object other) {
			if(this == other)  {
				return true; 
			}
			
			if(!(other instanceof AngriffsMove)) {
				return false; 
			}
			
			final AngriffsMove otherAttackMove = (AngriffsMove) other; 
			return super.equals(otherAttackMove) && getAngegriffeneFigur().equals(otherAttackMove.getAngegriffeneFigur()); 
		}
	
		@Override
		public boolean isAngriff() {
			return true; 
		}
		
		@Override
		public Figur getAngegriffeneFigur() {
			return this.angegriffeneFigur; 
		}
	}
	

	public static class NormalAttackMove extends AngriffsMove {

		public NormalAttackMove(Board board, Figur figur, int positionFeld, Figur angegriffeneFigur) {
			super(board, figur, positionFeld, angegriffeneFigur);
		}
		
		@Override
		public boolean equals(Object other) {
			return this == other || other instanceof NormalAttackMove && super.equals(other); 
		}
		
		@Override
		public String toString() {
			return figur.getFigurenTyp() + BoardUtils.getPositionAtKoordinate(this.positionFeld);
		}
	}
	
	
	public static class PawnMove extends Move {
		public PawnMove(Board board, Figur figur, int positionFeld) {
			super(board, figur, positionFeld);
		}
		
		@Override
		public boolean equals(Object other) {
			return this == other || other instanceof PawnMove && super.equals(other); 
		}
		
		@Override
		public String toString() {
			return BoardUtils.getPositionAtKoordinate(this.positionFeld); 
		}
	}
	
	public static class PawnAngriffMove extends AngriffsMove {
		public PawnAngriffMove(Board board, Figur figur, int positionFeld, Figur angegriffeneFigur) {
			super(board, figur, positionFeld, angegriffeneFigur);
		}
		
		@Override
		public boolean equals(Object other) {
			return this == other || other instanceof PawnAngriffMove && super.equals(other); 
		}
		
		@Override
		public String toString() {
			return 	BoardUtils.getPositionAtKoordinate(this.figur.getFigurPosition()).substring(0,1) + "x" + 
					BoardUtils.getPositionAtKoordinate(this.positionFeld); 
		}
		
	}
	
	public static final class PawnEnPassantAngriffMove extends PawnAngriffMove {
		public PawnEnPassantAngriffMove(Board board, Figur figur, int positionFeld, Figur angegriffeneFigur) {
			super(board, figur, positionFeld, angegriffeneFigur);
		}
		
		@Override
		public boolean equals(Object other) {
			return this == other || other instanceof PawnEnPassantAngriffMove && super.equals(other); 
		}
		
		@Override
		public Board ausfuehren() {
			
			final Builder builder = new Builder(); 
			for(final Figur figur : this.board.currentPlayer().getAktiveFiguren()) {
				if(!this.figur.equals(figur)) {
					builder.setFigur(figur); 
				}
			}
			
			for(final Figur figur : this.board.currentPlayer().getGegner().getAktiveFiguren()) {
				if(!figur.equals(this.getAngegriffeneFigur())) {
					builder.setFigur(figur); 
				}
			}
			
			builder.setFigur(this.figur.moveFigur(this)); 
			builder.setMoveMaker(this.board.currentPlayer().getGegner().getFarbe()); 
			return builder.build(); 
		}
	}
	
	
	public static class PawnPromotion extends PawnMove {

		final Move decoratedMove; 
		final Pawn promotedPawn; 
		
		public PawnPromotion(final Move decoratedMove) {
			super(decoratedMove.getBoard(), decoratedMove.getFigur(), decoratedMove.getPositionFeld());
			this.decoratedMove = decoratedMove; 
			this.promotedPawn = (Pawn) decoratedMove.getFigur(); 
		}
		
		@Override
		public int hashCode() {
			return decoratedMove.hashCode() + (31 * promotedPawn.hashCode()); 
		}
		
		@Override
		public boolean equals(Object other) {
			return this == other || other instanceof PawnPromotion && super.equals(other); 
		}
		
		
		@Override
		public Board ausfuehren() {
			
			final Board pawnMoveBoard = this.decoratedMove.ausfuehren(); 
			final Board.Builder builder = new Builder(); 
			
			for(final Figur figur : pawnMoveBoard.currentPlayer().getAktiveFiguren()) {
				if(!this.promotedPawn.equals(figur)) {
					builder.setFigur(figur); 
				}
			}
			
			for(final Figur figur : pawnMoveBoard.currentPlayer().getGegner().getAktiveFiguren()) {
				builder.setFigur(figur); 
			}
			builder.setFigur(this.promotedPawn.getPromotionFigur().moveFigur(this)); 
			builder.setMoveMaker(pawnMoveBoard.currentPlayer().getFarbe()); 
			return builder.build(); 
		}
		
		@Override
		public boolean isAngriff() {
			return this.decoratedMove.isAngriff(); 
		}
		
		@Override
		public Figur getAngegriffeneFigur() {
			return this.decoratedMove.getAngegriffeneFigur(); 
		}
		
		@Override
		public String toString() {
			return BoardUtils.getPositionAtKoordinate(this.figur.getFigurPosition()) + "-" +
	                   BoardUtils.getPositionAtKoordinate(this.positionFeld) + "=" + this.promotedPawn.getPromotionFigur().getFigurenTyp();		
			}
	}
	
	public static final class PawnJump extends Move {
		public PawnJump(Board board, Figur figur, int positionFeld) {
			super(board, figur, positionFeld);
		}
		
		@Override
		public Board ausfuehren() {
			final Builder builder = new Builder(); 
			for(final Figur figur : this.board.currentPlayer().getAktiveFiguren()) {
				if(!this.figur.equals(figur)) {
					builder.setFigur(figur); 
				}
			}
			
			for(final Figur figur : this.board.currentPlayer().getGegner().getAktiveFiguren()) {
				builder.setFigur(figur); 
			}
			final Pawn movedPawn = (Pawn) this.figur.moveFigur(this); 
			builder.setFigur(movedPawn); 
			builder.setEnPassantPawn(movedPawn); 
			builder.setMoveMaker(this.board.currentPlayer().getGegner().getFarbe()); 
			return builder.build(); 
		}
		
		@Override
		public String toString() {
			return BoardUtils.getPositionAtKoordinate(this.positionFeld);
		}
	}

	static abstract class RochadeMove extends Move {
		
		protected final Rook rochadeRook; 
		protected final int rochadeRookStart; 
		protected final int rochadeRookPosition; 
		
		public RochadeMove(Board board, Figur figur, int positionFeld, Rook rochadeRook, int rochadeRookStart, int rochadeRookPosition) {
			super(board, figur, positionFeld);
			this.rochadeRook = rochadeRook; 
			this.rochadeRookStart = rochadeRookStart; 
			this.rochadeRookPosition = rochadeRookPosition; 
		}
		
		public Rook getRochadeRook() {
			return this.rochadeRook; 
		}
		
		@Override
		public boolean isRochadeMove() {
			return true;
		}
		
		@Override
		public Board ausfuehren() {
			
			final Builder builder = new Builder(); 
			
			for(final Figur figur : this.board.currentPlayer().getAktiveFiguren()) {
				if(!this.figur.equals(figur) && !this.rochadeRook.equals(figur)) {
					builder.setFigur(figur); 
				}
			}
			
			for(final Figur figur : this.board.currentPlayer().getGegner().getAktiveFiguren()) {
				builder.setFigur(figur); 
			}
			
			builder.setFigur(this.figur.moveFigur(this)); 
			
			builder.setFigur(new Rook(this.rochadeRookPosition, this.rochadeRook.getFigurenFarbe()));
			builder.setMoveMaker(this.board.currentPlayer().getGegner().getFarbe()); 
			return builder.build(); 
		}
		
		@Override
		public int hashCode() {
			final int prime = 31; 
			int result = super.hashCode(); 
			
			result = prime * result + this.rochadeRook.hashCode(); 
			result = prime * result + this.rochadeRookPosition; 
			return result; 
		}
		
		@Override
		public boolean equals(Object other) {

			if(this == other) {
				return true; 
			}
			
			if(!(other instanceof RochadeMove)) {
				return false; 
			}
			final RochadeMove otherRochadeMove = (RochadeMove)other; 
			return super.equals(otherRochadeMove) && this.rochadeRook.equals(otherRochadeMove.getRochadeRook()); 
			
		}
		
	}
	
	public static final class KurzeRochadeMove extends RochadeMove {
		public KurzeRochadeMove(Board board, Figur figur, int positionFeld, Rook rochadeRook, int rochadeRookStart, int rochadeRookPosition) {
			super(board, figur, positionFeld, rochadeRook, rochadeRookStart, rochadeRookPosition);
		}
		
		@Override
		public boolean equals(Object other) {
			return this == other || other instanceof KurzeRochadeMove && super.equals(other); 
		}
		
		@Override
		public String toString() {
			return "O-O"; 
		}
	}
	
	public static final class LangeRochadeMove extends RochadeMove {
		public LangeRochadeMove(Board board, Figur figur, int positionFeld, Rook rochadeRook, int rochadeRookStart, int rochadeRookPosition)  {
			super(board, figur, positionFeld, rochadeRook, rochadeRookStart, rochadeRookPosition);
		}
		
		@Override
		public String toString() {
			return "O-O-O"; 
		}
		
		@Override
		public boolean equals(Object other) {
			return this == other || other instanceof LangeRochadeMove && super.equals(other); 
		}
	
	}
	
	public static final class NullMove extends Move {
		public NullMove() {
			super(null, -1); 
		}
		
		@Override
		public Board ausfuehren() {
			throw new RuntimeException("Kann nicht den Nullmove ausfuehren!"); 
		}
		
		@Override
		public int getCurrentPosition() {
			return -1; 
		}
	}
	
	
	public static class MoveFactory {
		
		private MoveFactory() {
			throw new RuntimeException("Nicht aufrufbar!"); 
		}
		
		public static Move createMove(final Board board, final int currentPosition, final int positionFeld) {
			for(final Move move : board.getAlleLegaleMoves()) {
				if(move.getCurrentPosition() == currentPosition && move.getPositionFeld() == positionFeld) {
					return move; 
				}
			}
			return NULL_MOVE; 
		}
	}


}
