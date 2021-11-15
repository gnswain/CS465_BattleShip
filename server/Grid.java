package server;

//import 

/**
 * @author Brandon Welch
 * @author Graham Swain
 * @version November 12, 2021
 *
 * CS465-01, Computer Networks
 * Dr. Scott Barlowe
 *
 * Fall 2021
 *
 * Project Three: Battleship(Multiuser game)
 * 
 * This class contains the logic for a single board of BattleShip.
 */
public class Grid {

    /** Character representation of a hit */
    public static final String HIT = "X";
    /** Character representation of a miss */
    public static final String MISS = "O";
    
    /** The board represented as a 2D array of Strings */
    private String[][] board;

    /**
     * Creates a new board given a size.
     * @param size Size of the board.
     */
    public Grid(int size) {
        board = new String[size][size];

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = " ";
            }
        }
    }

    /**
     * Returns a String representation of the Grid
     * @return String representation of the Grid
     */
    @Override
    public String toString() {
        String rtn = "  ";
        // Creates the row indices
        for (int i = 0; i < this.board.length; i++) {
            rtn += "  " + i + " ";
        }
        rtn += "\n";

        for (int i = 0; i < this.board.length; i++) {
            rtn += "  +---+---+---+---+---+---+---+---+---+---+\n" + i + " | ";
            for (int j = 0; j < this.board[i].length; j++) {
                rtn += this.board[i][j] + " | "; 
            }            
            rtn += "\n";
        }
        rtn += "  +---+---+---+---+---+---+---+---+---+---+";
        return rtn;
    }

    /**
     * Used for testing data input.
     *
     * @param args Not used. 
     */
    public static void main(String[] args) {
        Grid grid = new Grid(10);

        System.out.println(grid.toString());

    }//end main
}//end class Grid
