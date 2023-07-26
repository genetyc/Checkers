package geneticisst.checkerss;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;

import static geneticisst.checkerss.GameSettings.tileSize;

public class Shashka extends StackPane {
    public enum SType {
        DARK(1), LIGHT(-1), DAMKDARK, DAMKLIGHT;

        public final int way;

        SType() {
            this.way = 0;
        }

        SType(int way) {
            this.way = way;
        }
    }

    public SType sType;
    public double mouseX, mouseY, oldX, oldY;
    public boolean isDamka = false;
    public boolean isLight;


    public Shashka(SType sType, int x, int y) {
        this.sType = sType;
        move(x, y);
        Ellipse bg = new Ellipse(tileSize * 0.3125, tileSize * 0.26);
        bg.setFill(Color.BLACK);
        bg.setStroke(Color.BLACK);
        bg.setStrokeWidth(tileSize * 0.03);
        bg.setTranslateX((tileSize - tileSize *  0.625) / 2);
        bg.setTranslateY((tileSize - tileSize *  0.52) / 2 + tileSize * 0.07);

        Ellipse piece = new Ellipse(tileSize * 0.3125, tileSize * 0.26);
        if (sType == SType.DARK) {
            piece.setFill(Color.BROWN);
        }
        else if (sType == SType.LIGHT) piece.setFill(Color.BISQUE);
        piece.setStroke(Color.BLACK);
        piece.setStrokeWidth(tileSize * 0.03);
        piece.setTranslateX((tileSize - tileSize *  0.625) / 2);
        piece.setTranslateY((tileSize - tileSize *  0.52) / 2);

        getChildren().addAll(bg, piece);

        setOnMousePressed(e -> {
            mouseX = e.getSceneX();
            mouseY = e.getSceneY();
        });

        setOnMouseDragged(e ->{
            relocate(e.getSceneX() - mouseX + oldX, e.getSceneY() - mouseY + oldY);
            toFront();
        });

    }
    public void move(int x, int  y) {
        this.oldX = x * tileSize;
        this.oldY = y * tileSize;
        relocate(oldX, oldY);
    }

    public void cancel() {
        relocate(oldX, oldY);
    }

    public void promote() {
        if (!isDamka) isDamka = true;
        System.out.println("Promotion!");
    }
}
