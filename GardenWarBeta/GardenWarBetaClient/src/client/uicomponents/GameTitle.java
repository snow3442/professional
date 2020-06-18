package client.uicomponents;

import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.Text;

public class GameTitle extends Pane {
    private Text text;

    public GameTitle(String name) {
        String spread = "";
        for (char c : name.toCharArray()) {
            spread += c + " ";
        }

        text = new Text(spread);
        //Font font = Font.font("Algerian", FontWeight.BOLD, FontPosture.ITALIC, 42);
        //text.setFont(font);
        text.setFont(Font.font("Helvetica", FontPosture.ITALIC, 48));
        text.setFill(Color.WHITE);
        text.setEffect(new DropShadow(30, Color.BLACK));
        text.setFontSmoothingType(FontSmoothingType.LCD);
        getChildren().addAll(text);
    }

    public double getTitleWidth() {
        return text.getLayoutBounds().getWidth();
    }

    public double getTitleHeight() {
        return text.getLayoutBounds().getHeight();
    }
}
