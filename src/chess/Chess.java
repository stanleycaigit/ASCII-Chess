package chess;

import java.util.Scanner;

import chesspieces.ChessPiece;
import chesspieces.King;
import chesspieces.Pawn;
import chesspieces.Team;
import chesspieces.Bishop;
import chesspieces.Knight;
import chesspieces.Rook;
import chesspieces.Queen;

/**
 * Main class that runs the entire chess game
 * 
 * @author Eric Zhang
 * @author Stanley Cai
 *
 */

public class Chess {

	private static ChessPiece[][] board;

	/**
	 * The move number of the current move that the user in turn is making. For example, currentMoveNumber will be 1
	 * when white makes its first move, then currentMoveNumber will be 2 when black makes its first move, etc...
	 */
	private static int currentMoveNumber;

	/**
	 * The scanner to receive all input from both users
	 */
	private static Scanner in;

	/**
	 * A flag to indicate that white is requesting a draw when black moves
	 */
	private static boolean isWhiteRequestingDraw;

	/**
	 * A flag to indicate that black is requesting a draw when white moves
	 */
	private static boolean isBlackRequestingDraw;

	/**
	 * The main method to run the entire chess game
	 * 
	 * @param args
	 */
	public static void main( String[] args ) {
		setupBoard();
		boolean isGameOver = false;
		in = new Scanner( System.in );
		currentMoveNumber = 1;
		while ( !isGameOver ) {
			isGameOver = makeAMove( Team.WHITE );
			if ( !isGameOver ) {
				isGameOver = makeAMove( Team.BLACK );
			}
		}

		in.close();
	}

	/**
	 * Initialize board, and add all chess pieces onto it
	 */
	private static void setupBoard() {
		board = new ChessPiece[8][8];

		King whiteKing = new King( board, 7, 4, Team.WHITE );
		board[7][4] = whiteKing;

		King blackKing = new King( board, 0, 4, Team.BLACK );
		board[0][4] = blackKing;

		Team[] teams = new Team[] { Team.WHITE, Team.BLACK };
		int[] rows = new int[] { 7, 0 };
		King[] kings = new King[] { whiteKing, blackKing };

		for ( int i = 0; i < 2; i++ ) {
			// Add queens to board
			Queen queen = new Queen( board, rows[i], 3, teams[i], kings[i] );
			board[rows[i]][3] = queen;

			// Add rooks to board
			for ( int j = 0; j < 2; j++ ) {
				// cols are 0 and 7
				Rook rook = new Rook( board, rows[i], 7 * j, teams[i], kings[i] );
				board[rows[i]][7 * j] = rook;
			}

			// Add knights to board
			for ( int j = 0; j < 2; j++ ) {
				// Ensure cols are 1 and 6
				Knight knight = new Knight( board, rows[i], 5 * j + 1, teams[i], kings[i] );
				board[rows[i]][5 * j + 1] = knight;
			}

			// Add bishops to board
			for ( int j = 0; j < 2; j++ ) {
				// Ensure cols are 2 and 5
				Bishop bishop = new Bishop( board, rows[i], 3 * j + 2, teams[i], kings[i] );
				board[rows[i]][3 * j + 2] = bishop;
			}
		}

		// Add pawns to board
		rows = new int[] { 6, 1 };
		for ( int i = 0; i < 2; i++ ) {
			for ( int j = 0; j < 8; j++ ) {
				Pawn pawn = new Pawn( board, rows[i], j, teams[i], kings[i] );
				board[rows[i]][j] = pawn;
			}
		}
	}

	/**
	 * Print the board
	 */
	private static void printChessboard() {
		boolean blackSquare = false;

		for ( int i = 0; i < 8; i++ ) {
			for ( int j = 0; j < 8; j++ ) {
				if ( board[i][j] == null ) {
					if ( blackSquare ) {
						System.out.print( "## " );
					}
					else {
						System.out.print( "   " );
					}
				}
				else {
					System.out.print( board[i][j] + " " );
				}
				blackSquare = !blackSquare;
			}
			blackSquare = !blackSquare;
			System.out.println( 8 - i );
		}
		System.out.println( " a  b  c  d  e  f  g  h\n" );
	}

	/**
	 * Make one of the teams make a move
	 * 
	 * @param team - the team that will be making a move
	 * @return whether the move terminated the game
	 */
	private static boolean makeAMove( Team team ) {
		printChessboard();
		Team otherTeam;
		if ( team == Team.WHITE ) {
			otherTeam = Team.BLACK;
		}
		else {
			otherTeam = Team.WHITE;
		}

		if ( isTeamInCheckmate( team ) ) {
			System.out.println( "Checkmate" );
			System.out.println( otherTeam + " wins" );
			return true;
		}
		else if ( locateKing( team ).isInCheck() ) {
			System.out.println( "Check" );
		}

		while ( true ) {
			// Prompt user for move
			System.out.print( team + "'s move: " );
			String input = in.nextLine();
			String[] args = input.split( " " );

			if ( args.length >= 2 ) {
				if ( ( otherTeam == Team.BLACK && isBlackRequestingDraw )
						|| ( otherTeam == Team.WHITE && isWhiteRequestingDraw ) ) {
					System.out.println( "Illegal move, try again" );
					continue;
				}

				int[] startLocation = parseLocation( args[0] );
				int[] targetLocation = parseLocation( args[1] );
				if ( startLocation == null || targetLocation == null ) {
					continue;
				}

				int startRow = startLocation[0];
				int startCol = startLocation[1];
				int targetRow = targetLocation[0];
				int targetCol = targetLocation[1];
				ChessPiece targetPiece = board[startRow][startCol];

				// Retrieve additonal data
				char promotionPiece = '\0';
				boolean requestDraw = false;
				if ( args.length == 4 ) {
					if ( args[2].length() != 1 ) {
						continue;
					}
					if ( !( args[2].equals( "Q" ) || args[2].equals( "R" ) || args[2].equals( "B" )
							|| args[2].equals( "N" ) ) ) {
						continue;
					}
					if ( !args[3].equals( "draw?" ) ) {
						continue;
					}

					// User provided 3rd argument as promotion piece, and 4th argument as draw request
					promotionPiece = args[2].charAt( 0 );
					requestDraw = true;
				}
				else if ( args.length == 3 ) {
					if ( args[2].equals( "draw?" ) ) {
						requestDraw = true;
					}
					else if ( args[2].length() == 1 ) {
						if ( !( args[2].equals( "Q" ) || args[2].equals( "R" ) || args[2].equals( "B" )
								|| args[2].equals( "N" ) ) ) {
							continue;
						}
						promotionPiece = args[2].charAt( 0 );
					}
					else {
						continue;
					}
				}

				if ( targetPiece instanceof Pawn ) {
					Pawn targetPawn = (Pawn) targetPiece;
					if ( !Pawn.willPawnBePromoted( targetPawn, targetRow ) && promotionPiece != '\0' ) {
						// Cannot provide promotion piece if Pawn will not be promoted
						System.out.println( "Illegal move, try again" );
						continue;
					}

					if ( targetPiece == null || targetPiece.getTeam() != team
							|| !targetPiece.attemptMoveTo( targetRow, targetCol, currentMoveNumber ) ) {
						System.out.println( "Illegal move, try again" );
						continue;
					}

					if ( Pawn.willPawnBePromoted( targetPawn, targetRow ) ) {
						if ( promotionPiece == '\0' ) {
							promotionPiece = 'Q';
						}

						// Promote pawn
						targetPawn.promote( promotionPiece, currentMoveNumber );
					}
				}
				else {
					if ( promotionPiece != '\0' ) {
						// Cannot provide promotion piece if targetPiece is not Pawn
						System.out.println( "Illegal move, try again" );
						continue;
					}

					if ( targetPiece == null || targetPiece.getTeam() != team
							|| !targetPiece.attemptMoveTo( targetRow, targetCol, currentMoveNumber ) ) {
						System.out.println( "Illegal move, try again" );
						continue;
					}
				}

				if ( requestDraw ) {
					// Request draw
					if ( team == Team.WHITE ) {
						isWhiteRequestingDraw = true;
					}
					else {
						isBlackRequestingDraw = true;
					}
				}
			}
			else if ( args.length == 1 ) {
				// if resign
				if ( args[0].equals( "resign" ) ) {
					if ( team == Team.WHITE ) {
						System.out.println( "Black wins" );
						return true;
					}
					else if ( team == Team.BLACK ) {
						System.out.println( "White wins" );
						return true;
					}
				}
				else if ( args[0].equals( "draw" ) ) {
					// else its a draw, nothing gets printed
					if ( ( otherTeam == Team.WHITE && isWhiteRequestingDraw )
							|| ( otherTeam == Team.BLACK && isBlackRequestingDraw ) ) {
						return true;
					}
					else {
						continue;
					}
				}
				else {
					continue;
				}
			}

			break;
		}

		currentMoveNumber++;
		System.out.println();
		return false;
	}

	/**
	 * Parses a row and column position from a user input of the form "[file][rank]"
	 * 
	 * @param location - a String representation of a tile on the chessboard (e.g. "e4" or "g3")
	 * @return an integer array of the form [row, column] which has the corresponding row and column indices
	 */
	private static int[] parseLocation( String location ) {
		if ( location.length() != 2 ) {
			return null;
		}

		int col = fileToColumn( location.charAt( 0 ) );
		if ( col == -1 ) {
			return null;
		}
		// location.charAt( 1 ) == '1' -> location.charAt( 1 ) - 48 = 49 - 48 = 1, etc...
		int row = rankToRow( location.charAt( 1 ) - 48 );
		if ( row == -1 ) {
			return null;
		}
		return new int[] { row, col };
	}

	/**
	 * Converts the rank provided by the user to a row position on the board
	 * 
	 * @param rank - the rank provided by the user
	 * @return the corresponding row position on the board
	 */
	private static int rankToRow( int rank ) {
		if ( 8 - rank >= 0 && 8 - rank < 8 ) {
			return 8 - rank;
		}
		else {
			return -1;
		}
	}

	/**
	 * Converts the file provided by the user to a column position on the board
	 * 
	 * @param file - the file provided by the user
	 * @return the corresponding column position on the board
	 */
	private static int fileToColumn( char file ) {
		if ( file >= 'a' || file <= 'h' ) {
			return (int) ( file - 'a' );
		}
		else {
			return -1;
		}
	}

	/**
	 * Search the chess board for the King piece of a team
	 * 
	 * @param team - the team whose King to locate
	 * @return the King piece on the board of the team
	 */
	private static King locateKing( Team team ) {
		for ( int i = 0; i < 8; i++ ) {
			for ( int j = 0; j < 8; j++ ) {
				ChessPiece currentPiece = board[i][j];
				if ( currentPiece instanceof King && currentPiece.getTeam() == team ) {
					return (King) currentPiece;
				}
			}
		}
		return null;
	}

	/**
	 * Determine if the team is in checkmate
	 * 
	 * @param team - the team to determine if it is in checkmate
	 * @return if the team is in checkmate
	 */
	private static boolean isTeamInCheckmate( Team team ) {
		King currentKing = locateKing( team );
		return currentKing.isInCheckmate();
	}
}
