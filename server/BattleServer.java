package server;

import common.ConnectionAgent;
import common.MessageListener;
import common.MessageSource;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Brandon Welch
 * @author Graham Swain
 * @version December 4, 2021
 *
 * CS465-01, Computer Networks
 * Dr. Scott Barlowe
 *
 * Fall 2021
 *
 * Project Three: Battleship(Multiuser game)
 * 
 * This class is one of the classes that implement the server-side logic of this client-server 
 * application.  It is responsible for accepting incoming connections, creating ConnectionAgents, 
 * and passing the ConnectionAgent off to threads for processing.
 */
public class BattleServer implements MessageListener{

    /** The Server's socket to listen for incoming client requests. */
    ServerSocket serverSocket;

    /** */
    int current;

    /** */
    Game game;


    /**
     * Initializes a BattleServer to listen on the port provided.
     *
     * @param port The port to listen for incoming client requests.
     */
    public BattleServer(int port) {

        this.current = -1;  //unsure what to initialize to
        this.game = null;  //unsure what to initialize to

        try { 
            this.serverSocket = new ServerSocket(port);  //IllegalArgumentException
                                                         //SecurityException
        }//end try
        catch (IOException ioe) {
            System.err.println("IOException caught while initializing a ServerSocket.");
        }//end catch
    }//end constructor


    /**
     * Used to listen for and accept incoming client requests. Then creating a ConnectionAgent 
     * for the new client request.
     */
    public void listen() {
        
        /* An socket connection to a specific client */
        Socket socket = null;

        /* A ConnectionAgent representing the new client that has connected to the server. */
        ConnectionAgent agent = null;

        while (!serverSocket.isClosed()) {
            try {
                socket = serverSocket.accept();
System.out.println("Now serving client " + socket.getInetAddress() + "...");
                agent = new ConnectionAgent(socket);
                agent.run();  //start the ConnectionAgent's thread to process client commands.
            }//end try
            catch (IOException ioe) {
                System.err.println("IOException caught while attempting to accept a connection");
                ioe.printStackTrace();
            }//end catch
        }//end while
    }//end listen


    /**
     * Used to broadcast a message.
     *
     * @param message The message to be broadcast.
     */
    public void broadcast(String message) {


    }//end broadcast


    /**
     * Used to notify observers that the subject has received a message.
     *
     * @param message The message received by the subject.
     * @param source The source from which this message originated (if needed).
     */
    public void messageReceived(String message, MessageSource source) {



    }//end messageReceived
    
    /**
     * Used to notify observers that the subject will not receive new messages; observers can 
     * deregister themselves.
     *
     * @param source the <code>MessageSource</code> that does not expect more messages.
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
}//end class BattleServer
