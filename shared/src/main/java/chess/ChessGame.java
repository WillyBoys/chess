package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    ChessBoard board;
    TeamColor currentTurn;

    public ChessGame() {
        this.board = new ChessBoard();
        board.resetBoard();
        this.currentTurn = TeamColor.WHITE;

    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return currentTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        currentTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        // Make a copy of the board, make the move, use isInCheck to see if valid there.
        ChessBoard copyBoard = board;
        ChessPiece piece = copyBoard.getPiece(startPosition);
        piece.pieceMoves(copyBoard, startPosition);
        
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        // Makes the move on the board
        // Do this after determining whether the King is in check and checking for valid moves
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        // Check to see if the current state of the board has the King in check
        ChessPosition kingPosition = findKing(teamColor);
        if (kingPosition == null) {
            return false;
        }

        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                ChessPosition current = new ChessPosition(i, j);
                ChessPiece piece = board.getPiece(current);
                if (piece != null && piece.getTeamColor() != teamColor) {
                    //Run through all the moves the opposing team can make and see if they match the current position
                    Collection<ChessMove> moves = piece.pieceMoves(board, current);
                    for (ChessMove move : moves) {
                        if (move.getEndPosition().equals(kingPosition)) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    private ChessPosition findKing(TeamColor teamColor) {
        ChessPosition current;
        ChessPiece piece;
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                current = new ChessPosition(i, j);
                piece = board.getPiece(current);
                if (piece != null && piece.getPieceType() == ChessPiece.PieceType.KING && piece.getTeamColor() == teamColor) {
                    //Run through all the moves the opposing team can make and see if they match the current position
                    return current;
                }
            }
        }
        return null;
    }


    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessGame chessGame = (ChessGame) o;
        return Objects.equals(board, chessGame.board) && currentTurn == chessGame.currentTurn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, currentTurn);
    }

    @Override
    public String toString() {
        return "ChessGame{" +
                "board=" + board +
                ", currentTurn=" + currentTurn +
                "}\n";
    }
}
