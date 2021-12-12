CS 465 - Project 3

Authors: Graham Swain
         Brandon Welch

Date: December 11, 2021

This program creates a multi-user game of Battleship.  The server is expected to host only a single 
game of BattleShip at a time.  The game is won after all but one user has surrendered or no players  
have any ships remaining. All ship types and the number of ships are randomly chosen and placed on 
a users board. 


Compile: javac common/*.java client/*.java server/*.java


Server Usage:   java BattleShipDriver <port #> <grid size>
The Port # is required.
Grid Size is optional.

Client Usage:   java BattleDriver <hostname> <port #> <username>
All three arguments are required by the program.

Supported Client Commands:
  - /battle <username>
  - /start 
  - /fire <[0-9]+> <[0-9]+> <username>
  - /surrender
  - /display <username>

Errors:
  - After a game has finished and a winner is delcared the winner will need to use the /surrender 
    command to exit the game.
  - If a client terminates the game without using the /surrender command (ctl + C) the server is 
    unable to drop the client from the game.
  - If all clients terminate the game without using the /surrender command the server still assumes 
    all clients are connected and never ends the game.


Notes:
  - 
