package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import static ui.EscapeSequences.*;

public class GameplayUI {

    // Board dimensions.
    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final int SQUARE_SIZE_IN_PADDED_CHARS = 2;
    private static final int LINE_WIDTH_IN_PADDED_CHARS = 1;
    private static final int SQUARE_SIZE = 2;
    private static int tracker = 0;
    public static boolean playerWhite = true;


    // Padded characters.
    private static final String EMPTY = "   ";
    private static final String TEMPORARY = " X ";

    public static void main(String[] args) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);

        drawHeaders(out);

        drawChessBoard(out);

        drawHeaders(out);

        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void drawHeaders(PrintStream out) {

        setBlack(out);
        String[] whiteHeaders = {" 1 ", " 2 ", " 3 ", " 4 ", " 5 ", " 6 ", " 7 ", " 8 "};
        String[] blackHeaders = {" 8 ", " 7 ", " 6 ", " 5 ", " 4 ", " 3 ", " 2 ", " 1 "};
        if (playerWhite) {
            for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
                drawHeader(out, whiteHeaders[boardCol]);
            }
        } else {
            for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
                drawHeader(out, blackHeaders[boardCol]);
            }
        }

        setGray(out);
        out.println();
    }

    private static void drawHeader(PrintStream out, String headerText) {
        //Centers the header in the middle of the board
        int prefixLength = SQUARE_SIZE_IN_PADDED_CHARS / 2;
        int suffixLength = SQUARE_SIZE_IN_PADDED_CHARS - prefixLength;

        out.print(EMPTY.repeat(prefixLength));
        printHeaderText(out, headerText);
        out.print(EMPTY.repeat(suffixLength));
    }

    private static void printHeaderText(PrintStream out, String player) {
        //Edit the look of the header
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_GREEN);

        out.print(player);

        setBlack(out);
    }

    private static void drawChessBoard(PrintStream out) {

        for (int boardRow = 0; boardRow < BOARD_SIZE_IN_SQUARES; ++boardRow) {

            drawRowOfSquares(out);
            tracker++;
            if (boardRow < BOARD_SIZE_IN_SQUARES - 1) {
                // Draw horizontal row separator.
                setGray(out);
            }
        }
    }


    private static void drawRowOfSquares(PrintStream out) {
        boolean black = false;
        for (int squareRow = 0; squareRow < SQUARE_SIZE_IN_PADDED_CHARS + 1; ++squareRow) {
            if (!(squareRow == SQUARE_SIZE_IN_PADDED_CHARS / 2)) {
                for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
                    if (tracker % 2 == 0) {
                        setWhite(out);
                        black = false;
                    } else {
                        setLightGray(out);
                        black = true;
                    }
                    out.print(EMPTY.repeat(LINE_WIDTH_IN_PADDED_CHARS));
                    if (black) {
                        printBlackPlayer(out, EMPTY);
                    } else {
                        printPlayer(out, EMPTY);
                    }

                    out.print(EMPTY.repeat(LINE_WIDTH_IN_PADDED_CHARS));
                    tracker++;
                }
            } else {
                for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
                    if (tracker % 2 == 0) {
                        setWhite(out);
                        black = false;
                    } else {
                        setLightGray(out);
                        black = true;
                    }
                    if (squareRow == SQUARE_SIZE_IN_PADDED_CHARS / 2) {
                        int prefixLength = SQUARE_SIZE_IN_PADDED_CHARS / 2;
                        int suffixLength = SQUARE_SIZE_IN_PADDED_CHARS - prefixLength;

                        out.print(EMPTY.repeat(prefixLength));
                        if (black) {
                            printBlackPlayer(out, EMPTY);
                        } else {
                            printPlayer(out, EMPTY);
                        }
                        out.print(EMPTY.repeat(suffixLength));
                        tracker++;
                    }
                }
            }
            setGray(out);
            out.println();
        }
    }

    private static void setWhite(PrintStream out) {
        out.print(SET_BG_COLOR_WHITE);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void setRed(PrintStream out) {
        out.print(SET_BG_COLOR_RED);
        out.print(SET_TEXT_COLOR_RED);
    }

    private static void setBlack(PrintStream out) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_BLACK);
    }

    private static void setGray(PrintStream out) {
        out.print(SET_BG_COLOR_DARK_GREY);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void setLightGray(PrintStream out) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_BLACK);
    }

    private static void printPlayer(PrintStream out, String player) {
        out.print(SET_BG_COLOR_WHITE);
        out.print(SET_TEXT_COLOR_BLACK);

        out.print(player);
    }

    private static void printBlackPlayer(PrintStream out, String player) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_WHITE);

        out.print(player);
    }
}
