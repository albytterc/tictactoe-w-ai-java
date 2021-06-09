package tictactoe;

public class Coords {
    private final int down;
    private final int across;

    public Coords(int down, int across) {
        this.down = down;
        this.across = across;
    }

    public int getDown() {
        return down;
    }

    public int getAcross() {
        return across;
    }

    @Override
    public String toString() {
        return down + ", " + across;
    }
}
