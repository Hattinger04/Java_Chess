/**
 * @author Simon Greiderer 
 * @author Marc Underrain
 * 
 * Board Klasse, wo Informationen ueber das Schachbrett 
 * an sich gespeichert werden: 
 * 		
 * 		Weisser und Schwarzer Spieler
 * 		Das Board wird mit Figuren besetzt
 * 		Alle moeglichen legalen Moves werden berechnet
 */
package game.logik.board;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;


import game.logik.Farbe;
import game.logik.figuren.Bishop;
import game.logik.figuren.Figur;
import game.logik.figuren.King;
import game.logik.figuren.Knight;
import game.logik.figuren.Pawn;
import game.logik.figuren.Queen;
import game.logik.figuren.Rook;
import game.logik.player.BlackPlayer;
import game.logik.player.Player;
import game.logik.player.WhitePlayer;

public class Board {

	private final List<Feld> spielfeld;
	private final Collection<Figur> weisseFiguren; 
	private final Collection<Figur> schwarzeFiguren; 
	
	private final WhitePlayer weisserSpieler; 
	private final BlackPlayer schwarzerSpieler; 
	private final Player currentPlayer; 
	
	private final Pawn enPassantPawn; 
	
	private Board(final Builder builder) {
		this.spielfeld = erstelleSpielfeld(builder);
		this.weisseFiguren = berechneActiveFiguren(this.spielfeld, Farbe.WHITE);
		this.schwarzeFiguren = berechneActiveFiguren(this.spielfeld, Farbe.BLACK);
		
		this.enPassantPawn = builder.enPassantPawn; 
		
		final Collection<Move> legaleWeisseMoves = berechneLegaleMoves(this.weisseFiguren); 
		final Collection<Move> legaleSchwarzeMoves = berechneLegaleMoves(this.schwarzeFiguren);
		
		this.weisserSpieler = new WhitePlayer(this, legaleWeisseMoves, legaleSchwarzeMoves); 
		this.schwarzerSpieler = new BlackPlayer(this, legaleWeisseMoves, legaleSchwarzeMoves); 
		
		this.currentPlayer = builder.nextPlayer.choosePlayer(this.weisserSpieler, this.schwarzerSpieler); 
	}

	 @Override
	 public String toString() {
		 final StringBuilder builder = new StringBuilder(); 
		 for(int i = 0; i < BoardUtils.ANZAHL_FELDER; i++) {
			 final String feldText = this.spielfeld.get(i).toString(); 
			 builder.append(String.format("%3s", feldText));
			 if((i + 1) % BoardUtils.ANZAHL_FELDER_PRO_REIHE == 0) {
				 builder.append("\n"); 
			 }
		 }
		 return builder.toString(); 
	 }
	 
	 public Collection<Figur> getSchwarzeFiguren() {
		 return this.schwarzeFiguren; 
	 }
	

	 public Collection<Figur> getWeisseFiguren() {
		 return this.weisseFiguren; 
	 }
	
	 
	 public Pawn getEnPassantPawn() {
		return enPassantPawn;
	}
	 
	private Collection<Move> berechneLegaleMoves(final Collection<Figur> figuren) {
		return figuren.stream().flatMap(piece -> piece.berechneLegaleZuege(this).stream()).collect(Collectors.toList());
	}

	private static Collection<Figur> berechneActiveFiguren(final List<Feld> spielfeld, final Farbe farbe) {
		
		final List<Figur> aktiveFiguren = new ArrayList<Figur>(); 
		
		for(final Feld feld : spielfeld) {
			if(feld.isFeldBesetzt()) {
				final Figur figur = feld.getFigur(); 
				if(figur.getFigurenFarbe() == farbe) {
					aktiveFiguren.add(figur); 
				}
			}
		}
		return aktiveFiguren; 
	}

	public Feld getFeld(final int feldKoordinate) {
		return spielfeld.get(feldKoordinate); 
	}
	
	private List<Feld> erstelleSpielfeld(final Builder builder) {
		final Feld[] felder = new Feld[BoardUtils.ANZAHL_FELDER];

		for (int i = 0; i < BoardUtils.ANZAHL_FELDER; i++) {
			felder[i] = Feld.erstelleFeld(i, builder.boardConfig.get(i));
		}
		return Arrays.asList(felder); 
		
	}


	public Player currentPlayer() {
		return this.currentPlayer;
	}
	
	// erstelle Anfangsspielfeld: 
	public static Board erstelleStandartfeld() {
		
		final Builder builder = new Builder(); 
		
		// Schwarze Figuren: 
		
		builder.setFigur(new Rook(0, Farbe.BLACK)); 
		builder.setFigur(new Knight(1, Farbe.BLACK)); 
		builder.setFigur(new Bishop(2, Farbe.BLACK)); 
		builder.setFigur(new Queen(3, Farbe.BLACK)); 
		builder.setFigur(new King(4, Farbe.BLACK, true, true)); 
		builder.setFigur(new Bishop(5, Farbe.BLACK)); 
		builder.setFigur(new Knight(6, Farbe.BLACK)); 
		builder.setFigur(new Rook(7, Farbe.BLACK)); 

		for(int i = 8; i < 16; i++) {
			builder.setFigur(new Pawn(i, Farbe.BLACK)); 
		}
		
		// Weiße Figuren: 
		
		for(int i = 48; i < 56; i++) {
			builder.setFigur(new Pawn(i, Farbe.WHITE)); 
		}
		
		builder.setFigur(new Rook(56, Farbe.WHITE)); 
		builder.setFigur(new Knight(57, Farbe.WHITE)); 
		builder.setFigur(new Bishop(58, Farbe.WHITE)); 
		builder.setFigur(new Queen(59, Farbe.WHITE)); 
		builder.setFigur(new King(60, Farbe.WHITE, true, true)); 
		builder.setFigur(new Bishop(61, Farbe.WHITE)); 
		builder.setFigur(new Knight(62, Farbe.WHITE)); 
		builder.setFigur(new Rook(63, Farbe.WHITE)); 

		builder.setMoveMaker(Farbe.WHITE); 
		return builder.build(); 
	}

	public Player whitePlayer() {
		return this.weisserSpieler; 
	}
	public Player blackPlayer() {
		return this.schwarzerSpieler;  
	}
	
	public static class Builder {

		Map<Integer, Figur> boardConfig;
		Farbe nextPlayer;
		Pawn enPassantPawn; 
		
		public Builder() {
			this.boardConfig = new HashMap<Integer, Figur>(); 
		}

		public Builder setFigur(final Figur figur) {
			this.boardConfig.put(figur.getFigurPosition(), figur);
			return this;
		}

		public Builder setMoveMaker(final Farbe nextPlayer) {
			this.nextPlayer = nextPlayer;
			return this;
		}

		public Board build() {
			return new Board(this);
		}

		public void setEnPassantPawn(Pawn enPassantPawn) {
			this.enPassantPawn = enPassantPawn; 
		}
	}

	public Iterable<Move> getAlleLegaleMoves() {
		return Stream.concat(this.weisserSpieler.getLegalMoves().stream(),
                this.schwarzerSpieler.getLegalMoves().stream()).collect(Collectors.toList());
	}

}
