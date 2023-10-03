package geneticisst.checkerss;

import javafx.scene.effect.DropShadow;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;

import static geneticisst.checkerss.GameSettings.convert;
import static geneticisst.checkerss.GameSettings.tileSize;
import static geneticisst.checkerss.PossibleJumps.canCapture;
import static geneticisst.checkerss.Game.*;
import static geneticisst.checkerss.GameSettings.Effects.*;

public class Shashka extends StackPane {
    public double mouseX, mouseY, oldX, oldY;
    public boolean isDamka = false;
    public boolean isLight;

    public Shashka(boolean isLight, int x, int y) {
        this.isLight = isLight;
        move(x, y);
        Ellipse bg = new Ellipse(tileSize * 0.3125, tileSize * 0.26);
        bg.setFill(Color.BLACK);
        bg.setStroke(Color.BLACK);
        bg.setStrokeWidth(tileSize * 0.03);
        bg.setTranslateX((tileSize - tileSize *  0.625) / 2);
        bg.setTranslateY((tileSize - tileSize *  0.52) / 2 + tileSize * 0.07);

        Ellipse piece = new Ellipse(tileSize * 0.3125, tileSize * 0.26);
        piece.setFill(isLight ? Color.BISQUE : Color.BROWN);
        piece.setStroke(Color.BLACK);
        piece.setStrokeWidth(tileSize * 0.03);
        piece.setTranslateX((tileSize - tileSize *  0.625) / 2);
        piece.setTranslateY((tileSize - tileSize *  0.52) / 2);

        getChildren().addAll(bg, piece);

        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.BLACK);
        dropShadow.setRadius(8);
        dropShadow.setOffsetX(3);
        dropShadow.setOffsetY(3);

        setEffect(dropShadow);

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
        vibrationEffect(this);
    }

    public void promote() {
        if (!isDamka) {
            isDamka = true;
            Circle crown = new Circle(mouseX+50, mouseY+50, tileSize * 0.12);
            crown.setFill(Color.GOLD);
            getChildren().add(crown);
            System.out.println("Promotion!");
            if (canCapture(field, this, convert(oldX), convert(oldY))) {
                killers.clear();
                walkers.clear();
                killers.add(this);
            }
        }
    }
}
