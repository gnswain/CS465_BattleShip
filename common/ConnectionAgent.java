package common;

import java.lang.Runnable;
import java.util.Scanner;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

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
 * This class is responsible for sending messages to and receiving messages from remote hosts. 
 */
public class ConnectionAgent extends MessageSource implements Runnable {
    
    /* The Socket to which the server listens for client connections. */
    private Socket socket;

    /* The Scanner used to gather input. */
    private Scanner in;

    /* The Stream used to notify Observers. */
    private PrintStream out;

    /* The thread used to perform work for the ConnectionAgent. */
    private Thread thread;


    /**
     * Initializes a new ConnectionAgent with the provided socket.
     *
     * @param socket The socket used to communicate with the ConnectionAgent.
     */
    public ConnectionAgent(Socket socket) {
        
        super();
        this.socket = socket;
        try {
            this.in = new Scanner(socket.getInputStream());
            this.out = new PrintStream(socket.getOutputStream());
        }//end try
        catch (IOException e) {
            System.err.println(e.getMessage());
        }//end catch
    }//end constructor


    /**
     * Get the Socket to communicate with clients.
     *
     * @return A socket to communicate with clients.
     */
    public Socket getSocket() {

        return this.socket;
    }//end getSocket


    /**
     * Get an input scanner.
     *
     * @return An input scanner.
     */
    public Scanner getScanner() {

        return this.in;
    }//end getScanner


    /**
     * Get the PrintStream to notify Observers of changes.
     *
     * @return A PrintStream that will notify Observer's.
     */
    public PrintStream getPrintStream() {

        return this.out;
    }//end getPrintStream


    /**
     * Get the Thread.
     *
     * @return A thread.
     */
    public Thread getThread() {

        return this.thread;
    }//end getThread


    /**
     * Assign a new thread.
     *
     * @param thread New thread to be assigned.
     */
    public void setThread(Thread thread) {

        this.thread = thread;
    }//end setThread


    /**
     * Displays the state of the ConnectionAgent.
     */
    @Override
    public String toString() {

        /* Building the return value. */
        StringBuilder str = new StringBuilder();

        str.append("Empty connectionAgent tostring");

        return str.toString();
    }//end toString 


    /**
     * Sends a message.
     *
     * @param message The message you want to send.
     */
    public void sendMessage(String message) {

        this.out.println(message);
        this.out.flush();
        
        // /* Notify all registered listeners. */
        // this.notifyReceipt(message); //notifyReceipt is in the messageSource class
    }//end sendMessage


    /**
     * Determines if the client is connected to the server.
     *
     * true If the client has an open connection to the server.
     */
    public boolean isConnected(){

        return !this.socket.isClosed();
    }//end isConnected


    /**
     * Closes the input Scanner, PrintStream, Socket connection and remove the ConnectionAgent as
     * a MessageSource.
     */
    public void close() {

        /* if the socket is not closed then close it. */
        try {
            this.socket.close();
            this.in.close();
            this.out.close();
            this.closeMessageSource();
        }//end try
        catch (IOException ioe) {
            System.err.println("Unable to close the socket in ConnectionAgent.close()");
            ioe.printStackTrace();
        }//end catch
    }//end close


    /**
     * Work to be performed by a thread.  Continually get commands from the user to perform in the 
     * game.
     */
    @Override
    public void run() {

        this.thread = Thread.currentThread();

        while(!this.thread.isInterrupted()) {
            if(this.in.hasNext()) {
                this.notifyReceipt(this.in.nextLine());
            }//end if
        }//end while
    }//end run 
}//end class ConnectionAgent
