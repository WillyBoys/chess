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
                moves = addPawnMoves(board, myPosition);
                break;
            case ROOK:
                moves = addRookMoves(board, myPosition);
                break;
            case BISHOP:
                moves = addBishopMoves(board, myPosition);
                break;
            case KNIGHT:
                moves = addKnightMoves(board, myPosition);
                break;
            case QUEEN:
                moves = addQueenMoves(board, myPosition);
                break;
            case KING:
                moves = addKingMoves(board, myPosition);
                break;
            default:
                throw new IllegalStateException("Unexpected piece type: " + piece);
        }
        return moves;
    }

    private Collection<ChessMove> addPawnMoves(ChessBoard board, ChessPosition position) {
        ArrayList<ChessMove> moves = new ArrayList<>();

        //White Logic
        if (getTeamColor() == ChessGame.TeamColor.WHITE) {
            // First move
            if (position.getRow() == 2) {
                ChessPosition endPosition = new ChessPosition(position.getRow() + 1, position.getColumn());
                ChessPiece piece = board.getPiece(endPosition);
                ChessPosition endPosition2 = new ChessPosition(position.getRow() + 2, position.getColumn());
                ChessPiece piece2 = board.getPiece(endPosition2);
                if (piece == null && piece2 == null) {
                    moves.add(new ChessMove(position, endPosition2, null));
                }
            }


            // Any move
            ChessPosition endPosition = new ChessPosition(position.getRow() + 1, position.getColumn());
            ChessPiece piece = board.getPiece(endPosition);
            if (piece == null && endPosition.getRow() == 8) {
                moves.add(new ChessMove(position, endPosition, PieceType.KNIGHT));
                moves.add(new ChessMove(position, endPosition, PieceType.ROOK));
                moves.add(new ChessMove(position, endPosition, PieceType.QUEEN));
                moves.add(new ChessMove(position, endPosition, PieceType.BISHOP));
            } else if (piece == null) {
                moves.add(new ChessMove(position, endPosition, null));
            }

            // Attack
            ChessPosition attackPosition = new ChessPosition(position.getRow() + 1, position.getColumn() + 1);
            if (attackPosition.checkBounds()) {
                piece = board.getPiece(attackPosition);
                if (piece != null && getTeamColor() != piece.getTeamColor() && attackPosition.getRow() == 8) {
                    moves.add(new ChessMove(position, attackPosition, PieceType.KNIGHT));
                    moves.add(new ChessMove(position, attackPosition, PieceType.ROOK));
                    moves.add(new ChessMove(position, attackPosition, PieceType.QUEEN));
                    moves.add(new ChessMove(position, attackPosition, PieceType.BISHOP));
                } else if (piece != null && getTeamColor() != piece.getTeamColor()) {
                    moves.add(new ChessMove(position, attackPosition, null));
                }
            }
            ChessPosition attackPosition2 = new ChessPosition(position.getRow() + 1, position.getColumn() - 1);
            {
                if (attackPosition2.checkBounds()) {
                    ChessPiece piece2 = board.getPiece(attackPosition2);
                    if (piece2 != null && getTeamColor() != piece2.getTeamColor() && attackPosition2.getRow() == 8) {
                        moves.add(new ChessMove(position, attackPosition2, PieceType.KNIGHT));
                        moves.add(new ChessMove(position, attackPosition2, PieceType.ROOK));
                        moves.add(new ChessMove(position, attackPosition2, PieceType.QUEEN));
                        moves.add(new ChessMove(position, attackPosition2, PieceType.BISHOP));
                    } else if (piece2 != null && getTeamColor() != piece2.getTeamColor()) {
                        moves.add(new ChessMove(position, attackPosition2, null));
                    }
                }
            }
        }

        // Black Logic
        if (getTeamColor() == ChessGame.TeamColor.BLACK) {
            // First move
            if (position.getRow() == 7) {
                ChessPosition endPosition = new ChessPosition(position.getRow() - 1, position.getColumn());
                ChessPiece piece = board.getPiece(endPosition);
                ChessPosition endPosition2 = new ChessPosition(position.getRow() - 2, position.getColumn());
                ChessPiece piece2 = board.getPiece(endPosition2);
                if (piece == null && piece2 == null) {
                    moves.add(new ChessMove(position, endPosition2, null));
                }
            }

            // Any move
            ChessPosition endPosition = new ChessPosition(position.getRow() - 1, position.getColumn());
            ChessPiece piece = board.getPiece(endPosition);
            if (piece == null && endPosition.getRow() == 1) {
                moves.add(new ChessMove(position, endPosition, PieceType.KNIGHT));
                moves.add(new ChessMove(position, endPosition, PieceType.ROOK));
                moves.add(new ChessMove(position, endPosition, PieceType.QUEEN));
                moves.add(new ChessMove(position, endPosition, PieceType.BISHOP));
            } else if (piece == null) {
                moves.add(new ChessMove(position, endPosition, null));
            }

            // Attack
            ChessPosition attackPosition = new ChessPosition(position.getRow() - 1, position.getColumn() + 1);
            if (attackPosition.checkBounds()) {
                piece = board.getPiece(attackPosition);
                if (piece != null && getTeamColor() != piece.getTeamColor() && attackPosition.getRow() == 1) {
                    moves.add(new ChessMove(position, attackPosition, PieceType.KNIGHT));
                    moves.add(new ChessMove(position, attackPosition, PieceType.ROOK));
                    moves.add(new ChessMove(position, attackPosition, PieceType.QUEEN));
                    moves.add(new ChessMove(position, attackPosition, PieceType.BISHOP));
                } else if (piece != null && getTeamColor() != piece.getTeamColor()) {
                    moves.add(new ChessMove(position, attackPosition, null));
                }
            }
            ChessPosition attackPosition2 = new ChessPosition(position.getRow() - 1, position.getColumn() - 1);
            if (attackPosition2.checkBounds()) {
                ChessPiece piece2 = board.getPiece(attackPosition2);
                if (piece2 != null && getTeamColor() != piece2.getTeamColor() && attackPosition2.getRow() == 1) {
                    moves.add(new ChessMove(position, attackPosition2, PieceType.KNIGHT));
                    moves.add(new ChessMove(position, attackPosition2, PieceType.ROOK));
                    moves.add(new ChessMove(position, attackPosition2, PieceType.QUEEN));
                    moves.add(new ChessMove(position, attackPosition2, PieceType.BISHOP));
                } else if (piece2 != null && getTeamColor() != piece2.getTeamColor()) {
                    moves.add(new ChessMove(position, attackPosition2, null));
                }
            }
        }
        return moves;
    }

    private Collection<ChessMove> addRookMoves(ChessBoard board, ChessPosition position) {
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

    private Collection<ChessMove> addBishopMoves(ChessBoard board, ChessPosition position) {
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
        moveMax = 8 - Math.max(position.getRow(), 9 - position.getColumn());
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
        moveMax = 8 - Math.max(9 - position.getRow(), 9 - position.getColumn());
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

    private Collection<ChessMove> addKnightMoves(ChessBoard board, ChessPosition position) {
        ArrayList<ChessMove> moves = new ArrayList();
        int row = position.getRow();
        int col = position.getColumn();
        int tmprow = -1;
        int tmpcol = -1;

        //Up Right
        tmprow = row + 2;
        tmpcol = col + 1;
        if (tmprow <= 8 && tmpcol <= 8) {
            ChessPosition endPosition = new ChessPosition(tmprow, tmpcol);
            ChessPiece piece = board.getPiece(endPosition);
            if (piece == null || piece.getTeamColor() != getTeamColor()) {
                moves.add(new ChessMove(position, endPosition, null));
            }
        }

        //Up Left
        tmprow = row + 2;
        tmpcol = col - 1;
        if (tmprow <= 8 && tmpcol >= 1) {
            ChessPosition endPosition = new ChessPosition(tmprow, tmpcol);
            ChessPiece piece = board.getPiece(endPosition);
            if (piece == null || piece.getTeamColor() != getTeamColor()) {
                moves.add(new ChessMove(position, endPosition, null));
            }
        }

        //Right Up
        tmprow = row + 1;
        tmpcol = col + 2;
        if (tmprow <= 8 && tmpcol <= 8) {
            ChessPosition endPosition = new ChessPosition(tmprow, tmpcol);
            ChessPiece piece = board.getPiece(endPosition);
            if (piece == null || piece.getTeamColor() != getTeamColor()) {
                moves.add(new ChessMove(position, endPosition, null));
            }
        }

        //Right Down
        tmprow = row - 1;
        tmpcol = col + 2;
        if (tmprow >= 1 && tmpcol <= 8) {
            ChessPosition endPosition = new ChessPosition(tmprow, tmpcol);
            ChessPiece piece = board.getPiece(endPosition);
            if (piece == null || piece.getTeamColor() != getTeamColor()) {
                moves.add(new ChessMove(position, endPosition, null));
            }
        }

        //Down Right
        tmprow = row - 2;
        tmpcol = col + 1;
        if (tmprow >= 1 && tmpcol <= 8) {
            ChessPosition endPosition = new ChessPosition(tmprow, tmpcol);
            ChessPiece piece = board.getPiece(endPosition);
            if (piece == null || piece.getTeamColor() != getTeamColor()) {
                moves.add(new ChessMove(position, endPosition, null));
            }
        }

        //Down Left
        tmprow = row - 2;
        tmpcol = col - 1;
        if (tmprow >= 1 && tmpcol >= 1) {
            ChessPosition endPosition = new ChessPosition(tmprow, tmpcol);
            ChessPiece piece = board.getPiece(endPosition);
            if (piece == null || piece.getTeamColor() != getTeamColor()) {
                moves.add(new ChessMove(position, endPosition, null));
            }
        }

        //Left Up
        tmprow = row + 1;
        tmpcol = col - 2;
        if (tmprow <= 8 && tmpcol >= 1) {
            ChessPosition endPosition = new ChessPosition(tmprow, tmpcol);
            ChessPiece piece = board.getPiece(endPosition);
            if (piece == null || piece.getTeamColor() != getTeamColor()) {
                moves.add(new ChessMove(position, endPosition, null));
            }
        }

        //Left Down
        tmprow = row - 1;
        tmpcol = col - 2;
        if (tmprow >= 1 && tmpcol >= 1) {
            ChessPosition endPosition = new ChessPosition(tmprow, tmpcol);
            ChessPiece piece = board.getPiece(endPosition);
            if (piece == null || piece.getTeamColor() != getTeamColor()) {
                moves.add(new ChessMove(position, endPosition, null));
            }
        }
        return moves;
    }

    private Collection<ChessMove> addQueenMoves(ChessBoard board, ChessPosition position) {
        // Bishop Logic
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
        moveMax = 8 - Math.max(position.getRow(), 9 - position.getColumn());
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
        moveMax = 8 - Math.max(9 - position.getRow(), 9 - position.getColumn());
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

    private Collection<ChessMove> addKingMoves(ChessBoard board, ChessPosition position) {
        ArrayList<ChessMove> moves = new ArrayList<>();
        int row = position.getRow();
        int col = position.getColumn();
        int tmprow = -1;
        int tmpcol = -1;

        //Up Right
        tmprow = row + 1;
        tmpcol = col + 1;
        if (tmprow <= 8 && tmpcol <= 8) {
            ChessPosition endPosition = new ChessPosition(tmprow, tmpcol);
            ChessPiece piece = board.getPiece(endPosition);
            if (piece == null || piece.getTeamColor() != getTeamColor()) {
                moves.add(new ChessMove(position, endPosition, null));
            }
        }

        //Up Left
        tmprow = row + 1;
        tmpcol = col - 1;
        if (tmprow <= 8 && tmpcol >= 1) {
            ChessPosition endPosition = new ChessPosition(tmprow, tmpcol);
            ChessPiece piece = board.getPiece(endPosition);
            if (piece == null || piece.getTeamColor() != getTeamColor()) {
                moves.add(new ChessMove(position, endPosition, null));
            }
        }

        //Down Right
        tmprow = row - 1;
        tmpcol = col + 1;
        if (tmprow >= 1 && tmpcol <= 8) {
            ChessPosition endPosition = new ChessPosition(tmprow, tmpcol);
            ChessPiece piece = board.getPiece(endPosition);
            if (piece == null || piece.getTeamColor() != getTeamColor()) {
                moves.add(new ChessMove(position, endPosition, null));
            }
        }

        //Down Left
        tmprow = row - 1;
        tmpcol = col - 1;
        if (tmprow >= 1 && tmpcol >= 1) {
            ChessPosition endPosition = new ChessPosition(tmprow, tmpcol);
            ChessPiece piece = board.getPiece(endPosition);
            if (piece == null || piece.getTeamColor() != getTeamColor()) {
                moves.add(new ChessMove(position, endPosition, null));
            }
        }

        //Up
        tmprow = row + 1;
        tmpcol = col;
        if (tmprow <= 8) {
            ChessPosition endPosition = new ChessPosition(tmprow, tmpcol);
            ChessPiece piece = board.getPiece(endPosition);
            if (piece == null || piece.getTeamColor() != getTeamColor()) {
                moves.add(new ChessMove(position, endPosition, null));
            }
        }

        //Down
        tmprow = row - 1;
        tmpcol = col;
        if (tmprow >= 1) {
            ChessPosition endPosition = new ChessPosition(tmprow, tmpcol);
            ChessPiece piece = board.getPiece(endPosition);
            if (piece == null || piece.getTeamColor() != getTeamColor()) {
                moves.add(new ChessMove(position, endPosition, null));
            }
        }

        //Right
        tmprow = row;
        tmpcol = col + 1;
        if (tmpcol <= 8) {
            ChessPosition endPosition = new ChessPosition(tmprow, tmpcol);
            ChessPiece piece = board.getPiece(endPosition);
            if (piece == null || piece.getTeamColor() != getTeamColor()) {
                moves.add(new ChessMove(position, endPosition, null));
            }
        }

        //Left
        tmprow = row;
        tmpcol = col - 1;
        if (tmpcol >= 1) {
            ChessPosition endPosition = new ChessPosition(tmprow, tmpcol);
            ChessPiece piece = board.getPiece(endPosition);
            if (piece == null || piece.getTeamColor() != getTeamColor()) {
                moves.add(new ChessMove(position, endPosition, null));
            }
        }
        return moves;
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


