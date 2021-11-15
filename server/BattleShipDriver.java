package server;

import java.io.FileNotFoundException;

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
 * This class contains an application that can drive the BattleShipServer.  It parses command line 
 * options, instantiates a BattleServer, and calls its listen() method.  This takes two command 
 * line arguments, the port number for the server and the size of the board (if the size is left 
 * off, default to size 10 x 10).  Square arrays are assumed.
 */
public class BattleShipDriver {
    
    /**
     * This method serves as the entry point of the server program.
     *
     * @param args Command line arguments to the program.  There must be exactly _____ arguments. 
     * The first parameter specifies _____.  The second parameter, if present, must....
     */
    public static void main(String[] args) {

        if (args.length != 2) {
            System.err.println("\nUsage: java BattleServer <port #> <board size>");
            System.exit(1);
        }//end if    

        try {


        }//end try
        catch (NumberFormatException nfe) {
            System.err.println("NumberFormatException caught by driver.");
            System.err.println(nfe.getMessage());
        }//end catch
        catch (Exception e) {
            System.err.println("Exception caught by driver.");
            System.err.println(e.getMessage());
        }//end catch
    }//end main
}//end class BattleShipDriver
