package pieces;

import java.awt.Image;
import java.util.ArrayList;
import logic.Board;
import logic.Move;

public class King extends Piece {
	
	private boolean castle = true;
	private Piece checking;
	private Rook left, right;
	
	public King(int r, int c, boolean isWhite, Board board, Image icon, Rook left, Rook right) {
		super(r, c, isWhite, board, icon);
		this.left = left;
		this.right = right;
	}

	public boolean checkMove(int r, int c) {
		if(getR() == r && getC() == c)
			return false;
		
		if(Math.abs(getR() - r) < 2 && Math.abs(c - getC()) < 2) {
			castle = false;
			return true;
		}
		
		if(getR() == r && Math.abs(c - getC()) == 2) {
			if(c - getC() > 0)
				return checkCastleRight();
			else
				return checkCastleLeft();
		}
		
		return false;
	}
	
	public boolean hypoMove(int r, int c, Piece moving) {
		if(!withinRange(r, c))
			return false;
		Piece eaten = getBoard().getPiece(r, c);
		if(eaten != null && eaten.isWhite() == isWhite()) 
			return false;
		int oldR = moving.getR(), oldC = moving.getC();
		getBoard().setPiece(oldR, oldC, null);
		getBoard().setPiece(r, c, moving);
		if(inCheck()) {
			getBoard().setPiece(oldR, oldC, moving);
			getBoard().setPiece(r, c, eaten);
			return false;
		}
		getBoard().setPiece(oldR, oldC, moving);
		getBoard().setPiece(r, c, eaten);
		return true;
	}
	
	public boolean inCheck() {
		return checkLs() || checkLines() || checkDiagonals() || checkPawns() || checkKing();
	}
	
	public void inCheckMate() {
		Piece tempChecking = checking;
		if(checking.canBeBlocked(getR(), getC()))
			return;
		ArrayList<Piece> canBeEatenBy = checking.canBeEatenBy();
		for(Piece piece : canBeEatenBy) {
			checking = tempChecking;
			if(hypoMove(checking.getR(), checking.getC(), piece)) {
				return;
			}
		}
		for(int i = 0; i < 3; i++) {
			if(hypoMove(getR() + 1, getC() - 1 + i, this)) 
				return;
		}	
		for(int i = 0; i < 3; i++) {
			if(hypoMove(getR() - 1, getC() - 1 + i, this))
				return;
		}	
		if(hypoMove(getR(), getC() - 1, this)) 
			return;
		if(hypoMove(getR(), getC() + 1, this))
			return;
		getBoard().endGame();
		getBoard().getSound().gameEndSound();
	}
	
	private boolean checkLines() {
		Piece piece;
		
		for(int i = getR() + 1; i < 8; i++) {
			piece = getBoard().getPiece(i, getC());
			if(piece != null) {
				if(piece.isWhite() == isWhite())
					break;
				if(piece instanceof Rook || piece instanceof Queen) {
					checking = piece;
					return true;
				}
				break;
			}
		}
		for(int i = getR() - 1; i > -1; i--) {
			piece = getBoard().getPiece(i, getC());
			if(piece != null) {
				if(piece.isWhite() == isWhite())
					break;
				if(piece instanceof Rook || piece instanceof Queen) {
					checking = piece;
					return true;
				}
				break;
			}
		}
		for(int i = getC() + 1; i < 8; i++) {
			piece = getBoard().getPiece(getR(), i);
			if(piece != null) { 
				if(piece.isWhite() == isWhite())
					break;
				if(piece instanceof Rook || piece instanceof Queen) {
					checking = piece;
					return true;
				}
				break;
			}
		}
		for(int i = getC() - 1; i > -1; i--) {
			piece = getBoard().getPiece(getR(), i);
			if(piece != null) {
				if(piece.isWhite() == isWhite())
					break;
				if(piece instanceof Rook || piece instanceof Queen) {
					checking = piece;
					return true;
				}
				break;
			}
		}
		
		return false;
	}
	
	private boolean checkDiagonals() {
		Piece piece;
		int limit = Math.min(7 - getC(), getR()) + 1;
		
		for(int i = 1; i < limit; i++) {
			piece = getBoard().getPiece(getR() - i, getC() + i);
			if(piece != null) {
				if(piece.isWhite() == isWhite())
					break;
				if(piece instanceof Bishop || piece instanceof Queen) {
					checking = piece;
					return true;
				}
				break;
			}
		}
		
		limit = Math.min(7 - getC(), 7 - getR()) + 1;
		
		for(int i = 1; i < limit; i++) {
			piece = getBoard().getPiece(getR() + i, getC() + i);
			if(piece != null) {
				if(piece.isWhite() == isWhite())
					break;
				if(piece instanceof Bishop || piece instanceof Queen) {
					checking = piece;
					return true;
				}
				break;
			}
		}
		
		limit = Math.min(getC(), 7 - getR()) + 1;
		
		for(int i = 1; i < limit; i++) {
			piece = getBoard().getPiece(getR() + i, getC() - i);
			if(piece != null) {
				if(piece.isWhite() == isWhite())
					break;
				if(piece instanceof Bishop || piece instanceof Queen) {
					checking = piece;
					return true;
				}
				break;
			}
		}
		
		limit = Math.min(getC(), getR()) + 1;
		
		for(int i = 1; i < limit; i++) {
			piece = getBoard().getPiece(getR() - i, getC() - i);
			if(piece != null) {
				if(piece.isWhite() == isWhite())
					break;
				if(piece instanceof Bishop || piece instanceof Queen) {
					checking = piece;
					return true;
				}
				break;
			}
		}
		
		return false;
	}
	
	private boolean checkLs() {
		Piece piece;
		
		if(withinRange(getR() - 2, getC() + 1)) {
			piece = getBoard().getPiece(getR() - 2, getC() + 1);
			if(piece instanceof Knight && piece.isWhite() != isWhite()) {
				checking = piece;
				return true;
			}
		}
		if(withinRange(getR() - 1, getC() + 2)) {
			piece = getBoard().getPiece(getR() - 1, getC() + 2);
			if(piece instanceof Knight && piece.isWhite() != isWhite()) {
				checking = piece;
				return true;
			}
		}
		if(withinRange(getR() + 1, getC() + 2)) {
			piece = getBoard().getPiece(getR() + 1, getC() + 2);
			if(piece instanceof Knight && piece.isWhite() != isWhite()) {
				checking = piece;
				return true;
			}
		}
		if(withinRange(getR() + 2, getC() + 1)) {
			piece = getBoard().getPiece(getR() + 2, getC() + 1);
			if(piece instanceof Knight && piece.isWhite() != isWhite()) {
				checking = piece;
				return true;
			}
		}
		if(withinRange(getR() + 2, getC() - 1)) {
			piece = getBoard().getPiece(getR() + 2, getC() - 1);
			if(piece instanceof Knight && piece.isWhite() != isWhite()) {
				checking = piece;
				return true;
			}
		}
		if(withinRange(getR() + 1, getC() - 2)) {
			piece = getBoard().getPiece(getR() + 1, getC() - 2);
			if(piece instanceof Knight && piece.isWhite() != isWhite()) {
				checking = piece;
				return true;
			}
		}
		if(withinRange(getR() - 1, getC() - 2)) {
			piece = getBoard().getPiece(getR() - 1, getC() - 2);
			if(piece instanceof Knight && piece.isWhite() != isWhite()) {
				checking = piece;
				return true;
			}
		}
		if(withinRange(getR() - 2, getC() - 1)) {
			piece = getBoard().getPiece(getR() - 2, getC() - 1);
			if(piece instanceof Knight && piece.isWhite() != isWhite()) {
				checking = piece;
				return true;
			}
		}
		
		return false;
	}
	
	private boolean checkPawns() {
		if(isWhite() || getBoard().isMirrored()) {
			if(getR() - 1 < 0)
				return false;
			
			Piece piece;
			
			if(getC() - 1 > 0) {
				piece = getBoard().getPiece(getR() - 1, getC() - 1);
				if(piece instanceof Pawn && piece.isWhite() != isWhite()) {
					checking = piece;
					return true;
				}
			}
			
			if(getC() + 1 < 8) {
				piece = getBoard().getPiece(getR() - 1, getC() + 1);
				if(piece instanceof Pawn && piece.isWhite() != isWhite()) {
					checking = piece;
					return true;
				}
			}
		}
		else {
			if(getR() + 1 > 7)
				return false;
			
			Piece piece;
			
			if(getC() - 1 > 0) {
				piece = getBoard().getPiece(getR() + 1, getC() - 1);
				if(piece instanceof Pawn && piece.isWhite() != isWhite()) {
					checking = piece;
					return true;
				}
			}
			
			if(getC() + 1 < 8) {
				piece = getBoard().getPiece(getR() + 1, getC() + 1);
				if(piece instanceof Pawn && piece.isWhite() != isWhite()) {
					checking = piece;
					return true;
				}
			}
		}
		
		return false;
	}
	
	private boolean checkKing() {
		for(int i = 0; i < 3; i++) {
			if(getBoard().getPiece(getR() + 1, getC() - 1 + i) instanceof King) {
				checking = getBoard().getPiece(getR() + 1, getC() - 1 + i);
				return true;
			}
		}	
		for(int i = 0; i < 3; i++) {
			if(getBoard().getPiece(getR() - 1, getC() - 1 + i) instanceof King) {
				checking = getBoard().getPiece(getR() - 1, getC() - 1 + i);
				return true;
			}
		}	
		if(getBoard().getPiece(getR(), getC() - 1) instanceof King) {
			checking = getBoard().getPiece(getR(), getC() - 1);
			return true;
		}
		if(getBoard().getPiece(getR(), getC() + 1) instanceof King) {
			checking = getBoard().getPiece(getR(), getC() + 1);
			return true;
		}
	
		return false;
	}
	
	private boolean checkCastleRight() {
		if(castle && right.canCastle() && getBoard().getPiece(right.getR(), right.getC()).equals(right)) {
			King temp = (King) copy();
			for(int i = 1; i < 3; i++) {
				if(getBoard().getPiece(getR(), getC() + i) != null)
					return false;
			}
			int oldC = getC();
			for(int i = 0; i < 2; i++) {
				if(inCheck()) {
					getBoard().setPiece(getR(), getC(), null);
					getBoard().setPiece(getR(), oldC, this);
					return false;
				}
				getBoard().setPiece(getR(), getC(), null);
				getBoard().setPiece(getR(), getC() + 1, this);
			}
			if(canRecordMove())
				getBoard().getHistory().add(new Move(temp, right.copy(), getR(), getC(), getBoard()));
			disableMoveRecord();
			getBoard().setPiece(getR(), 7, null);
			getBoard().setPiece(getR(), getC() - 1, right);
			castle = false;
			right.cantCastle();
			King king;
			if(isWhite()) 
				king = getBoard().blackKing();
			else
				king = getBoard().whiteKing();
			if(king.inCheck())
				getBoard().getSound().checkSound();
			else
				getBoard().getSound().castleSound();
			return true;
		}
		return false;
	}
	private boolean checkCastleLeft() {
		if(castle && left.canCastle() && getBoard().getPiece(left.getR(), left.getC()).equals(left)) {
			King temp = (King) copy();
			for(int i = 1; i < 3; i++) {
				if(getBoard().getPiece(getR(), getC() - i) != null) {
					return false;
				}
			}
			int oldC = getC();
			for(int i = 0; i < 2; i++) {
				if(inCheck()) {
					getBoard().setPiece(getR(), getC(), null);
					getBoard().setPiece(getR(), oldC, this);
					return false;
				}
				getBoard().setPiece(getR(), getC(), null);
				getBoard().setPiece(getR(), getC() - 1, this);
			}
			if(canRecordMove())
				getBoard().getHistory().add(new Move(temp, left.copy(), getR(), getC(), getBoard()));
			disableMoveRecord();
			getBoard().setPiece(getR(), 0, null);
			getBoard().setPiece(getR(), getC() + 1, left);
			castle = false;
			left.cantCastle();
			King king;
			if(isWhite()) 
				king = getBoard().blackKing();
			else
				king = getBoard().whiteKing();
			if(king.inCheck())
				getBoard().getSound().checkSound();
			else
				getBoard().getSound().castleSound();
			return true;
		}
		return false;
	}
	
	private boolean withinRange(int r, int c) {
		return r > -1 && r < 8 && c > -1 && c < 8;
	}
	
	public void cantCastle() {
		castle = false;
	}
	
	public Piece copy() {
		King copy = new King(getR(), getC(), isWhite(), getBoard(), getIcon(), left, right);
		if(!castle) {
			copy.cantCastle();
		}
		return copy;
	}
	
	public void setRight(Rook rook) {
		right = rook;
	}
	
	public void setLeft(Rook rook) {
		left = rook;
	}

	public boolean canBeBlocked(int r, int c) {
		return false;
	}
}
