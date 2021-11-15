package client;

import common.MessageListener; 
import common.MessageSource; 
import java.io.PrintStream;

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
 * This class is responsible for writing messages to a PrintStream (e.g., System.out).  The class 
 * implements the MessageListener interface, indicating that it plays the role of "observer" in an 
 * instance of the observer pattern.
 */
public class PrintStreamMessageListener implements MessageListener {

    /** The PrintStream to output to. */
    PrintStream out;
    
    /**
     * Initializes a new PrintStreamMessageListener with the specified PrintStream.
     *
     * @param out The PrintStream to output to. 
     */
    public PrintStreamMessageListener(PrintStream out) {

        this.out = out;
    }//end constructor


    /**
     * Used to notify observers that the subject has received a message.
     *
     * @param message The message received by the subject.
     * @param source The source from which this message originated (if needed).
     */
    public void messageReceived(String message, MessageSource source) {


    }//end messageReceived


    /**
     * used to notify observers that the subject will not receive new messages;  observers can 
     * deregister themselves.
     *
     * @param source The <code>MessageSource</code> that does not expect more messages. 
     */
    public void sourceClosed(MessageSource source) {


    }//end sourceClosed


    /**
     * Used for testing data input.
     *
     * @param args Not used. 
     */
    public static void main(String[] args) {

    }//end main
}//end class PrintStreamMessageListener
