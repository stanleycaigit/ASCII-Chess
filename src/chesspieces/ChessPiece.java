package chesspieces;

/**
 * The ChessPiece class represents a generic chess piece in a chess game with methods that any chess piece, regardless of type, can call
 * 
 * @author Eric Zhang
 * @author Stanley Cai
 *
 */
public abstract class ChessPiece {

	/**
	 * The team of this chess piece (white or black)
	 */
	private final Team team;

	/**
	 * The King of this chess piece's team (null for the King itself)
	 */
	private final King selfKing;
	
	/**
	 * The chessboard that the piece will reside in
	 */
	private ChessPiece[][] board;

	/**
	 * The number of moves that this piece has already made 
	 */
	private int numMoves;
	
	/**
	 * The move number of the last move that the chess piece made 
	 */
	private int lastMoveNumber;

	/**
	 * Create a generic chess piece
	 * 
	 * @param board    - the chessboard that the piece will reside in
	 * @param row      - the starting row position of the piece on the board
	 * @param col      - the starting column position of the piece on the board
	 * @param team     - the team of this chess piece (white or black)
	 * @param selfKing - the King of this chess piece's team (null for the King itself)
	 */
	public ChessPiece( ChessPiece[][] board, int row, int col, Team team, King selfKing ) {
		this.board = board;
		this.team = team;
		this.selfKing = selfKing;

		setLocation( row, col );
		numMoves = 0;
		lastMoveNumber = 0;
	}

	/**
	 * Attempt to move a chess piece from its current location to [targetRow, targetCol]
	 * 
	 * @param targetRow	- row position to go to
	 * @param targetCol	- column position to go to
	 * @param currentMoveNumber - move number of the current move in the game
	 * @return whether the chess piece successfully moved to [targetRow, targetCol]
	 */
	public abstract boolean attemptMoveTo( int targetRow, int targetCol, int currentMoveNumber );

	/**
	 * Determine if the chess piece can legally move to [targetRow, targetCol]
	 * 
	 * @param targetRow - the row for the piece to go to
	 * @param targetCol - the column for the piece to go to
	 * @param checkPutSelfKingInCheck - checks whether own king will be in check after your own move
	 * @return whether the chess piece can legally move to [targetRow, targetCol]
	 */
	public abstract boolean canMoveTo( int targetRow, int targetCol, boolean checkPutSelfKingInCheck );

	/**
	 * Determine if moving to this location will put selfKing in check. Precondition: this piece can legally move to
	 * this location otherwise
	 * 
	 * @param targetRow - the row that the piece will go to
	 * @param targetCol - the column that the piece will go to
	 * @return if selfKing will be put in check after this piece moves to [targetRow, targetCol]
	 */
	public boolean willPutSelfKingInCheckAt( int targetRow, int targetCol ) {
		ChessPiece previousChessPiece = getBoard()[targetRow][targetCol];
		int previousRow = getRow();
		int previousCol = getCol();

		// Move self to specified location
		setLocation( targetRow, targetCol );

		boolean isSelfKingInCheck = selfKing.isInCheck();

		// Restore self to original position
		setLocation( previousRow, previousCol );
		getBoard()[targetRow][targetCol] = previousChessPiece;

		return isSelfKingInCheck;
	}

	/**
	 * Get the board that the chess piece is in (or was in before being captured)
	 * 
	 * @return the board that the chess piece is/was in
	 */
	public ChessPiece[][] getBoard() {
		return board;
	}

	/**
	 * Search the board for the chess piece, and return its column position on the board
	 * 
	 * @return the column position of the chess piece on the chessboard, or -1 if the piece is no longer on the board
	 */
	public int getCol() {
		for ( int i = 0; i < 8; i++ ) {
			for ( int j = 0; j < 8; j++ ) {
				if ( getBoard()[i][j] == this ) {
					return j;
				}
			}
		}
		return -1;
	}

	/**
	 * Search the board for the chess piece, and return its row position on the chessboard
	 * 
	 * @return the row position of the chess piece on the chessboard, or -1 if the piece is no longer on the board
	 */
	public int getRow() {
		for ( int i = 0; i < 8; i++ ) {
			for ( int j = 0; j < 8; j++ ) {
				if ( getBoard()[i][j] == this ) {
					return i;
				}
			}
		}
		return -1;
	}

	/**
	 * Do the actual updating of the chess piece's location. Precondition: the chess piece can legally move to this
	 * location
	 * 
	 * @param row - the row of the location to move to
	 * @param col - the column of the location to move to
	 */
	public void setLocation( int row, int col ) {
		int currentRow = getRow();
		int currentCol = getCol();

		// Vacate current position
		if ( currentRow >= 0 && currentCol >= 0 ) {
			getBoard()[currentRow][currentCol] = null;
		}
		getBoard()[row][col] = this;
	}

	/**
	 * Get the team of the chess piece
	 * @return the team of the chess piece
	 */
	public Team getTeam() {
		return team;
	}

	/**
	 * Get the King of the chess piece's team
	 * @return the King of the chess piece's team
	 */
	public King getSelfKing() {
		return selfKing;
	}

	/**
	 * Get the number of moves that this chess piece has made
	 * @return the number of moves that this chess piece has made
	 */
	public int getNumMoves() {
		return numMoves;
	}

	/**
	 * Increment the total number of moves the piece has made
	 */
	public void incrementNumMoves() {
		numMoves++;
	}

	/**
	 * Retrieve the most recent move number of the piece
	 * @return most recent move number of the piece
	 */
	public int getLastMoveNumber() {
		return lastMoveNumber;
	}

	/**
	 * Update the piece's most recent move number
	 * @param lastMoveNumber - piece's new move number to be updated to
	 */
	public void setLastMoveNumber( int lastMoveNumber ) {
		this.lastMoveNumber = lastMoveNumber;
	}
}
