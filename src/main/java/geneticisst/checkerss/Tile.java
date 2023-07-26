package geneticisst.checkerss;

import javafx.scene.chart.PieChart;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import static geneticisst.checkerss.GameSettings.WIDTH;
import static geneticisst.checkerss.GameSettings.HEIGHT;
import static geneticisst.checkerss.GameSettings.tileSize;

public class Tile extends Rectangle {
    int x; int y; boolean isWhite;
    private Shashka shashka;
    Color light = Color.rgb(255,228,205);
    Color dark = Color.rgb(99,70,45);

    public Tile(int x, int y, boolean isWhite) {
        setWidth(tileSize);
        setHeight(tileSize);
        relocate(x * tileSize, y * tileSize);
        setFill(isWhite ? light : dark);
    };

    public boolean hasShashka() {
        return shashka != null;
    }

    public Shashka getShashka() {
        return shashka;
    }

    public void setShashka(Shashka shashka) {
        this.shashka = shashka;
    }

}
