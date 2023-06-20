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
import java.util.concurrent.CancellationException;
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

import game.data.Client;
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
import game.logik.player.Countdown;
import game.logik.player.MoveStatus;
import game.logik.player.MoveTransition;
import game.logik.player.Player;
import game.sound.PlaySound;
import net.miginfocom.swing.MigLayout;

public class GameFrameOnline extends Observable {

	private JFrame gameFrame;
	private MotionPanel contentPane;

	private BoardPanel boardPanel;
	private GameHistoryPanel gameHistoryPanel;

	private boolean isFinished = false;

	private Feld sourceFeld;
	private Feld positionFeld;
	private Figur userMovedFigur;

	private PlayerType weissType;
	private PlayerType schwarzType;

	private MoveLogMensch moveLog;

	private static Dimension BOARD_PANEL_DIMENSION = new Dimension(400, 380);
	private static Dimension TILE_PANEL_DIMENSION = new Dimension(10, 12);

	private Color lightFeldColor = Color.decode("#E0FFFF");
	private Color darkFeldColor = Color.decode("#27408B");

	private Countdown countdown1, countdown2; 
	
	private boolean nichtDursichtig = false;

	private JLabel label_1_1, label_1_2, label_2_1, label_2_2, doppelpunkt1, doppelpunkt2;

	private static Farbe spielerFarbe = Farbe.WHITE;
	private Board chessBoard;
	private BoardDirection boardDirection;

	private boolean doOnce = true;

	private boolean isTimeOut = false; 
		
	// Instanz der Klasse erstellen
	private static GameFrameOnline INSTANCE = new GameFrameOnline();

	/**
	 * Erstelle das Frame, mit allen JPanels und JButtons
	 * 
	 */
	public GameFrameOnline() {
		System.out.println("GUI");
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
		GUIUtils.style();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane
				.setLayout(new MigLayout("", "[500,grow][100,grow,fill]", "[120:n,grow,fill][750,grow][120:n,grow]"));
		this.chessBoard = Board.erstelleStandartfeld();
		this.boardPanel = new BoardPanel();
		this.gameHistoryPanel = new GameHistoryPanel();
		this.moveLog = new MoveLogMensch();
		this.boardDirection = BoardDirection.NORMAL;
		this.addObserver(new TableGameAiWatcher());

		this.gameFrame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent event) {
				if (!GameFrameOnline.get().isFinished) {
					Client.sendDisconnect(Client.getUsername());
				}
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

		JLabel lbl_gegner = new JLabel(Client.getGegnername());
		lbl_gegner.setFont(GUIUtils.font);
		panel_titel.add(lbl_gegner, "cell 0 1");

		JPanel panel_uhr1 = new JPanel();
		panel_uhr1.setOpaque(nichtDursichtig);
		contentPane.add(panel_uhr1, "cell 1 0");
		panel_uhr1.setLayout(
				new MigLayout("", "[10px,grow,fill][10px,grow,fill][10px,grow,fill][10px,grow,fill][10px,grow,fill]",
						"[10px,grow,fill]"));


		label_1_1 = new JLabel();
		label_1_1.setFont(GUIUtils.fontGross);
		label_1_1.setForeground(Color.GREEN);
		panel_uhr1.add(label_1_1, "cell 1 0,alignx left,aligny top");

		doppelpunkt1 = new JLabel();
		doppelpunkt1.setFont(GUIUtils.fontGross);
		doppelpunkt1.setForeground(Color.GREEN);
		doppelpunkt1.setText(":");
		panel_uhr1.add(doppelpunkt1, "cell 2 0,alignx right");

		label_1_2 = new JLabel();
		label_1_2.setFont(GUIUtils.fontGross);
		label_1_2.setForeground(Color.GREEN);
		panel_uhr1.add(label_1_2, "cell 3 0,alignx left,aligny top");


		countdown1 = new Countdown(); 
		countdown1.init(600000 - 500000, 0, label_1_1, label_1_2);

		JPanel panel = new JPanel();
		panel.setOpaque(nichtDursichtig);
		contentPane.add(panel, "cell 0 2,grow");
		panel.setLayout(new MigLayout("", "[grow][grow][grow]", "[grow][grow]"));

		JLabel lblSpielername = new JLabel(Client.getUsername());
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
				GameFrameOnline.get().gameFrame.dispatchEvent(new WindowEvent(gameFrame, WindowEvent.WINDOW_CLOSING));
			}
		});
		panel.add(btnExit, "cell 2 1,grow");

		JPanel panel_uhr2 = new JPanel();
		panel_uhr2.setOpaque(nichtDursichtig);
		contentPane.add(panel_uhr2, "cell 1 2,grow");
		panel_uhr2.setLayout(new MigLayout("", "[grow][grow][grow][grow][grow]", "[grow]"));


		label_2_1 = new JLabel();
		label_2_1.setFont(GUIUtils.fontGross);
		label_2_1.setForeground(Color.GREEN);
		panel_uhr2.add(label_2_1, "cell 1 0,grow");

		doppelpunkt2 = new JLabel();
		doppelpunkt2.setFont(GUIUtils.fontGross);
		doppelpunkt2	.setForeground(Color.GREEN);
		doppelpunkt2.setText(":");
		panel_uhr2.add(doppelpunkt2, "cell 2 0,grow");

		label_2_2 = new JLabel();
		label_2_2.setFont(GUIUtils.fontGross);
		label_2_2.setForeground(Color.GREEN);
		panel_uhr2.add(label_2_2, "cell 3 0,grow");

		countdown2 = new Countdown(); 
		countdown2.init(600000 - 500000, 0, label_2_1, label_2_2);
		
		this.gameFrame.setContentPane(contentPane);
		this.gameFrame.setVisible(true);
	}

	public void updateGegner(Farbe farbe) {
		spielerFarbe = farbe;
		if (spielerFarbe == Farbe.BLACK) {
			this.boardDirection = boardDirection.opposite();
			boardPanel.drawBoard(chessBoard);
			weissType = PlayerType.GEGNER;
			schwarzType = PlayerType.SPIELER;
			countdown1.start();
		} else {
			schwarzType = PlayerType.GEGNER;
			weissType = PlayerType.SPIELER;
			countdown2.start();
		}
		moveMadeUpdate(PlayerType.SPIELER);
	}

	public static GameFrameOnline get() {
		return INSTANCE;
	}

	/**
	 * Instanz der Klasse ueberschreiben
	 */
	public static void setGameFrame() {
		INSTANCE = new GameFrameOnline();
	}

	public Board getGameBoard() {
		return this.chessBoard;
	}

	public void updateGameBoard(final Board board) {
		this.chessBoard = board;
	}

	private MoveLogMensch getMoveLog() {
		return moveLog;
	}

	public GameHistoryPanel getGameHistoryPanel() {
		return gameHistoryPanel;
	}

	private BoardPanel getBoardPanel() {
		return boardPanel;
	}

	public Countdown getCountdown1() {
		return countdown1;
	}
	
	public Countdown getCountdown2() {
		return countdown2;
	}
	
	public void setTimeOut(boolean isTimeOut) {
		this.isTimeOut = isTimeOut;
	}
	
	public void turnBoard() {
		boardDirection = boardDirection.opposite();
		boardPanel.drawBoard(chessBoard);
	}

	public JLabel getLabel_2_1() {
		return label_2_1;
	}
	
	/**
	 * @param player
	 * @return true when player is Computer
	 * 
	 */
	public boolean isGegnerPlayer(final Player player) {
		if (player.getFarbe() == Farbe.WHITE) {
			return weissType == PlayerType.GEGNER;
		}
		return schwarzType == PlayerType.GEGNER;
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

			System.out.println("update");
			if (GameFrameOnline.get().isGegnerPlayer(GameFrameOnline.get().getGameBoard().currentPlayer())
					&& !GameFrameOnline.get().getGameBoard().currentPlayer().isSchachMatt()
					&& !GameFrameOnline.get().getGameBoard().currentPlayer().isPatt() && !GameFrameOnline.get().isFinished) {
				// erstelle AI Thread
				final CheckForMoves checkForMoves = new CheckForMoves();
				checkForMoves.execute();
			}
			if (GameFrameOnline.get().getGameBoard().currentPlayer().isSchachMatt()
					&& GameFrameOnline.get().isGegnerPlayer(GameFrameOnline.get().getGameBoard().currentPlayer())) {
				new PlaySound().playIngameSound("Win.mp3");
				GameFrameOnline.get().isFinished = true;
				Client.sendWin(Client.getUsername());
				JOptionPane.showMessageDialog(null, "DU HAST SCHACHMATT GESETZT!");
			}
			if (GameFrameOnline.get().getGameBoard().currentPlayer().isSchachMatt()
					&& !GameFrameOnline.get().isGegnerPlayer(GameFrameOnline.get().getGameBoard().currentPlayer())) {
				GameFrameOnline.get().isFinished = true;
				JOptionPane.showMessageDialog(null, "DU WURDEST SCHACHMATT GESETZT!");
			}
			if (GameFrameOnline.get().getGameBoard().currentPlayer().isPatt()) {
				Client.sendDraw(Client.getUsername());
				GameFrameOnline.get().isFinished = true;
				JOptionPane.showMessageDialog(null, "PATT!");
			} 
			if(GameFrameOnline.get().isTimeOut) {
				Client.sendWin(Client.getGegnername()); 
				GameFrameOnline.get().isFinished = true;
				JOptionPane.showMessageDialog(null, "DEINE ZEIT IST ABGELAUFEN!");
			}
		}
	}

	/**
	 * Klasse, welche das Board updatet und den Algorithmus anwirft
	 */
	private static class CheckForMoves extends SwingWorker<Move, String> {

		private CheckForMoves() {
		}

		@Override
		public void done() {
			try {
				Move move = get();
				GameFrameOnline.get().updateGameBoard(
						GameFrameOnline.get().getGameBoard().currentPlayer().makeMove(move).getTransitionBoard());
				GameFrameOnline.get().getMoveLog().addMove(move);
				if (BoardUtils.playSoundEffects) {
					PlaySound.playIngameSound("Move.mp3");
				}
				GameFrameOnline.get().getBoardPanel().drawBoard(GameFrameOnline.get().getGameBoard());
				GameFrameOnline.get().moveMadeUpdate(PlayerType.GEGNER);
				GameFrameOnline.get().getCountdown2().start();										
				GameFrameOnline.get().getCountdown1().stop();										

			} catch (InterruptedException | ExecutionException | CancellationException e) {
				System.out.println("Spiel wurde geschlossen!");
			}
		}

		@Override
		protected Move doInBackground() throws Exception {
			if (!GameFrameOnline.get().isFinished) {
				String message = Client.waitForMessage();
				System.out.println("Message: " + message);
				if (message.startsWith("ße")) {
					GameFrameOnline.get().isFinished = true;
					Client.sendWin(Client.getUsername());
					JOptionPane.showMessageDialog(null, "Dein Gegner hat die Verbindung verloren");
					return null;
				}
				String[] moveString = message.split("-");
				Move move = MoveFactory.createMove(GameFrameOnline.get().chessBoard, Integer.valueOf(moveString[0]),
						Integer.valueOf(moveString[1]));
				return move;
			}
			this.cancel(true);
			return null;
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
	public static class MoveLogMensch {

		private final List<Move> moves;

		public MoveLogMensch() {
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
		SPIELER, GEGNER;
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

					if (!GameFrameOnline.get().isGegnerPlayer(chessBoard.currentPlayer()) && !isFinished) {
						if (SwingUtilities.isRightMouseButton(event)) {
							// Reset alle Actionen
							sourceFeld = null;
							userMovedFigur = null;
						} else if (SwingUtilities.isLeftMouseButton(event)) {
							// Unterscheidet, ob bisher ein Feld gedrueckt wurde, oder noch nicht
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
									if (move.isAngriff()) {
										if (BoardUtils.playSoundEffects) {
											PlaySound.playIngameSound("Capture.mp3");
										}
									}
									if (transition.getMoveStatus().isDone()) {
										// Fuehre einen Move aus und stelle ihn am Board da
										doOnce = true;
										chessBoard = transition.getTransitionBoard();
										moveLog.addMove(move);
										GameFrameOnline.get().notifyObservers(); 
										Client.sendMove(String.valueOf(move.getCurrentPosition()) + "-"
												+ String.valueOf(move.getPositionFeld()));
										GameFrameOnline.get().getCountdown2().stop();
										GameFrameOnline.get().getCountdown1().start();
										if (BoardUtils.playSoundEffects) {
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
							gameHistoryPanel.redoMensch(chessBoard, moveLog);
							if (GameFrameOnline.get().isGegnerPlayer(chessBoard.currentPlayer()) && doOnce) {
								GameFrameOnline.get().moveMadeUpdate(PlayerType.SPIELER);
								doOnce = false;
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
					final BufferedImage image = ImageIO
							.read(new File(BoardUtils.DEFAULT_FIGUREN_IMAGE_PATH
									+ board.getFeld(this.feldID).getFigur().getFigurenFarbe().toString().substring(0, 1)
											.toLowerCase()
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
	}
}