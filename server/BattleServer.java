package server;

import common.ConnectionAgent;
import common.MessageListener;
import common.MessageSource;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

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
    private ServerSocket serverSocket;

    /** Index of the player whose turn it is. */
    private int current;

    /** List of usernames of players connected to the game. Used to track whose turn it is. */
    private ArrayList<String> usernames;

    /** Game being played. */
    private Game game;

    /** Stores ConnectionAgents for all connected users. */
    private ArrayList<ConnectionAgent> users;


    /**
     * Initializes a BattleServer to listen on the port provided. Uses default grid size.
     *
     * @param port The port to listen for incoming client requests.
     */
    public BattleServer(int port) {
        this.game = new Game();
        this.users = new ArrayList<>();

        this.current = -1;  //unsure what to initialize to
        this.game = null;  //unsure what to initialize to

        try { 
            this.serverSocket = new ServerSocket(port);  //IllegalArgumentException
                                                         //SecurityException
        }//end try
        catch (IOException ioe) {
            System.err.println("\nIOException caught while initializing a ServerSocket.\n");
            System.exit(1);
        }//end catch
        catch (IllegalArgumentException iae) {
            System.err.println("\nPort number is out of range.\n");
            System.exit(1);
        }//end catch
    }//end constructor

    /**
     * Initializes a BattleServer to listen on the port provided and with a specified game size.
     *
     * @param port The port to listen for incoming client requests.
     * @param gridSize Size of the grids to be played on.
     */
    public BattleServer(int port, int gridSize) {
        this(port);
        this.game = new Game(gridSize);
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
                users.add(agent);
                agent.run();  //start the ConnectionAgent's thread to process client commands.
            }//end try
            catch (IOException ioe) {
                System.err.println("\nIOException caught while attempting to accept a connection.");
                System.err.println();
                ioe.printStackTrace();
                System.exit(1);
            }//end catch
        }//end while
    }//end listen


    /**
     * Used to broadcast a message.
     *
     * @param message The message to be broadcast.
     */
    public void broadcast(String message) {
        for (ConnectionAgent a : users) {
            a.sendMessage(message);
        }
    } //end broadcast


    /**
     * Used to notify observers that the subject has received a message.
     *
     * @param message The message received by the subject.
     * @param source The source from which this message originated (if needed).
     */
    public void messageReceived(String message, MessageSource source) {
        String[] command = message.split(" ");

        switch (command[0]) {                               // Assumes correct input. Case sensitive
            case "/battle":
                battle(command, source);
                break;
            case "/start":
                start(command, source);
                break;
            case "/fire":
                fire(command, source);
                break;
            case "/surrender":
                surrender(command, source);
                break;
            case "/display":
                display(command, source);
                break;
        }
    } //end messageReceived
    
    /**
     * Used to notify observers that the subject will not receive new messages; observers can 
     * deregister themselves.
     *
     * @param source the <code>MessageSource</code> that does not expect more messages.
     */
    public void sourceClosed(MessageSource source) {


    } //end sourceClosed

    /**
     * Handles the /battle command.
     * @param command /battle <username>
     * @param source Source sending command.
     */
    private void battle(String[] command, MessageSource source) {
        if (command.length != 2) {
            sendMessage(source, "Usage: /battle <username>");
            return;
        }
        if (game.isStarted()) { // Can't join if game has started
            sendMessage(source, "Game already in progress");
        } else {
            String username = command[1];
            for (String name : usernames) {
                if (username.equals(name)) {
                    sendMessage(source, "Username '" + username + "' already taken");
                    return; // Checks to see if username is taken
                }
            }

            // Player joins successfully
            usernames.add(username);
            broadcast("!!! " + username + " has entered battle");
        }
    } // end battle

    /**
     * Handles the /start command.
     * @param command /start
     * @param source Source sending command.
     */
    private void start(String[] command, MessageSource source) {
        if (command.length != 1) {
            sendMessage(source, "Usage: /start");
            return;
        }
        if (!game.isStarted() && users.size() >= 2) {
            // Game is successfully started
            this.current = 0;
            this.game.startGame();

            // Adds all the connected users to the game
            for (String username : usernames)
                game.addPlayer(username);

            broadcast("The game begins");
            broadcast(usernames.get(current) + " it is your turn");
        } else if (!game.isStarted() && this.users.size() < 2) {
            // too few players to begin game
            sendMessage(source, "Not enough players to play the game");
        } else if (game.isStarted()) {
            // game already in progress
            sendMessage(source, "The game has already started");
        }
    } // end start

    /**
     * Handles the /fire command.
     * @param command /fire <[0-9]+> <[0-9]+> <username>
     * @param source Source sending command.
     */
    private void fire(String[] command, MessageSource source) {
        if (command.length != 4) {
            sendMessage(source, "Usage: /fire <[0-9]+> <[0-9]+> <username>");
            return;
        }
    } // end fire

    /**
     * Handles the /surrender command.
     * @param command /surrender
     * @param source Source sending command.
     */
    private void surrender(String[] command, MessageSource source) {
        if (command.length != 1) {
            sendMessage(source, "Usage: /surrender");
            return;
        }
    } // end surrender

    /**
     * Handles the /display command.
     * @param command /display <username>
     * @param source Source sending command.
     */
    private void display(String[] command, MessageSource source) {
        if (command.length != 2) {
            sendMessage(source, "Usage: /display <username>");
            return;
        }
    } // end display

    /**
     * Sends a message to a specific player.
     * @param source Player to send message to.
     * @param message Message being sent.
     */
    private void sendMessage(MessageSource source, String message) {
        for (ConnectionAgent player : users) {
            if (player.equals(source)) {
                player.sendMessage(message);
                return;
            }
        }
    } // end sendMessage

    /**
     * Switches the current turn to the next player. Loops back to 0 if it is on the last player.
     */
    private void nextTurn() {
        current = current >= this.usernames.size() - 1 ? 0 : ++current;
        broadcast(usernames.get(current) + " it is your turn");
    } // end nextTurn()


    /**
     * Used for testing data input.
     *
     * @param args Not used. 
     */
    public static void main(String[] args) {



    }//end main
}//end class BattleServer
