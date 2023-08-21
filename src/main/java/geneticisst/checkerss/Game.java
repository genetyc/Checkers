package geneticisst.checkerss;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import kotlin.Pair;

import java.io.IOException;
import java.util.List;

import static geneticisst.checkerss.GameSettings.*;

public class Game extends Application {
    Group tiles = new Group();
    Group shashki = new Group();
    Tile[][] field = new Tile[8][8];
    boolean lightsTurn = true;
    int lightShashkas = 12;
    int darkShashkas = 12;

    private Parent board() {
        Pane pane = new Pane();
        pane.setPrefSize(WIDTH * tileSize, HEIGHT * tileSize);
        pane.getChildren().addAll(tiles, shashki);
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                Tile tile = new Tile(j, i, (j + i) % 2 == 0);
                field[j][i] = tile;
                tiles.getChildren().add(tile);
                if (((i + 1) % 2 != 0 && (j + 1) % 2 == 0) ||
                        (i + 1) % 2 == 0 && (j + 1) % 2 != 0) {
                    if (i < 3) {
                        Shashka shashka = makeShashka(Shashka.SType.DARK, j, i);
                        shashka.isLight = false;
                        tile.setShashka(shashka);
                        shashki.getChildren().add(shashka);
                    }
                    if (i > 4) {
                        Shashka shashka = makeShashka(Shashka.SType.LIGHT, j, i);
                        shashka.isLight = true;
                        tile.setShashka(shashka);
                        shashki.getChildren().add(shashka);
                    }
                }
            }
        }
        return pane;
    }

    private Shashka makeShashka(Shashka.SType sType, int x, int y) {
        Shashka shashka = new Shashka(sType, x, y);
        shashka.setOnMouseReleased(e -> {
            double mouseX = e.getSceneX();
            double mouseY = e.getSceneY();
            int oldX = convert(shashka.oldX);
            int oldY = convert(shashka.oldY);
            int newX = convert(mouseX);
            int newY = convert(mouseY);
            int xDiff = Math.abs(newX-oldX);
            int yDiff = Math.abs(newY-oldY);
            if (field[newX][newY].hasShashka() || (newX + newY) % 2 == 0
                    || mouseX < 0 || mouseY < 0 || mouseX > tileSize * WIDTH || mouseY > tileSize * HEIGHT
                    || shashka.isLight != lightsTurn || xDiff != yDiff) {
                shashka.cancel();
            } else {
                switch (xDiff) {
                    case 1 -> {
                        if (shashka.sType.way != 0 && shashka.sType.way == newY - oldY) {
                            field[oldX][oldY].setShashka(null);
                            shashka.move(newX, newY);
                            if ((newY == 0 && shashka.isLight) || (newY == 7 && !shashka.isLight)) {
                                if (!shashka.isDamka) {
                                    shashka.promote();
                                }
                            }
                            field[newX][newY].setShashka(shashka);
                            lightsTurn = !lightsTurn;
                        } else {
                            shashka.cancel();
                        }
                    }
                    case 2 -> {
                        int eatenX = (newX + oldX) / 2;
                        int eatenY = (newY + oldY) / 2;
                        Shashka eatenShashka = field[eatenX][eatenY].getShashka();
                        if (eatenShashka != null && eatenShashka.isLight != shashka.isLight) {
                            field[oldX][oldY].setShashka(null);
                            field[eatenX][eatenY].setShashka(null);
                            shashki.getChildren().remove(eatenShashka);
                            shashka.move(newX, newY);
                            if ((newY == 0 && shashka.isLight) || (newY == 7 && !shashka.isLight)) {
                                if (!shashka.isDamka) {
                                    shashka.promote();
                                }
                            }
                            field[newX][newY].setShashka(shashka);
                            lightsTurn = !lightsTurn;
                        } else {
                            if (!shashka.isDamka) shashka.cancel(); else {
                                field[oldX][oldY].setShashka(null);
                                shashka.move(newX, newY);
                                field[newX][newY].setShashka(shashka);
                                lightsTurn = !lightsTurn;
                            }
                        }

                    }
                    default -> {
                        if (!shashka.isDamka) shashka.cancel();
                        else {
                            field[oldX][oldY].setShashka(null);
                            shashka.move(newX, newY);
                            field[newX][newY].setShashka(shashka);
                            lightsTurn = !lightsTurn;
                        }
                    }
                }
            }
        });

        return shashka;
    }

    @Override
    public void start(Stage stage) throws IOException {
        Scene scene = new Scene(board());
        stage.setTitle("Shashki");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}