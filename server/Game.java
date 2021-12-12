package server;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * @author Graham Swain
 * @author Brandon Welch
 *
 * @version December 11, 2021
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
    private Hashtable<String, Grid> players; // Hashtable makes more sense here. Will handle the queue in battle server

    /** Board size for the game */
    private int boardSize;

    /** If the player has started a game */
    private boolean startedGame;

    /** True if game is over */
    private boolean gameOver;
    

    /**
     * Creates a game with the default board size.
     */
    public Game() {

        this(DEFAULT_GRID);
    }//end default constructor


    /**
     * Creates a game with the specified board size.
     *
     * @param size Size of the board being used.
     */
    public Game(int size) {

        this.players = new Hashtable<String, Grid>();
        this.startedGame = false;
        this.gameOver = false;
        this.boardSize = size;
    }//end constructor


    /**
     * Adds a user to the game given a username.
     *
     * @param username Username of player being added to the game.
     */
    public void addPlayer(String username) {

        this.players.put(username, new Grid(this.boardSize, username));
        this.players.get(username).placeShips();
    }//end addPlayer


    /**
     * Removes a player from the game given a user name.
     *
     * @param username Username of player being removed from the game.
     */
    public void removePlayer(String username) {

        this.players.remove(username);
    }//end removePlayer


    /**
     * Gets the full Grid of the specified player.
     *
     * @param username Username of the player's Grid to retrieve.
     *
     * @return Full Grid of specified player.
     */
    public String getFullGrid(String username) {

        return this.players.get(username).getFullGrid();
    }//end getFullGrid

    /**
     * Gets the Private Grid of the specified player.
     *
     * @param username Username of the player's Grid to retrieve.
     *
     * @return Private Grid of specified player.
     */
    public String getPublicGrid(String username) {

        return this.players.get(username).getPublicGrid();
    }//end getPublicGrid


    /**
     * Sets startedGame to True.
     */
    public void startGame() {

        this.startedGame = true;
    }//end startGame


    /**
     * Returns true if the Game has started.
     *
     * @return True if Game has started.
     */
    public boolean isStarted() {

        return this.startedGame;
    }//end isStarted


    /**
     * Returns how many players are joined to the current Game.
     *
     * @return How many players are in current Game.
     */
    public int amountOfPlayers() {

        return this.players.size();
    }//amountOfPlayers


    /**
     * Sets gameOver to true. Signifies game being over.
     */
    public void gameOver() {

        this.gameOver = true;
    }//end gameOver


    /**
     * Returns if game is over.
     *
     * @return True if game is over.
     */
    public boolean isGameOver() {

        return this.gameOver;
    }//end isGameOver


    /**
     * Returns if specified player has ships left on the board.
     *
     * @param username Username of player's board being checked.
     *
     * @return True if player has ships left in play.
     */
    public boolean shipsLeft(String username) {

        return this.players.get(username).shipsLeft();
    }//end shipsLeft


    /**
     * Checks to see if a shot is valid against a specified.
     *
     * @param row Row of space being shot.
     * @param col Column of space being shot.
     * @param username Username whose Grid is being shot at.
     *
     * @return True if shot can be made.
     */
    public boolean isValidShot(int row, int col, String username) {

        return this.players.get(username).isValidShot(row, col);
    }//end isValidShot


    /**
     * Takes a shot at a space. Does not check validity of shot.
     *
     * @param row Row of space being shot.
     * @param col Column of space being shot.
     * @param username
     *
     * @return True if shot hits.
     */
    public boolean shoot(int row, int col, String username) {

        return this.players.get(username).shot(row, col);
    }//end shoot


    /**
     * Removes a user from the game.
     *
     * @param username User to be removed from the game.
     */
    public void remove(String username) {

        this.players.remove(username);
    }//end remove
    

    /**
     * Gets an ArrayList of players in the game.
     *
     * @return ArrayList of players currently in the game.
     */
    public ArrayList<String> getPlayers() {

        ArrayList<String> rtn = new ArrayList<>();

        for (String s : players.keySet()) {
            rtn.add(s);
        }//end for

        return rtn;
    }//end getPlayers


    /**
     * Checks to see if a player is currently in the game.
     *
     * @param username Username being searched for.
     *
     * @return True if player is in the game.
     */
    public boolean isPlayerInGame(String username) {

        for (String u : players.keySet()) {
System.out.println(u + " is in keys");
            if (u.equals(username)) {
                return true;
            }//end if
        }//end for
        return false;
    }//end isPlayerInGame


    /**
     * Used for testing data input.
     *
     * @param args Not used. 
     */
    public static void main(String[] args) {


    }//end main
}//end class Game
