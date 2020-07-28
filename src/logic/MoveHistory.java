package logic;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class MoveHistory implements MouseListener {
	private int limit;
	private ArrayList<Move> moves;
	private ArrayList<String> moveStrings;
	private Board board;
	
	public MoveHistory(Board board) {
		this.board = board;
		moves = new ArrayList<Move>();
		moveStrings = new ArrayList<String>();
	}
	
	public void add(Move move) {
		if(limit == moves.size()) {
			moveStrings.add(move.getMoveString(board));
			moves.add(move);
			limit++;
		}
		else if(move.getMoveString(board).equals(moveStrings.get(limit))) {
			limit++;
		}
		else {
			moveStrings.set(limit, move.getMoveString(board));
			moves.set(limit++, move);
			while(limit != moves.size()) {
				moves.remove(limit);
				moveStrings.remove(limit);
			}
		}
	}
	
	public void backward() {
		if(limit > 0) {
			if(board.isMirrored() && !board.getTurn())
				board.mirrorPieces();
			moves.get(--limit).revert(board);
			if(board.isMirrored() && board.getTurn())
				board.mirrorPieces();
			board.resetMoveScrollPane();
			board.setTurn(!board.getTurn());
		}
	}
	
	public void forward() {
		if(limit < moves.size()) {
			moves.get(limit++).restoreRevert(board);
			board.changeTurn();
		}
	}
	
	public JPanel getMoves(JScrollPane scrollPane) {
		JPanel movePanel = new JPanel(new GridBagLayout());
		int height = 4 * board.getSquareSize() / 13; 
		int width = (3 * board.getSquareSize() - 24) / 3;
		int rows = 0;
		GridBagConstraints c = new GridBagConstraints();
		movePanel.setBackground(Color.WHITE);
		JLabel label = new JLabel("Move No.");
		label.setPreferredSize(new Dimension(width, height));
		movePanel.add(label, c);
		label = new JLabel("White");
		label.setPreferredSize(new Dimension(width, height));
		movePanel.add(label, c);
		label = new JLabel("Black");
		label.setPreferredSize(new Dimension(width, height));
		movePanel.add(label);
		boolean newMove = true;
		int counter = 0;
		for(int i = 0; i < moveStrings.size(); i++) {
			if(newMove) {
				label = new JLabel("" + ++counter + ".");
				label.setPreferredSize(new Dimension(width, height));
				c.gridy = ++rows;
				movePanel.add(label, c);
			}
			label = new MoveLabel(i + 1, moves.get(i).getSpecificationString() + moveStrings.get(i));
			label.setPreferredSize(new Dimension(width, height));
			label.addMouseListener(this);
			if(i == limit - 1) {
				label.setOpaque(true);
				label.setBackground(Color.YELLOW);
			}
			movePanel.add(label, c);
			newMove = !newMove;
		}
		c.gridy = rows + 1;
		c.weighty =  1;
		movePanel.add(new JLabel(""), c);
		return movePanel;
	}
	
	public void promotedTo(String promotedTo) {
		String move = moveStrings.get(limit - 1);
		for(int i = move.length() - 1; i > -1; i--) {
			if(move.charAt(i) == 'Q' || move.charAt(i) == 'N' || move.charAt(i) == 'B' || move.charAt(i) == 'R') {
				return;
			}
		}
		moveStrings.set(limit - 1, moveStrings.get(limit - 1) + promotedTo);
	}
	
	public void check() {
		moveStrings.set(limit - 1, moveStrings.get(limit - 1) + "+");
	}
	
	public void checkMate() {
		String checkString = moveStrings.get(limit - 1);
		if(board.getTurn())
			moveStrings.set(limit - 1, checkString.substring(0, checkString.length() - 1) + "# 1-0");
		else
			moveStrings.set(limit - 1, checkString.substring(0, checkString.length() - 1) + "# 0-1");
	}
	
	public void reset() {
		moves = new ArrayList<Move>();
		moveStrings = new ArrayList<String>();
		limit = 0;
	}

	
	public void mouseClicked(MouseEvent e) {
		int limitTo = ((MoveLabel)e.getSource()).getLimit();
		board.getSound().mute();
		if(limit > limitTo) {
			while(limit != limitTo)
				backward();
			backward();
			board.getSound().unmute();
			forward();
		}
		else {
			while(limit < limitTo - 1)
				forward();
			board.getSound().unmute();
			forward();
		}
		board.repaint();
	}
	
	public void setSpecificationString() {
		moves.get(limit - 1).setSpecificationString(board);
	}
	
	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}
}
