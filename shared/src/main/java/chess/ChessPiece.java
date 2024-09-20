package chess;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    PieceType type;
    ChessGame.TeamColor teamColor;
    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.type = type;
        this.teamColor = pieceColor;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING, QUEEN, BISHOP, KNIGHT, ROOK, PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return teamColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPiece piece = board.getPiece(myPosition);
        Collection<ChessMove> moves = new ArrayList<>();

        if (piece == null) {
            return moves;
        }

        switch (piece.getPieceType()) {
            case PAWN:
                // Implement pawn move logic
                addPawnMoves(board, myPosition);
                break;
            case ROOK:
                // Implement rook move logic
                moves = addRookMoves(board, myPosition);
                break;
            case BISHOP:
                // Implement bishop move logic
                moves = addBishopMoves(board, myPosition);
                break;
            case KNIGHT:
                // Implement knight move logic
                addKnightMoves(board, myPosition);
                break;
            case QUEEN:
                // Implement queen move logic
                moves = addQueenMoves(board, myPosition);
                break;
            case KING:
                // Implement king move logic
                addKingMoves(board, myPosition);
                break;
            default:
                throw new IllegalStateException("Unexpected piece type: " + piece);
        }
        return moves;
    }
        // Example methods for adding specific piece moves

        private void addPawnMoves(ChessBoard board, ChessPosition position){
            // Implement pawn movement logic here
        }

        private Collection<ChessMove> addRookMoves (ChessBoard board, ChessPosition position){
            ArrayList<ChessMove> moves = new ArrayList<>();
            // Right
            for (int i = 1; i < 9 - position.getColumn(); i++) {
                ChessPosition endPosition = new ChessPosition(position.getRow(), position.getColumn() + i);
                ChessPiece piece = board.getPiece(endPosition);
                if (piece == null || piece.getTeamColor() != getTeamColor()) {
                    moves.add(new ChessMove(position, endPosition, null));
                }
                if (piece != null) {
                    break;
                }
            }
            // Left
            for (int i = 1; i < position.getColumn(); i++) {
                ChessPosition endPosition = new ChessPosition(position.getRow(), position.getColumn() - i);
                ChessPiece piece = board.getPiece(endPosition);
                if (piece == null || piece.getTeamColor() != getTeamColor()) {
                    moves.add(new ChessMove(position, endPosition, null));
                }
                if (piece != null) {
                    break;
                }
            }
            // Down
            for (int i = 1; i < position.getRow(); i++) {
                ChessPosition endPosition = new ChessPosition(position.getRow() - i, position.getColumn());
                ChessPiece piece = board.getPiece(endPosition);
                if (piece == null || piece.getTeamColor() != getTeamColor()) {
                    moves.add(new ChessMove(position, endPosition, null));
                }
                if (piece != null) {
                    break;
                }
            }
            // Up
            for (int i = 1; i < 9 - position.getRow(); i++) {
                ChessPosition endPosition = new ChessPosition(position.getRow() + i, position.getColumn());
                ChessPiece piece = board.getPiece(endPosition);
                if (piece == null || piece.getTeamColor() != getTeamColor()) {
                    moves.add(new ChessMove(position, endPosition, null));
                }
                if (piece != null) {
                    break;
                }
            }
            return moves;
        }

        private Collection<ChessMove> addBishopMoves (ChessBoard board, ChessPosition position){
            ArrayList<ChessMove> moves = new ArrayList<>();
            //Moving Up and Right
            int moveMax = 8 - Math.max(position.getRow(), position.getColumn());
            for (int i = 1; i <= moveMax; i++) {
                ChessPosition endPosition = new ChessPosition(position.getRow() + i, position.getColumn() + i);
                ChessPiece piece = board.getPiece(endPosition);
                if (piece == null || piece.getTeamColor() != getTeamColor()) {
                    moves.add(new ChessMove(position, endPosition, null));
                }
                if (piece != null) {
                    break;
                }
            }
            // Moving Down and Right
            moveMax = 8 - Math.max(9 - position.getRow(), position.getColumn());
            for (int i = 1; i <= moveMax; i++) {
                ChessPosition endPosition = new ChessPosition(position.getRow() - i, position.getColumn() + i);
                ChessPiece piece = board.getPiece(endPosition);
                if (piece == null || piece.getTeamColor() != getTeamColor()) {
                    moves.add(new ChessMove(position, endPosition, null));
                }
                if (piece != null) {
                    break;
                }
            }
            // Moving Left and Up
            moveMax = 8 - Math.max(position.getRow(),9 - position.getColumn());
            for (int i = 1; i <= moveMax; i++) {
                ChessPosition endPosition = new ChessPosition(position.getRow() + i, position.getColumn() - i);
                ChessPiece piece = board.getPiece(endPosition);
                if (piece == null || piece.getTeamColor() != getTeamColor()) {
                    moves.add(new ChessMove(position, endPosition, null));
                }
                if (piece != null) {
                    break;
                }
            }
            // Moving Left and Down
            moveMax = 8 - Math.max(9 - position.getRow(),9 - position.getColumn());
            for (int i = 1; i <= moveMax; i++) {
                ChessPosition endPosition = new ChessPosition(position.getRow() - i, position.getColumn() - i);
                ChessPiece piece = board.getPiece(endPosition);
                if (piece == null || piece.getTeamColor() != getTeamColor()) {
                    moves.add(new ChessMove(position, endPosition, null));
                }
                if (piece != null) {
                    break;
                }
            }
            return moves;
        }

        private void addKnightMoves (ChessBoard board, ChessPosition position){
            // Implement knight movement logic here
        }

        private Collection<ChessMove> addQueenMoves (ChessBoard board, ChessPosition position){
            // Bishop Logic
            ArrayList < ChessMove > moves = new ArrayList<>();
            //Moving Up and Right
            int moveMax = 8 - Math.max(position.getRow(), position.getColumn());
            for (int i = 1; i <= moveMax; i++) {
                ChessPosition endPosition = new ChessPosition(position.getRow() + i, position.getColumn() + i);
                ChessPiece piece = board.getPiece(endPosition);
                if (piece == null || piece.getTeamColor() != getTeamColor()) {
                    moves.add(new ChessMove(position, endPosition, null));
                }
                if (piece != null) {
                    break;
                }
            }
            // Moving Down and Right
            moveMax = 8 - Math.max(9 - position.getRow(), position.getColumn());
            for (int i = 1; i <= moveMax; i++) {
                ChessPosition endPosition = new ChessPosition(position.getRow() - i, position.getColumn() + i);
                ChessPiece piece = board.getPiece(endPosition);
                if (piece == null || piece.getTeamColor() != getTeamColor()) {
                    moves.add(new ChessMove(position, endPosition, null));
                }
                if (piece != null) {
                    break;
                }
            }
            // Moving Left and Up
            moveMax = 8 - Math.max(position.getRow(),9 - position.getColumn());
            for (int i = 1; i <= moveMax; i++) {
                ChessPosition endPosition = new ChessPosition(position.getRow() + i, position.getColumn() - i);
                ChessPiece piece = board.getPiece(endPosition);
                if (piece == null || piece.getTeamColor() != getTeamColor()) {
                    moves.add(new ChessMove(position, endPosition, null));
                }
                if (piece != null) {
                    break;
                }
            }
            // Moving Left and Down
            moveMax = 8 - Math.max(9 - position.getRow(),9 - position.getColumn());
            for (int i = 1; i <= moveMax; i++) {
                ChessPosition endPosition = new ChessPosition(position.getRow() - i, position.getColumn() - i);
                ChessPiece piece = board.getPiece(endPosition);
                if (piece == null || piece.getTeamColor() != getTeamColor()) {
                    moves.add(new ChessMove(position, endPosition, null));
                }
                if (piece != null) {
                    break;
                }
            }

            // Rook Logic
            // Right
            for (int i = 1; i < 9 - position.getColumn(); i++) {
                ChessPosition endPosition = new ChessPosition(position.getRow(), position.getColumn() + i);
                ChessPiece piece = board.getPiece(endPosition);
                if (piece == null || piece.getTeamColor() != getTeamColor()) {
                    moves.add(new ChessMove(position, endPosition, null));
                }
                if (piece != null) {
                    break;
                }
            }
            // Left
            for (int i = 1; i < position.getColumn(); i++) {
                ChessPosition endPosition = new ChessPosition(position.getRow(), position.getColumn() - i);
                ChessPiece piece = board.getPiece(endPosition);
                if (piece == null || piece.getTeamColor() != getTeamColor()) {
                    moves.add(new ChessMove(position, endPosition, null));
                }
                if (piece != null) {
                    break;
                }
            }
            // Down
            for (int i = 1; i < position.getRow(); i++) {
                ChessPosition endPosition = new ChessPosition(position.getRow() - i, position.getColumn());
                ChessPiece piece = board.getPiece(endPosition);
                if (piece == null || piece.getTeamColor() != getTeamColor()) {
                    moves.add(new ChessMove(position, endPosition, null));
                }
                if (piece != null) {
                    break;
                }
            }
            // Up
            for (int i = 1; i < 9 - position.getRow(); i++) {
                ChessPosition endPosition = new ChessPosition(position.getRow() + i, position.getColumn());
                ChessPiece piece = board.getPiece(endPosition);
                if (piece == null || piece.getTeamColor() != getTeamColor()) {
                    moves.add(new ChessMove(position, endPosition, null));
                }
                if (piece != null) {
                    break;
                }
            }
            return moves;
        }

        private void addKingMoves (ChessBoard board, ChessPosition position){
            ArrayList<ChessMove> moves = new ArrayList<>();
            //Moving Up and Right
            ChessPosition endPosition = new ChessPosition(position.getRow() + 1, position.getColumn() + 1);
            ChessPiece piece = board.getPiece(endPosition);
            if (piece == null || piece.getTeamColor() != getTeamColor()) {
                moves.add(new ChessMove(position, endPosition, null));
            }
        }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return type == that.type && teamColor == that.teamColor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, teamColor);
    }

    @Override
    public String toString() {
        return "ChessPiece{" +
                "type=" + type +
                ", teamColor=" + teamColor +
                "}\n";
    }
}


