package tictactoe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class TextUI {
    private final Scanner scanner;
    private Board board;

    public TextUI(Scanner scanner) {
        this.scanner = scanner;
    }

    public void init() {
        while (true) {
            board = new Board();
            System.out.print("Input command: > ");
            String command = scanner.nextLine();
            if (command.equalsIgnoreCase("exit")) {
                scanner.close();
                return;
            }

            String[] parts = command.split(" ");
            ArrayList<String> modes = new ArrayList<>();
            Collections.addAll(modes, "user", "easy", "medium", "hard");
            if (parts.length != 3 ||
                    !parts[0].equalsIgnoreCase("start") ||
                    !modes.contains(parts[1]) || !modes.contains(parts[2])
            ) {
                System.out.println("Bad parameters!");
            } else {
                board.printBoard();
                startGame(parts[1], parts[2]);
            }
        }

    }

    public void startGame(String xPlayer, String oPlayer) { // select the mode from the string and pass it to the playMoves methods
        if (xPlayer.equalsIgnoreCase("user") && oPlayer.equalsIgnoreCase("user")) {
            board.playerX = new Player(PlayerType.X, false);
            board.playerO = new Player(PlayerType.O, false);
            usersPlayMoves();
        } else if (!xPlayer.equalsIgnoreCase("user") && !oPlayer.equalsIgnoreCase("user")) {
            board.playerX = new Player(PlayerType.X, true);
            board.playerO = new Player(PlayerType.O, true);
            botsPlayMoves(xPlayer, oPlayer);
        } else if (xPlayer.equalsIgnoreCase("user")) {
            board.playerX = new Player(PlayerType.X, false);
            board.playerO = new Player(PlayerType.O, true);
            userAndBotPlayMoves(board.playerX, oPlayer);
        } else {
            board.playerX = new Player(PlayerType.X, true);
            board.playerO = new Player(PlayerType.O, false);
            userAndBotPlayMoves(board.playerX, xPlayer);
        }
        System.out.println(printOutcome());
    }

    private String printOutcome() {
        if (board.playerX.isWinner()) {
            return "X wins";
        } else if (board.playerO.isWinner()) {
            return "O wins";
        }

        return "Draw";
    }

    private void usersPlayMoves() {
        boolean gameOver = false;
        while (!gameOver) {
            System.out.print("Enter the coordinates: > ");
            String coords = scanner.nextLine();
            if (validCoords(coords)) {
                gameOver = board.addToBoard(coords);
            }
        }
    }

    private void userAndBotPlayMoves(Player player1, String mode) {
        boolean gameOver = false;
        while (!gameOver) {
            if (player1.isBot) { // X is bot, O is user
                if (mode.equalsIgnoreCase("easy")) {
                    gameOver = board.aiMoveEasy();
                } else if (mode.equalsIgnoreCase("medium")) {
                    gameOver = board.aiMoveMedium();
                } else {
                    gameOver = board.aiMoveHard();
                }

                if (!gameOver) {
                    String coords;
                    do {
                        System.out.print("Enter the coordinates: > ");
                        coords = scanner.nextLine();
                    } while (!validCoords(coords));

                    gameOver = board.addToBoard(coords);
                }
            } else { // X is user, O is bot
                String coords;
                do {
                    System.out.print("Enter the coordinates: > ");
                    coords = scanner.nextLine();
                } while (!validCoords(coords));

                gameOver = board.addToBoard(coords);
                if (gameOver) break;
                if (mode.equalsIgnoreCase("easy")) {
                    gameOver = board.aiMoveEasy();
                } else if (mode.equalsIgnoreCase("medium")) {
                    gameOver = board.aiMoveMedium();
                } else {
                    gameOver = board.aiMoveHard();
                }
            }
        }
    }

    private void botsPlayMoves(String modeX, String modeO) {
        boolean gameOver = false;
        while (!gameOver) {
            gameOver = modeX.equalsIgnoreCase("easy") ? board.aiMoveEasy() : board.aiMoveMedium();
            if (gameOver) break;
            gameOver = modeO.equalsIgnoreCase("easy") ? board.aiMoveEasy() : board.aiMoveMedium();
        }
    }

    public boolean validCoords(String coords) {
        String[] parts = coords.split("[, ]+");
        String first;
        String second;

        try {
            first = parts[0];
            second = parts[1];
        } catch (Exception e) {
            System.out.println("You should enter numbers!");
            return false;
        }

        int down;
        int across;

        try {
            down = Integer.parseInt(first);
            across = Integer.parseInt(second);
        } catch (Exception e) {
            System.out.println("You should enter numbers!");
            return false;
        }

        if ((down > 3 || down < 1) || (across > 3 || across < 1)) {
            System.out.println("Coordinates should be from 1 to 3!");
            return false;
        }

        if (board.isFilled(down - 1, across - 1)) {
            System.out.println("This cell is occupied! Choose another one!");
            return false;
        }

        return true;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        TextUI tUI = new TextUI(scanner);
        tUI.init();

    }
}
