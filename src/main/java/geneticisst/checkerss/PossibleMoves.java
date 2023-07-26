package geneticisst.checkerss;

import java.util.LinkedList;

import static geneticisst.checkerss.GameSettings.*;
import static geneticisst.checkerss.Moves.MoveType.*;

public class PossibleMoves {

    /*public static LinkedList<Moves> getAllTheMoves(Tile[][] field) {
        LinkedList<Moves> allTheMoves = new LinkedList<>();
        for (int i = 0; i < WIDTH; i++) {
            for ()
        }
        return allTheMoves;
    }*/

    LinkedList<Moves> steps = new LinkedList<>();
    LinkedList<Moves> jumps = new LinkedList<>();

    public static LinkedList<Moves> anySteps(Tile[][] field, int x, int y) {
        LinkedList<Moves> possibleSteps = new LinkedList<>();
        Shashka shashka = field[x][y].getShashka();
        if (!shashka.isDamka) {
            if (!field[x + 1][y + shashka.sType.way].hasShashka() || !field[x - 1][y + shashka.sType.way].hasShashka()) {
                possibleSteps.add(new Moves(STEP, shashka));
                System.out.println("There is a step move");
            } else {
                System.out.println("There is no any step moves!");
            }
        }
        return possibleSteps;
    }/*

    public static LinkedList<Moves> anyJumps(Tile[][] field, int x, int y) {
        LinkedList<Moves> possibleSteps = new LinkedList<>();
        Shashka shashka = field[x][y].getShashka();
        if (!shashka.isDamka) {
            if (!field[x + 1][y + shashka.sType.way].hasShashka() || !field[x - 1][y + shashka.sType.way].hasShashka()) {
                possibleSteps.add(new Moves(STEP, shashka));
                System.out.println("There is a step move");
            } else {
                System.out.println("There is no any step moves!");
            }
        }
        return possibleSteps;
    }*/

}
