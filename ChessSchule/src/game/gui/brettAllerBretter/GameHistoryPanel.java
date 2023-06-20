/**
 * @author Simon Greiderer 
 * @author Marc Underrain
 * 
 * Klasse welche anhand des MoveLogs alle Zuege 
 * beider Spieler anzeigen laesst
 * 
 * Angezeigt wird jeder Zug in der typischen 
 * Schachnotation
 * 
 */
package game.gui.brettAllerBretter;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import game.gui.GUIUtils;
import game.gui.brettAllerBretter.GameFrame.MoveLog;
import game.gui.brettAllerBretter.GameFrameOnline.MoveLogMensch;
import game.logik.board.Board;
import game.logik.board.BoardUtils;
import game.logik.board.Move;

// DEFAULT TABLE CELL LISTENER

public class GameHistoryPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final DataModel model; 
	private final JScrollPane scrollPane; 
	private static final Dimension HISTORY_PANEL_DIMENSION = new Dimension(150, 40); 
	private boolean isThemeDefault = BoardUtils.theme.equals(GUIUtils.DEFAULT_THEME_DEFAULT); 

	
	public GameHistoryPanel() {
		this.setLayout(new BorderLayout());
		this.model = new DataModel();
		final JTable table = new JTable(model); 
		final DefaultTableCellRenderer renderer = new DefaultTableCellRenderer(); 
		table.setOpaque(isThemeDefault);
		renderer.setOpaque(isThemeDefault);
		renderer.setFont(GUIUtils.font);
		table.setRowHeight(25);
		table.setFont(GUIUtils.font);
		table.getTableHeader().setFont(GUIUtils.font);
		table.setForeground(Color.ORANGE);
		renderer.setHorizontalAlignment(JLabel.CENTER);
		setOpaque(isThemeDefault);
		this.scrollPane = new JScrollPane(table);
		scrollPane.setOpaque(isThemeDefault);
		scrollPane.getViewport().setOpaque(isThemeDefault);
		scrollPane.setColumnHeaderView(table.getTableHeader());
		scrollPane.setPreferredSize(HISTORY_PANEL_DIMENSION);
		table.setShowGrid(false);
		table.setDefaultRenderer(Object.class, renderer);
		this.add(scrollPane, BorderLayout.CENTER);
		this.setVisible(true);
	}
	
	
	public void redo(final Board board, final MoveLog moveLog) {
		int currentRow = 0; 
		this.model.clear();
		
		for(final Move move : moveLog.getMoves()) {
			final String moveText = move.toString(); 
			if(move.getFigur().getFigurenFarbe().isWhite()) {
				this.model.setValueAt(moveText, currentRow, 0); 
			} else if(move.getFigur().getFigurenFarbe().isBlack()) {
				this.model.setValueAt(moveText, currentRow, 1);
				currentRow++; 
			}
		}
		
		if(moveLog.getMoves().size() > 0) {
			final Move lastMove = moveLog.getMoves().get(moveLog.size() - 1); 
			final String moveText = lastMove.toString(); 
			
			if(lastMove.getFigur().getFigurenFarbe().isWhite()) {
				this.model.setValueAt(moveText + calculateCheckAndCheckMateHash(board), currentRow, 0);
			} else if(lastMove.getFigur().getFigurenFarbe().isBlack()) {
				this.model.setValueAt(moveText + calculateCheckAndCheckMateHash(board), currentRow - 1, 1);
			}
		}
		
		final JScrollBar vertical = scrollPane.getVerticalScrollBar(); 
		vertical.setValue(vertical.getMaximum());
	}
	
	public void redoMensch(final Board board, final MoveLogMensch moveLog) {
		int currentRow = 0; 
		this.model.clear();
		
		for(final Move move : moveLog.getMoves()) {
			try {	
				final String moveText = move.toString(); 
				if(move.getFigur().getFigurenFarbe().isWhite()) {
					this.model.setValueAt(moveText, currentRow, 0); 
				} else if(move.getFigur().getFigurenFarbe().isBlack()) {
					this.model.setValueAt(moveText, currentRow, 1);
					currentRow++; 
				}
			} catch (NullPointerException e) {}
		}
		
		if(moveLog.getMoves().size() > 0) {
			final Move lastMove = moveLog.getMoves().get(moveLog.size() - 1); 
			final String moveText = lastMove.toString(); 
			
			if(lastMove.getFigur().getFigurenFarbe().isWhite()) {
				this.model.setValueAt(moveText + calculateCheckAndCheckMateHash(board), currentRow, 0);
			} else if(lastMove.getFigur().getFigurenFarbe().isBlack()) {
				this.model.setValueAt(moveText + calculateCheckAndCheckMateHash(board), currentRow - 1, 1);
			}
		}
		
		final JScrollBar vertical = scrollPane.getVerticalScrollBar(); 
		vertical.setValue(vertical.getMaximum());
	}
	
	private String calculateCheckAndCheckMateHash(final Board board) {
		if(board.currentPlayer().isSchachMatt()) {
			return "#"; 
		} else if(board.currentPlayer().isSchach()) {
			return "+"; 
		}
		return ""; 
	}

	private static class DataModel extends DefaultTableModel {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private final List<Row> values;  
		private static final String[] NAMES = {"White", "Black"}; 
	
		public DataModel() {
			this.values = new ArrayList<Row>();
		}
		
		public void clear() {
			this.values.clear();
			setRowCount(0);
		}
		
		@Override
		public int getRowCount() {
			if(this.values == null) {
				return 0; 
			}
			
			return this.values.size(); 
		}
		
		@Override
		public int getColumnCount() {
			return NAMES.length; 
		}
		
		@Override
		public Object getValueAt(int row, int column) {
			final Row currentRow = this.values.get(row); 
			if(column == 0) {
				return currentRow.getWhiteMove(); 
			} else if(column == 1) {
				return currentRow.getBlackMove(); 
			} 
			return null; 
		}
		
		@Override
		public void setValueAt(Object aValue, int row, int column) {
			final Row currentRow; 
			if(this.values.size() <= row) {
				currentRow = new Row(); 
				this.values.add(currentRow); 
			} else {
				currentRow = this.values.get(row); 
			}
			
			if(column == 0) {
				currentRow.setWhiteMove((String)aValue);
				fireTableRowsInserted(row, row);
			} else if(column == 1) {
				currentRow.setBlackMove((String)aValue);
				fireTableCellUpdated(row, column);
			}
		}
		
		@Override
		public Class<?> getColumnClass(int columnIndex) {
			return Move.class; 
		}
		
		@Override
		public String getColumnName(int column) {
			return NAMES[column]; 
		}
		
	}
	
	private static class Row {
		
		private String whiteMove; 
		private String blackMove; 
		
		public Row() {
			
		}
		
		public String getWhiteMove() {
			return whiteMove;
		}
		
		public String getBlackMove() {
			return blackMove;
		}
		
		public void setWhiteMove(String whiteMove) {
			this.whiteMove = whiteMove;
		}
		
		public void setBlackMove(String blackMove) {
			this.blackMove = blackMove;
		}
	}
	
}
