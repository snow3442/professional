package client.uicomponents;

import javafx.beans.binding.Bindings;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class GameMenuItem extends Pane {
    private final String MENUCLICKSOUNDURL = "src/audio/click_hover/buttonclick.wav";
    private Text text;
    private Effect shadow = new DropShadow(5, Color.BLACK);
    private Effect blur = new BoxBlur(1, 1, 3);

    public GameMenuItem(String name, double width, double height) {
        Polygon bg = new Polygon(
                0, 0,
                width, 0,
                (width+15), height/2,
                width, height,
                0, height
        );
        bg.setStroke(Color.color(0.52, 0.37, 0.26, 0.9));
        bg.setStrokeWidth(3);
        bg.setStrokeType(StrokeType.OUTSIDE);
        bg.setEffect(new GaussianBlur());
        bg.fillProperty().bind(
                Bindings.when(pressedProperty())
                        .then(Color.color(0.05, 0.05, 0.05, 1))
                        .otherwise(Color.color(0.1, 0.1, 0.1, 0.8))
        );

        text = new Text(name);
        text.setTranslateX(5);
        text.setTranslateY(20);
        text.setFont(Font.font("Algerian", FontWeight.BOLD, 22));
        text.setFill(Color.WHITE);
        text.setFontSmoothingType(FontSmoothingType.LCD);
        text.effectProperty().bind(
                Bindings.when(hoverProperty())
                        .then(shadow)
                        .otherwise(blur)
        );

        getChildren().addAll(bg, text);
    }

    public void setOnAction(Runnable action) {
        setOnMouseClicked(e -> {
            MediaControl.getInstance().playGameSound(MENUCLICKSOUNDURL);
            action.run();
        });

    }
}
