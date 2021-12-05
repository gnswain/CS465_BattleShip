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
     * @param args Command line arguments to the program.  There must be at least one argument. 
     * The first parameter specifies the port number the server will listen on.  The second 
     * parameter, if present, will represent the size of the game board.
     */
    public static void main(String[] args) {

        int port = -1; // invalid port to indicate uninitialized variable
        int gridSize = 0;  //invalid port to indicate uninitialized variable

        if (args.length == 1) {
            try {
                port = Integer.parseInt(args[1]);
            }//end try
            catch (NumberFormatException nfe) {
                System.err.println(port + "is not a valid port #.");
            }//end catch
        } else if (args.length == 2) {
            try {
                port = Integer.parseInt(args[1]);
                gridSize = Integer.parseInt(args[2]);
            }//end try
            catch (NumberFormatException nfe) {
                System.err.println("Unable to parse command line arguments into numbers.");
            }//end catch
            
        } else {    
            System.err.println("\nUsage: java BattleServer <port #>");
            System.err.println("\nUsage: java BattleServer <port #> <board size>");
            System.exit(1);
        }//end else

        try {
            
            //parse command line options
            
            BattleServer server = new BattleServer(port);
            server.listen();

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
