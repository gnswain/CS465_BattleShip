package client;

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
 * This class contains an application that can drive the BattleClient.  It parses command line 
 * options, instantiates a BattleClient, reads messages from the keyboards, and sends them to the 
 * client.  The command line arguments are: hostname, port number and the user's nickname.  All of 
 * these command line arguments are required.
 */
public class BattleDriver {
    
    /**
     * This method serves as the entry point of the client program.
     *
     * @param args Command line arguments to the program.  There must be exactly four arguments. 
     * The first parameter specifies the hostname you wish to connect to.  The second parameter is 
     * the port number you wish to connect to to host with.  The third parameter is the username 
     * you wish to use while in a game.
     */
    public static void main(String[] args) {

        if (args.length != 3) {
            System.err.println("\nUsage: java BattleDriver <hostname> <port #> <username>\n");
            System.exit(1);
        }//end if    

        final String HOSTNAME = args[0];
        int port = -1;  //invalid port to indicate uninitialized variable
        final String USERNAME = args[2];

        try {
            port = Integer.parseInt(args[1]);
        }//end try
        catch (NumberFormatException nfe) {
            System.err.println("\nThe port provided is not a number.");
            System.err.println(nfe.getMessage() + "\n");
            System.exit(1);
        }//end catch

        /* Create and initialize a new client. */
        BattleClient client = new BattleClient(HOSTNAME, port, USERNAME);

        client.connect();
    }//end main
}//end class BattleDriver
