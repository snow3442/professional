package client.uicomponents;

import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;

public class Avatar extends Circle {
    private final String SAMPLE1 = "../../images/users/sample1.jpg";
    private final String SAMPLE2 = "../../images/users/sample2.jpg";

    public Avatar(int id) {
        setStrokeWidth(3);
        setStrokeType(StrokeType.CENTERED);
        setStroke(Color.CORAL);
        if (id == 1) {
            setFill(new ImagePattern(new Image(getClass().getResourceAsStream(SAMPLE1))));
        } else if (id == 2) {
            setFill(new ImagePattern(new Image(getClass().getResourceAsStream(SAMPLE2))));
        }
        setEffect(new DropShadow(+10d, 0d, +2d, Color.WHITESMOKE));
    }

    public Avatar(String url){
        setFill(new ImagePattern(new Image(getClass().getResourceAsStream(url))));
        setStrokeWidth(3);
        setStrokeType(StrokeType.CENTERED);
        setStroke(Color.CORAL);
        setEffect(new DropShadow(+10d, 0d, +2d, Color.WHITESMOKE));
    }


}
