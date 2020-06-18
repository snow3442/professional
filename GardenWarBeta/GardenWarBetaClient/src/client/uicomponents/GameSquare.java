package client.uicomponents;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;


public class GameSquare extends Pane {
    private ImageView ivBackground;
    private ImageView ivItem=new ImageView();
    private final String GROUNDURL = "../../images/backgrounds/ground.jpg";
    private GameView.GardenWarClient client;
    private Text txtMaturity = new Text("");

    public GameSquare(GameView.GardenWarClient client){
        this.client = client;
        ivBackground = new ImageView(new Image(getClass().getResourceAsStream(GROUNDURL)));
        ivBackground.fitWidthProperty().bind(prefWidthProperty());
        ivBackground.fitHeightProperty().bind(prefHeightProperty());
        getChildren().add(ivBackground);
        ivItem.fitWidthProperty().bind(prefWidthProperty());
        ivItem.fitHeightProperty().bind(prefHeightProperty());
        txtMaturity.layoutXProperty().bind(prefWidthProperty().subtract(30));
        txtMaturity.setLayoutY(10);
        txtMaturity.getStyleClass().add("txtMaturity");
        getChildren().add(ivItem);
        getChildren().add(txtMaturity);
        getStyleClass().add("gamesquare");
    }

    public void addLayerImage(String url){
        Platform.runLater(()->{
            ivItem.setImage(new Image(getClass().getResourceAsStream(url)));
        });
    }

    public Text getTxtMaturity() {
        return txtMaturity;
    }

    public ImageView getIvItem() {
        return ivItem;
    }
}
