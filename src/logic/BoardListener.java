package logic;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import pieces.Piece;

public class BoardListener implements MouseListener, ComponentListener, MouseMotionListener, KeyListener, ActionListener {
	private int prevX, prevY, centiSeconds, pastWidth, pastHeight;
	private boolean shownEnd = false;
	private Board board;
	private Piece selectedPiece;
	private JFrame window; 
	private Timer timer;
	
	public BoardListener(Board board, JFrame window) {
		this.board = board;
		this.window = window;
		board.addMouseListener(this);
		board.addMouseMotionListener(this);
		board.addKeyListener(this);
		window.addComponentListener(this);
		board.setListener(this);
		timer = new Timer(10, (ActionListener) this);
		timer.start();
	}

	public void mousePressed(MouseEvent e) {
		if(board.canMove()) {
			prevX = e.getX();
			prevY = e.getY();
			
			int newR = e.getY() / board.getSquareSize(), newC = e.getX() / board.getSquareSize();
			if(newR > 7 || newC > 7) {
				return;
			}
			board.setSelectedR(newR);
			board.setSelectedC(newC);
			board.setSelectedRTwo(-1);
			board.setSelectedCTwo(-1);
			
			Piece targetPiece = board.getPiece(newR, newC);
			
			if(targetPiece != null && targetPiece.isWhite() == board.getTurn()) {
				selectedPiece = targetPiece;
				selectedPiece.setDragged(true);
			}
			board.repaint();
		}
	}

	public void mouseReleased(MouseEvent e) {
		if(selectedPiece != null && board.canMove()) {
			int newR = e.getY() / board.getSquareSize(), newC = e.getX() / board.getSquareSize();
			if(newR > 7 || newR < 0 || newC > 7 || newC < 0)
				return;
			if(!board.gameEnd() && (board.getPiece(newR, newC) == null || board.getPiece(newR, newC).isWhite() != board.getTurn())) {
				if(selectedPiece.move(newR, newC)) {
					selectedPiece.setDragged(false);
					selectedPiece = null;
					board.setSelectedR(newR);
					board.setSelectedC(newC);
					board.changeTurn();
				}
				else
					selectedPiece.setDragged(false);
			}
			else
				selectedPiece.setDragged(false);
			board.repaint();
		}
	}
	
	public void mouseDragged(MouseEvent e) {
		if(selectedPiece != null) {
			int changeX = e.getX() - prevX, changeY = e.getY() - prevY;
			prevX = e.getX();
			prevY = e.getY();
			selectedPiece.setX(selectedPiece.getX() + changeX);
			selectedPiece.setY(selectedPiece.getY() + changeY);
			if(e.getX() < 0 || e.getX() > board.getHeight() || e.getY() < 0 || e.getY() > board.getHeight()) {
				selectedPiece.setDragged(false);
			}
			board.repaint();
		}
	}

	public void mouseExited(MouseEvent e) {
	}
	
	public void componentResized(ComponentEvent e) {
		if(Math.abs(board.getWidth() - pastWidth) > 0 && board.getHeight() - pastHeight == 0)
			board.setPreferredSize(new Dimension(board.getWidth() - board.getWidth() % 11, 8 * (board.getWidth() - board.getWidth() % 11) / 11));
		else
			board.setPreferredSize(new Dimension(11 * (board.getHeight() - board.getHeight() % 8) / 8, board.getHeight() - board.getHeight() % 8));
		pastWidth = board.getWidth();
		pastHeight = board.getHeight();
		board.resetSquareDimension();
		board.getMoveScrollPane().setBounds(8 * board.getSquareSize(), 2 * board.getSquareSize(), 3 * board.getSquareSize(), 4 * board.getSquareSize());
		window.pack();
	}

	public void keyReleased(KeyEvent e) {
		switch(e.getKeyCode()) {
			case KeyEvent.VK_R: try {board.reset();} catch (IOException e1) {e1.printStackTrace();} break;
			case KeyEvent.VK_D:
			case KeyEvent.VK_RIGHT: board.getHistory().forward(); break;
			case KeyEvent.VK_A:
			case KeyEvent.VK_LEFT: board.getHistory().backward();
		}
		board.repaint();
	}
	
	public void actionPerformed(ActionEvent e) {
		centiSeconds++;
		if(board.gameEnd()) {
			if(!shownEnd) {
				if(board.getTurn()) 
					JOptionPane.showMessageDialog(board, new JLabel("Black Won By Checkmate!", JLabel.CENTER), "Game End", JOptionPane.PLAIN_MESSAGE);
				else
					JOptionPane.showMessageDialog(board, new JLabel("White Won By Checkmate!", JLabel.CENTER), "Game End", JOptionPane.PLAIN_MESSAGE);
				shownEnd = true;
			}
			timer.stop();
		}
		else if(centiSeconds == 100) {
			centiSeconds = 0;
			if(board.getTurn())
				board.tickWhite();
			else
				board.tickBlack();
		}	
	}
	
	public void setSelectedPiece(Piece piece) {
		selectedPiece = piece;
	}
	
	public Piece getSelectedPiece() {
		return selectedPiece;
	}
	
	public void start() {
		centiSeconds = 0;
		timer.start();
	}
	
	public void stop() {
		timer.stop();
	}
	
	public void shownEnd(boolean shown) {
		shownEnd = shown;
	}
	
	public void mouseClicked(MouseEvent e) {
	}
	public void mouseMoved(MouseEvent e) {
	}
	public void mouseEntered(MouseEvent e) {
	}
	public void componentMoved(ComponentEvent e) {
	}
	public void componentShown(ComponentEvent e) {
	}
	public void componentHidden(ComponentEvent e) {
	}
	public void keyTyped(KeyEvent e) {
	}
	public void keyPressed(KeyEvent e) {
	}
}
