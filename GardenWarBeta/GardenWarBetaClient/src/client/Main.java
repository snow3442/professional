package client;

import client.uicomponents.GameMenuItem;
import client.uicomponents.GameTitle;
import client.uicomponents.LoginPane;
import client.uicomponents.MediaControl;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.CacheHint;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javafx.util.Pair;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.List;

public class Main extends Application {

    private static final int WIDTH = 1200;
    private static final int HEIGHT = 680;
    private List<Pair<String, Runnable>> primaryMenuData = Arrays.asList(
            new Pair<String, Runnable>("Start Game", () -> {
                loadLogin();
            }),
            new Pair<String, Runnable>("Instructions", () -> {

            }),
            new Pair<String, Runnable>("About", () -> {

            }),
            new Pair<String, Runnable>("Credit", () -> {
            }),
            new Pair<String, Runnable>("Exit to Desktop", ()->{
                Platform.exit();
                System.exit(0);
            })
    );

    private final double lineX = WIDTH / 2 - 100;
    private final double lineY = HEIGHT / 3;
    private Pane root = new Pane();
    private VBox menuBox = new VBox(-5);
    private Line line;
    //audio variables
    private final String BACKGROUNDMUSICURL = "src/audio/backmusic/welcome.mp3";

    @Override
    public void start(Stage stage) throws Exception {
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setScene(new Scene(createMenus(), WIDTH, HEIGHT));
        stage.show();
    }

    private Parent createMenus() throws MalformedURLException {
        addBackground();
        addTitle();
        addLine(lineX, lineY);
        loadMenu(primaryMenuData);
        addMenu();
        startAnimation();
        MediaControl.getInstance().play(BACKGROUNDMUSICURL);
        return root;
    }

    private void addBackground() {
        Image image = new Image(getClass().getResourceAsStream("../images/backgrounds/background.jpg"));
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(WIDTH);
        imageView.setFitHeight(HEIGHT);
        root.getChildren().add(imageView);
    }

    private void addTitle() {
        GameTitle title = new GameTitle("Garden Wars");
        title.setTranslateX(WIDTH / 2 - title.getTitleWidth() / 2);
        title.setTranslateY(HEIGHT / 4);
        root.getChildren().add(title);
    }

    private void addLine(double x, double y) {
        line = new Line(x, y, x, y + 300);
        line.setStrokeWidth(3);
        line.setStroke(Color.color(1, 1, 1, 0.75));
        line.setEffect(new DropShadow(5, Color.BLACK));
        line.setScaleY(0);
        root.getChildren().add(line);
    }


    private void startAnimation() {
        ScaleTransition st = new ScaleTransition(Duration.seconds(1), line);
        st.setToY(1);
        st.setOnFinished(e -> {
            for (int i = 0; i < menuBox.getChildren().size(); i++) {
                Node n = menuBox.getChildren().get(i);
                TranslateTransition tt = new TranslateTransition(Duration.seconds(1 + i * 0.15), n);
                tt.setToX(0);
                tt.setOnFinished(e2 -> n.setClip(null));
                tt.play();
            }
        });
        st.play();
    }

    private void loadMenu(List<Pair<String, Runnable>> menuData) {
        menuBox.getChildren().clear();
        menuBox.setTranslateX(lineX);
        menuBox.setTranslateY(lineY + 30);
        menuData.forEach(data -> {
            GameMenuItem item = new GameMenuItem(data.getKey(), 200, 30);
            item.setOnAction(data.getValue());
            item.setTranslateX(-300);
            Rectangle clip = new Rectangle(300, 30);
            clip.translateXProperty().bind(item.translateXProperty().negate());
            item.setClip(clip);
            item.setCache(true);
            item.setCacheHint(CacheHint.SPEED);
            menuBox.getChildren().addAll(item);
        });

    }

    private void addMenu() {
        root.getChildren().add(menuBox);
    }


    private void loadLogin() {
        LoginPane loginPane = new LoginPane();
    }


    public static void main(String[] args) {
        launch(args);
    }

}
