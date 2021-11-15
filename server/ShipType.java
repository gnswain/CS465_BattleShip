package server;

import java.io.Serializable;

/**
 * @author Brandon Welch
 * @author Graham Swain
 * @version November 14, 2021
 *
 * CS465-01, Computer Networks
 * Dr. Scott Barlowe
 *
 * Fall 2021
 *
 * Project Three: Battleship(Multiuser game)
 * 
 * Enumeration for allowed Ships in Battleship.
 */
public enum ShipType implements Serializable, Comparable<ShipType> {
    
    /** Literal for Carrier ships. */
    CARRIER(2),

    /** Literal for Battleship ships. */
    BATTLESHIP(3),

    /** Literal for Cruiser ships. */
    CRUISER(4),

    /** Literal for Submarine ships. */
    SUBMARINE(3),

    /** Literal for Destroyer ships. */
    DESTROYER(5);

    /** The size of a ship or the number of squares consumed on a Grid. */
    private final int size;

    /**
     * Displays the String representation of the Enum's literal name.
     */
    ShipType(int size) {
    
        this.size = size;
    }//end constructor 


    /**
     * Gets the size of the ship.
     *
     * @return The size of the ship or the number of squares a ship will consume on a grid.
     */
    public int getSize() {

        return this.size;
    }//end getSize 


    /**
     * Displays the String representation of the Enum's literal name. This will only display the 
     * first letter of the literal as it would appear on a grid.
     */
    @Override
    public String toString() {

        return this.name().toUpperCase().substring(0, 1);
    }//end toString 
}//end class ShipType
