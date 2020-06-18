package client.uicomponents;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public abstract class PopUpWindow extends Stage {
    private Rectangle background;
    private Group root;

    public PopUpWindow(){
        setSize();
        initConfigs();
        render();
        show();
    }

    /**
     * default configs for all Pop up windows
     */
    public void initConfigs(){
        initStyle(StageStyle.TRANSPARENT);
        //setAlwaysOnTop(true);
        root = new Group();
        Scene scene = new Scene(root, getWidth(), getHeight());
        scene.getStylesheets().add("css/login.css");
        setScene(scene);
        Rectangle background = new Rectangle();
        background.setWidth(getWidth());
        background.setHeight(getHeight());
        background.setId("background-rect");
        root.getChildren().add(background);
    }

    /**
     * add extra UI on the background
     * @param node
     */
    public void addNode(Node node){
        root.getChildren().add(node);
    }

    public abstract void setSize();

    public abstract void render();

}
