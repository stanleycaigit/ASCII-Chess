package chesspieces;

/**
 * The Pawn class represents a Pawn piece in a chess game
 * 
 * @author Eric Zhang
 * @author Stanley Cai
 *
 */
public class Pawn extends ChessPiece {

	/**
	 * Constructor for an instance of a Pawn
	 * 
	 * @param board    - the game board
	 * @param row      - starting row index for the Pawn
	 * @param col      - starting column index for the Pawn
	 * @param team     - what team (black or white)
	 * @param selfKing - King instance of the same team
	 * 
	 */
	public Pawn( ChessPiece[][] board, int row, int col, Team team, King selfKing ) {
		super( board, row, col, team, selfKing );
	}

	/**
	 * Attempt to move the Pawn from its current location to [targetRow, targetCol]. NOTE: attemptMoveTo does not take
	 * care of promotions!
	 * 
	 * @param targetRow         - row position to move to
	 * @param targetCol         - column position to move to
	 * @param currentMoveNumber - move number of the current move in the game
	 * @return whether the Pawn successfully moved to [targetRow, targetCol]
	 */
	@Override
	public boolean attemptMoveTo( int targetRow, int targetCol, int currentMoveNumber ) {
		if ( canMoveTo( targetRow, targetCol, true ) ) {
			setLocation( targetRow, targetCol );

			incrementNumMoves();
			setLastMoveNumber( currentMoveNumber );
			return true;
		}
		else if ( canEnPessantLeftTo( targetRow, targetCol, currentMoveNumber ) ) {
			// Remove target pawn from board
			getBoard()[getRow()][getCol() - 1] = null;

			setLocation( targetRow, targetCol );
			incrementNumMoves();
			setLastMoveNumber( currentMoveNumber );
			return true;
		}
		else if ( canEnPessantRightTo( targetRow, targetCol, currentMoveNumber ) ) {
			// Remove target pawn from board
			getBoard()[getRow()][getCol() + 1] = null;

			setLocation( targetRow, targetCol );
			incrementNumMoves();
			setLastMoveNumber( currentMoveNumber );
			return true;
		}
		else {
			return false;
		}
	}

	/**
	 * Determine if the Pawn can legally move to [targetRow, targetCol]
	 * 
	 * @param targetRow               - the row for the piece to move to
	 * @param targetCol               - the column for the piece to move to
	 * @param checkPutSelfKingInCheck - checks whether own king will be in check after your own move
	 * @return whether the Pawn can legally move to [targetRow, targetCol]
	 */
	@Override
	public boolean canMoveTo( int targetRow, int targetCol, boolean checkPutSelfKingInCheck ) {
		// Out of bounds
		if ( targetRow >= 8 || targetRow < 0 || targetCol >= 8 || targetCol < 0 ) {
			return false;
		}

		ChessPiece targetPiece = getBoard()[targetRow][targetCol];
		int rowChange = targetRow - getRow();
		int colChange = targetCol - getCol();
		if ( ( getTeam() == Team.WHITE && rowChange == -1 ) || ( getTeam() == Team.BLACK && rowChange == 1 ) ) {
			// Can move straight up/down (depending on team) by one if the target location is empty
			if ( colChange == 0 && targetPiece == null ) {
				return !checkPutSelfKingInCheck || !willPutSelfKingInCheckAt( targetRow, targetCol );
			}
			// Can move up by one and left/right by one if the target location contains an enemy piece
			else if ( Math.abs( colChange ) == 1 && ( targetPiece != null && targetPiece.getTeam() != getTeam() ) ) {
				return !checkPutSelfKingInCheck || !willPutSelfKingInCheckAt( targetRow, targetCol );
			}
			else {
				return false;
			}
		}
		else if ( ( getTeam() == Team.WHITE && rowChange == -2 ) || ( getTeam() == Team.BLACK && rowChange == 2 ) ) {
			// Can move straight up/down (depending on team) by two if the target location is empty and numMoves == 0
			if ( colChange == 0 && targetPiece == null && getNumMoves() == 0 ) {
				return !checkPutSelfKingInCheck || !willPutSelfKingInCheckAt( targetRow, targetCol );
			}
			else {
				return false;
			}
		}
		else {
			return false;
		}
	}

	/**
	 * Determine if the Pawn can en pessant to the right to [targetRow, targetCol]. The parameter
	 * checkPutSelfKingInCheck is not necessary as there isn't any place where checkPutSelfKingInCheck would equal false
	 * when calling this method.
	 * 
	 * @param targetRow         - the row for the piece to move to
	 * @param targetCol         - the column for the piece to move to
	 * @param currentMoveNumber - move number of the current move in the game
	 * @return whether the Pawn can en pessant to the right
	 */
	public boolean canEnPessantRightTo( int targetRow, int targetCol, int currentMoveNumber ) {
		if ( getTeam() == Team.WHITE && !( getRow() == 3 && targetRow == 2 ) ) {
			return false;
		}
		if ( getTeam() == Team.BLACK && !( getRow() == 4 && targetRow == 5 ) ) {
			return false;
		}
		if ( getCol() == 7 ) {
			return false;
		}

		int colChange = targetCol - getCol();
		if ( colChange == 1 ) {
			ChessPiece adjacentRightPiece = getBoard()[getRow()][getCol() + 1];
			if ( adjacentRightPiece instanceof Pawn ) {
				Pawn targetPawn = (Pawn) adjacentRightPiece;
				if ( targetPawn != null && getTeam() == targetPawn.getTeam() ) {
					// Target pawn must be on the enemy team
					return false;
				}
				if ( targetPawn.getLastMoveNumber() != currentMoveNumber - 1 || targetPawn.getNumMoves() != 1 ) {
					// Target pawn must have moved immediately before
					return false;
				}

				return !willPutSelfKingInCheckAt( targetRow, targetCol );
			}
		}
		return false;
	}

	/**
	 * Determine if the Pawn can en pessant to the left to [targetRow, targetCol]. The parameter checkPutSelfKingInCheck
	 * is not necessary as there isn't any place where checkPutSelfKingInCheck would equal false when calling this
	 * method.
	 * 
	 * @param targetRow         - the row for the piece to move to
	 * @param targetCol         - the column for the piece to move to
	 * @param currentMoveNumber - move number of the current move in the game
	 * @return whether the Pawn can en pessant to the left
	 */
	public boolean canEnPessantLeftTo( int targetRow, int targetCol, int currentMoveNumber ) {
		if ( getTeam() == Team.WHITE && !( getRow() == 3 && targetRow == 2 ) ) {
			return false;
		}
		if ( getTeam() == Team.BLACK && !( getRow() == 4 && targetRow == 5 ) ) {
			return false;
		}
		if ( getCol() == 0 ) {
			return false;
		}

		int colChange = targetCol - getCol();
		if ( colChange == -1 ) {
			ChessPiece adjacentLeftPiece = getBoard()[getRow()][getCol() - 1];
			if ( adjacentLeftPiece instanceof Pawn ) {
				Pawn targetPawn = (Pawn) adjacentLeftPiece;
				if ( targetPawn != null && getTeam() == targetPawn.getTeam() ) {
					// Target pawn must be on the enemy team
					return false;
				}
				if ( targetPawn.getLastMoveNumber() != currentMoveNumber - 1 || targetPawn.getNumMoves() != 1 ) {
					// Target pawn must have moved immediately before
					return false;
				}

				return !willPutSelfKingInCheckAt( targetRow, targetCol );
			}
		}
		return false;
	}

	/**
	 * Promote the Pawn if it is in a position to be promoted.
	 * 
	 * @param promotionPiece    - the chess piece to promote to
	 * @param currentMoveNumber - move number of the current move in the game
	 * @return whether the promotion was successful
	 */
	public boolean promote( char promotionPiece, int currentMoveNumber ) {
		if ( !willPawnBePromoted( this, getRow() ) ) {
			return false;
		}
		switch ( promotionPiece ) {
			case 'Q': {
				getBoard()[getRow()][getCol()] = new Queen( getBoard(), getRow(), getCol(), getTeam(), getSelfKing() );
				return true;
			}
			case 'R': {
				getBoard()[getRow()][getCol()] = new Rook( getBoard(), getRow(), getCol(), getTeam(), getSelfKing() );
				return true;
			}
			case 'B': {
				getBoard()[getRow()][getCol()] = new Bishop( getBoard(), getRow(), getCol(), getTeam(), getSelfKing() );
				return true;
			}
			case 'N': {
				getBoard()[getRow()][getCol()] = new Knight( getBoard(), getRow(), getCol(), getTeam(), getSelfKing() );
				return true;
			}
			default: {
				return false;
			}
		}

	}

	/**
	 * Determine if the Pawn will be promoted at the indicated row.
	 * 
	 * @param pawn      - the Pawn in question
	 * @param targetRow - the row that the Pawn will move to
	 * @return if the Pawn will be promoted after moving to the target row
	 */
	public static boolean willPawnBePromoted( Pawn pawn, int targetRow ) {
		return ( pawn.getTeam() == Team.WHITE && targetRow == 0 ) || ( pawn.getTeam() == Team.BLACK && targetRow == 7 );
	}

	/**
	 * Returns a String representation of Pawn along with its team
	 * @return a String representation of Pawn along with its team
	 */
	public String toString() {
		if ( getTeam() == Team.WHITE ) {
			return "wP";
		}
		else {
			return "bP";
		}
	}
}
