package server;

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
        int gridSize = 0;  // invalid gridSize to indicate uninitialized variable
        BattleServer server = null; // uninitialized server
        
        if (args.length == 1 || args.length == 2) {
            try {
                port = Integer.parseInt(args[0]);
            }//end try
            catch (NumberFormatException nfe) {
                System.err.println("\n'" + args[0] + "' is not a valid port #.\n");
                System.exit(1);
            }//end catch

            if (args.length == 2) {
                try {
                    gridSize = Integer.parseInt(args[1]);
                    if (gridSize < Grid.MIN_SIZE || gridSize > Grid.MAX_SIZE) {
                        throw new NumberFormatException();
                    }//end if
                    server = new BattleServer(port, gridSize);
                }//end try
                catch (NumberFormatException nfe) {
                    System.err.println("\nGrid size must be between " + Grid.MIN_SIZE + " and " +
                                        Grid.MAX_SIZE + ".\n");
                    System.exit(1);
                }//end catch
            } else {
                server = new BattleServer(port);
            }//end else
        } else {    
            System.err.println("\nUsage: java BattleServer <port #>\n\n   OR");
            System.err.println("\nUsage: java BattleServer <port #> <board size>\n");
            System.exit(1);
        }//end else

        try {
            //parse command line options
            server.listen();

        }//end try
        catch (NumberFormatException nfe) {
            System.err.println("\nNumberFormatException caught by driver.\n");
            System.err.println(nfe.getMessage());
        }//end catch
        catch (Exception e) {
            System.err.println("\nException caught by driver.\n");
            System.err.println(e.getMessage());
        }//end catch
    }//end main
}//end class BattleShipDriver
