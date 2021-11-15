package server;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

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
 * This class contains the logic for the game of BattleShip.  It has a grid for each client.
 */
public class Game {
    /** Sets the default size for the grid */
    public static final int DEFAULT_GRID = 10;
    
    /** Board for the first player */
    private Grid playerOne;
    /** Board for the second player */
    private Grid playerTwo;
    /** If the player has started a game */
    private boolean startedGame;
    /** Keeps track of whose turn it is */
    private boolean playerOneTurn;
    /** True if game is over */
    private boolean gameOver;
    /** Used to get user input */
    private Scanner in;             // Will almost definitely get removed.
    
    /**
     * Creates a game with the default board size given the players' usernames.
     * @param playerOne Username of player one.
     * @param playerTwo Username of player two.
     */
    public Game(String playerOne, String playerTwo) {
        this(playerOne, playerTwo, DEFAULT_GRID);
    }

    /**
     * Creates a game with the given board size and given the players' usernames.
     * @param playerOne Username of player one.
     * @param playerTwo Username of player two.
     * @param size Size of the board being used.
     */
    public Game(String playerOne, String playerTwo, int size) {
        this.playerOne = new Grid(size, playerOne);
        this.playerTwo = new Grid(size, playerTwo);
        startedGame = false;
        gameOver = false;
        playerOneTurn = false;
        placeShips();
        in = new Scanner(System.in);
    }

    /**
     * Handles a players turn. A lot of this functionality will get moved for the networked version.
     */
    public void turn() {
        // easy way to reference the player whose turn it is
        Grid current, other;
        if (playerOneTurn) {
            current = playerOne;
            other = playerTwo;
        } else {
            current = playerTwo;
            other = playerOne;
        }

        System.out.println("\n" + current.getUsername() + " it is your turn");

        System.out.println("\n" + other.getUsername() + "'s board\n" + other.getFullGrid() + "\n");
        
        boolean valid = false;
        while (!valid) {
            try {
                int row, col;
                System.out.print("Please select a row > ");
                row = Integer.parseInt(in.nextLine());
                System.out.print("Please select a column > ");
                col = Integer.parseInt(in.nextLine());

                valid = other.isValidShot(row, col);
                
                if (valid) {
                    boolean hit = other.shot(row, col);
                    System.out.println("\nShots fired at " + other.getUsername() + " by " 
                                        + current.getUsername());
                    if (hit) {
                        if (!other.shipsLeft()) {
                            System.out.println("GAME OVER: " + current.getUsername() + " wins!");
                            gameOver = true;
                            return; // checks win condition, only needs to check if hits that turn
                        }
                    }

                    // Switches whose turn it is
                    playerOneTurn = !playerOneTurn;
                } else {
                    System.out.println("Invalid shot for " + current.getUsername() +
                    " please try again.");
                } // end if valid
            } catch (NumberFormatException e) {
                System.out.println("Rows and Columns must be integers.");
            }
        } // end while
    } // end turn

    /**
     * Places the ships randomly for both players.
     */
    public void placeShips() {
        Size size = Size.getSizeByInt(this.playerOne.getSize());

        int total = ThreadLocalRandom.current().nextInt(size.min, size.max);
        ShipType[] types = ShipType.values();

        int placed = 0;
        while (placed < total) {
            ShipType ship = types[ThreadLocalRandom.current().nextInt(types.length - 1)];
            int row = ThreadLocalRandom.current().nextInt(0, playerOne.getSize());
            int col = ThreadLocalRandom.current().nextInt(0, playerOne.getSize());

            boolean vertical = ThreadLocalRandom.current().nextBoolean();
            char orientation = vertical ? 'v' : 'h';
            boolean positive = ThreadLocalRandom.current().nextBoolean();
            int dir = positive ? 1 : -1;

            if (playerOne.placeShip(ship, row, col, orientation, dir)) {
                placed++;
            }
        }
        // TODO create method that takes in a reference to a Grid that does this.
        placed = 0;
        while (placed < total) {
            ShipType ship = types[ThreadLocalRandom.current().nextInt(types.length - 1)];
            int row = ThreadLocalRandom.current().nextInt(0, playerOne.getSize());
            int col = ThreadLocalRandom.current().nextInt(0, playerOne.getSize());

            boolean vertical = ThreadLocalRandom.current().nextBoolean();
            char orientation = vertical ? 'v' : 'h';
            boolean positive = ThreadLocalRandom.current().nextBoolean();
            int dir = positive ? 1 : -1;

            if (playerTwo.placeShip(ship, row, col, orientation, dir)) {
                placed++;
            }
        }
    }
    
    /**
     * Used for testing data input.
     *
     * @param args Not used. 
     */
    public static void main(String[] args) {
        Game game = new Game("PlayerOne", "PlayerTwo", 5);

        while (!game.gameOver) {
            game.turn();
        }

    } //end main

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
    }
}//end class Game
