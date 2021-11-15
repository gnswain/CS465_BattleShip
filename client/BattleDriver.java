package client;

//import client.

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
 * This class contains an application that can drive the BattleClient.  It parses command line 
 * options, instantiates a BattleClient, reads messages from the keyboards, and sends them to the 
 * client.  The command line arguments are: hostname, port number and the user's nickname.  All of 
 * these command line arguments are required.
 */
public class BattleDriver {
    
    /**
     * This method serves as the entry point of the client program.
     *
     * @param args Command line arguments to the program.  There must be exactly _____ arguments. 
     * The first parameter specifies _____.  The second parameter, if present, must....
     */
    public static void main(String[] args) {

        if (args.length != 3) {
            System.err.println("\nUsage: java BattleDriver <hostname> <port #> <username>");
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
}//end class BattleDriver
