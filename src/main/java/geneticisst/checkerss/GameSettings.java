package geneticisst.checkerss;

public class GameSettings {
    public static int tileSize = 100;
    public static final int HEIGHT = 8;
    public static final int WIDTH = 8;

    public static int convert(double coord) {
        return (int) coord / tileSize;
        //return (int) (coord + tileSize / 2) / tileSize;   //я не знаю, что это, но это было в видосе и на гитхабе
    }
}
