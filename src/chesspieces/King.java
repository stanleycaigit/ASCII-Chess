package chesspieces;

/**
 * The King class represents a King piece in a chess game
 * 
 * @author Eric Zhang
 * @author Stanley Cai
 *
 */

public class King extends ChessPiece {
	/**
	 * Constructor for an instance of a King
	 * 
	 * @param board - the game board
	 * @param row   - starting row index for the King
	 * @param col   - starting column index for the King
	 * @param team  - what team (black or white)
	 * 
	 */
	public King( ChessPiece[][] board, int row, int col, Team team ) {
		super( board, row, col, team, null );
	}

	/**
	 * Attempt to move the King from its current location to [targetRow, targetCol]
	 * 
	 * @param targetRow         - row position to move to
	 * @param targetCol         - column position to move to
	 * @param currentMoveNumber - move number of the current move in the game
	 * @return whether the King successfully moved to [targetRow, targetCol]
	 */
	public boolean attemptMoveTo( int targetRow, int targetCol, int currentMoveNumber ) {
		if ( canMoveTo( targetRow, targetCol, true ) ) {
			setLocation( targetRow, targetCol );

			incrementNumMoves();
			setLastMoveNumber( currentMoveNumber );
			return true;
		}
		else if ( canCastleRightTo( targetRow, targetCol ) ) {
			// Vacate current position
			setLocation( targetRow, targetCol );

			// Move Rook to the left of King
			Rook targetRook = (Rook) getBoard()[getRow()][7];
			targetRook.setLocation( getRow(), 5 );

			incrementNumMoves();
			setLastMoveNumber( currentMoveNumber );
			targetRook.incrementNumMoves();
			targetRook.setLastMoveNumber( currentMoveNumber );
			return true;
		}
		else if ( canCastleLeftTo( targetRow, targetCol ) ) {
			// Vacate current position
			setLocation( targetRow, targetCol );

			// Move Rook to the right of King
			Rook targetRook = (Rook) getBoard()[getRow()][0];
			targetRook.setLocation( getRow(), 3 );

			incrementNumMoves();
			setLastMoveNumber( currentMoveNumber );
			targetRook.incrementNumMoves();
			targetRook.setLastMoveNumber( currentMoveNumber );
			return true;
		}
		else {
			return false;
		}
	}

	/**
	 * Determine if the King can legally move to [targetRow, targetCol]
	 * 
	 * @param targetRow               - the row for the piece to move to
	 * @param targetCol               - the column for the piece to move to
	 * @param checkPutSelfKingInCheck - checks whether own king will be in check after your own move
	 * @return whether the King can legally move to [targetRow, targetCol]
	 */
	@Override
	public boolean canMoveTo( int targetRow, int targetCol, boolean checkPutSelfKingInCheck ) {
		if ( targetRow < 0 || targetRow >= 8 || targetCol < 0 || targetCol >= 8 ) {
			// Out of bounds
			return false;
		}

		int rowChange = targetRow - getRow();
		int colChange = targetCol - getCol();
		int distanceSquared = rowChange * rowChange + colChange * colChange;
		// If distance == 1 or distance == Math.sqrt( 2 ) (account for double-precision error), then
		// this King can move to that spot
		if ( distanceSquared == 1 || distanceSquared == 2 ) {
			ChessPiece targetPiece = getBoard()[targetRow][targetCol];
			// Cannot move onto a piece of the same team, and cannot move onto a position that will put it in check
			if ( targetPiece != null && targetPiece.getTeam() == getTeam() ) {
				return false;
			}

			// Call isInCheckAt instead of willPutSelfKingInCheckAt
			// - First, selfKing is null
			// - Otherwise, if selfKing = this, willPutSelfKingInCheckAt( row, col ) moves King to target location and
			// calls selfKing.isInCheck() = isInCheck(), which is equivalent to isInCheckAt( row, col )
			return !checkPutSelfKingInCheck || !isInCheckAt( targetRow, targetCol );
		}

		return false;
	}

	/**
	 * Determine if the King can castle to the right to [targetRow, targetCol]
	 * 
	 * @param targetRow - the row for the piece to move to
	 * @param targetCol - the column for the piece to move to
	 * @return whether the King can castle to the right
	 */
	public boolean canCastleRightTo( int targetRow, int targetCol ) {
		if ( getNumMoves() != 0 || isInCheck() ) {
			return false;
		}

		// Must move to a specific location
		if ( getTeam() == Team.WHITE && !( targetRow == 7 && targetCol == 6 ) ) {
			return false;
		}
		else if ( getTeam() == Team.BLACK && !( targetRow == 0 && targetCol == 6 ) ) {
			return false;
		}

		Rook targetRook = null;
		try {
			if ( getTeam() == Team.WHITE ) {
				targetRook = (Rook) getBoard()[7][7];
			}
			else {
				targetRook = (Rook) getBoard()[0][7];
			}
		}
		catch ( ClassCastException e ) {
			return false;
		}

		// Rook cannot have moved before
		if ( targetRook.getNumMoves() != 0 ) {
			// Target rook cannot have moved before
			return false;
		}

		// Check locations that the King will go to; they cannot be occupied or under attack
		if ( getBoard()[getRow()][5] != null || isInCheckAt( getRow(), 5 ) ) {
			return false;
		}
		if ( getBoard()[getRow()][6] != null || isInCheckAt( getRow(), 6 ) ) {
			return false;
		}
		return true;
	}

	/**
	 * Determine if the King can castle to the left to [targetRow, targetCol]
	 * 
	 * @param targetRow - the row for the piece to move to
	 * @param targetCol - the column for the piece to move to
	 * @return whether the King can castle to the left
	 */
	public boolean canCastleLeftTo( int targetRow, int targetCol ) {
		if ( getNumMoves() != 0 || isInCheck() ) {
			return false;
		}

		// Must move to a specific location
		if ( getTeam() == Team.WHITE && !( targetRow == 7 && targetCol == 2 ) ) {
			return false;
		}
		else if ( getTeam() == Team.BLACK && !( targetRow == 0 && targetCol == 2 ) ) {
			return false;
		}

		Rook targetRook = null;
		try {
			if ( getTeam() == Team.WHITE ) {
				targetRook = (Rook) getBoard()[7][0];
			}
			else {
				targetRook = (Rook) getBoard()[0][0];
			}
		}
		catch ( ClassCastException e ) {
			return false;
		}

		// Rook cannot have moved before
		if ( targetRook != null && targetRook.getNumMoves() != 0 ) {
			// Target rook cannot have moved before
			return false;
		}

		// Check locations that the King will go to; they cannot be occupied or under attack
		if ( getBoard()[getRow()][3] != null || isInCheckAt( getRow(), 3 ) ) {
			return false;
		}
		if ( getBoard()[getRow()][2] != null || isInCheckAt( getRow(), 2 ) ) {
			return false;
		}
		return true;
	}

	/**
	 * Determine whether the King is currently in check
	 * 
	 * @return whether the King is currently in Check
	 */
	public boolean isInCheck() {
		return isInCheckAt( getRow(), getCol() );
	}

	/**
	 * Determine if the King will be in check after moving to [targetRow, targetCol]. Precondition: the King can legally
	 * move to this [targetRow, targetCol] otherwise
	 * 
	 * @param targetRow - the row for the piece to move to
	 * @param targetCol - the column for the piece to move to
	 * @return whether the King will be in check after moving to [targetRow, targetCol]
	 */
	private boolean isInCheckAt( int targetRow, int targetCol ) {
		ChessPiece previousChessPiece = getBoard()[targetRow][targetCol];
		int previousRow = getRow();
		int previousCol = getCol();

		// Move King to specified location
		setLocation( targetRow, targetCol );

		boolean willBePutInCheck = false;
		for ( int i = 0; i < 8; i++ ) {
			for ( int j = 0; j < 8; j++ ) {
				ChessPiece enemyPiece = getBoard()[i][j];
				// Cannot be put in check by own piece
				if ( enemyPiece == null || enemyPiece.getTeam() == getTeam() ) {
					continue;
				}

				// If an enemy Pawn can en pessant to the King's position, it can also directly move to the King's
				// position
				// An enemy King won't be able to castle to the King's position, as that will require the King to block
				// the path between the King and the castling Rook
				// Therefore, no need to check if enemyPiece can en pessant to castle to the King's position
				if ( enemyPiece.canMoveTo( targetRow, targetCol, false ) ) {
					willBePutInCheck = true;
				}
			}
		}

		// Restore King to original position
		setLocation( previousRow, previousCol );
		getBoard()[targetRow][targetCol] = previousChessPiece;

		return willBePutInCheck;
	}

	/**
	 * Determine if the King is currently in checkmate
	 * 
	 * @return whether the King is currently in checkmate
	 */
	public boolean isInCheckmate() {
		if ( !isInCheck() ) {
			return false;
		}

		// King is in checkmate if none of its own pieces can move anywhere
		for ( int index1 = 0; index1 < 64; index1++ ) {
			int r1 = index1 / 8;
			int c1 = index1 % 8;

			ChessPiece friendlyPiece = getBoard()[r1][c1];
			if ( friendlyPiece == null || friendlyPiece.getTeam() != getTeam() ) {
				continue;
			}

			for ( int index2 = 0; index2 < 64; index2++ ) {
				int r2 = index2 / 8;
				int c2 = index2 % 8;

				if ( friendlyPiece.canMoveTo( r2, c2, true ) ) {
					return false;
				}
			}
		}

		return true;
	}

	/**
	 * Returns a String representation of King along with its team
	 * @return a String representation of King along with its team
	 */
	public String toString() {
		if ( getTeam() == Team.WHITE ) {
			return "wK";
		}
		else {
			return "bK";
		}
	}
}
