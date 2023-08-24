package geneticisst.checkerss;

public class PossibleJumps {
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
        } else {
            for (int[] direction : directions) {
                int xMove = x + direction[0];
                int yMove = y + direction[1];
                while (isValidPosition(xMove, yMove)) {
                    Shashka eatenShashka = field[xMove][yMove].getShashka();
                    if (eatenShashka != null) {
                        if (eatenShashka.isLight == shashka.isLight) {
                            break;
                        } else {
                            try {
                                return !field[xMove + direction[0]][yMove + direction[1]].hasShashka();
                            } catch (ArrayIndexOutOfBoundsException ignored) {}
                        }
                    }
                    xMove+=direction[0];
                    yMove+=direction[1];
                }
            }
        }
        return false;
    }

    public static boolean isValidPosition(int x, int y) {
        return x >= 0 && x < 8 && y >= 0 && y < 8;
    }

}
