package pieces;

import java.awt.Image;
import logic.Board;

public class Queen extends Piece {
	
	public Queen(int r, int c, boolean isWhite, Board board, Image icon) {
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
			return true;
		}
		
		return false;
	}

	public boolean canBeBlocked(int r, int c) {
		int diffR = getR() - r, diffC = c - getC();
		if(diffR < 0)
			diffR++;
		if(diffC < 0)
			diffC++;
		if(diffR > 0)
			diffR--;
		if(diffC > 0)
			diffC--;
		if(Math.abs(diffR) == Math.abs(diffC)) {
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
			else if(diffR < 0 && diffC > 0){
				for(int i = 1; i <= diffC; i++) {
					if(getBoard().canBlock(getR() + i, getC() + i))
						return true;
				}
			}
		}
		else {
			if(diffR == 0) {
				if(diffC > 0) {
					for(int i = 1; i <= diffC; i++) {
						if(getBoard().canBlock(getR(), getC() + i))
							return true;
					}
				}
				else {
					for(int i = 1; i <= (-1 * diffC); i++) {
						if(getBoard().canBlock(getR(), getC() - i))
							return true;
					}
				}
			}
			else if(diffC == 0) {
				if(diffR > 0) {
					for(int i = 1; i <= diffR; i++) {
						if(getBoard().canBlock(getR() - i, getC()))
							return true;
					}
				}
				else {
					for(int i = 1; i <= (-1 * diffR); i++) {
						if(getBoard().canBlock(getR() + i, getC()))
							return true;
					}
				}
			}
		}
		return false;
	}
}
