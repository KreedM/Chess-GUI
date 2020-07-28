package pieces;

import java.awt.Image;
import logic.Board;

public class Knight extends Piece {
	
	public Knight(int r, int c, boolean isWhite, Board board, Image icon) {
		super(r, c, isWhite, board, icon);
	}

	public boolean checkMove(int r, int c) {
		if(Math.abs(getR() - r) == 1 && Math.abs(getC() - c) == 2)
			return true;
		
		if(Math.abs(getR() - r) == 2 && Math.abs(getC() - c) == 1)
			return true;
		
		return false;
	}

	public boolean canBeBlocked(int r, int c) {
		return false;
	}
}
