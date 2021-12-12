package server;

import common.ConnectionAgent;
import common.MessageListener;
import common.MessageSource;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;

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
    private ArrayList<String> players;

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
        this.players = new ArrayList<>();
        this.current = 0;  //unsure what to initialize to

        try { 
            this.serverSocket = new ServerSocket(port); //SecurityException
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

        while (!serverSocket.isClosed()) {
            try {
                socket = serverSocket.accept();
                System.out.println("Now serving client " + socket.getInetAddress() + "...");

                agent = new ConnectionAgent(socket);
                agent.addMessageListener(this);

                /* Create and start a connectionAgent's Thread */
                Thread thread = new Thread(agent);
                thread.start();

                users.add(agent);
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
     * Used to broadcast a message to all other ConnectionAgents listening.
     *
     * @param message The message to be broadcast to all other ConnectionAgents listening.
     */
    public void broadcast(String message) {

        for (ConnectionAgent a : users) {
            a.sendMessage(message);
        }//end for
    }//end broadcast


    /**
     * Used to notify observers that the subject has received a message.
     *
     * @param message The message received by the subject.
     * @param source The source from which this message originated (if needed).
     */
    public void messageReceived(String message, MessageSource source) {

        String[] command = message.strip().split(" ");

        if (!game.isGameOver()) {
            switch (command[0]) {   
                case "/battle":
                    this.battle(command, source);
                    break;
                case "/start":
                    this.start(command, source);
                    break;
                case "/fire":
                    this.fire(command, source);
                    break;
                case "/surrender":
                    this.surrender(command, source);
                    break;
                case "/display":
                    this.display(command, source);
                    break;
                case "":
                    // ignore
                    break;
                default:
                    ((ConnectionAgent) source).sendMessage("Invalid command: '" + message + "'");
            }//end switch
        } else if (command[0].equals("/battle")) {
            this.game = new Game(gridSize);
            this.battle(command, source); 
        } else if (command[0].equals("/surrender")) {
            this.surrender(command, source);
        } else {
            ((ConnectionAgent) source).sendMessage("Game is over, use /battle to start again or" +
                                                    " /surrender to quit");
        }//end else
    }//end messageReceived
    

    /**
     * Used to notify observers that the subject will not receive new messages; observers can 
     * deregister themselves.
     *
     * @param source the <code>MessageSource</code> that does not expect more messages.
     */
    public void sourceClosed(MessageSource source) {

        source.removeMessageListener(this);
    }//end sourceClosed


    /**
     * Handles the /battle command from users.
     *
     * @param command /battle <username>
     * @param source The MessageSource that is sending a command.
     */
    private void battle(String[] command, MessageSource source) {

        if (command.length != 2) {
            //sendMessage(source, "Failed to join: usage: /battle <username>");
            return;
        }//end if

        ConnectionAgent agent = (ConnectionAgent) source;

        if (this.usersToSource.containsValue(source)) {
            agent.sendMessage("Failed to join: you are already in the game.");
            return;
        }//end if

        if (this.game.isStarted()) { // Can't join if game has started
            agent.sendMessage("Failed to join: the game is already in progress.");
        } else {
            String username = command[1];
            for (String name : players) {

                //Checks to see if username is already taken
                if (username.equals(name)) {
                    agent.sendMessage("Failed to join: username '" + username + "' already taken.");
                    return;
                }//end if
            }//end for

            // Player joins successfully
            this.usersToSource.put(username, source);
            this.broadcast("!!! " + username + " has entered battle");
        }//end else
        this.players.add(command[1]);
    }//end battle
    

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
        }//end if

        // Game is successfully started
        if (!this.game.isStarted() && this.usersToSource.size() >= 2) {

            //randomize which player will go first.
            Random random = new Random();
            this.current = random.nextInt(this.players.size());

            this.game.startGame();

            // Adds all the connected users to the game
            for (String username : players) {
                this.game.addPlayer(username);
            }//end for

            this.broadcast("The game begins\n" + this.game.getPlayers().get(current) + 
                            " it is your turn.");

        // too few players to begin game
        } else if (!this.game.isStarted() && this.users.size() < 2) {
            agent.sendMessage("Failed to start: not enough players to play the game.");

        // game already in progress
        } else if (this.game.isStarted()) {
            agent.sendMessage("Failed to start: the game has already started.");
        }//end else if
    }//end start


    /**
     * Handles the /fire command. In row column format.
     *
     * @param command /fire <[0-9]+> <[0-9]+> <username>
     * @param source Source sending command.
     */
    private void fire(String[] command, MessageSource source) {

        ConnectionAgent agent = (ConnectionAgent) source;

        if (command.length != 4) {
            agent.sendMessage("Failed to fire: usage: /fire <[0-9]+> <[0-9]+> <username>");
            return;
        } else if (!this.game.isStarted()) { // make sure the game has started
            agent.sendMessage("Failed to fire: game not in progress");
            return;
        } else if (!this.usersToSource.containsKey(command[3])) { // if target not in game
            agent.sendMessage("Failed to fire: user '" + command[3] + "' not found");
            return;
        } else if (!this.game.isPlayerInGame(this.getUsername(source))) {
            agent.sendMessage("Failed to fire: you are not in the game");
            return;
        }//end else if

        String curPlayer = this.game.getPlayers().get(current); // player whose turn it is
        
        if (!this.usersToSource.get(curPlayer).equals(source)) {// if it's not their turn
            agent.sendMessage("Move Failed, player turn: " + curPlayer);
        } else if (curPlayer.equals(command[3])) {
            agent.sendMessage("Failed to fire: don't shoot yourself");
        } else {
            int row = -1; int col = -1; // -1 represents default/error value
            try {
                row = Integer.parseInt(command[1]);
            }//end try 
            catch (NumberFormatException e) {
                agent.sendMessage("Failed to fire: row must be an integer");
                return;
            }//end catch
            try {
                col = Integer.parseInt(command[2]);
            }//end try 
            catch (NumberFormatException e) {
                agent.sendMessage("Failed to fire: col must be an integer");
                return;
            }//end catch
            if (!this.game.isValidShot(row, col, command[3])) {
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

                if (!this.game.shipsLeft(command[3])) {
                    this.game.remove(command[3]);
                    this.broadcast(command[3] + " has been eliminated");

                    if (this.game.amountOfPlayers() == 1) {
                        this.broadcast("GAME OVER: " + curPlayer + " wins!");
                        this.game.gameOver();
                        this.users.remove(agent);
                        if (this.users.size() == 0) {
                            System.exit(0);
                        }//end if
                    } else { 
                        this.nextTurn();
                    }//end little else
                } else {
                    this.nextTurn();
                }//end check for ships left
            }//end little else
        }//end big else
    }//end fire


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

        this.broadcast("!!!" + this.getUsername(source) + " surrendered");
        this.removePlayer(source);

    //NOTE: close the connection agents socket and remove them from the game. 

    }//end surrender


    /**
     * Removes a player from the game.
     *
     * @param source MessageSource of the player to be removed.
     */
    private void removePlayer(MessageSource source) {

        ConnectionAgent agent = (ConnectionAgent) source;
        String username = getUsername(source);

        if (!username.equals("")) {
            // Remove the player from our various data structures
            this.players.remove(username);
            this.usersToSource.remove(username);
            this.game.remove(username);
            this.users.remove(agent);
            
            if (this.users.size() == 0) {
                System.exit(0);
            }//end if

            if (this.usersToSource.size() == 1 && this.game.isStarted()) {
                this.broadcast("You are the only player remaining\nGAME OVER: " + 
                          this.usersToSource.get(this.players.get(0)) + " wins!");
                this.game.gameOver();
                this.game.remove(players.get(0));
            }//end if
        }//end if
    }//end removePlayer


    /**
     * Handles the /display command.
     *
     * @param command /display <username>
     * @param source Source sending command.
     */
    private void display(String[] command, MessageSource source) {

        ConnectionAgent agent = (ConnectionAgent) source;

        if (command.length != 2) {
            agent.sendMessage("Display failed: usage: /display <username>");
            return;
        }//end if

        if (!this.game.isStarted()) {
            agent.sendMessage("Display failed: game has not started");
            return;
        }

        if (!this.game.isPlayerInGame(command[1])) {
            agent.sendMessage("Display failed: user '" + command[1] + "' does not exist");
            return;
        }//end if

        // checks to see if they're trying to see their own board
        if (this.usersToSource.get(command[1]).equals(source)) {
            agent.sendMessage(this.game.getFullGrid(command[1]));
        } else {
            agent.sendMessage(this.game.getPublicGrid(command[1]));
        }//end else
    }//end display


    /**
     * Switches the current turn to the next player. Loops back to 0 if it is on the last player.
     */
    private void nextTurn() {

        current = current >= this.game.amountOfPlayers() - 1 ? 0 : ++current;
        this.broadcast(this.game.getPlayers().get(current) + " it is your turn");
    }//end nextTurn()
    
    /**
     * Gets the username from a specified MessageSource.
     *
     * @param source The MessageSource containing the username.
     *
     * @return The username from the provided source.
     */
    private String getUsername(MessageSource source) {

        for (java.util.Map.Entry<String, MessageSource> entry : usersToSource.entrySet()) {
            if (source.equals(entry.getValue()))
                return entry.getKey();
        }//end for
        return "";
    }//end getUserName


    /**
     * Used for testing data input.
     *
     * @param args Not used. 
     */
    public static void main(String[] args) {


    }//end main
}//end class BattleServer
