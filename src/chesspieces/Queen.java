package chesspieces;

/**
 * The Queen class represents a Queen piece in a chess game
 * 
 * @author Eric Zhang
 * @author Stanley Cai
 *
 */
public class Queen extends ChessPiece {
	/**
	 * Constructor for an instance of a Queen
	 * 
	 * @param board	- the game board
	 * @param row - starting row index for the Queen
	 * @param col - starting column index for the Queen
	 * @param team - what team (black or white)
	 * @param selfKing - King instance of the same team
	 * 
	 */
	public Queen( ChessPiece[][] board, int row, int col, Team team, King selfKing ) {
		super( board, row, col, team, selfKing );
	}

	/**
	 * Attempt to move the Queen from its current location to [targetRow, targetCol] on the board
	 * 
	 * @param targetRow	- row position to go to
	 * @param targetCol	- column position to go to
	 * @param currentMoveNumber - move number of the current move in the game
	 * @return whether the Queen successfully moved to [targetRow, targetCol] on the board
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
	 * Determine if the Queen can legally move to [targetRow, targetCol]
	 * 
	 * @param targetRow - the row for the piece to move to
	 * @param targetCol - the column for the piece to move to
	 * @param checkPutSelfKingInCheck - checks whether own king will be in check after your own move
	 * @return whether the Queen can legally move to [targetRow, targetCol]
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
		if ( rowChange == 0 || colChange == 0 || Math.abs( rowChange ) == Math.abs( colChange ) ) {
			ChessPiece targetPiece = getBoard()[targetRow][targetCol];
			// Cannot move onto a piece of the same team
			if ( targetPiece != null && targetPiece.getTeam() == getTeam() ) {
				return false;
			}

			// Check each tile along the Queen's path to target destination
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
	 * Returns a String representation of Queen along with its team
	 * @return a String representation of Queen along with its team
	 */
	public String toString() {
		if ( getTeam() == Team.WHITE ) {
			return "wQ";
		}
		else {
			return "bQ";
		}
	}
}
