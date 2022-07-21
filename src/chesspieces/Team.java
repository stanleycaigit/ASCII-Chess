package chesspieces;

/**
 * This enum represents the white and black teams in chess.
 * @author Eric Zhang
 * @author Stanley Cai
 *
 */
public enum Team {
	/**
	 * Team white
	 */
	WHITE {
		public String toString() {
			return "White";
		}
	},
	
	/**
	 * Team black
	 */
	BLACK {
		public String toString() {
			return "Black";
		}
	}
}
