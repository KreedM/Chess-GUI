package pieces;

import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import logic.Board;
import logic.Move;

public abstract class Piece {
	
	private int r, c, x, y;
	private Image icon;
	private Board board;
	private boolean isWhite, dragged, canRecordMove, canPlaySound;
	private ArrayList<Piece> canBeEatenByList;
	
	public Piece(int r, int c, boolean isWhite, Board board, Image icon) {
		this.r = r;
		this.c = c;
		x = c * 90;
		y = r * 90;
		this.isWhite = isWhite;
		this.board = board;
		this.icon = icon;
	}
	
	public Board getBoard() {
		return board;
	}
	
	public boolean isWhite() {
		return isWhite;
	}
	
	public void setDragged(boolean dragged) {
		x = c * board.getSquareSize();
		y = r * board.getSquareSize();
		this.dragged = dragged;
	}
	
	public Image getIcon() {
		return icon;
	}
	
	public void setImage(Image icon) {
		this.icon = icon;
	}
	
	public int getR() {
		return r;
	}

	public void setR(int r) {
		this.r = r;
	}

	public int getC() {
		return c;
	}

	public void setC(int c) {
		this.c = c;
	}
	
	public int getX() {
		return x;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public int getY() {
		return y;
	}
	
	public void setY(int y) {
		this.y = y;
	}

	public boolean move(int r, int c) {
		King king;
		if(isWhite)
			king = board.whiteKing();
		else
			king = board.blackKing();
		
		int oldR = this.r, oldC = this.c;
		Piece placeholder = board.getPiece(r, c);
		board.setPiece(oldR, oldC, null);
		board.setPiece(r, c, this);
		if(king.inCheck()) {
			board.setPiece(r, c, placeholder);
			board.setPiece(oldR, oldC, this);
			return false;
		}
		board.setPiece(r, c, placeholder);
		board.setPiece(oldR, oldC, this);
		canRecordMove = true;
		canPlaySound = true;
		Piece temp = copy();
		if(!checkMove(r, c))
			return false;
		else {
			if(canRecordMove)
				board.getHistory().add(new Move(temp, placeholder, r, c, board));
			board.setPiece(oldR, oldC, null);
			board.getHistory().setSpecificationString();
			board.setSelectedRTwo(oldR);
			board.setSelectedCTwo(oldC);
			board.setPiece(r, c, this);
			if(canRecordMove) {
				if(!isWhite)
					king = board.whiteKing();
				else
					king = board.blackKing();
				if(king.inCheck()) {
					board.getHistory().check();
					if(canPlaySound)
						board.getSound().checkSound();
					if(board.isMirrored())
						board.mirrorPieces();
					king.inCheckMate();
					if(board.isMirrored())
						board.mirrorPieces();
				}
				else if(placeholder == null && canPlaySound)
					board.getSound().moveSound();
				else if(canPlaySound) {
					board.getSound().captureSound();
				}
			}
			return true;
		}
	}
	
	public ArrayList<Piece> canBeEatenBy() {
		canBeEatenByList = new ArrayList<Piece>();
		checkLs();
		checkLines();
		checkDiagonals();
		checkPawns();
		checkKing();
		return canBeEatenByList;
	}
	
	private boolean checkLines() {
		Piece piece;
		
		for(int i = getR() + 1; i < 8; i++) {
			piece = getBoard().getPiece(i, getC());
			if(piece != null) {
				if(piece.isWhite() == isWhite())
					break;
				if(piece instanceof Rook || piece instanceof Queen)
					canBeEatenByList.add(piece);
				break;
			}
		}
		for(int i = getR() - 1; i > -1; i--) {
			piece = getBoard().getPiece(i, getC());
			if(piece != null) {
				if(piece.isWhite() == isWhite())
					break;
				if(piece instanceof Rook || piece instanceof Queen)
					canBeEatenByList.add(piece);
				break;
			}
		}
		for(int i = getC() + 1; i < 8; i++) {
			piece = getBoard().getPiece(getR(), i);
			if(piece != null) { 
				if(piece.isWhite() == isWhite())
					break;
				if(piece instanceof Rook || piece instanceof Queen)
					canBeEatenByList.add(piece);
				break;
			}
		}
		for(int i = getC() - 1; i > -1; i--) {
			piece = getBoard().getPiece(getR(), i);
			if(piece != null) {
				if(piece.isWhite() == isWhite())
					break;
				if(piece instanceof Rook || piece instanceof Queen)
					canBeEatenByList.add(piece);
				break;
			}
		}
		
		return false;
	}
	
	private void checkDiagonals() {
		Piece piece;
		int limit = Math.min(7 - getC(), getR()) + 1;
		
		for(int i = 1; i < limit; i++) {
			piece = getBoard().getPiece(getR() - i, getC() + i);
			if(piece != null) {
				if(piece.isWhite() == isWhite())
					break;
				if(piece instanceof Bishop || piece instanceof Queen)
					canBeEatenByList.add(piece);
				break;
			}
		}
		
		limit = Math.min(7 - getC(), 7 - getR()) + 1;
		
		for(int i = 1; i < limit; i++) {
			piece = getBoard().getPiece(getR() + i, getC() + i);
			if(piece != null) {
				if(piece.isWhite() == isWhite())
					break;
				if(piece instanceof Bishop || piece instanceof Queen)
					canBeEatenByList.add(piece);
				break;
			}
		}
		
		limit = Math.min(getC(), 7 - getR()) + 1;
		
		for(int i = 1; i < limit; i++) {
			piece = getBoard().getPiece(getR() + i, getC() - i);
			if(piece != null) {
				if(piece.isWhite() == isWhite())
					break;
				if(piece instanceof Bishop || piece instanceof Queen)
					canBeEatenByList.add(piece);
				break;
			}
		}
		
		limit = Math.min(getC(), getR()) + 1;
		
		for(int i = 1; i < limit; i++) {
			piece = getBoard().getPiece(getR() - i, getC() - i);
			if(piece != null) {
				if(piece.isWhite() == isWhite())
					break;
				if(piece instanceof Bishop || piece instanceof Queen)
					canBeEatenByList.add(piece);
				break;
			}
		}
	}
	
	private void checkLs() {
		Piece piece;
		
		if(withinRange(getR() - 2, getC() + 1)) {
			piece = getBoard().getPiece(getR() - 2, getC() + 1);
			if(piece instanceof Knight && piece.isWhite() != isWhite())
				canBeEatenByList.add(piece);
		}
		if(withinRange(getR() - 1, getC() + 2)) {
			piece = getBoard().getPiece(getR() - 1, getC() + 2);
			if(piece instanceof Knight && piece.isWhite() != isWhite())
				canBeEatenByList.add(piece);
		}
		if(withinRange(getR() + 1, getC() + 2)) {
			piece = getBoard().getPiece(getR() + 1, getC() + 2);
			if(piece instanceof Knight && piece.isWhite() != isWhite())
				canBeEatenByList.add(piece);
		}
		if(withinRange(getR() + 2, getC() + 1)) {
			piece = getBoard().getPiece(getR() + 2, getC() + 1);
			if(piece instanceof Knight && piece.isWhite() != isWhite())
				canBeEatenByList.add(piece);
		}
		if(withinRange(getR() + 2, getC() - 1)) {
			piece = getBoard().getPiece(getR() + 2, getC() - 1);
			if(piece instanceof Knight && piece.isWhite() != isWhite())
				canBeEatenByList.add(piece);
		}
		if(withinRange(getR() + 1, getC() - 2)) {
			piece = getBoard().getPiece(getR() + 1, getC() - 2);
			if(piece instanceof Knight && piece.isWhite() != isWhite())
				canBeEatenByList.add(piece);
		}
		if(withinRange(getR() - 1, getC() - 2)) {
			piece = getBoard().getPiece(getR() - 1, getC() - 2);
			if(piece instanceof Knight && piece.isWhite() != isWhite())
				canBeEatenByList.add(piece);
		}
		if(withinRange(getR() - 2, getC() - 1)) {
			piece = getBoard().getPiece(getR() - 2, getC() - 1);
			if(piece instanceof Knight && piece.isWhite() != isWhite())
				canBeEatenByList.add(piece);
		}
	}
	
	private void checkPawns() {
		if(isWhite() || getBoard().isMirrored()) {
			if(getR() - 1 < 0)
				return;
			
			Piece piece;
			
			if(getC() - 1 > 0) {
				piece = getBoard().getPiece(getR() - 1, getC() - 1);
				if(piece instanceof Pawn && piece.isWhite() != isWhite())
					canBeEatenByList.add(piece);
			}
			
			if(getC() + 1 < 8) {
				piece = getBoard().getPiece(getR() - 1, getC() + 1);
				if(piece instanceof Pawn && piece.isWhite() != isWhite())
					canBeEatenByList.add(piece);
			}
		}
		else {
			if(getR() + 1 > 7)
				return;
			
			Piece piece;
			
			if(getC() - 1 > 0) {
				piece = getBoard().getPiece(getR() + 1, getC() - 1);
				if(piece instanceof Pawn && piece.isWhite() != isWhite())
					canBeEatenByList.add(piece);
			}
			
			if(getC() + 1 < 8) {
				piece = getBoard().getPiece(getR() + 1, getC() + 1);
				if(piece instanceof Pawn && piece.isWhite() != isWhite())
					canBeEatenByList.add(piece);
			}
		}
	}
	
	private void checkKing() {
		for(int i = 0; i < 3; i++) {
			if(getBoard().getPiece(getR() + 1, getC() - 1 + i) instanceof King)
				canBeEatenByList.add(getBoard().getPiece(getR() + 1, getC() - 1 + i));
		}	
		for(int i = 0; i < 3; i++) {
			if(getBoard().getPiece(getR() - 1, getC() - 1 + i) instanceof King)
				canBeEatenByList.add(getBoard().getPiece(getR() - 1, getC() - 1 + i));
		}	
		if(getBoard().getPiece(getR(), getC() - 1) instanceof King)
			canBeEatenByList.add(getBoard().getPiece(getR(), getC() - 1));
		if(getBoard().getPiece(getR(), getC() + 1) instanceof King)
			canBeEatenByList.add(getBoard().getPiece(getR(), getC() + 1));
	}
	
	private boolean withinRange(int r, int c) {
		return r > -1 && r < 8 && c > -1 && c < 8;
	}
	
	public boolean canRecordMove() {
		return canRecordMove;
	}
	
	public void disableMoveRecord() {
		canRecordMove = false;
	}
	
	public boolean canPlaySound() {
		return canPlaySound;
	}
	
	public void playSound(boolean canPlay) {
		canPlaySound = canPlay;
	}
	
	public abstract boolean checkMove(int r, int c);
	
	public abstract boolean canBeBlocked(int r, int c);
	
	public void draw(Graphics g) {
		if(dragged)
			g.drawImage(icon, x, y, board.getSquareSize(), board.getSquareSize(), null);
		else
			g.drawImage(icon, c * board.getSquareSize(), r * board.getSquareSize(), board.getSquareSize(), board.getSquareSize(), null);
	}
	
	public Piece copy() {
		return this;
	}
}
