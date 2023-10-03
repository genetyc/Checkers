package geneticisst.checkerss;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import static geneticisst.checkerss.GameSettings.*;
import static geneticisst.checkerss.PossibleJumps.canCapture;
import static geneticisst.checkerss.PossibleMoves.canMove;
import static geneticisst.checkerss.GameSettings.Effects.*;

public class Game extends Application {
    static Group tiles = new Group();
    static Group shashki = new Group();
    static Tile[][] field = new Tile[8][8];
    static boolean lightsTurn = true;
    static int lightShashkas = 12;
    static int darkShashkas = 12;
    static boolean game = true;
    static LinkedList<Shashka> killers = new LinkedList<>();
    static LinkedList<Shashka> walkers = new LinkedList<>();
    static boolean lightsWon;

    public static Parent board() {
        Pane pane = new Pane();

        String imagePath = "src/assets/bg.png";
        Image backgroundImage = new Image(new File(imagePath).toURI().toString());

        BackgroundImage backgroundImg = new BackgroundImage(backgroundImage,
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER, BackgroundSize.DEFAULT);

        Background background = new Background(backgroundImg);
        pane.setBackground(background);

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
                        Shashka shashka = makeShashka(false, j, i);
                        tile.setShashka(shashka);
                        shashki.getChildren().add(shashka);
                    }
                    if (i > 4) {
                        Shashka shashka = makeShashka(true, j, i);
                        tile.setShashka(shashka);
                        shashki.getChildren().add(shashka);
                    }
                }
                if (i == 0 || j == 0) {
                    Text coordinateText = new Text((char) ('A' + j) +""+ (HEIGHT - i));
                    coordinateText.setFont(Font.font("Arial", FontWeight.BOLD, 12));
                    coordinateText.setLayoutX(j * tileSize + 5);
                    coordinateText.setLayoutY(i * tileSize + 15);
                    pane.getChildren().add(coordinateText);
                }
            }
        }
        return pane;
    }

    public static Shashka makeShashka(boolean isLight, int x, int y) {
        Shashka shashka = new Shashka(isLight, x, y);
        shashka.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.SECONDARY) {
                boolean canKill = PossibleJumps.canCapture(field, shashka, convert(shashka.oldX), convert(shashka.oldY));
                boolean canMove = PossibleMoves.canMove(field, shashka, convert(shashka.oldX), convert(shashka.oldY));
                System.out.printf("Can%s move, can%s kill%n", canMove ? "" : "'t", canKill ? "" : "'t");
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
                if (killers.isEmpty() && walkers.isEmpty()) gameOver(!lightsTurn);
            }
        });
        return shashka;
    }

    static void gameOver(boolean lightsWon) {
        if (game) System.out.printf("Game over, %s won!", lightsWon ? "lights" : "darks");
        game = false;
        //add a game-ending effect, something like evaporating all left pieces away
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