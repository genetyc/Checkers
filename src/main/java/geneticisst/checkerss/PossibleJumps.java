package geneticisst.checkerss;

public class PossibleJumps {
    Shashka shashka;
    int oldX, oldY, newX, newY;

    public PossibleJumps(Shashka shashka, int oldX, int oldY, int newX, int newY) {
        this.shashka = shashka;
        this.oldX = oldX;
        this.oldY = oldY;
        this.newX = newX;
        this.newY = newY;
    }

    public static boolean canCapture(Tile[][] field, Shashka shashka, int x, int y) {
        int[][] directions = {{1, -1}, {1, 1}, {-1, -1}, {-1, 1}};

        if (!shashka.isDamka) {
            for (int[] direction : directions) {
                int xNeighbor = x + direction[0];
                int yNeighbor = y + direction[1];
                int xJump = x + 2 * direction[0];
                int yJump = y + 2 * direction[1];

                if (isValidPosition(xJump, yJump) && field[xNeighbor][yNeighbor].hasShashka()) {
                    Shashka neighborShashka = field[xNeighbor][yNeighbor].getShashka();
                    if (neighborShashka.isLight != shashka.isLight &&
                            !field[xJump][yJump].hasShashka()) {
                        return true;
                    }
                }
            }
            return false;
        } else {
            for (int[] direction : directions) {
                int xMove = x + direction[0];
                int yMove = y + direction[1];


            }
            return false;
        }
    }

    public static boolean isValidPosition(int x, int y) {
        return x >= 0 && x < 8 && y >= 0 && y < 8;
    }

}
