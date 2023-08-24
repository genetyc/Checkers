package geneticisst.checkerss;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.LinkedList;

import static geneticisst.checkerss.GameSettings.*;
import static geneticisst.checkerss.PossibleJumps.canCapture;
import static geneticisst.checkerss.PossibleMoves.canMove;

public class Game extends Application {
    Group tiles = new Group();
    Group shashki = new Group();
    static Tile[][] field = new Tile[8][8];
    boolean lightsTurn = true;
    static int lightShashkas = 12;
    static int darkShashkas = 12;
    static boolean game = true;
    static LinkedList<Shashka> killers = new LinkedList<>();
    static LinkedList<Shashka> walkers = new LinkedList<>();

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
        shashka.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.SECONDARY) {
                boolean canKill = PossibleJumps.canCapture(field, shashka, convert(shashka.oldX), convert(shashka.oldY));
                boolean canMove = PossibleMoves.canMove(field, shashka, convert(shashka.oldX), convert(shashka.oldY));
                System.out.printf("Can move -> %s, can kill -> %s%n", canMove, canKill);
            }
        });
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
                lightsTurn = PossibleMoves.possibleMoves(field, shashka, oldX, oldY, newX, newY, xDiff, lightsTurn, shashki);
                killers.clear();
                walkers.clear();
                for (Node child : shashki.getChildren()) {
                    if (child instanceof Shashka shashka1) {
                        if (shashka1.isLight == lightsTurn) {
                            if (canCapture(field, shashka1, convert(shashka1.oldX), convert(shashka1.oldY))) {
                                killers.add(shashka1);
                            } else if (canMove(field, shashka1, convert(shashka1.oldX), convert(shashka1.oldY))) {
                                walkers.add(shashka1);
                            }
                        }
                    }
                }
                if (killers.isEmpty() && walkers.isEmpty()) gameOver();
            }
        });
        return shashka;
    }

    static void gameOver() {
        System.out.println("Game over");
        game = false;
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