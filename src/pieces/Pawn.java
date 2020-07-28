package pieces;

import java.awt.Image;
import logic.Board;
import logic.Move;

public class Pawn extends Piece {
	
	private boolean enPassant, canEatPassant;
	
	public Pawn(int r, int c, boolean isWhite, Board board, Image icon) {
		super(r, c, isWhite, board, icon);
		canEatPassant = true;
	}

	public boolean checkMove(int r, int c) {
		if(getR() == r && getC() == c)
			return false;
		
		if(getBoard().isMirrored() || isWhite())
			return whiteCheck(r, c);
		
		return blackCheck(r, c);	
	}
	
	public boolean whiteCheck(int r, int c) {
		int diffR = getR() - r, diffC = c - getC();
		
		if(diffC == 0 && diffR == 1 && getBoard().getPiece(r, c) == null) {
			if(r == 0)
				getBoard().toBePromoted(this);
			return true;
		}
		
		if(diffR == 1 && Math.abs(diffC) == 1) {
			if(getBoard().getPiece(r, c) == null) {
				if(canEatPassant && getBoard().getPiece(getR(), c) instanceof Pawn && ((Pawn)getBoard().getPiece(getR(), c)).enPassantAble()) {
					if(canRecordMove()) {
						getBoard().getHistory().add(new Move(copy(), getBoard().getPiece(getR(), c), r, c, getBoard()));
						disableMoveRecord();
					}
					getBoard().setPiece(getR(), c, null);
					getBoard().setPiece(getR(), getC(), null);
					getBoard().setPiece(r, c, this);
					King king;
					if(isWhite()) 
						king = getBoard().blackKing();
					else
						king = getBoard().whiteKing();
					if(king.inCheck())
						getBoard().getSound().checkSound();
					else {
						getBoard().getSound().captureSound();
					}
					playSound(false);
					return true;
				}
			}
			else if(getBoard().getPiece(r, c).isWhite() != isWhite()) {
				if(r == 0)
					getBoard().toBePromoted(this);
				return true;
			}
		}
		
		else if(diffC == 0 && diffR == 2 && getR() == 6 && getBoard().getPiece(5, getC()) == null && getBoard().getPiece(4, getC()) == null) {
			enPassant = true;
			if(isWhite())
				getBoard().whitePassant(this);
			else
				getBoard().blackPassant(this);
			return true;
		}
		
		return false;
	}
	
	public boolean blackCheck(int r, int c) {
		int diffR = getR() - r, diffC = c - getC();
		
		if(diffC == 0 && diffR == -1 && getBoard().getPiece(r, c) == null) {
			if(r == 7)
				getBoard().toBePromoted(this);
			return true;
		}
		
		if(diffR == -1 && Math.abs(diffC) == 1) {
			if(getBoard().getPiece(r, c) == null) {
				if(canEatPassant && getBoard().getPiece(getR(), c) instanceof Pawn && ((Pawn)getBoard().getPiece(getR(), c)).enPassantAble()) {
					if(canRecordMove()) {
						getBoard().getHistory().add(new Move(copy(), getBoard().getPiece(getR(), c), r, c, getBoard()));
						disableMoveRecord();
					}
					getBoard().setPiece(getR(), c, null);
					getBoard().setPiece(getR(), getC(), null);
					getBoard().setPiece(r, c, this);
					King king;
					if(isWhite()) 
						king = getBoard().blackKing();
					else
						king = getBoard().whiteKing();
					if(king.inCheck())
						getBoard().getSound().checkSound();
					else
						getBoard().getSound().captureSound();
					playSound(false);
					return true;
				}
			}
			else if(getBoard().getPiece(r, c).isWhite()) {
				if(r == 7)
					getBoard().toBePromoted(this);
				return true;
			}
		}
		
		else if(diffC == 0 && diffR == -2 && getR() == 1 && getBoard().getPiece(2, getC()) == null && getBoard().getPiece(3, getC()) == null) {
			enPassant = true;
			getBoard().blackPassant(this);
			return true;
		}
		return false;
	}
	
	public void canEatPassant(boolean canEat) {
		canEatPassant = canEat;
	}
	
	public boolean enPassantAble() {
		return enPassant;
	}
	
	public void noMorePassant() {
		enPassant = false;
	}	
	public void yesMorePassant() {
		enPassant = true;
	}	
	
	public Piece copy() {
		Pawn copy = new Pawn(getR(), getC(), isWhite(), getBoard(), getIcon());
		if(enPassant) {
			copy.yesMorePassant();
		}
		return copy;
	}
	
	public boolean canBeBlocked(int r, int c) {
		return false;
	}
}
