package server;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Graham Swain
 * @author Brandon Welch
 * @version December 5, 2021
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

    /** Maximum size for a Grid. */
    public static final int MAX_SIZE = 10;

    /** Minimum size for a Grid. */
    public static final int MIN_SIZE = 5;

    /** Character representation of a hit. */
    public static final String HIT = "X";

    /** Character representation of a miss. */
    public static final String MISS = "O";
    
    /** The board represented as a 2D array of Strings. */
    private String[][] board;

    /** Player who owns the board. */
    private String username;

    /** The size of the board. */
    private int size;


    /**
     * Creates a new square board with the specified size.
     *
     * @param size The size of the board.
     */
    public Grid(int size, String username) {

        this.username = username;
        this.board = new String[size][size];
        this.size = size;

        /* fill the board with blank spaces. */
        for (int i = 0; i < this.board.length; i++) {
            for (int j = 0; j < this.board[i].length; j++) {
                this.board[i][j] = " ";
            }//end for
        }//end for
    }//end constructor


    /**
     * Returns a String representation of the Grid showing full details
     *
     * @return String representation of the Grid
     */
    public String getFullGrid() {

        String rtn = "  ";
        String divider = "  "; // Used to divide the rows

        // Creates the row indices
        for (int i = 0; i < this.board.length; i++) {
            rtn += "  " + i + " ";
            divider += "+---";
        }//end for
        rtn += "\n";

        // Creates the rest of the board
        for (int i = 0; i < this.board.length; i++) {
            rtn += divider + "+\n" + i + " | ";
            for (int j = 0; j < this.board[i].length; j++) {
                rtn += this.board[i][j] + " | "; 
            }//end for       
            rtn += "\n";
        }//end for
        rtn += divider + "+";
        return rtn;
    }//end getFullGrid


    /**
     * Returns a String representation of the Grid showing only hits and misses.
     *
     * @return String representation of the Grid
     */
    public String getPublicGrid() {

        String rtn = "  ";
        String divider = "  "; // Used to divide the rows

        // Creates the row indices
        for (int i = 0; i < this.board.length; i++) {
            rtn += "  " + i + " ";
            divider += "+---";
        }
        rtn += "\n";

        // Creates the rest of the board
        for (int i = 0; i < this.board.length; i++) {
            rtn += divider + "+\n" + i + " | ";
            for (int j = 0; j < this.board[i].length; j++) {
                if (this.board[i][j].equals(MISS) || this.board[i][j].equals(HIT))
                    rtn += this.board[i][j] + " | "; 
                else
                    rtn += "  | ";
            }//end for    
            rtn += "\n";
        }// end for
        rtn += divider + "+";
        return rtn;
    }//end getPublicGrid


    /**
     * Attempts to place a ship at a given coordinate in a given direction.
     *
     * @param ship Ship to be placed.
     * @param row Row to place ships first piece.
     * @param col Column to place ships first piece.
     * @param orientation Orientation of the ship -> 'v' for vertical, 'h' for horizontal.
     * @param dir Direction of ship -> 1 for up/right, -1 for down/left.
     *
     * @return True if ship was successfully placed.
     */
    private boolean placeShip(ShipType ship, int row, int col, char orientation, int dir) {

        // check to see if within bounds
        if (row < 0 || row >= this.board.length || col < 0 || col >= this.board.length)
            return false;
        
        if (orientation == 'v')
            return placeVertical(ship, row, col, dir);

        else if (orientation == 'h')
            return placeHorizontal(ship, row, col, dir);

        return false;
    }//end placeShip


    /**
     * Attempts to place a ship vertical on a Grid at a given coordinate.
     *
     * @param ship Ship to be placed.
     * @param row Row to place ships first piece.
     * @param col Column to place ships first piece.
     * @param dir Direction of ship -> 1 for up, -1 for down.
     *
     * @return True if ship was successfully placed.
     */
    private boolean placeVertical(ShipType ship, int row, int col, int dir) {

        dir *= -1;
        try {
            // Checks to see if any other ships are in the path before placing
            for (int i = 0; i < ship.getSize(); i++) {
                if (!this.board[row  + (i * dir)][col].equals(" "))
                    return false;
            }//end for
        }//end try 
        catch (IndexOutOfBoundsException e) {
            return false; // ship went over the edge
        }//end catch
        
        // places ship
        for (int i = 0; i < ship.getSize(); i++) {
            this.board[row + (i * dir)][col] = ship.toString();
        }//end for
        return true;
    }//end placeVertical


    /**
     * Attempts to place a ship horizontal on a Grid at a given coordinate.
     *
     * @param ship Ship to be placed.
     * @param row Row to place ships first piece.
     * @param col Column to place ships first piece.
     * @param dir Direction of ship -> 1 for right, -1 for left.
     *
     * @return True if ship was successfully placed.
     */
    private boolean placeHorizontal(ShipType ship, int row, int col, int dir) {

        try {
            // Checks to see if any other ships are in the path before placing
            for (int i = 0; i < ship.getSize(); i++) {
                if (!this.board[row][col + (i * dir)].equals(" "))
                    return false;
            }//end for
        }//end try
        catch (IndexOutOfBoundsException e) {
            return false; // ship went over the edge
        }//end catch
        
        // places ship
        for (int i = 0; i < ship.getSize(); i++) {
            this.board[row][col + (i * dir)] = ship.toString();
        }//end for
        return true;
    }//end placeHorizontal


    /**
     * Checks to see if a shot can be made. A shot is invalid if it is off of the board or if that
     * space has already been guessed;
     *
     * @param row Row of space being shot.
     * @param col Column of space being shot.
     * @return True if shot can be made.
     */
    public boolean isValidShot(int row, int col) {
        if (row < 0 || row >= this.board.length || col < 0 || col >= this.board.length) {
            return false;
        }//end if
        if (this.board[row][col].equals(HIT) || this.board[row][col].equals(MISS)) {
            return false; // TODO ask if shooting at the same place twice is allowed
        }//end if
        return true;
    }//end isValidShot


    /**
     * Takes a shot at a space. Does not check validity of shot.
     *
     * @param row Row of space being shot.
     * @param col Column of space being shot.
     *
     * @return True if shot is a hit.
     */
    public boolean shot(int row, int col) {

        if (this.board[row][col].equals(" ")) {
            // shot misses
            this.board[row][col] = MISS;
            return false;
        } else {
            //shot hits
            this.board[row][col] = HIT;
            return true;
        }//end else
    }//end shot


    /**
     * Checks to see if there are still ships on the board.
     *
     * @return True if there is at least one part of one ship left.
     */
    public boolean shipsLeft() {

        for (String[] row : board)
            for (String space : row)
                if (!(space.equals(HIT) || space.equals(MISS) || space.equals(" ")))
                    return true;
        return false;
    }//end shipsLeft


    /**
     * Places ships randomly onto the board. Amount of ships placed is based off size.
     */
    public void placeShips() {

        Size size = Size.getSizeByInt(this.size);

        int total = ThreadLocalRandom.current().nextInt(size.min, size.max + 1);
        ShipType[] types = ShipType.values();

        int placed = 0;
        while (placed < total) {
            ShipType ship = types[ThreadLocalRandom.current().nextInt(types.length - 1)];
            int row = ThreadLocalRandom.current().nextInt(0, this.getSize());
            int col = ThreadLocalRandom.current().nextInt(0, this.getSize());

            boolean vertical = ThreadLocalRandom.current().nextBoolean();
            char orientation = vertical ? 'v' : 'h';
            boolean positive = ThreadLocalRandom.current().nextBoolean();
            int dir = positive ? 1 : -1;

            if (this.placeShip(ship, row, col, orientation, dir)) {
                placed++;
            }//end if
        }//end while
    }//end placeShips


    /**
     * Gets the owner of the board.
     *
     * @return Username of the board's owner.
     */
    public String getUsername() {

        return this.username;
    }//end getUsername

    /**
     * Gets the size of the board.
     *
     * @return This size of the board.
     */
    public int getSize() {

        return this.size;
    }//end getSize


    /**
     * Used for testing data input.
     *
     * @param args Not used. 
     */
    public static void main(String[] args) {
        Grid grid = new Grid(8, "PlayerOne");

        grid.placeShips();
        System.out.println(grid.getFullGrid());
    }//end main


    /**
     * @author Graham Swain
     * @author Brandon Welch
     * @version December 5, 2021
     *
     * Stores the available board sizes and how many ships can be placed.
     */
    private enum Size {
        /** Literal for Grid's of size ten. */
        TEN(4, 6),

        /** Literal for Grid's of size nine. */
        NINE(3, 5),

        /** Literal for Grid's of size eight. */
        EIGHT(3, 5),

        /** Literal for Grid's of size seven. */
        SEVEN(2, 3),

        /** Literal for Grid's of size six. */
        SIX(2, 3),

        /** Literal for Grid's of size five. */
        FIVE(1, 2);

        /** Minimum amount of ships allowed on a Grid. */
        private int min;

        /** Maximum amount of ships allowed on a Grid. */
        private int max;

        /**
         * Creates enum given size, min, and max.
         *
         * @param size Size of the board.
         * @param min Minimum number of ships allowed.
         * @param max Maximum number of ships allowed.
         */
        private Size(int min, int max) {

            this.min = min;
            this.max = max;
        }//end constructor


        /**
         * Gets appropriate enum given size as an int.
         *
         * @param size Size of the board.
         *
         * @return Enum holding min and max ships allowed.
         */
        public static Size getSizeByInt(int size) {

            switch (size) {
                case 5:
                    return FIVE;
                case 6:
                    return SIX;
                case 7:
                    return SEVEN;
                case 8:
                    return EIGHT;
                case 9:
                    return NINE;
                case 10:
                    return TEN;
            }//end switch
            return null;
        }//end getSizebyInt
    }//end enum Size 
}//end class Grid
