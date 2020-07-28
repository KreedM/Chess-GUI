package logic;

import java.awt.event.KeyListener;
import java.io.IOException;
import javax.imageio.ImageIO;
import pieces.Bishop;
import pieces.King;
import pieces.Knight;
import pieces.Pawn;
import pieces.Piece;
import pieces.Queen;
import pieces.Rook;

public class Move {
	private int fromRow, fromCol;
	private String move, specific;
	private Piece moving, eaten;
	private BoardListener listener;
	
	public Move(Piece moving, Piece eaten, int toRow, int toCol, Board board) {
		this.moving = moving;
		this.eaten = eaten;
		if(board.isMirrored() && !board.getTurn()) {
			fromRow = 7 - moving.getR();
			fromCol = 7 - moving.getC();
			move = "" + ((char)(97 + (7 - toCol))) + (toRow + 1);
		}
		else {
			fromRow = moving.getR();
			fromCol = moving.getC();
			move = "" + ((char)(97 + toCol)) + (8 - toRow);
		}
		if(eaten != null && moving.isWhite() == eaten.isWhite()) {
			int diffC = eaten.getC() - moving.getC();
			if(board.isMirrored() && !moving.isWhite()) {
				if(diffC > 0)
					move = "O-O-O";
				else
					move = "O-O";
			}
			else {
				if(diffC > 0)
					move = "O-O";
				else
					move = "O-O-O";
			}
		}
		listener = (BoardListener)board.getListeners(KeyListener.class)[0];
	}
	
	public void revert(Board board) {
		int r, c;
		if(eaten != null) {
			if(board.isMirrored() && board.getTurn()) {
				r = 7 - eaten.getR();
				c = 7 - eaten.getC();
			}
			else {
				r = eaten.getR();
				c = eaten.getC(); 
			}
		}
		else {
			r = 8 - Integer.parseInt(move.substring(1));
			c = move.charAt(0) - 97;
		}
		if(move.equals("O-O")) {
			board.setPiece(r, c - 1, null);
			board.setPiece(r, c - 2, null);
			if(board.isMirrored() && !moving.isWhite())
				((King)moving).setLeft((Rook)eaten);
			else 
				((King)moving).setRight((Rook)eaten);
		}	
		else if(move.equals("O-O-O")) {
			board.setPiece(r, c + 2, null);
			board.setPiece(r, c + 3, null);
			if(board.isMirrored() && !moving.isWhite())
				((King)moving).setRight((Rook)eaten);
			else 
				((King)moving).setLeft((Rook)eaten);
		}
		else
			board.setPiece(8 - Integer.parseInt(move.substring(1)), move.charAt(0) - 97, null);
		board.setPiece(r, c, eaten);
		board.setPiece(fromRow, fromCol, moving);
		board.setSelectedR(fromRow);
		board.setSelectedC(fromCol);
		board.setSelectedRTwo(-1);
		board.setSelectedCTwo(-1);
		listener.setSelectedPiece(moving);
		if(moving instanceof King)
			board.setKing((King) moving);
		King king;
		if(move.equals("O-O") || move.equals("O-O-O"))
			board.getSound().castleSound();
		else {
			if(moving.isWhite()) 
				king = board.blackKing();
			else
				king = board.whiteKing();
			if(king.inCheck())
				board.getSound().checkSound();
			else 
				board.getSound().moveSound();
		}
	}
	
	public void restoreRevert(Board board) {
		int r, c;
		if(board.isMirrored() && !board.getTurn()) {
			if(move.equals("O-O")) {
				r = moving.getR(); 
				c = moving.getC() - 2;
			}
			else if(move.equals("O-O-O")) {
				r = moving.getR(); 
				c = moving.getC() + 2;
			}
			else {
				r = Integer.parseInt(move.substring(1)) - 1;
				c = 7 - (move.charAt(0) - 97);
			}
			board.setSelectedRTwo(7 - fromRow);
			board.setSelectedCTwo(7 - fromCol);
		}
		else {
			if(move.equals("O-O")) {
				r = moving.getR(); 
				c = moving.getC() + 2;
			}
			else if(move.equals("O-O-O")) {
				r = moving.getR(); 
				c = moving.getC() - 2;
			}
			else {
				r = 8 - Integer.parseInt(move.substring(1));
				c = move.charAt(0) - 97;
			}
			board.setSelectedRTwo(fromRow);
			board.setSelectedCTwo(fromCol);
		}
		Piece temp = moving.copy();
		if(temp instanceof King) {
			board.setKing((King) temp);
			board.setPiece(moving.getR(), moving.getC(), temp);
		}
		temp.disableMoveRecord();
		if(move.equals("O-O") || move.equals("O-O-O"))
			eaten = eaten.copy();
		else if(moving instanceof Pawn && (r == 0 || r == 7)) {
			char promotedTo = 'Q';
			for(int i = move.length() - 1; i > -1; i--) {
				if(move.charAt(i) == 'Q' || move.charAt(i) == 'N' || move.charAt(i) == 'B' || move.charAt(i) == 'R') {
					promotedTo = move.charAt(i);
					break;
				}
			}
			try {
				if(moving.isWhite()) {
					switch(promotedTo) {
						case 'Q': temp = new Queen(temp.getR(), temp.getC(), temp.isWhite(), board, ImageIO.read(getClass().getResource("/img/white/q.png"))); board.getHistory().promotedTo("Q"); break;
						case 'R': temp = new Rook(temp.getR(), temp.getC(), temp.isWhite(), board, ImageIO.read(getClass().getResource("/img/white/r.png"))); board.getHistory().promotedTo("R"); break;
						case 'B': temp = new Bishop(temp.getR(), temp.getC(), temp.isWhite(), board, ImageIO.read(getClass().getResource("/img/white/b.png"))); board.getHistory().promotedTo("B"); break;
						case 'N': temp = new Knight(temp.getR(), temp.getC(), temp.isWhite(), board, ImageIO.read(getClass().getResource("/img/white/n.png"))); board.getHistory().promotedTo("N");
					}
				}
				else {
					switch(promotedTo) {
						case 'Q': temp = new Queen(temp.getR(), temp.getC(), temp.isWhite(), board, ImageIO.read(getClass().getResource("/img/black/q.png"))); board.getHistory().promotedTo("Q"); break;
						case 'R': temp = new Rook(temp.getR(), temp.getC(), temp.isWhite(), board, ImageIO.read(getClass().getResource("/img/black/r.png"))); board.getHistory().promotedTo("R"); break;
						case 'B': temp = new Bishop(temp.getR(), temp.getC(), temp.isWhite(), board, ImageIO.read(getClass().getResource("/img/black/b.png"))); board.getHistory().promotedTo("B"); break;
						case 'N': temp = new Knight(temp.getR(), temp.getC(), temp.isWhite(), board, ImageIO.read(getClass().getResource("/img/black/n.png"))); board.getHistory().promotedTo("N");
					}
				}
			}
			catch(IOException e) {}
		}
		temp.playSound(true);
		temp.checkMove(r, c);
		board.setPiece(r, c, temp);
		if(board.isMirrored() && !board.getTurn()) 
			board.setPiece(7 - fromRow, 7 - fromCol, null);
		else 
			board.setPiece(fromRow, fromCol, null);
		board.setSelectedR(r);
		board.setSelectedC(c);
		King king;
		if(!move.equals("O-O") && !move.equals("O-O-O")) {
			if(!temp.isWhite())
				king = board.whiteKing();
			else
				king = board.blackKing();
			if(king.inCheck()) {
				if(temp.canPlaySound())
					board.getSound().checkSound();
				king.inCheckMate();
			}
			else if(eaten == null && temp.canPlaySound())
				board.getSound().moveSound();
			else if(temp.canPlaySound()) {
				board.getSound().captureSound();
			}
		}
		listener.setSelectedPiece(null);
	}
	
	public String getMoveString(Board board) {
		if(move.equals("O-O") || move.equals("O-O-O"))
			return move;
		String tempMoveString = move;
		if(eaten != null) {
			tempMoveString = "x" + tempMoveString;
			if(moving instanceof Pawn) {
				if(board.isMirrored() && !moving.isWhite())
					tempMoveString = "" + (char)(97 + (7 - moving.getC())) + tempMoveString;
				else 
					tempMoveString = "" + (char)(97 + (moving.getC())) + tempMoveString;
			}
		}
		if(moving instanceof King) 
			tempMoveString = "K" + tempMoveString;
		else if(moving instanceof Queen) 
			tempMoveString = "Q" + tempMoveString;
		else if(moving instanceof Rook) 
			tempMoveString = "R" + tempMoveString;
		else if(moving instanceof Knight) 
			tempMoveString = "N" + tempMoveString;
		else if(moving instanceof Bishop)
			tempMoveString = "B" + tempMoveString;
		
		return tempMoveString;
	}
	public void setSpecificationString(Board board) {
		int r, c;
		if(eaten != null) {
			if(board.isMirrored() && board.getTurn()) {
				r = 7 - eaten.getR();
				c = 7 - eaten.getC();
			}
			else {
				r = eaten.getR();
				c = eaten.getC(); 
			}
		}
		else {
			r = 8 - Integer.parseInt(move.substring(1));
			c = move.charAt(0) - 97;
		}
		specific = board.getSpecificationString(moving, r, c);
	}
	
	public String getSpecificationString() {
		return specific;
	}
}
