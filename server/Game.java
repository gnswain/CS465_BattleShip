package server;

import java.util.Hashtable;

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
 * This class contains the logic for the game of BattleShip. It has a grid for each client.
 */
public class Game {
    /** Sets the default size for the grid */
    public static final int DEFAULT_GRID = 10;
    
    /** Players in the game */
    private Hashtable<String, Grid> players;      // Hashtable makes more sense here. Will handle the queue in battle server
    /** Board size for the game */
    private int boardSize;
    /** If the player has started a game */
    private boolean startedGame;
    /** True if game is over */
    private boolean gameOver;                              // Don't know if we need this.
    
    /**
     * Creates a game with the default board size.
     */
    public Game() {
        this(DEFAULT_GRID);
    }

    /**
     * Creates a game with the given board size and given the players' usernames.
     * @param size Size of the board being used.
     */
    public Game(int size) {
        this.players = new Hashtable<String, Grid>();
        this.startedGame = false;
        this.gameOver = false;
        this.boardSize = size;
    }

    /**
     * Adds a user to the game given a username.
     * @param username Username of player being added to the game.
     */
    public void addPlayer(String username) {
        this.players.put(username, new Grid(this.boardSize, username));
    }

    /**
     * Removes a player from the game given a user name.
     * @param username Username of player being removed from the game.
     */
    public void removePlayer(String username) {
        this.players.remove(username);
    }

    /**
     * Gets the full Grid of the specified player.
     * @param username Username of the player's Grid to retrieve.
     * @return Full Grid of specified player.
     */
    public String getFullGrid(String username) {
        return this.players.get(username).getFullGrid();
    }

    /**
     * Gets the Private Grid of the specified player.
     * @param username Username of the player's Grid to retrieve.
     * @return Private Grid of specified player.
     */
    public String getPublicGrid(String username) {
        return this.players.get(username).getPublicGrid();
    }

    /**
     * Sets startedGame to True.
     */
    public void startGame() {
        this.startedGame = true;
    }

    /**
     * Returns if the Game has started.
     * @return True if Game has started.
     */
    public boolean isStarted() {
        return this.startedGame;
    }

    /**
     * Returns how many players are joined to the current Game.
     * @return How many players are in current Game.
     */
    public int amountOfPlayers() {
        return this.players.size();
    }

    /**
     * Sets gameOver to true. Signifies game being over.
     */
    public void gameOver() {
        this.gameOver = true;
    }

    /**
     * Returns if game is over.
     * @return True if game is over.
     */
    public boolean isGameOver() {
        return this.gameOver;
    }

    /**
     * Returns if specified player has ships left on the board.
     * @param username Username of player's board being checked.
     * @return True if player has ships left in play.
     */
    public boolean shipsLeft(String username) {
        return this.players.get(username).shipsLeft();
    }


    //TODO will likely end up removing turn(). Keeping it for reference for now.
    /**
     * Adds a player to the game given their username.
     * @param username Username of player being added.
     */
    // public void addPlayers(String username) {
    //     Grid player = new Grid(this.boardSize, username);
    //     player.placeShips();
    //     players.add(player);
    // }

    // /**
    //  * Handles a players turn. A lot of this functionality will get moved for the networked version.
    //  */
    // public void turn() {
    //     // easy way to reference the player whose turn it is
    //     Grid current, other;
    //     if (playerOneTurn) {
    //         current = playerOne;
    //         other = playerTwo;
    //     } else {
    //         current = playerTwo;
    //         other = playerOne;
    //     }

    //     System.out.println("\n" + current.getUsername() + " it is your turn");

    //     System.out.println("\n" + other.getUsername() + "'s Private board\n" + other.getFullGrid() 
    //     + "\n");

    //     System.out.println("\n" + other.getUsername() + "'s Public board\n" + other.getPublicGrid() 
    //     + "\n");
        
    //     boolean valid = false;
    //     while (!valid) {
    //         try {
    //             int row, col;
    //             System.out.print("Please select a row > ");
    //             row = Integer.parseInt(in.nextLine().strip());
    //             System.out.print("Please select a column > ");
    //             col = Integer.parseInt(in.nextLine().strip());

    //             valid = other.isValidShot(row, col);
                
    //             if (valid) {
    //                 boolean hit = other.shot(row, col);
    //                 System.out.println("\nShots fired at " + other.getUsername() + " by " 
    //                                     + current.getUsername());
    //                 if (hit) {
    //                     if (!other.shipsLeft()) {
    //                         System.out.println("GAME OVER: " + current.getUsername() + " wins!");
    //                         gameOver = true;
    //                         return; // checks win condition, only needs to check if hits that turn
    //                     }
    //                 }

    //                 // Switches whose turn it is
    //                 playerOneTurn = !playerOneTurn;
    //             } else {
    //                 System.out.println("Invalid shot for " + current.getUsername() +
    //                 " please try again.");
    //             } // end if valid
    //         } catch (NumberFormatException e) {
    //             System.out.println("Rows and Columns must be integers.");
    //         }
    //     } // end while
    // } // end turn
    
    /**
     * Used for testing data input.
     *
     * @param args Not used. 
     */
    public static void main(String[] args) {

    } //end main
}//end class Game
