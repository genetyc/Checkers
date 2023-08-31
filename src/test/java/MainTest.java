import geneticisst.checkerss.Game;
import geneticisst.checkerss.Shashka;
import geneticisst.checkerss.Tile;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.layout.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.LinkedList;

import static geneticisst.checkerss.GameSettings.*;
import static geneticisst.checkerss.PossibleJumps.canCapture;
import static geneticisst.checkerss.PossibleMoves.canMove;
import static geneticisst.checkerss.Game.makeShashka;

public class MainTest {
    static Group tiles = new Group();
    static Group shashki = new Group();
    static Tile[][] field = new Tile[8][8];
    static LinkedList<Shashka> killers = new LinkedList<>();
    static LinkedList<Shashka> walkers = new LinkedList<>();

    Shashka lightClosedShashka = field[7][6].getShashka();
    int lightsX = convert(lightClosedShashka.oldX);
    int lightsY = convert(lightClosedShashka.oldY);
    Shashka darkOpenShashka = field[5][2].getShashka();
    int darksX = convert(darkOpenShashka.oldX);
    int darksY = convert(darkOpenShashka.oldY);

    private static Parent board() {
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
                        Shashka shashka = makeShashka(false, j, i);
                        shashka.isLight = false;
                        tile.setShashka(shashka);
                        shashki.getChildren().add(shashka);
                    }
                    if (i > 4) {
                        Shashka shashka = Game.makeShashka(true, j, i);
                        shashka.isLight = true;
                        tile.setShashka(shashka);
                        shashki.getChildren().add(shashka);
                    }
                }
            }
        }
        return pane;
    }

    @BeforeAll
    public static void setUp() {
        board();
    }

    @Test
    public void fieldProperties() {
        assertFalse(field[0][0].hasShashka());
        assertTrue(field[0][1].hasShashka());
    }

    @Test
    public void shashkaPropertiesTest() {
        assertFalse(lightClosedShashka.isDamka);
        assertFalse(darkOpenShashka.isLight);
    }

    @Test
    public void movableShashkaTest() {
        assertFalse(canMove(field, lightClosedShashka, lightsX, lightsY));
        assertTrue(canMove(field, darkOpenShashka, darksX, darksY));
    }

    @Test
    public void killTest() {
        Shashka shashka = makeShashka(false, 5, 4);
        field[5][4].setShashka(shashka);

        Shashka lightOne = field[6][5].getShashka();
        Shashka lightTwo = field[4][5].getShashka();
        Shashka lightThree = field[7][6].getShashka();
        assertTrue(canCapture(field, lightOne, convert(lightOne.oldX), convert(lightOne.oldY)));
        assertTrue(canCapture(field, lightTwo, convert(lightTwo.oldX), convert(lightTwo.oldY)));
        assertFalse(canCapture(field, lightThree, convert(lightThree.oldX), convert(lightThree.oldY)));
    }
}
