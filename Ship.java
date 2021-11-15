package common;

import java.io.Serializable;

/**
 * @author Brandon Welch
 * @version November 7, 2021
 *
 * CS465-01, Computer Networks
 * Dr. Scott Barlowe
 *
 * Fall 2021
 *
 * Project Two: Where the Magic Happens
 * 
 * Enumeration for allowed Card type in Magic the Gathering.
 */
public enum CardType implements Serializable, Comparable<CardType> {
    
    /** Literal for Creature cards. */
    CREATURE,

    /** Literal for Land cards. */
    LAND,

    /** Literal for Spell cards. */
    SPELL,

    /** Literal for Unknown cards. */
    UNKNOWN;


    /**
     * Displays the String representation of the Enum's literal name.
     */
    @Override
    public String toString() {

        return this.name();
    }//end toString 
}//end class Card
