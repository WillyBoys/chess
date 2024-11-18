package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;

public class GameplayUI {

    // Board dimensions.
    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final int SQUARE_SIZE_IN_PADDED_CHARS = 2;
    private static final int LINE_WIDTH_IN_PADDED_CHARS = 1;
    private static int tracker = 0;
    public static boolean playerWhite = true;
    private static int letterTracker = 0;
    private static int letterTrackerAfter = 0;
    private static int printPieces = 0;
    static String[] whiteTopPieces = {" R ", " N ", " B ", " K ", " Q ", " B ", " N ", " R ", " P ", " P ", " P ", " P ", " P ", " P ", " P ", " P "};
    static String[] whiteBottomPieces = {" P ", " P ", " P ", " P ", " P ", " P ", " P ", " P ", " R ", " N ", " B ", " Q ", " K ", " B ", " N ", " R "};
    static String[] blackTopPieces = {" R ", " N ", " B ", " Q ", " K ", " B ", " N ", " R ", " P ", " P ", " P ", " P ", " P ", " P ", " P ", " P "};
    static String[] blackBottomPieces = {" P ", " P ", " P ", " P ", " P ", " P ", " P ", " P ", " R ", " N ", " B ", " K ", " Q ", " B ", " N ", " R "};


    // Padded characters.
    private static final String EMPTY = "   ";
    private static final String TEMPORARY = " X ";

    public static void main(String[] args) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);

        //White Board
        if (args[1].equals("WHITE") || args[1].equals("white") || args[1].equals("OBSERVE") || args[1].equals("observe")) {
            out.println("Here is White Board");
            printEmptyBlack(out);
            drawHeaders(out);

            drawChessBoard(out);

            printEmptyBlack(out);
            drawHeaders(out);

            out.println();
        } else {
            //Black Board
            out.println("Here is Black Board");
            playerWhite = false;
            tracker = 0;
            letterTracker = 0;
            letterTrackerAfter = 0;
            printEmptyBlack(out);
            drawHeaders(out);

            drawChessBoard(out);

            printEmptyBlack(out);
            drawHeaders(out);
        }
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void drawHeaders(PrintStream out) {

        setBlack(out);
        String[] whiteHeaders = {" A ", " B ", " C ", " D ", " E ", " F ", " G ", " H "};
        String[] blackHeaders = {" H ", " G ", " F ", " E ", " D ", " C ", " B ", " A "};
        if (playerWhite) {
            for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
                drawHeader(out, whiteHeaders[boardCol]);
            }
            printEmptyBlack(out);

        } else {
            for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
                drawHeader(out, blackHeaders[boardCol]);
            }
            printEmptyBlack(out);

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
        out.print(SET_TEXT_COLOR_LIGHT_GREY);

        out.print(player);

        setBlack(out);
    }

    private static void drawChessBoard(PrintStream out) {

        for (int boardRow = 1; boardRow <= BOARD_SIZE_IN_SQUARES; ++boardRow) {

            drawRowOfSquares(out, boardRow);
            tracker++;
        }
    }


    private static void drawRowOfSquares(PrintStream out, int boardRow) {
        boolean black = false;
        for (int squareRow = 0; squareRow < SQUARE_SIZE_IN_PADDED_CHARS + 1; ++squareRow) {
            if (!(squareRow == SQUARE_SIZE_IN_PADDED_CHARS / 2)) {
                printEmptyBlack(out);

                //THIS IS WHERE WE START PRINTING ALL THE SQUARES
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
                printEmptyBlack(out);
            } else {
                if (playerWhite) {
                    printLetterStuff(out);
                } else {
                    printBlackLetterStuff(out);
                }
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
                        helperFunction(out, boardRow, black);

                        out.print(EMPTY.repeat(suffixLength));
                        tracker++;
                    }
                }
                if (playerWhite) {
                    printLetterStuffAfter(out);
                } else {
                    printBlackLetterStuffAfter(out);
                }
            }
            setGray(out);
            out.println();
        }
    }

    private static void helperFunction(PrintStream out, int boardRow, boolean black) {
        if (playerWhite && boardRow < 3) {
            if (black) {
                printBlackPiecesOnBlack(out, blackTopPieces);
            } else {
                printBlackPiecesOnWhite(out, blackTopPieces);
            }
        } else if (playerWhite && boardRow > 6) {
            if (black) {
                printWhitePiecesOnBlack(out, whiteBottomPieces);
            } else {
                printWhitePiecesOnWhite(out, whiteBottomPieces);
            }
        } else if (!playerWhite && boardRow < 3) {
            if (black) {
                printWhitePiecesOnBlack(out, whiteTopPieces);
            } else {
                printWhitePiecesOnWhite(out, whiteTopPieces);
            }
        } else if (!playerWhite && boardRow > 6) {
            if (black) {
                printBlackPiecesOnBlack(out, blackBottomPieces);
            } else {
                printBlackPiecesOnWhite(out, blackBottomPieces);
            }
        } else {
            if (black) {
                printBlackPlayer(out, EMPTY);
            } else {
                printPlayer(out, EMPTY);
            }
        }
    }

    private static void printEmptyBlack(PrintStream out) {
        setBlack(out);
        out.print(EMPTY.repeat(SQUARE_SIZE_IN_PADDED_CHARS / 2));
    }

    private static void printLetterStuff(PrintStream out) {
        String[] letters = {" 8 ", " 7 ", " 6 ", " 5 ", " 4 ", " 3 ", " 2 ", " 1 "};
        printBlackHeader(out, letters[letterTracker]);
        letterTracker++;
    }

    private static void printBlackLetterStuff(PrintStream out) {
        String[] letters = {" 1 ", " 2 ", " 3 ", " 4 ", " 5 ", " 6 ", " 7 ", " 8 "};
        printBlackHeader(out, letters[letterTracker]);
        letterTracker++;
    }

    private static void printLetterStuffAfter(PrintStream out) {
        String[] letters = {" 8 ", " 7 ", " 6 ", " 5 ", " 4 ", " 3 ", " 2 ", " 1 "};
        printBlackHeader(out, letters[letterTrackerAfter]);
        letterTrackerAfter++;
    }

    private static void printBlackLetterStuffAfter(PrintStream out) {
        String[] letters = {" 1 ", " 2 ", " 3 ", " 4 ", " 5 ", " 6 ", " 7 ", " 8 "};
        printBlackHeader(out, letters[letterTrackerAfter]);
        letterTrackerAfter++;
    }

    private static void setWhite(PrintStream out) {
        out.print(SET_BG_COLOR_WHITE);
        out.print(SET_TEXT_COLOR_WHITE);
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

    private static void printBlackPiecesOnBlack(PrintStream out, String[] topPieces) {
        out.print(RESET_TEXT_BOLD_FAINT);
        out.print(SET_TEXT_ITALIC);
        printBlackPlayer(out, topPieces[printPieces]);
        printPieces++;
        if (printPieces == 16) {
            printPieces = 0;
        }
    }

    private static void printBlackPiecesOnWhite(PrintStream out, String[] topPieces) {
        out.print(RESET_TEXT_BOLD_FAINT);
        out.print(SET_TEXT_ITALIC);
        printPlayer(out, topPieces[printPieces]);
        printPieces++;
        if (printPieces == 16) {
            printPieces = 0;
        }
    }

    private static void printWhitePiecesOnWhite(PrintStream out, String[] bottomPieces) {
        out.print(RESET_TEXT_ITALIC);
        out.print(SET_TEXT_BOLD);
        printPlayer(out, bottomPieces[printPieces]);
        printPieces++;
        if (printPieces == 16) {
            printPieces = 0;
        }
    }

    private static void printWhitePiecesOnBlack(PrintStream out, String[] bottomPieces) {
        out.print(RESET_TEXT_ITALIC);
        out.print(SET_TEXT_BOLD);
        printBlackPlayer(out, bottomPieces[printPieces]);
        printPieces++;
        if (printPieces == 16) {
            printPieces = 0;
        }
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

    private static void printBlackHeader(PrintStream out, String player) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_LIGHT_GREY);

        out.print(player);
    }
}
