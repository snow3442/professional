package client.uicomponents;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.HashMap;
import java.util.Map;

public class ActionPaneButton extends Pane {

    private static final Map<String, String> urlMap;
    static {
        urlMap = new HashMap<>();
        urlMap.put("FARMER", "../../images/gameicons/farmer.png");
        urlMap.put("GOV", "../../images/gameicons/gov.png");
        urlMap.put("WEATHER", "../../images/gameicons/weather.png");
        urlMap.put("ACTION", "../../images/gameicons/action.png");
        urlMap.put("INFO", "../../images/gameicons/info.png");
        urlMap.put("ENDTURN", "../../images/gameicons/endturn.png");
    }

    public ActionPaneButton(String type){
        getStyleClass().add("actionPaneButton");
        Image image = new Image(getClass().getResourceAsStream(urlMap.get(type)));
        ImageView ivIcon = new ImageView(image);
        ivIcon.fitWidthProperty().bind(prefWidthProperty());
        ivIcon.fitHeightProperty().bind(prefHeightProperty());
        getChildren().add(ivIcon);
    }

}
