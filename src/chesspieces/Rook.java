package chesspieces;

/**
 * The Rook class represents a Rook piece in a chess game
 * 
 * @author Eric Zhang
 * @author Stanley Cai
 *
 */
public class Rook extends ChessPiece {
	/**
	 * Constructor for an instance of a Rook
	 * 
	 * @param board	- the game board
	 * @param row - starting row index for the Rook
	 * @param col - starting column index for the Rook
	 * @param team - what team (black or white)
	 * @param selfKing - King instance of the same team
	 * 
	 */
	public Rook( ChessPiece[][] board, int row, int col, Team team, King selfKing ) {
		super( board, row, col, team, selfKing );
	}

	/**
	 * Determine if the Rook can legally move to [targetRow, targetCol]
	 * 
	 * @param targetRow - the row for the piece to move to
	 * @param targetCol - the column for the piece to move to
	 * @param currentMoveNumber - move number of the current move in the game
	 * @return whether the Rook can legally move to [targetRow, targetCol]
	 */
	@Override
	public boolean attemptMoveTo( int targetRow, int targetCol, int currentMoveNumber ) {
		if ( canMoveTo( targetRow, targetCol, true ) ) {
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
	 * Attempt to move the Bishop from its current location to [targetRow, targetCol]
	 * 
	 * @param targetRow - the row for the piece to move to
	 * @param targetCol - the column for the piece to move to
	 * @param checkPutSelfKingInCheck - checks whether own king will be in check after your own move
	 * @return whether the Rook can legally move to [targetRow, targetCol]
	 */
	@Override
	public boolean canMoveTo( int targetRow, int targetCol, boolean checkPutSelfKingInCheck ) {
		// Out of bounds
		if ( targetRow >= 8 || targetRow < 0 || targetCol >= 8 || targetCol < 0 ) {
			return false;
		}

		int rowChange = targetRow - getRow();
		int colChange = targetCol - getCol();

		if ( rowChange == 0 && colChange == 0 ) {
			// Destination and target location are the same
			return false;
		}

		if ( rowChange == 0 || colChange == 0 ) {
			ChessPiece targetPiece = getBoard()[targetRow][targetCol];
			// Cannot move onto a piece of the same team
			if ( targetPiece != null && targetPiece.getTeam() == getTeam() ) {
				return false;
			}

			int currentRow = getRow();
			int currentCol = getCol();
			// Advance one space towards target location
			if ( currentRow < targetRow ) {
				currentRow++;
			}
			else if ( currentRow > targetRow ) {
				currentRow--;
			}
			if ( currentCol < targetCol ) {
				currentCol++;
			}
			else if ( currentCol > targetCol ) {
				currentCol--;
			}

			// Check each tile along the Rook's path to target destination
			while ( currentRow != targetRow || currentCol != targetCol ) {
				if ( getBoard()[currentRow][currentCol] != null ) {
					// Path to target destination is obstructed by friendly or enemy piece
					return false;
				}

				// Advance towards target location
				if ( currentRow < targetRow ) {
					currentRow++;
				}
				else if ( currentRow > targetRow ) {
					currentRow--;
				}
				if ( currentCol < targetCol ) {
					currentCol++;
				}
				else if ( currentCol > targetCol ) {
					currentCol--;
				}
			}
			return !checkPutSelfKingInCheck || !willPutSelfKingInCheckAt( targetRow, targetCol );
		}
		return false;
	}
	
	/**
	 * Returns a String representation of Rook along with its team
	 * @return a String representation of Rook along with its team
	 */
	public String toString() {
		if ( getTeam() == Team.WHITE ) {
			return "wR";
		}
		else {
			return "bR";
		}
	}
}
