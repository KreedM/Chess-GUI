package pieces;

import java.awt.Image;
import logic.Board;

public class Rook extends Piece {
	
	private boolean castle = true;
	
	public Rook(int r, int c, boolean isWhite, Board board, Image icon) {
		super(r, c, isWhite, board, icon);
	}

	public boolean checkMove(int r, int c) {
		if(getR() == r && getC() == c) {
			return false;
		}
		
		int diffR = getR() - r, diffC = c - getC();
		
		if(diffR == 0) {
			if(diffC > 0) {
				for(int i = 1; i < diffC; i++) {
					if(getBoard().getPiece(getR(), getC() + i) != null)
						return false;
				}
			}
			else {
				for(int i = 1; i < (-1 * diffC); i++) {
					if(getBoard().getPiece(getR(), getC() - i) != null)
						return false;
				}
			}
			castle = false;
			return true;
		}
		
		if(diffC == 0) {
			if(diffR > 0) {
				for(int i = 1; i < diffR; i++) {
					if(getBoard().getPiece(getR() - i, getC()) != null)
						return false;
				}
			}
			else {
				for(int i = 1; i < (-1 * diffR); i++) {
					if(getBoard().getPiece(getR() + i, getC()) != null)
						return false;
				}
			}
			castle = false;
			return true;
		}
		return false;
	}
	
	public boolean canCastle() {
		return castle;
	}
	
	public void cantCastle() {
		castle = false;
	}
	
	public Piece copy() {
		Rook copy = new Rook(getR(), getC(), isWhite(), getBoard(), getIcon());
		if(!castle) {
			copy.cantCastle();
		}
		return copy;
	}

	public boolean canBeBlocked(int r, int c) {
		int diffR = getR() - r, diffC = c - getC();
		if(diffR == 0) {
			if(diffC > 0) {
				for(int i = 1; i < diffC; i++) {
					if(getBoard().canBlock(getR(), getC() + i))
						return true;
				}
			}
			else {
				for(int i = 1; i < (-1 * diffC); i++) {
					if(getBoard().canBlock(getR(), getC() - i))
						return true;
				}
			}
		}
		else if(diffC == 0) {
			if(diffR > 0) {
				for(int i = 1; i < diffR; i++) {
					if(getBoard().canBlock(getR() - i, getC()))
						return true;
				}
			}
			else {
				for(int i = 1; i < (-1 * diffR); i++) {
					if(getBoard().canBlock(getR() + i, getC()))
						return true;
				}
			}
		}
		
		return false;
	}
}
