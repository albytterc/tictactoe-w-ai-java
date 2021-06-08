package tictactoe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Board implements Cloneable {
    private char[][] board;
    Player playerX;
    Player playerO;

    public Board() {
        board = new char[3][3];

        for (char[] chars : board) {
            Arrays.fill(chars, ' ');
        }
    }

    public void printBoard() {
        System.out.println("---------");
        for (char[] chars : board) {
            System.out.print("| ");
            for (char aChar : chars)
                System.out.print(aChar + " ");

            System.out.println("|");
        }
        System.out.println("---------");
    }

    public boolean aiMoveEasy() {
        System.out.println("Making move level \"easy\"");
        ArrayList<Coords> coordsAvailable = getAvailableCoords();
        // Select a random index from the arraylist and add it to the board
        return selectRandCoords(coordsAvailable);
    }

    private boolean selectRandCoords(ArrayList<Coords> coordsAvailable) {
        Random rand = new Random();
        int index = rand.nextInt(coordsAvailable.size());
        Coords coord = coordsAvailable.get(index);
        int down = coord.getDown();
        int across = coord.getAcross();
        return addToBoard(down + 1 + ", " + (across + 1));
    }

    private ArrayList<Coords> getAvailableCoords() {
        ArrayList<Coords> coordsAvailable = new ArrayList<>();
        // loop through the board and add all available spaces to the arraylist
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (!isFilled(i, j)) {
                    coordsAvailable.add(new Coords(i, j));
                }
            }
        }
        return coordsAvailable;
    }

    public boolean aiMoveMedium() {
        System.out.println("Making move level \"medium\"");
        ArrayList<Coords> coordsAvailable = getAvailableCoords();
        Coords emptySpot;
        Coords stopWin;
        if (playerO.isBot) {
            emptySpot = winningMove(playerO);
            stopWin = winningMove(playerX);
        } else {
            emptySpot = winningMove(playerX);
            stopWin = winningMove(playerO);
        }
        if (emptySpot != null) {
            return addToBoard(emptySpot.toString());
        } else if (stopWin != null) {
            return addToBoard(stopWin.toString());
        }
        return selectRandCoords(coordsAvailable);
    }

    public boolean aiMoveHard() {
        System.out.println("Making move level \"hard\"");
        Player player = playerX.isBot ? playerX : playerO;
        return addToBoard(findBestMove(this, player).toString());
    }

    public Coords findBestMove(Board b, Player player) {
        Coords move = null;
        ArrayList<Coords> freeMoves = b.getAvailableCoords();

        if (playerX.isBot) {
            int bestScore = Integer.MIN_VALUE;
            for (Coords coords : freeMoves) {
                b.board[coords.getDown()][coords.getAcross()] = 'X';
                int score = minimax(b, player, coords);
                if (bestScore < score) {
                    bestScore = score;
                    move = coords;
                }
                b.board[coords.getDown()][coords.getAcross()] = ' ';
            }
        } else {
            int bestScore = Integer.MAX_VALUE;
            for (Coords coords : freeMoves) {
                b.board[coords.getDown()][coords.getAcross()] = 'O';
                int score = minimax(b, player, coords);
                if (bestScore > score) {
                    bestScore = score;
                    move = coords;
                }
                b.board[coords.getDown()][coords.getAcross()] = ' ';
            }
        }
        return new Coords(move.getDown() + 1, move.getAcross() + 1);
    }

    public int minimax(Board b, Player player, Coords move) {
        if (b.gameOver(player, move)) {
            if (b.playerX.isWinner() && b.playerX.isBot || b.playerO.isWinner() && b.playerO.isBot) {
                return 10;
            } else if (b.playerX.isWinner() || b.playerO.isWinner()) {
                return -10;
            } else {
                return 0;
            }
        }

        ArrayList<Coords> emptySpots = b.getAvailableCoords();
        int score = playerX.isBot ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        for (Coords coords : emptySpots) {
            b.board[coords.getDown()][coords.getAcross()] = player.getTypeChar();
            if (playerX.isBot) {
                score = Math.max(score, minimax(b, b.playerO, coords));
            } else {
                score = Math.min(score, minimax(b, b.playerX, coords));
            }
            b.board[coords.getDown()][coords.getAcross()] = ' ';
        }
        return score;
    }

    private Coords winningMove(Player player) {
        // Find all available moves
        // Play each move and check if the move leads to a win
            // if it does return the coords of the move
            // otherwise reset the board and try the next move

        for (Coords move : getAvailableCoords()) {
            int down = move.getDown();
            int across = move.getAcross();
            board[down][across] = player.getTypeChar();
            boolean winningMoveFound = gameOver(player, move);
            board[down][across] = ' ';
            player.setWinner(false);
            if (winningMoveFound) return new Coords(down + 1, across + 1);
        }
        return null;
    }

    public boolean addToBoard(String coords) {
        Coords nums = processCoords(coords);
        int down = nums.getDown();
        int across = nums.getAcross();

        Player player;
        if (playerX.getMoveCount() == playerO.getMoveCount()) {
            player = playerX;
        } else {
            player = playerO;
        }

        player.playMove();
        board[down][across] = player.getTypeChar();

        printBoard();
        return gameOver(player, nums);
    }

    public boolean isFilled(int down, int across) {
        return board[down][across] != ' ';
    }


    public boolean gameOver(Player player, Coords lastMove) {
        int down = lastMove.getDown();
        int across = lastMove.getAcross();
        if (board[down][0] == board[down][1] && board[down][1] == board[down][2]) {
            player.setWinner(true);
            return true;
        } else if (board[0][across] == board[1][across] && board[1][across] == board[2][across]) {
            player.setWinner(true);
            return true;
        }

        if (down == across) {
            if (board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
                player.setWinner(true);
                return true;
            }
        }

        if (down + across == 2) {
            if (board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
                player.setWinner(true);
                return true;
            }
        }

        // No winners yet; check if board is full
        for (char[] row : board) {
            for (char spot : row) {
                if (spot == ' ') {
                    return false;
                }
            }
        }
        return true;
    }

    // Converts the coordinates string given in the prompt to two integers
    // Returns an array of the two numbers
    private Coords processCoords(String coords) {
        String[] parts = coords.split("[ ,]+");
        String first = parts[0];
        String second = parts[1];
        int down = Integer.parseInt(first);
        int across = Integer.parseInt(second);
        return new Coords(down - 1, across - 1);
    }

}