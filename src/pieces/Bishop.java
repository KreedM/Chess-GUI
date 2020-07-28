package pieces;

import java.awt.Image;
import logic.Board;

public class Bishop extends Piece {
	
	public Bishop(int r, int c, boolean isWhite, Board board, Image icon) {
		super(r, c, isWhite, board, icon);
	}

	public boolean checkMove(int r, int c) {
		if(getR() == r && getC() == c)
			return false;
		
		int diffR = getR() - r, diffC = c - getC();
		
		if(Math.abs(diffR) == Math.abs(diffC)) {
			if(diffR > 0 && diffC > 0) {
				for(int i = 1; i < diffR; i++) {
					if(getBoard().getPiece(getR() - i, getC() + i) != null)
						return false;
				}
			}
			else if(diffR < 0 && diffC < 0) {
				for(int i = 1; i < (-1 * diffR); i++) {
					if(getBoard().getPiece(getR() + i, getC() - i) != null)
						return false;
				}
			}
			else if(diffR > 0 && diffC < 0) {
				for(int i = 1; i < diffR; i++) {
					if(getBoard().getPiece(getR() - i, getC() - i) != null)
						return false;
				}
			}
			else {
				for(int i = 1; i < diffC; i++) {
					if(getBoard().getPiece(getR() + i, getC() + i) != null)
						return false;
				}
			}
			return true;
		}
		
		return false;
	}

	public boolean canBeBlocked(int r, int c) {
		int diffR = getR() - r - 1, diffC = c - getC() - 1;
		if(diffR > 0 && diffC > 0) {
			for(int i = 1; i <= diffR; i++) {
				if(getBoard().canBlock(getR() - i, getC() + i))
					return true;
			}
		}
		else if(diffR < 0 && diffC < 0) {
			for(int i = 1; i <= (-1 * diffR); i++) {
				if(getBoard().canBlock(getR() + i, getC() - i))
					return true;
			}
		}
		else if(diffR > 0 && diffC < 0) {
			for(int i = 1; i <= diffR; i++) {
				if(getBoard().canBlock(getR() - i, getC() - i))
					return true;
			}
		}
		else {
			for(int i = 1; i <= diffC; i++) {
				if(getBoard().canBlock(getR() + i, getC() + i))
					return true;
			}
		}
		
		return false;
	}
}
