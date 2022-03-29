------------------------------------------------------------------------
Texas Hold 'Em Poker Game app README
------------------------------------------------------------------------

PROJECT TITLE: Texas Hold 'Em Poker Game
PURPOSE OF PROJECT: Generate a console texas hold 'em poker game
VERSION or DATE: 3/22/2022
HOW TO START THIS PROJECT: Run main in Driver with no args
AUTHORS: Alvaro Licea
CURRENT ISSUES:
-user can crash program if they enter a really big number
-actions don't work yet
-cards are all face-up (should be face-down unless current player)

USER INSTRUCTIONS:
0. If you don't know how this game works, please research or go to:
    https://www.instructables.com/Learn-To-Play-Poker---Texas-Hold-Em-aka-Texas-Ho/
    
1. At the start, the app prompts you to enter how many players are
    playing, then their names, how many chips they start off with,
    and also how much are the blinds. 
    
    -The max # of players for a playable game is 22. The minimum is 1.
    -A player can be named anything except the exit command (*).
    -The max # of starting chips is 2147483647. The minimum is 2.

2. The game then draws 1 card for each player in order to determine
    who is the button, small blind, and big blind.
    
    -The player who has the highest card will be the designated button.
    -The player next to the button is the small blind.
    -The player next to the small blind is the big blind.
    
3. The game will now play rounds of poker until 1 player remains
    on the table, or until the user inputs the exit command.
    
    Round (a-f):

    a. Pre-Flop action:
        Blinds are in the pot, players get dealt 2 cards
        Player next to big blind (who is not small blind) acts first.
        Can either call the big blind, raise, fold, or leave the table.
        Each player then acts on the current play ending with big blind
        
    b. Flop action:
        1 card is burned, 3 cards are dealt to the board.
        Player next to button (who is not big blind) acts first.
        Can either bet, check, fold, or leave the table.
        Each player then acts on the current play ending with the button
        
    c. Turn action:
        1 card is burned, 1 card is dealt to the board.
        Player next to button (who is not big blind) acts first.
        Can either bet, check, fold, or leave the table.
        Each player then acts on the current play ending with the button
        
    d. River action:
        1 card is burned, 1 card is dealt to the board.
        Player next to button (who is not big blind) acts first.
        Can either bet, check, fold, or leave the table.
        Each player then acts on the current play ending with the button
        
    e. Once the final bets are in, the players who remained in play reveal
        their cards. The player with the best hand(s) wins the chips.
        
    f. if more than 1 player remains, Another round begins.
    
4. To exit the game at any point, enter *.
    
