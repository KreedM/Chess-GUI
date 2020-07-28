package logic;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import pieces.Piece;
import pieces.Bishop;
import pieces.King;
import pieces.Knight;
import pieces.Pawn;
import pieces.Queen;
import pieces.Rook;

public class Board extends JPanel{
	private int selectedR = -1, selectedC = -1;
	private int selectedRTwo = -1, selectedCTwo = -1;
	private int squareDimension;
	private int whiteMinutes, whiteSeconds, blackMinutes, blackSeconds;
	private int ogWhiteMinutes, ogWhiteSeconds, ogBlackMinutes, ogBlackSeconds;
	private boolean whitesTurn = true, mirrored, gameEnd, whiteIncrement, blackIncrement, canMove;
	private static final Color beige = new Color(240, 217, 182);
	private static final Color brown = new Color(181, 136 ,100);
	private static final Color yellowWhite = new Color(248,235,101);
	private static final Color yellowBlack = new Color(218,195,65);
	private Piece[][] pieces;
	private MoveHistory moves;
	private King whiteKing, blackKing;
	private Pawn promotedPawn, whitePassant, blackPassant;
	private PromotionWindow promotionWindow;
	private SoundFX sound;
	private JScrollPane moveScrollPane;
	private BoardListener listener;

	public Board(JFrame frame, boolean mirrored, int height, int whiteMinutes, int whiteSeconds, int blackMinutes, int blackSeconds) throws IOException {
		this.whiteMinutes = whiteMinutes + whiteSeconds / 60;
		this.whiteSeconds = whiteSeconds % 60;
		this.blackMinutes = blackMinutes + blackSeconds / 60;
		this.blackSeconds = blackSeconds % 60;
		this.ogWhiteMinutes = this.whiteMinutes;
		this.ogWhiteSeconds = this.whiteSeconds;
		this.ogBlackMinutes = this.blackMinutes;
		this.ogBlackSeconds = this.blackSeconds;
		canMove = true;
		if(whiteMinutes == 0 && whiteSeconds == 0)
			whiteIncrement = true;
		if(blackMinutes == 0 && blackSeconds == 0)
			blackIncrement = true;
		this.mirrored = mirrored;
		moves = new MoveHistory(this);
		sound = new SoundFX();
		setPreferredSize(new Dimension(11 * (height - height % 8) / 8, height - height % 8));
		squareDimension = (height - height % 8) / 8;
		initializePieces();
		promotionWindow = new PromotionWindow(this, null);
		moveScrollPane = new JScrollPane();
		moveScrollPane.setBounds(8 * squareDimension, 2 * squareDimension, 3 * squareDimension, 4 * squareDimension);
		moveScrollPane.setViewportView(moves.getMoves(moveScrollPane));
		add(moveScrollPane);
		sound.gameStartSound();
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		boolean isWhite = true;
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				if(isWhite)
					g.setColor(beige);
				else
					g.setColor(brown);
				g.fillRect(j * squareDimension, i * squareDimension, squareDimension, squareDimension);
				isWhite = !isWhite;
			}
			isWhite = !isWhite;
		}
		
		if(selectedR != -1 && selectedC != -1) {
			if((selectedR + selectedC) % 2 == 1)
				g.setColor(yellowBlack);
			else 
				g.setColor(yellowWhite);
			g.fillRect(selectedC * squareDimension, selectedR * squareDimension, squareDimension, squareDimension);
		}
		if(selectedRTwo != -1 && selectedCTwo != -1) {
			if((selectedRTwo + selectedCTwo) % 2 == 1)
				g.setColor(yellowBlack);
			else 
				g.setColor(yellowWhite);
			g.fillRect(selectedCTwo * squareDimension, selectedRTwo * squareDimension, squareDimension, squareDimension);
		}
		
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				if(pieces[i][j] != null && (i != selectedR || j != selectedC))
					pieces[i][j].draw(g);
			}
		}
		
		if(getPiece(selectedR, selectedC) != null) {
			getPiece(selectedR, selectedC).draw(g);
		}
		g.setFont(new Font("TimesRoman", Font.PLAIN, squareDimension));
		
		if(!mirrored || whitesTurn) {
			g.setColor(Color.BLACK);
			g.fillRect(8 * squareDimension, 0, 3 * squareDimension, 2 * squareDimension);
			g.setColor(Color.WHITE);
			g.fillRect(8 * squareDimension, 6 * squareDimension, 3 * squareDimension, 2 * squareDimension);
		}
		else if(mirrored) {
			g.setColor(Color.WHITE);
			g.fillRect(8 * squareDimension, 0, 3 * squareDimension, 2 * squareDimension);
			g.setColor(Color.BLACK);
			g.fillRect(8 * squareDimension, 6 * squareDimension, 3 * squareDimension, 2 * squareDimension);
			g.setColor(Color.WHITE);
		}
		String timeString = "";
		if(blackMinutes < 10)
			timeString += "0" + blackMinutes;
		else
			timeString += blackMinutes;
		timeString += ":";
		if(blackSeconds < 10)
			timeString += "0" + blackSeconds;
		else
			timeString += blackSeconds;
		int fontY = 0;
		if(mirrored && !whitesTurn)
			fontY = squareDimension + g.getFontMetrics().getHeight() / 4 + 6 * squareDimension + 1;
		else
			fontY = squareDimension + g.getFontMetrics().getHeight() / 4 + 1;
		int fontX = 8 * squareDimension + (3 * squareDimension - g.getFontMetrics().stringWidth(timeString)) / 2;
		g.drawString(timeString, fontX, fontY);
		
		timeString = "";
		if(whiteMinutes < 10)
			timeString += "0" + whiteMinutes;
		else
			timeString += whiteMinutes;
		timeString += ":";
		if(whiteSeconds < 10)
			timeString += "0" + whiteSeconds;
		else
			timeString += whiteSeconds;

		g.setColor(Color.BLACK);
		if(mirrored && !whitesTurn)
			fontY = squareDimension + g.getFontMetrics().getHeight() / 4 + 1;
		else
			fontY = squareDimension + g.getFontMetrics().getHeight() / 4 + 6 * squareDimension + 1;
		fontX = 8 * squareDimension + (3 * squareDimension - g.getFontMetrics().stringWidth(timeString)) / 2;
		g.drawString(timeString, fontX, fontY);
	}
	
	private void initializePieces() throws IOException {
		pieces = new Piece[8][8];
		
		//Pawns
		for(int i = 0; i < 8; i++)
			pieces[6][i] = new Pawn(6, i, true, this, ImageIO.read(getClass().getResource("/img/white/p.png")));
		for(int i = 0; i < 8; i++)
			pieces[1][i] = new Pawn(1, i, false, this, ImageIO.read(getClass().getResource("/img/black/p.png")));
		
		//Bishops
		pieces[7][2] = new Bishop(7, 2, true, this, ImageIO.read(getClass().getResource("/img/white/b.png")));
		pieces[7][5] = new Bishop(7, 5, true, this, ImageIO.read(getClass().getResource("/img/white/b.png")));
		pieces[0][2] = new Bishop(0, 2, false, this, ImageIO.read(getClass().getResource("/img/black/b.png")));
		pieces[0][5] = new Bishop(0, 5, false, this, ImageIO.read(getClass().getResource("/img/black/b.png")));
		
		//Knights
		pieces[7][1] = new Knight(7, 1, true, this, ImageIO.read(getClass().getResource("/img/white/n.png")));
		pieces[7][6] = new Knight(7, 6, true, this, ImageIO.read(getClass().getResource("/img/white/n.png")));
		pieces[0][1] = new Knight(0, 1, false, this, ImageIO.read(getClass().getResource("/img/black/n.png")));
		pieces[0][6] = new Knight(0, 6, false, this, ImageIO.read(getClass().getResource("/img/black/n.png")));
		
		//Rooks
		pieces[7][0] = new Rook(7, 0, true, this, ImageIO.read(getClass().getResource("/img/white/r.png")));
		pieces[7][7] = new Rook(7, 7, true, this, ImageIO.read(getClass().getResource("/img/white/r.png")));
		pieces[0][0] = new Rook(0, 0, false, this, ImageIO.read(getClass().getResource("/img/black/r.png")));
		pieces[0][7] = new Rook(0, 7, false, this, ImageIO.read(getClass().getResource("/img/black/r.png")));
		
		//Queens
		pieces[7][3] = new Queen(7, 3, true, this, ImageIO.read(getClass().getResource("/img/white/q.png")));
		pieces[0][3] = new Queen(0, 3, false, this, ImageIO.read(getClass().getResource("/img/black/q.png")));
		
		//Kings
		pieces[7][4] = new King(7, 4, true, this, ImageIO.read(getClass().getResource("/img/white/k.png")), (Rook) pieces[7][0], (Rook) pieces[7][7]);
		whiteKing = (King) pieces[7][4];
		if(mirrored)
			pieces[0][4] = new King(0, 4, false, this, ImageIO.read(getClass().getResource("/img/black/k.png")), (Rook) pieces[0][7], (Rook) pieces[0][0]);
		else
			pieces[0][4] = new King(0, 4, false, this, ImageIO.read(getClass().getResource("/img/black/k.png")), (Rook) pieces[0][0], (Rook) pieces[0][7]);
		
		blackKing = (King) pieces[0][4];
	}
	
	public void setListener(BoardListener listener) {
		this.listener = listener;
	}
	
	public Piece getPiece(int r, int c) {
		if(r > 7 || r < 0 || c > 7 || c < 0)
			return null;
		return pieces[r][c];
	}
	
	public Piece setPiece(int r, int c, Piece piece) {
		Piece placeholder = pieces[r][c];
		pieces[r][c] = piece;
		if(piece != null) {
			pieces[r][c].setR(r);
			pieces[r][c].setC(c);
			pieces[r][c].setDragged(false);
		}
		return placeholder;
	}
	
	public boolean canBlock(int r, int c) {
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				Piece piece = getPiece(i, j);
				if(piece == null || piece instanceof King) 
					continue;
				if(piece.isWhite() != whitesTurn) {
					if(piece instanceof Pawn) {
						piece.disableMoveRecord();
						((Pawn)piece).canEatPassant(false);
					}
					if(piece.checkMove(r, c))
						return true;
					if(piece instanceof Pawn)
						((Pawn)piece).canEatPassant(true);
				}
			}
		}
		return false;
	}
	
	public String getSpecificationString(Piece piece, int r, int c) {
		if(!(piece instanceof King) && !(piece instanceof Pawn)) {
			for(int i = 0; i < 8; i++) {
				for(int j = 0; j < 8; j++) {
					if(i == piece.getR() && j == piece.getC())
						continue;
					Piece other = getPiece(i, j);
					if(other != null && other.getClass().equals(piece.getClass()) && other.isWhite() == piece.isWhite() && !other.equals(piece)) {
						other.playSound(false);
						if(other.checkMove(r, c)) {
							int row;
							char column;
							if(mirrored && !other.isWhite()) {
								row = piece.getR() + 1;
								column = (char) (97 + (7 - other.getC()));
							}
							else {
								row = 8 - other.getR();
								column = (char) (97 + piece.getC());
							}
							if(piece.getC() == other.getC())
								return "" + column + row;
							else {
								return "" + column;
							}
						}
					}
				}
			}
		}
		return "";
	}
	
	public boolean getTurn() {
		return whitesTurn;
	}
	
	public void setTurn(boolean turn) {
		whitesTurn = turn;
	}
	
	public void changeTurn() {
		if(!gameEnd && promotedPawn != null && (promotedPawn.getR() == 0 || promotedPawn.getR() == 7)) {
			canMove = false;
			promotionWindow.setPawn(promotedPawn);
			promotionWindow.show();
			promotedPawn = null;
		}
		else {
			whitesTurn = !whitesTurn;
			if(mirrored) {
				mirrorPieces();
			}
			if(whitesTurn) {
				if(whitePassant != null && whitePassant.equals(getPiece(whitePassant.getR(), whitePassant.getC()))) {
					whitePassant.noMorePassant();
					whitePassant = null;
				}
			}
			else {
				if(blackPassant != null && blackPassant.equals(getPiece(blackPassant.getR(), blackPassant.getC()))) {
					blackPassant.noMorePassant();
					blackPassant = null;
				}
			}
			resetMoveScrollPane();
		}
	}
	public void mirrorPieces() {
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 4; j++)
				setPiece(i, j, setPiece(7 - i, 7 - j, pieces[i][j]));
		}
		for(int i = 4; i < 8; i++) {
			for(int j = 0; j < 4; j++)
				setPiece(i, j, setPiece(7 - i, 7 - j, pieces[i][j]));
		}
		selectedR = 7 - selectedR;
		selectedC = 7 - selectedC;
		selectedRTwo = 7 - selectedRTwo;
		selectedCTwo = 7 - selectedCTwo;
	}
	
	public boolean gameEnd() {
		return gameEnd;
	}
	
	public void endGame() {
		if(!gameEnd) {
			gameEnd = true;
			moves.checkMate();
		}
	}
	
	public void tickWhite() {
		if(whiteIncrement) {
			if(whiteSeconds == 59) {
				whiteSeconds = 0;
				whiteMinutes++;
			}
			else 
				whiteSeconds++;
		}
		else {
			if(whiteSeconds == 0) {
				if(whiteMinutes == 0) {
					gameEnd = true;
					if(listener.getSelectedPiece() != null) 
						listener.getSelectedPiece().setDragged(false);
					listener.shownEnd(true);
					JOptionPane.showMessageDialog(this, new JLabel("Black Won By Time!", JLabel.CENTER), "Game End", JOptionPane.PLAIN_MESSAGE);
				}
				else {
					whiteMinutes--;
					whiteSeconds = 59;
				}
			}
			else
				whiteSeconds--;
		}
		repaint();
	}
	
	public void tickBlack() {
		if(blackIncrement) {
			if(blackSeconds == 59) {
				blackSeconds = 0;
				blackMinutes++;
			}
			else 
				blackSeconds++;
		}
		else {
			if(blackSeconds == 0) {
				if(blackMinutes == 0) {
					gameEnd = true;
					if(listener.getSelectedPiece() != null) 
						listener.getSelectedPiece().setDragged(false);
					listener.shownEnd(true);
					JOptionPane.showMessageDialog(this, new JLabel("White Won By Time!", JLabel.CENTER), "Game End", JOptionPane.PLAIN_MESSAGE);
				}
				else {
					blackMinutes--;
					blackSeconds = 59;
				}
			}
			else
				blackSeconds--;
		}
		repaint();
	}
	
	public void setKing(King king) {
		if(king.isWhite())
			whiteKing = king;
		else
			blackKing = king;
	}
	
	public boolean canMove() {
		return canMove;
	}
	
	public void setCanMove(boolean canMove) {
		this.canMove = canMove;
	}
	
	public void resetSquareDimension() {
		squareDimension = getHeight() / 8;
	}
	
	public JScrollPane getMoveScrollPane() {
		return moveScrollPane;
	}
	
	public void resetMoveScrollPane() {
		moveScrollPane.setViewportView(moves.getMoves(moveScrollPane));
		moveScrollPane.revalidate();
	}
	
	public King whiteKing() {
		return whiteKing;
	}
	
	public King blackKing() {
		return blackKing;
	}
	
	public void toBePromoted(Pawn pawn) {
		promotedPawn = pawn;
	}
	
	public void whitePassant(Pawn whitePassant) {
		this.whitePassant = whitePassant;
	}
	
	public void blackPassant(Pawn blackPassant) {
		this.blackPassant = blackPassant;
	}
	
	public boolean isMirrored() {
		return mirrored;
	}
	
	public int getSquareSize() {
		return squareDimension;
	}
	
	public MoveHistory getHistory() {
		return moves;
	}
	
	public SoundFX getSound() {
		return sound;
	}
	
	public int getSelectedR() {
		return selectedR;
	}
	
	public void setSelectedR(int r) {
		selectedR = r;
	}
	
	public int getSelectedC() {
		return selectedC;
	}

	public void setSelectedC(int c) {
		selectedC = c;
	}
	
	public int getSelectedRTwo() {
		return selectedRTwo;
	}
	
	public void setSelectedRTwo(int r) {
		selectedRTwo = r;
	}
	
	public int getSelectedCTwo() {
		return selectedCTwo;
	}

	public void setSelectedCTwo(int c) {
		selectedCTwo = c;
	}
	
	public void reset() throws IOException {
		gameEnd = false;
		whiteMinutes = ogWhiteMinutes;
		whiteSeconds = ogWhiteSeconds;
		blackMinutes = ogBlackMinutes;
		blackSeconds = ogBlackSeconds;
		selectedR = -1;
		selectedRTwo = -1;
		selectedC = -1;
		selectedCTwo = -1;
		initializePieces();
		moves.reset();
		whitesTurn = true;
		moveScrollPane.setViewportView(moves.getMoves(moveScrollPane));
		moveScrollPane.revalidate();
		add(moveScrollPane);
		listener.shownEnd(false);
		listener.start();
		sound.gameStartSound();
	}
}
