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
 * This class contains the logic for the game of BattleShip.  It has a grid for each client.
 */
public class Game {
    
    /**
     * Used for testing data input.
     *
     * @param args Not used. 
     */
    public static void main(String[] args) {

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

        private Size(int size, int min, int max) {
            this.size = size;
            this.min = min;
            this.max = max;
        }
    }
}//end class Game
