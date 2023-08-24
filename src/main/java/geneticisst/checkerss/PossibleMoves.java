package geneticisst.checkerss;

import javafx.scene.Group;

import static geneticisst.checkerss.GameSettings.convert;
import static geneticisst.checkerss.PossibleJumps.*;
import static geneticisst.checkerss.Game.killers;
import static geneticisst.checkerss.Game.walkers;

public class PossibleMoves {
    public static boolean possibleMoves(Tile[][] field, Shashka shashka, int oldX, int oldY, int newX, int newY, int xDiff, boolean lightsTurn, Group shashki) {
        switch (xDiff) {
            case 1 -> {
                if ((shashka.sType.way == newY - oldY || shashka.isDamka) && killers.isEmpty()) {
                    field[oldX][oldY].setShashka(null);
                    shashka.move(newX, newY);
                    if ((newY == 0 && shashka.isLight) || (newY == 7 && !shashka.isLight)) {
                        if (!shashka.isDamka) {
                            shashka.promote();
                        }
                    }
                    field[newX][newY].setShashka(shashka);
                    if (killers.isEmpty()) lightsTurn = !lightsTurn;
                } else {
                    shashka.cancel();
                }
            }
            case 2 -> {
                int eatenX = (newX + oldX) / 2;
                int eatenY = (newY + oldY) / 2;
                Shashka eatenShashka = field[eatenX][eatenY].getShashka();
                if (eatenShashka != null) {
                    if (eatenShashka.isLight != shashka.isLight && killers.contains(shashka)) {
                        field[oldX][oldY].setShashka(null);
                        field[eatenX][eatenY].setShashka(null);
                        shashki.getChildren().remove(eatenShashka);
                        if (eatenShashka.isLight) Game.lightShashkas--;
                        else Game.darkShashkas--;
                        if (Game.lightShashkas == 0 || Game.darkShashkas == 0) Game.gameOver();
                        shashka.move(newX, newY);
                        if ((newY == 0 && shashka.isLight) || (newY == 7 && !shashka.isLight)) {
                            if (!shashka.isDamka) {
                                shashka.promote();
                            }
                        }
                        field[newX][newY].setShashka(shashka);
                        if (!canCapture(field, shashka, convert(shashka.oldX), convert(shashka.oldY))) {
                            lightsTurn = !lightsTurn;
                        } else {
                            killers.clear();
                            walkers.clear();
                            killers.add(shashka);
                        }
                    } else shashka.cancel();
                } else {
                    if (!shashka.isDamka) shashka.cancel();
                    else {
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
                    int xDirection = (newX > oldX) ? 1 : -1;
                    int yDirection = (newY > oldY) ? 1 : -1;
                    int xMove = oldX + xDirection;
                    int yMove = oldY + yDirection;
                    boolean hasKilled = false;
                    boolean validMove = true;
                    Shashka eatenShashka = null;
                    while (xMove != newX && yMove != newY) {
                        Shashka possibleShashka = field[xMove][yMove].getShashka();
                        if (possibleShashka != null) {
                            if (possibleShashka.isLight == shashka.isLight || hasKilled
                                    || field[xMove+xDirection][yMove+yDirection].hasShashka()
                                    || !killers.contains(shashka)) {
                                validMove = false;
                                break;
                            }
                            else {
                                eatenShashka = field[xMove][yMove].getShashka();
                                hasKilled = true;
                            }
                        }
                        xMove+=xDirection;
                        yMove+=yDirection;
                    }
                    if (validMove) {
                        field[oldX][oldY].setShashka(null);
                        shashka.move(newX, newY);
                        field[newX][newY].setShashka(shashka);
                        if (eatenShashka != null) {
                            field[convert(eatenShashka.oldX)][convert(eatenShashka.oldY)].setShashka(null);
                            shashki.getChildren().remove(eatenShashka);
                            if (eatenShashka.isLight) Game.lightShashkas--;
                            else Game.darkShashkas--;
                            if (Game.lightShashkas == 0 || Game.darkShashkas == 0) Game.gameOver();
                        }
                        if (!canCapture(field, shashka, convert(shashka.oldX), convert(shashka.oldY))) {
                            lightsTurn = !lightsTurn;
                        } else {
                            killers.clear();
                            walkers.clear();
                            killers.add(shashka);
                        }
                    } else shashka.cancel();
                }
            }
        }
        return lightsTurn;  //add some sounds and killing effects
    }

    public static boolean canMove(Tile[][] field, Shashka shashka, int x, int y) {
        if (!canCapture(field, shashka, x, y)) {
            int[][] lightDirections = {{1, -1}, {-1, -1}};
            int[][] darkDirections = {{1, 1}, {-1, 1}};
            int[][] directions = {{1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
            if (!shashka.isDamka) {
                for (int[] direction : shashka.isLight ? lightDirections : darkDirections) {
                    int xMove = x + direction[0];
                    int yMove = y + direction[1];
                    if (isValidPosition(xMove, yMove) && !field[xMove][yMove].hasShashka()) {
                        return true;
                    }
                }
            } else {
                for (int[] direction : directions) {
                    int xMove = x + direction[0];
                    int yMove = y + direction[1];
                    if (isValidPosition(xMove, yMove) && !field[xMove][yMove].hasShashka()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}