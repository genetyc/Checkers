package geneticisst.checkerss;

public class GameSettings {
    public static int tileSize = 100;
    public static final int HEIGHT = 8;
    public static final int WIDTH = 8;

    public static int convert(double coord) {
        return (int) coord / tileSize;
    }
}
