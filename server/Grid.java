package server;

import java.util.concurrent.ThreadLocalRandom;

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
     * Creates a new board given a size.
     * @param size Size of the board.
     */
    public Grid(int size, String username) {
        board = new String[size][size];
        this.size = size;

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = " ";
            }
        }

        this.username = username;
    } // end Grid

    /**
     * Returns a String representation of the Grid showing full details
     * @return String representation of the Grid
     */
    public String getFullGrid() {
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
                rtn += this.board[i][j] + " | "; 
            }            
            rtn += "\n";
        }
        rtn += divider + "+";
        return rtn;
    } // end getFullGrid

    /**
     * Returns a String representation of the Grid showing only hits and misses
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
            } // end inner for    
            rtn += "\n";
        } // end out for
        rtn += divider + "+";
        return rtn;
    } // end getPublicGrid

    /**
     * Attempts to place a ship at a given coordinate in a given direction.
     * @param ship Ship to be placed.
     * @param row Row to place ships first piece.
     * @param col Column to place ships first piece.
     * @param orientation Orientation of the ship -> 'v' for vertical, 'h' for horizontal.
     * @param dir Direction of ship -> 1 for up/right, -1 for down/left.
     * @return True if ship was successfully placed.
     */
    public boolean placeShip(ShipType ship, int row, int col, char orientation, int dir) {
        // check to see if within bounds
        if (row < 0 || row >= board.length || col < 0 || col >= board.length)
            return false;
        
        if (orientation == 'v')
            return placeVertical(ship, row, col, dir);
        else if (orientation == 'h')
            return placeHorizontal(ship, row, col, dir);

        return false;
    } // end placeShip

    /**
     * Attempts to place a ship at a given coordinate in a given direction.
     * @param ship Ship to be placed.
     * @param row Row to place ships first piece.
     * @param col Column to place ships first piece.
     * @param dir Direction of ship -> 1 for up, -1 for down.
     * @return True if ship was successfully placed.
     */
    private boolean placeVertical(ShipType ship, int row, int col, int dir) {
        dir *= -1;
        try {
            // Checks to see if any other ships are in the path before placing
            for (int i = 0; i < ship.getSize(); i++) {
                if (!board[row  + (i * dir)][col].equals(" "))
                    return false;
            }
        } catch (IndexOutOfBoundsException e) {
            return false; // ship went over the edge
        }
        
        // places ship
        for (int i = 0; i < ship.getSize(); i++) {
            board[row + (i * dir)][col] = ship.toString();
        }
        return true;
    } // end placeVertical

    /**
     * Attempts to place a ship at a given coordinate in a given direction.
     * @param ship Ship to be placed.
     * @param row Row to place ships first piece.
     * @param col Column to place ships first piece.
     * @param dir Direction of ship -> 1 for right, -1 for left.
     * @return True if ship was successfully placed.
     */
    private boolean placeHorizontal(ShipType ship, int row, int col, int dir) {
        try {
            // Checks to see if any other ships are in the path before placing
            for (int i = 0; i < ship.getSize(); i++) {
                if (!board[row][col + (i * dir)].equals(" "))
                    return false;
            }
        } catch (IndexOutOfBoundsException e) {
            return false; // ship went over the edge
        }
        
        // places ship
        for (int i = 0; i < ship.getSize(); i++) {
            board[row][col + (i * dir)] = ship.toString();
        }
        return true;
    } // end placeHorizontal

    /**
     * Checks to see if a shot can be made. A shot is invalid if it is off of the board or if that
     * space has already been guessed;
     * @param row Row of space being shot.
     * @param col Column of space being shot.
     * @return True if shot can be made.
     */
    public boolean isValidShot(int row, int col) {
        if (row < 0 || row >= board.length || col < 0 || col >= board.length) {
            return false;
        }
        if (board[row][col].equals(HIT) || board[row][col].equals(MISS)) {
            return false;
        }
        return true;
    } // end isValidShot

    /**
     * Takes a shot at a space. Does not check validity of shot.
     * @param row Row of space being shot.
     * @param col Column of space being shot.
     * @return True if shot hits.
     */
    public boolean shot(int row, int col) {
        if (board[row][col].equals(" ")) {
            // shot misses
            board[row][col] = MISS;
            return false;
        } else {
            //shot hits
            board[row][col] = HIT;
            return true;
        }
    } // ends shot

    /**
     * Checks to see if there are still ships on the board.
     * @return True if there is at least one part of one ship left.
     */
    public boolean shipsLeft() {
        for (String[] row : board)
            for (String space : row)
                if (!(space.equals(HIT) || space.equals(MISS) || space.equals(" ")))
                    return true;
        return false;
    } // end shipsLeft

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
            }
        }
    }

    /**
     * Gets the owner of the board.
     * @return Username of the board's owner.
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Gets the size of the board.
     */
    public int getSize() {
        return this.size;
    }

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
     * @author Brandon Welch
     * @author Graham Swain
     * @version November 12, 2021
     *
     * Stores the available board sizes and how many ships can be placed.
     */
    private enum Size {
        TEN(10, 4, 6),
        NINE(9, 3, 5),
        EIGHT(8, 3, 5),
        SEVEN(7, 2, 3),
        SIX(6, 2, 3),
        FIVE(5, 1, 2);

        /** Size of the board */
        private int size;
        /** Minimum amount of ships allowed */
        private int min;
        /** Maxium amount of ships allowed */
        private int max;

        /**
         * Creates enum given size, min, and max.
         * @param size Size of the board.
         * @param min Minimum number of ships allowed.
         * @param max Maximum number of ships allowed.
         */
        private Size(int size, int min, int max) {
            this.size = size;
            this.min = min;
            this.max = max;
        }

        /**
         * Gets appropriate enum given size as an int.
         * @param size Size of the board.
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
            }
            return null;
        }
    } // end enum Size 
}//end class Grid
