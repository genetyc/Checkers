package geneticisst.checkerss;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.scene.Group;
import javafx.util.Duration;

public class GameSettings {
    public static int tileSize = 100;
    public static final int HEIGHT = 8;
    public static final int WIDTH = 8;

    public static int convert(double coord) {
        return (int) coord / tileSize;
    }
    public static class Effects {
        public static void playExplosionEffect(Shashka shashka, Tile[][] field, Group shashki) {
            ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(0.5), shashka);
            scaleTransition.setFromX(1.0);
            scaleTransition.setFromY(1.0);
            scaleTransition.setToX(1.5);
            scaleTransition.setToY(1.5);
            scaleTransition.setAutoReverse(true);
            scaleTransition.setCycleCount(2);
            scaleTransition.play();
            scaleTransition.setOnFinished(actionEvent -> playFadeOutEffect(shashka, field, shashki));
        }

        public static void playFadeOutEffect(Shashka shashka, Tile[][] field, Group shashki) {
            FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1), shashka);
            fadeTransition.setFromValue(1.0);
            fadeTransition.setToValue(0.0); //
            fadeTransition.setOnFinished(event -> {
            });
            fadeTransition.play();
        }
    }
}
