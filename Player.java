package tictactoe;
enum PlayerType { X, O }
public class Player {
    private final PlayerType type;
    private int moveCount;
    private boolean isWinner;
    boolean isBot;

    public Player(PlayerType type, boolean isBot) {
        this.type = type;
        isWinner = false;
        moveCount = 0;
        this.isBot = isBot;
    }

    public void playMove() {
        moveCount++;
    }

    public void setWinner(boolean hasWon) {
        isWinner = hasWon;
    }

    public boolean isWinner() {
        return isWinner;
    }

    public int getMoveCount() {
        return moveCount;
    }

    public char getTypeChar() {
        return type.toString().charAt(0);
    }
}
