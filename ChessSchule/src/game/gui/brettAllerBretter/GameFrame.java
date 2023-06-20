/**
 * @author Simon Greiderer 
 * @author Marc Underrain
 * 
 * Klasse zeigt das Schachbrett, zeichnet bei Spielerzuegen 
 * das Brett neu und falls gegen den Computer gespielt werden soll, 
 * wird auch der Algorithmus hier aufgerufen
 * 
 */
package game.gui.brettAllerBretter;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutionException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;

import com.google.common.collect.Lists;

import game.gui.GUIUtils;
import game.gui.design.StartFrame;
import game.gui.design.panel.MotionPanel;
import game.logik.Farbe;
import game.logik.board.Board;
import game.logik.board.BoardUtils;
import game.logik.board.Feld;
import game.logik.board.Move;
import game.logik.board.Move.MoveFactory;
import game.logik.figuren.Figur;
import game.logik.player.MoveStatus;
import game.logik.player.MoveTransition;
import game.logik.player.Player;
import game.logik.player.ai.MiniMax;
import game.logik.player.ai.MoveStrategy;
import game.sound.PlaySound;
import net.miginfocom.swing.MigLayout;

public class GameFrame extends Observable {

	private JFrame gameFrame;
	private MotionPanel contentPane;

	private BoardPanel boardPanel;
	private GameHistoryPanel gameHistoryPanel;

	private Feld sourceFeld;
	private Feld positionFeld;
	private Figur userMovedFigur;

	private PlayerType weissType;
	private PlayerType schwarzType;

	private int kiStrenght = 4;
	private boolean isOnce;

	private MoveLog moveLog;

	private static Dimension BOARD_PANEL_DIMENSION = new Dimension(400, 380);
	private static Dimension TILE_PANEL_DIMENSION = new Dimension(10, 12);

	private Color lightFeldColor = Color.decode("#E0FFFF");
	private Color darkFeldColor = Color.decode("#27408B");

	private static Farbe spielerFarbe = Farbe.WHITE;
	private Board chessBoard;
	private BoardDirection boardDirection;

	private boolean nichtDursichtig = false;

	// Instanz der Klasse erstellen
	private static GameFrame INSTANCE = new GameFrame();

	/**
	 * Erstelle das Frame, mit allen JPanels und JButtons
	 * 
	 */
	public GameFrame() {
		this.gameFrame = new JFrame("Chess");
		this.gameFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.gameFrame.setBounds(100, 100, 1080, 900);
		this.gameFrame.setUndecorated(true);
		if (BoardUtils.theme.equals(GUIUtils.DEFAULT_THEME_JUJUTSU_KAISEN)) {
			contentPane = new MotionPanel(gameFrame, "theme_jujutsu.jpg");
		} else if (BoardUtils.theme.equals(GUIUtils.DEFAULT_THEME_AOT)) {
			contentPane = new MotionPanel(gameFrame, "theme_AOT.jpg");
		} else {
			contentPane = new MotionPanel(gameFrame);
			nichtDursichtig = true;
		}
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane
				.setLayout(new MigLayout("", "[500,grow][100,grow,fill]", "[120:n,grow,fill][750,grow][120:n,grow]"));
		this.chessBoard = Board.erstelleStandartfeld();
		this.boardPanel = new BoardPanel();
		this.gameHistoryPanel = new GameHistoryPanel();
		this.moveLog = new MoveLog();
		this.boardDirection = BoardDirection.NORMAL;
		this.addObserver(new TableGameAiWatcher());

		this.gameFrame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent event) {
				PlaySound.stopIngameSound();
				event.getWindow().dispose();
				new StartFrame().setVisible(true);
			}
		});

		JPanel panel_game = this.boardPanel;
		panel_game.setOpaque(nichtDursichtig);
		panel_game.setBorder(BorderFactory.createLineBorder(Color.black));
		contentPane.add(panel_game, "cell 0 1,grow");

		JPanel panel_zuege = this.gameHistoryPanel;
		contentPane.add(panel_zuege, "cell 1 1,grow");

		JPanel panel_titel = new JPanel();
		panel_titel.setOpaque(nichtDursichtig);
		contentPane.add(panel_titel, "cell 0 0,grow");
		panel_titel.setLayout(new MigLayout("", "[grow]", "[grow][]"));

		JPanel panel_bild = new JPanel();
		panel_bild.setOpaque(nichtDursichtig);
		panel_titel.add(panel_bild, "cell 0 0,grow");

		JLabel lbl_gegner = new JLabel("Computer");
		lbl_gegner.setFont(GUIUtils.font);
		panel_titel.add(lbl_gegner, "cell 0 1");

		JPanel panel = new JPanel();
		panel.setOpaque(nichtDursichtig);
		contentPane.add(panel, "cell 0 2,grow");
		panel.setLayout(new MigLayout("", "[grow][grow][grow]", "[grow][grow]"));

		JLabel lblSpielername = new JLabel("Spieler");
		lblSpielername.setFont(GUIUtils.font);
		panel.add(lblSpielername, "cell 0 0,aligny top");

		JButton btnTurnBoard = new JButton("Turn Board");
		btnTurnBoard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				turnBoard();
			}
		});
		panel.add(btnTurnBoard, "cell 0 1,grow");

		JButton btnAufgeben = new JButton("Surrender");
		btnAufgeben.setFocusPainted(false);
		panel.add(btnAufgeben, "cell 1 1,grow");

		JButton btnExit = new JButton("Exit");
		btnExit.setFocusPainted(false);
		btnExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Ruft die oben geschriebene WindowClosing auf
				GameFrame.get().gameFrame.dispatchEvent(new WindowEvent(gameFrame, WindowEvent.WINDOW_CLOSING));
			}
		});
		panel.add(btnExit, "cell 2 1,grow");

		this.gameFrame.setContentPane(contentPane);
		this.gameFrame.setVisible(true);
	}

	/**
	 * @param farbe
	 * @param value
	 * 
	 *              Methode wird bei ButtonClick aufgerufen, wenn man gegen den
	 *              Computer spielen moechte
	 */
	public void setKiValues(Farbe farbe, int value) {
		spielerFarbe = farbe;
		kiStrenght = value;
		updateKI();
	}

	public void updateKI() {
		if (spielerFarbe == Farbe.BLACK) {
			this.boardDirection = boardDirection.opposite();
			boardPanel.drawBoard(chessBoard);
			weissType = PlayerType.COMPUTER;
			schwarzType = PlayerType.MENSCH;
		} else {
			schwarzType = PlayerType.COMPUTER;
			weissType = PlayerType.MENSCH;
		}
		moveMadeUpdate(PlayerType.MENSCH);
	}

	public static GameFrame get() {
		return INSTANCE;
	}

	/**
	 * Instanz der Klasse ueberschreiben
	 */
	public static void setGameFrame() {
		INSTANCE = new GameFrame();
	}

	public Board getGameBoard() {
		return this.chessBoard;
	}

	public void updateGameBoard(final Board board) {
		this.chessBoard = board;
	}

	private MoveLog getMoveLog() {
		return moveLog;
	}

	public GameHistoryPanel getGameHistoryPanel() {
		return gameHistoryPanel;
	}

	private BoardPanel getBoardPanel() {
		return boardPanel;
	}

	public void turnBoard() {
		boardDirection = boardDirection.opposite();
		boardPanel.drawBoard(chessBoard);
	}

	/**
	 * @param player
	 * @return true when player is Computer
	 * 
	 */
	public boolean isAIPlayer(final Player player) {
		if (player.getFarbe() == Farbe.WHITE) {
			return weissType == PlayerType.COMPUTER;
		}
		return schwarzType == PlayerType.COMPUTER;
	}

	/**
	 * @param playerType
	 * 
	 *                   Wenn ein Zug gemacht wurde, wird diese Methode aufgerufen
	 *                   und dem Observer mitgeteilt, dass er ein Update machen soll
	 */
	private void moveMadeUpdate(final PlayerType playerType) {
		setChanged();
		notifyObservers(playerType);
	}

	/**
	 * Klasse, welche ueber das Spiel mit einem Observer "wacht" und bei bestimmten
	 * Events, Methoden ausfuehrt
	 */
	private static class TableGameAiWatcher implements Observer {

		@SuppressWarnings("static-access")
		@Override
		public void update(Observable o, Object arg) {

			if (GameFrame.get().isAIPlayer(GameFrame.get().getGameBoard().currentPlayer())
					&& !GameFrame.get().getGameBoard().currentPlayer().isSchachMatt()
					&& !GameFrame.get().getGameBoard().currentPlayer().isPatt()) {
				// erstelle AI Thread
				final AiThinkTank thinkTank = new AiThinkTank();
				thinkTank.execute();
			}
			if (GameFrame.get().getGameBoard().currentPlayer().isSchachMatt()
					&& GameFrame.get().isAIPlayer(GameFrame.get().getGameBoard().currentPlayer())) {
				new PlaySound().playIngameSound("Win.mp3");
				JOptionPane.showMessageDialog(null, "DU HAST SCHACHMATT GESETZT!");
				o.deleteObservers();

			}
			if (GameFrame.get().getGameBoard().currentPlayer().isSchachMatt()
					&& !GameFrame.get().isAIPlayer(GameFrame.get().getGameBoard().currentPlayer())) {
				JOptionPane.showMessageDialog(null, "DU WURDEST SCHACHMATT GESETZT!");
				o.deleteObservers();
			}
			if (GameFrame.get().getGameBoard().currentPlayer().isPatt()) {
				JOptionPane.showMessageDialog(null, "PATT!");
				o.deleteObservers();
			}
		}
	}

	/**
	 * Klasse, welche das Board updatet und den Algorithmus anwirft
	 */
	private static class AiThinkTank extends SwingWorker<Move, String> {

		private AiThinkTank() {
		}

		@Override
		public void done() {
			try {
				final Move bestMove = get();
				GameFrame.get().updateGameBoard(
						GameFrame.get().getGameBoard().currentPlayer().makeMove(bestMove).getTransitionBoard());
				GameFrame.get().getMoveLog().addMove(bestMove);
				GameFrame.get().getBoardPanel().drawBoard(GameFrame.get().getGameBoard());
				GameFrame.get().moveMadeUpdate(PlayerType.COMPUTER);
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}

		@Override
		protected Move doInBackground() throws Exception {
			final MoveStrategy miniMax = new MiniMax(GameFrame.get().kiStrenght);
			final Move bestMove = miniMax.ausfuehren(GameFrame.get().getGameBoard());
			return bestMove;
		}
	}

	// Zum drehen des Boards benoetigt
	public enum BoardDirection {

		NORMAL {
			@Override
			List<FeldPanel> traverse(List<FeldPanel> boardFelder) {
				return boardFelder;
			}

			@Override
			BoardDirection opposite() {
				return FLIPPED;
			}
		},
		FLIPPED {
			@Override
			List<FeldPanel> traverse(List<FeldPanel> boardFelder) {
				return Lists.reverse(boardFelder);
			}			

			@Override
			BoardDirection opposite() {
				return NORMAL;
			}
		};

	abstract List<FeldPanel> traverse(final List<FeldPanel> boardFelder);

	abstract BoardDirection opposite();

}

/**
 * BoardPanel ist das eigentliche Schachbrett, auf welchem 64 Felder /
 * FeldPanels erstellt werden
 */
private class BoardPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	final List<FeldPanel> boardFelder;

	public BoardPanel() {
		super(new GridLayout(8, 8));
		this.boardFelder = new ArrayList<>();
		for (int i = 0; i < BoardUtils.ANZAHL_FELDER; i++) {
			final FeldPanel feldPanel = new FeldPanel(this, i);
			this.boardFelder.add(feldPanel);
			add(feldPanel);
		}
		setPreferredSize(BOARD_PANEL_DIMENSION);
		validate();
	}

	public void drawBoard(final Board board) {
		removeAll();
		for (final FeldPanel feldPanel : boardDirection.traverse(boardFelder)) {
			feldPanel.drawFeld(board);
			add(feldPanel);
		}
		validate();
		repaint();
	}
}

/**
 * Klasse zum Speichern der gespielten Moves
 */
public static class MoveLog {

	private final List<Move> moves;

	public MoveLog() {
		this.moves = new ArrayList<Move>();
	}

	public List<Move> getMoves() {
		return this.moves;
	}

	public void addMove(final Move move) {
		this.moves.add(move);
	}

	public int size() {
		return this.moves.size();
	}

	public void clear() {
		this.moves.clear();
	}

	public Move removeMove(int index) {
		return this.moves.remove(index);
	}

	public boolean removeMove(final Move move) {
		return this.moves.remove(move);
	}
}

/**
 * Definiert ob Mensch oder Computer
 */
enum PlayerType {
	MENSCH, COMPUTER;
}

/**
 * FeldPanel Klasse, welche bei einem MouseClick-Event bestimmte Methoden
 * ausfuehrt.
 * 
 * Auﬂerdem wird ein Thread erstellt, welche die GUI staendig updated.
 */
private class FeldPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final int feldID;

	FeldPanel(final BoardPanel boardPanel, final int feldID) {
		super(new GridBagLayout());
		this.feldID = feldID;
		setPreferredSize(TILE_PANEL_DIMENSION);
		assignFeldColor();
		assignFeldFigurIcon(chessBoard);
		highlightLegal(chessBoard);

		addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {

			}

			@Override
			public void mousePressed(MouseEvent event) {

				if (!GameFrame.get().isAIPlayer(chessBoard.currentPlayer())) {
					if (SwingUtilities.isRightMouseButton(event)) {
						// Reset allle Actionen
						sourceFeld = null;
						userMovedFigur = null;
					} else if (SwingUtilities.isLeftMouseButton(event)) {
						/*
						 * Unterscheidet, ob bisher ein Feld gedrueckt wurde, oder noch nicht
						 */
						if (sourceFeld == null) {
							// Wenn keine Figur ausgewaehlt wurde:
							sourceFeld = chessBoard.getFeld(feldID);
							userMovedFigur = sourceFeld.getFigur();
							// Wenn auf ein Feld ohne Figur gedrueckt wurde,
							// reset SourceFeld
							if (userMovedFigur == null) {
								sourceFeld = null;
							}
						} else {
							// Wenn eine Figur bereits ausgewaehlt wurde:
							positionFeld = chessBoard.getFeld(feldID);
							if (positionFeld.getFigur() != null && positionFeld.getFigur()
									.getFigurenFarbe() == sourceFeld.getFigur().getFigurenFarbe()) {
								userMovedFigur = positionFeld.getFigur();
								sourceFeld = positionFeld;
							} else {
								final Move move = MoveFactory.createMove(chessBoard, sourceFeld.getFeldKoordinate(),
										positionFeld.getFeldKoordinate());
								final MoveTransition transition = chessBoard.currentPlayer().makeMove(move);

								if (transition.getMoveStatus().isDone()) {
									// Fuehre einen Move aus und stelle ihn am Board da
									isOnce = true;
									chessBoard = transition.getTransitionBoard();
									moveLog.addMove(move);

									if (move.isAngriff()) {
										if (BoardUtils.playSoundEffects) {
											PlaySound.playIngameSound("Capture.mp3");
										}
									} else if (BoardUtils.playSoundEffects) {
										PlaySound.playIngameSound("Move.mp3");
									}
								}
								sourceFeld = null;
								positionFeld = null;
								userMovedFigur = null;
							}

						}
					}
				}
				// Thread
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						gameHistoryPanel.redo(chessBoard, moveLog);
						if (GameFrame.get().isAIPlayer(chessBoard.currentPlayer()) && isOnce) {
							GameFrame.get().moveMadeUpdate(PlayerType.MENSCH);
							isOnce = false;
						}
						boardPanel.drawBoard(chessBoard);
					}
				});

			}

			@Override
			public void mouseExited(MouseEvent e) {

			}

			@Override
			public void mouseEntered(MouseEvent e) {

			}

			@Override
			public void mouseClicked(MouseEvent event) {
			}
		});
		validate();
	}

	// Zeichnet ein Feld
	public void drawFeld(Board board) {
		assignFeldFigurIcon(board);
		highlightTileBorder(board);
		assignFeldColor();
		highlightLegal(board);
		validate();
		repaint();

	}

	/**
	 * @param board
	 * 
	 *              Zeichnet Umrandung um FeldPanels
	 */
	private void highlightTileBorder(final Board board) {
		if (userMovedFigur != null && userMovedFigur.getFigurenFarbe() == board.currentPlayer().getFarbe()
				&& userMovedFigur.getFigurPosition() == this.feldID) {
			setBorder(BorderFactory.createLineBorder(Color.cyan));
		} else {
			setBorder(null);
		}
	}

	/**
	 * @param board
	 * 
	 *              Zeichnet die Figuren
	 */
	private void assignFeldFigurIcon(final Board board) {
		removeAll();
		if (board.getFeld(this.feldID).isFeldBesetzt()) {
			try {
				final BufferedImage image = ImageIO.read(new File(BoardUtils.DEFAULT_FIGUREN_IMAGE_PATH
						+ board.getFeld(this.feldID).getFigur().getFigurenFarbe().toString().substring(0, 1)
						+ board.getFeld(this.feldID).getFigur().toString() + ".png"));
				add(new JLabel(new ImageIcon(image)));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @param board
	 * 
	 *              Zeichnet alle legalen Moves
	 */
	private void highlightLegal(final Board board) {
		if (BoardUtils.highlightLegalMoves) {
			for (final Move move : pieceLegalMoves(board)) {
				if (move.getPositionFeld() == this.feldID) {
					if (move.isAngriff()) {
						setBorder(BorderFactory.createLineBorder(Color.red));
					} else {
						BufferedImage image;
						try {
							image = ImageIO.read(new File("./resources/images/ingame/blue.png"));
							add(new JLabel(new ImageIcon(image)));
						} catch (IOException e) {
							System.out.println("Fehler beim Auslesen der Bilddatei!");
						}
					}

				}
			}
		}
	}

	private Collection<Move> pieceLegalMoves(final Board board) {
		if (userMovedFigur != null && userMovedFigur.getFigurenFarbe() == board.currentPlayer().getFarbe()) {
			Collection<Move> moves = new ArrayList<Move>();
			for (final Move move : userMovedFigur.berechneLegaleZuege(board)) {
				final MoveTransition transition = chessBoard.currentPlayer().makeMove(move);
				if (transition.getMoveStatus() == MoveStatus.DONE) {
					moves.add(move);
				}
			}
			moves.addAll(chessBoard.currentPlayer().berechneKingRochade(chessBoard.currentPlayer().getLegalMoves(),
					chessBoard.currentPlayer().getGegner().getLegalMoves()));
			return moves;
		}
		return Collections.emptyList();
	}

	// Gibt allen Panels eine Background Color
	private void assignFeldColor() {

		if (BoardUtils.theme.equals(GUIUtils.DEFAULT_THEME_DEFAULT)) {
			setOpaque(true);
			if (BoardUtils.EIGHT_RANK[this.feldID] || BoardUtils.SIXTH_RANK[this.feldID]
					|| BoardUtils.FOUTH_RANK[this.feldID] || BoardUtils.SECOND_RANK[this.feldID]) {
				setBackground(this.feldID % 2 == 0 ? lightFeldColor : darkFeldColor);
			} else if (BoardUtils.SEVENTH_RANK[this.feldID] || BoardUtils.FIFTH_RANK[this.feldID]
					|| BoardUtils.THRID_RANK[this.feldID] || BoardUtils.FIRST_RANK[this.feldID]) {
				setBackground(this.feldID % 2 != 0 ? lightFeldColor : darkFeldColor);
			}
		} else {
			setBackground(new Color(0, 0, 0, 0));
		}

	}
}}