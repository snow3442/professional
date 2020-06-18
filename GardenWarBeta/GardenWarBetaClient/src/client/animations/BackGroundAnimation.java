package client.animations;

import client.uicomponents.MediaControl;
import javafx.animation.ParallelTransition;
import javafx.animation.PathTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.CacheHint;
import javafx.scene.Group;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.effect.Glow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Random;

public class BackGroundAnimation {

    private SequentialTransition transition = new SequentialTransition();
    private Random random = new Random();

    public BackGroundAnimation(Pane pane) {
        buildAnimation(pane);
        //let it play forever
        //transition.setCycleCount(Timeline.INDEFINITE);
        //transition.setAutoReverse(true);
        //transition.play();
    }

    private void buildAnimation(Pane pane) {
        getSnow(pane,30000).play();
    }

    /**
     * creates the snow effect on the gameboard
     * @param root
     * @param time
     * @return
     */
    private ParallelTransition getSnow(Pane root, int time){
        ArrayList<Circle> smokes = new ArrayList<>();
        int MAXSMOKE = 200;
        Group group = new Group();
        double animationWidth = root.getWidth();
        double animationHeight = root.getHeight();
        double initX = 0;
        double initY = 0;
        //build smokes
        for (int i = 0; i < MAXSMOKE; i++) {
            Circle smoke = new Circle(initX + animationWidth * Math.random(),
                    initY + animationHeight*Math.random(), 2+2*random.nextDouble());
            smoke.setManaged(false);
            smoke.setFill(Color.color(0.07, 1, 0.17, random.nextDouble()));
            Glow glow = new Glow(1.5);
            glow.setInput(new GaussianBlur(100));
            smoke.setEffect(glow);
            smoke.setCache(true);
            smoke.setCacheHint(CacheHint.SPEED);
            smokes.add(smoke);
            group.getChildren().add(smoke);
            root.getChildren().add(smoke);
        }
        root.getChildren().add(group);
        // build parallel transition
        ParallelTransition pt = new ParallelTransition();
        for (Circle circle : smokes) {
            Path path = new Path();
            path.setVisible(false);
            path.getElements().add(new MoveTo(circle.getCenterX(), circle.getCenterY()));
            path.getElements().add(new LineTo(
                    initX + animationWidth * Math.random(),
                    initY+animationHeight*Math.random()));
            PathTransition pathTransition = new PathTransition();
            pathTransition.setDuration(Duration.millis(time));
            pathTransition.setNode(circle);
            pathTransition.setPath(path);
            pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
            pathTransition.setCycleCount(Timeline.INDEFINITE);
            pt.getChildren().add(pathTransition);

        }
        Platform.runLater(() -> {
            pt.setOnFinished(e -> {
                for (Circle smoke : smokes) {
                    smoke.setVisible(false);
                }
                root.setEffect(null);
            });
        });
        return pt;
    }

}
