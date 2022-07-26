# ASCII-Chess
My partner (@Eric Zhang) and I's implementation of a fully functional and playable chess game using ASCII representation for our Software Methodology class.

The 8x8 chess board is represented using whitespace as the while tiles and ## as black tiles. Chess pieces are represented by their respective ASCII characters (wP and bP for Pawns, wK and bK for Kings, wN and bN for Knights, etc.) Each chess piece is able to perform its intended movement with the code also monitoring illegal movements such as blockages, out of bounds, or simply moving the piece wrong. Upon all of the regular features and functionalities of chess that we implemented, notable features include check, checkmate, pawn promotion, en passant, and castling.

This is a 2 player game which requires argument inputs in the form of a [piece] followed by [tile position]. The board is printed each time a player makes the move and prompts the opposing player to make a move after the other has went. The game checks for the legality of the move before proceeding and will prompt a message when an illegal move is made as well as printing messages whenever a player is checked or checkmated. Furthermore, both players also have the option to draw or forfeit the game.

This project implements inheritance where all chess pieces inherit common attributes and functionalities from a parent ChessPiece class. Every unique chess piece then extends this class and implements its own functionality unique to that chess piece. With that, this project heavily uses Object oriented programming within implementing various chess piece classes and polymorphism when similar classes perform the same functionalities.
