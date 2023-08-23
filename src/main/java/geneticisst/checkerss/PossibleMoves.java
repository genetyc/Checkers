package geneticisst.checkerss;

import javafx.scene.Group;

import static geneticisst.checkerss.GameSettings.convert;

public class PossibleMoves {
    public static boolean possibleMoves(Tile[][] field, Shashka shashka, int oldX, int oldY, int newX, int newY, int xDiff, boolean lightsTurn, Group shashki) {
        switch (xDiff) {
            case 1 -> {
                if ((shashka.sType.way != 0 && shashka.sType.way == newY - oldY) || shashka.isDamka) {
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
                if (eatenShashka != null) {
                    if (eatenShashka.isLight != shashka.isLight) {
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
                        lightsTurn = !lightsTurn;
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
                        eatenShashka = field[xMove][yMove].getShashka();
                        if (eatenShashka != null) {
                            if (eatenShashka.isLight == shashka.isLight || hasKilled
                                    || field[xMove+xDirection][yMove+yDirection].hasShashka()) {
                                validMove = false;
                                break;
                            }
                            else {
                                hasKilled = true;
                                if (eatenShashka.isLight) Game.lightShashkas--;
                                else Game.darkShashkas--;
                                if (Game.lightShashkas == 0 || Game.darkShashkas == 0) Game.gameOver();
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
                        }
                        lightsTurn = !lightsTurn;
                    } else shashka.cancel();
                }
            }
        }
        return lightsTurn;
    }
}

/**
 * shashka.setOnMouseReleased(e -> {
 *     // ... (ваш существующий код)
 *
 *     int deltaX = newX - oldX;
 *     int deltaY = newY - oldY;
 *
 *     // Проверка на диагональное движение
 *     if (Math.abs(deltaX) != Math.abs(deltaY)) {
 *         shashka.cancel();
 *         return;
 *     }
 *
 *     int stepX = deltaX > 0 ? 1 : -1;
 *     int stepY = deltaY > 0 ? 1 : -1;
 *
 *     int x = oldX + stepX;
 *     int y = oldY + stepY;
 *
 *     boolean hasEaten = false;
 *
 *     while (x != newX && y != newY) {
 *         if (field[x][y].hasShashka()) {
 *             Shashka targetShashka = field[x][y].getShashka();
 *             if (targetShashka.isLight != shashka.isLight) {
 *                 // Удаление съеденной шашки
 *                 getChildren().remove(targetShashka);
 *                 field[x][y].setShashka(null);
 *                 hasEaten = true;
 *             }
 *         }
 *         x += stepX;
 *         y += stepY;
 *     }
 *
 *     if (!hasEaten && field[newX][newY].hasShashka()) {
 *         shashka.cancel();
 *         return;
 *     }
 *
 *     // ... (остальная часть кода)
 * });*/
