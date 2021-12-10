package server;

import common.ConnectionAgent;
import common.MessageListener;
import common.MessageSource;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Hashtable;

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

    /** Links users to a MessageSource. */
    private Hashtable<String, MessageSource> usersToSource;

    /** Size of grids being used. */
    private int gridSize;


    /**
     * Initializes a BattleServer to listen on the port provided. Uses default grid size.
     *
     * @param port The port to listen for incoming client requests.
     */
    public BattleServer(int port) {

        this(port, Game.DEFAULT_GRID);
    }//end constructor


    /**
     * Initializes a BattleServer to listen on the port provided and with a specified game size.
     *
     * @param port The port to listen for incoming client requests.
     * @param gridSize Size of the grids to be played on.
     */
    public BattleServer(int port, int gridSize) {

        this.gridSize = gridSize;
        this.game = new Game(gridSize);
        this.users = new ArrayList<>();
        this.usersToSource = new Hashtable<>();
        this.usernames = new ArrayList<>();
        this.current = 0;  //unsure what to initialize to

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
     * Used to listen for and accept incoming client requests. Then creating a ConnectionAgent 
     * for the new client request.
     */
    public void listen() {
        /* An socket connection to a specific client */
        Socket socket = null;

        /* A ConnectionAgent representing the new client that has connected to the server. */
        ConnectionAgent agent = null;

System.out.println("Entered listen()");
        while (!serverSocket.isClosed()) {
System.out.println("Entered listen() while loop");
            try {
System.out.println("Entered try block");
                socket = serverSocket.accept();
System.out.println("Accepted a new socket");
//System.out.println("Now serving client " + socket.getInetAddress() + "...");
                agent = new ConnectionAgent(socket);
                agent.addMessageListener(this);
System.out.println("added agent " + agent + " to users ArrayList");
//                 agent.run();  //start the ConnectionAgent's thread to process client commands.
// System.out.println("Calling ConnectionAgent's run()");

            Thread thread = new Thread(agent);
            thread.start();

            users.add(agent);

            }//end try
            catch (IOException ioe) {
                System.err.println("\nIOException caught while attempting to accept a connection.");
                System.err.println();
                ioe.printStackTrace();
                System.exit(1);
            } // end catch
        } // end while
    } // end listen


    /**
     * Used to broadcast a message.
     *
     * @param message The message to be broadcast.
     */
    public void broadcast(String message) {
        for (ConnectionAgent a : users) {
            a.sendMessage(message);
        }
    } // end broadcast


    /**
     * Used to notify observers that the subject has received a message.
     *
     * @param message The message received by the subject.
     * @param source The source from which this message originated (if needed).
     */
    public void messageReceived(String message, MessageSource source) {
        String[] command = message.strip().split(" ");
System.out.println("Message received: '" + message + "'" + " message[0]: '" + command[0] + "'");
        if (!game.isGameOver()) {
            switch (command[0]) {   
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
                case "":
                    // ignore
                    break;
                default:
                    ((ConnectionAgent) source).sendMessage("Invalid command: '" + message + "'");
            } // end switch
        } else if (command[0].equals("/battle")) {
            game = new Game(gridSize);
            battle(command, source); 
        } else if (command[0].equals("/surrender")) {
            surrender(command, source);
        } else {
            ((ConnectionAgent) source).sendMessage("Game is over, use /battle to start again or" +
                                                    " /surrender to quit");
        }
    } // end messageReceived
    
    /**
     * Used to notify observers that the subject will not receive new messages; observers can 
     * deregister themselves.
     *
     * @param source the <code>MessageSource</code> that does not expect more messages.
     */
    public void sourceClosed(MessageSource source) {
        removePlayer(source);
        source.removeMessageListener(this);
    } //end sourceClosed

    /**
     * Handles the /battle command from users.
     *
     * @param command /battle <username>
     * @param source Source sending command.
     */
    private void battle(String[] command, MessageSource source) {
        if (command.length != 2) {
            //sendMessage(source, "Failed to join: usage: /battle <username>");
            return;
        } // end if
        ConnectionAgent agent = (ConnectionAgent) source;
        if (usersToSource.containsValue(source)) {
            agent.sendMessage("Failed to join: you are already in the game.");
            return;
        } // end if
        if (game.isStarted()) { // Can't join if game has started
            agent.sendMessage("Failed to join: game already in progress");
        } else {
            String username = command[1];
            for (String name : usernames) {
                if (username.equals(name)) {
                    agent.sendMessage("Failed to join: username '" + username + "' already taken");
                    return; // Checks to see if username is taken
                } // end if
            } // end for

            // Player joins successfully
System.out.println("Putting: " + username + " into the game");
            usersToSource.put(username, source);
            usernames.add(username);
            broadcast("!!! " + username + " has entered battle");
        } // end else
    } // end battle
    
    /**
     * Handles the /start command.
     *
     * @param command /start
     * @param source Source sending command.
     */
    private void start(String[] command, MessageSource source) {
        ConnectionAgent agent = (ConnectionAgent) source;

        if (command.length != 1) {
            agent.sendMessage("Failed to start: usage: /start");
            return;
        }
        if (!game.isStarted() && usersToSource.size() >= 2) {
            // Game is successfully started
            this.current = 0;
            this.game.startGame();

            // Adds all the connected users to the game
            for (String username : usernames)
                game.addPlayer(username);

            broadcast("The game begins\n" + usernames.get(current) + " it is your turn");
        } else if (!game.isStarted() && this.users.size() < 2) {
            // too few players to begin game
            agent.sendMessage("Failed to start: not enough players to play the game");
        } else if (game.isStarted()) {
            // game already in progress
            agent.sendMessage("Failed to start: the game has already started");
        }//end elseif
    }//end start


    /**
     * Handles the /fire command. In row col format.
     *
     * @param command /fire <[0-9]+> <[0-9]+> <username>
     * @param source Source sending command.
     */
    private void fire(String[] command, MessageSource source) {
        ConnectionAgent agent = (ConnectionAgent) source;
        if (command.length != 4) {
            agent.sendMessage("Failed to fire: usage: /fire <[0-9]+> <[0-9]+> <username>");
            return;
        } else if (!game.isStarted()) { // make sure the game has started
            agent.sendMessage("Failed to fire: game not in progress");
            return;
        } else if (!usersToSource.containsKey(command[3])) { // if target not in game
            agent.sendMessage("Failed to fire: user '" + command[3] + "' not found");
            return;
        }

        String curPlayer = usernames.get(current); // player whose turn it is
        
        if (!usersToSource.get(curPlayer).equals(source)) {// if it's not their turn
            agent.sendMessage("Move Failed, player turn: " + curPlayer);
        } else if (curPlayer.equals(command[3])) {
            agent.sendMessage("Failed to fire: don't shoot yourself");
        } else {
            int row = -1; int col = -1; // -1 represents default/error value
            try {
                row = Integer.parseInt(command[1]);
            } catch (NumberFormatException e) {
                agent.sendMessage("Failed to fire: row must be an integer");
                return;
            } 
            try {
                col = Integer.parseInt(command[2]);
            } catch (NumberFormatException e) {
                agent.sendMessage("Failed to fire: col must be an integer");
                return;
            }
            if (!game.isValidShot(row, col, command[3])) {
                agent.sendMessage("Failed to fire: invalid shot");
            } else {
                boolean hit = game.shoot(row, col, command[3]);
                String success = "";
                if (hit) 
                    success = "HIT";
                else
                    success = "MISS";
                agent.sendMessage("Shots fired at " + command[3] + " by " + curPlayer + ": " + 
                                   success);
                
System.out.println(game.getFullGrid(command[3]));
                if (!game.shipsLeft(command[3])) {
                    usernames.remove(command[3]);
                    game.remove(command[3]);
                    broadcast(command[3] + " has been eliminated");

                    if (usernames.size() == 1) {
                        for (String s : usernames) {
                            System.out.println(s);
                        }
                        broadcast("GAME OVER: " + curPlayer + " wins!");
                        game.gameOver();
                    } else { 
                        nextTurn();
                    }
                }
            }
        }
    } // end fire


    /**
     * Handles the /surrender command.
     *
     * @param command /surrender
     * @param source Source sending command.
     */
    private void surrender(String[] command, MessageSource source) {
        ConnectionAgent agent = (ConnectionAgent) source;

        if (command.length != 1) {
            agent.sendMessage("Usage: /surrender");
            return;
        }//end if

    //NOTE: close the connection agents socket and remove them from the game. 

    }//end surrender

    private void removePlayer(MessageSource source) {
        ConnectionAgent agent = (ConnectionAgent) source;
        String username = getUsername(source);

        if (!username.equals("")) {
            // Remove the player from our various data structures
            usernames.remove(username);
            users.remove(agent);
            usersToSource.remove(username);
            game.remove(username);

            if (this.usersToSource.size() == 1 && game.isStarted()) {
                broadcast("You are the only player remaing\nGAME OVER: " + 
                          usersToSource.get(usernames.get(0)) + " wins!");
                game.gameOver();
                usersToSource.remove(usernames.get(0));
            }
        }
    }


    /**
     * Handles the /display command.
     *
     * @param command /display <username>
     * @param source Source sending command.
     */
    private void display(String[] command, MessageSource source) {
        ConnectionAgent agent = (ConnectionAgent) source;

        if (command.length != 2) {
            agent.sendMessage("Usage: /display <username>");
            return;
        }//end if
    }//end display

    /**
     * Switches the current turn to the next player. Loops back to 0 if it is on the last player.
     */
    private void nextTurn() {

        current = current >= this.usernames.size() - 1 ? 0 : ++current;
        broadcast(usernames.get(current) + " it is your turn");
    }//end nextTurn()
    
    private String getUsername(MessageSource source) {
        for (java.util.Map.Entry<String, MessageSource> entry : usersToSource.entrySet()) {
            if (source.equals(entry.getValue()))
                return entry.getKey();
        }
        return "";
    }

    /**
     * Used for testing data input.
     *
     * @param args Not used. 
     */
    public static void main(String[] args) {



    }//end main
}//end class BattleServer
