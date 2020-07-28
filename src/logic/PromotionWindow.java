package logic;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import pieces.Piece;
import pieces.Bishop;
import pieces.Knight;
import pieces.Pawn;
import pieces.Queen;
import pieces.Rook;

public class PromotionWindow implements ActionListener {
	private JFrame frame;
	private Board board;
	private Pawn pawn;

	public PromotionWindow(Board board, Pawn pawn) {
		this.board = board;
		this.pawn = pawn;
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setTitle("Promote Your Pawn");
		
		JPanel promotionPanel = new JPanel();
		
		JButton queen = new JButton("Queen");
		JButton rook = new JButton("Rook");
		JButton bishop = new JButton("Bishop");
		JButton knight = new JButton("Knight");
		queen.addActionListener(this);
		rook.addActionListener(this);
		bishop.addActionListener(this);
		knight.addActionListener(this);
		
		promotionPanel.add(queen);
		promotionPanel.add(rook);
		promotionPanel.add(bishop);
		promotionPanel.add(knight);
		
		frame.setContentPane(promotionPanel);
		frame.pack();
		frame.setResizable(true);
		frame.setLocationRelativeTo(null);
		frame.setVisible(false);
	}
	
	public void actionPerformed(ActionEvent e) {
		Piece pieceToAdd = null;
		String pieceName = e.getActionCommand();
		try {
			if(pawn.isWhite()) {
				switch(pieceName) {
					case "Queen": pieceToAdd = new Queen(pawn.getR(), pawn.getC(), pawn.isWhite(), board, ImageIO.read(getClass().getResource("/img/white/q.png"))); board.getHistory().promotedTo("Q"); break;
					case "Rook": pieceToAdd = new Rook(pawn.getR(), pawn.getC(), pawn.isWhite(), board, ImageIO.read(getClass().getResource("/img/white/r.png"))); board.getHistory().promotedTo("R"); break;
					case "Bishop": pieceToAdd = new Bishop(pawn.getR(), pawn.getC(), pawn.isWhite(), board, ImageIO.read(getClass().getResource("/img/white/b.png"))); board.getHistory().promotedTo("B"); break;
					case "Knight": pieceToAdd = new Knight(pawn.getR(), pawn.getC(), pawn.isWhite(), board, ImageIO.read(getClass().getResource("/img/white/n.png"))); board.getHistory().promotedTo("N");
				}
			}
			else {
				switch(pieceName) {
					case "Queen": pieceToAdd = new Queen(pawn.getR(), pawn.getC(), pawn.isWhite(), board, ImageIO.read(getClass().getResource("/img/black/q.png"))); board.getHistory().promotedTo("Q"); break;
					case "Rook": pieceToAdd = new Rook(pawn.getR(), pawn.getC(), pawn.isWhite(), board, ImageIO.read(getClass().getResource("/img/black/r.png"))); board.getHistory().promotedTo("R"); break;
					case "Bishop": pieceToAdd = new Bishop(pawn.getR(), pawn.getC(), pawn.isWhite(), board, ImageIO.read(getClass().getResource("/img/black/b.png"))); board.getHistory().promotedTo("B"); break;
					case "Knight": pieceToAdd = new Knight(pawn.getR(), pawn.getC(), pawn.isWhite(), board, ImageIO.read(getClass().getResource("/img/black/n.png"))); board.getHistory().promotedTo("N");
				}
			}
		}
		catch(IOException e1) {
			e1.printStackTrace();
		}
		board.setPiece(pawn.getR(), pawn.getC(), pieceToAdd);
		if(board.getTurn() && board.whiteKing().inCheck()) {
			board.getHistory().check();
			board.whiteKing().inCheckMate();
			board.getSound().checkSound();
		}
		else if(board.blackKing().inCheck()) {
			board.getHistory().check();
			board.blackKing().inCheckMate();
			board.getSound().checkSound();
		}
		board.setCanMove(true);
		board.changeTurn();
		frame.setVisible(false);
		board.repaint();
	}
	public void setPawn(Pawn pawn) {
		this.pawn = pawn;
	}
	public void show() {
		frame.setVisible(true);
	}
}
