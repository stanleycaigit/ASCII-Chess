package chesspieces;

/**
 * The Bishop class represents a Bishop piece in a chess game
 * 
 * @author Eric Zhang
 * @author Stanley Cai
 *
 */
public class Bishop extends ChessPiece {
	/**
	 * Constructor for an instance of a Bishop
	 * 
	 * @param board	- the game board
	 * @param row - starting row index for the Bishop
	 * @param col - starting column index for the Bishop
	 * @param team - what team (black or white)
	 * @param selfKing - King instance of the same team
	 * 
	 */
	public Bishop( ChessPiece[][] board, int row, int col, Team team, King selfKing ) {
		super( board, row, col, team, selfKing );
	}
	
	/**
	 * Attempt to move the Bishop from its current location to [targetRow, targetCol]
	 * 
	 * @param targetRow	- row position to move to
	 * @param targetCol	- column position to move to
	 * @param currentMoveNumber - move number of the current move in the game
	 * @return whether the Bishop successfully moved to [targetRow, targetCol]
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
	 * Determine if the Bishop can legally move to [targetRow, targetCol]
	 * 
	 * @param targetRow - the row for the piece to move to
	 * @param targetCol - the column for the piece to move to
	 * @param checkPutSelfKingInCheck - checks whether own king will be in check after your own move
	 * @return whether the Bishop can legally move to [targetRow, targetCol]
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

		if ( Math.abs( rowChange ) == Math.abs( colChange ) ) {
			ChessPiece targetPiece = getBoard()[targetRow][targetCol];
			// Cannot move onto a piece of the same team, and cannot move onto a position that will put it in check
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
			
			// Check each tile along the Bishop's path to target destination
			while ( currentRow != targetRow || currentCol != targetCol ) {
				if ( getBoard()[currentRow][currentCol] != null && getBoard()[currentRow][currentCol] != this ) {
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
	 * Returns a String representation of Bishop along with its team
	 * @return a String representation of Bishop along with its team
	 */
	public String toString() {
		if ( getTeam() == Team.WHITE ) {
			return "wB";
		}
		else {
			return "bB";
		}
	}
}
