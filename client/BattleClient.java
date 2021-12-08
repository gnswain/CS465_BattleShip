package client;

import common.MessageListener;
import common.MessageSource;
import common.ConnectionAgent;
import java.net.UnknownHostException;
import java.net.Socket;
import java.net.InetAddress;

/**
 * @author Graham Swain
 * @author Brandon Welch
 *
 * @version December 8, 2021
 *
 * CS465-01, Computer Networks
 * Dr. Scott Barlowe
 *
 * Fall 2021
 *
 * Project Three: Battleship(Multiuser game)
 * 
 * This class is one of the classes that implement the client-side logic of this client-server 
 * application.  It is responsible for creating a ConnectionAgent, reading input from the user, and 
 * sending that input to the server via the ConnectionAgent.  The class implements the 
 * MessageListener interface (i.e., it can "observe" objects that are MessageSources).  The class 
 * also extends MessageSource, indicating that it also plays the role of "subject" in an instance 
 * of the observer pattern.
 */
public class BattleClient extends MessageSource implements MessageListener {
    
    /** The name of the server to connect to. */
    private InetAddress host;

    /** The port # to send requests to. */
    private int port;

    /** The username to interact with. */
    private String username;


    /**
     * Initializes a new BattleClient.
     *
     * @param hostname The name of the host server you wish to connect to.
     * @param port The port number the server is listening on.
     * @param username The user you wish to interact with. 
     */
    public BattleClient(String hostname, int port, String username) {

        try {
            this.host = InetAddress.getByName(hostname); //SecurityException
        }//end try
        catch (UnknownHostException uhe) {
            System.err.println("\nThe hostname/server you are trying to reach is invalid.\n");
            System.err.println(uhe.getMessage());
            uhe.printStackTrace();
        }//end catch

        this.port = port;
        this.username = username;
    }//end constructor


    /**
     * Used to connect to the server.
     *
     */
    protected void connect() {
System.out.println("called BattleClient.connect()");
        /* The clients Socket to connect with the Server. */
        Socket socket = new Socket(this.host, this.port);
System.out.println("creating a connection agent in BattleClient.listen()");        
        ConnectionAgent agent = new ConnectionAgent(socket);



    }//end main


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
}//end class BattleClient
