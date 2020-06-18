package client.uicomponents;

import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;

public class SliderPane extends Pane {

    private Label lblCategory = new Label();
    private Label lblName = new Label();
    private final double WIDTH = 435;
    private final double HEIGHT = 360;
    private final String DEFAULTURL = "../../images/gameicons/default.png";
    private final String FARMERURL = "../../images/gameicons/farmer.png";
    private final String GOVURL = "../../images/gameicons/gov.png";
    private final String WEATHERURL = "../../images/gameicons/weather.png";
    private final String INFOURL = "../../images/gameicons/info.png";
    private VBox resourceInfoPane = new VBox();
    private ImageView icon;
    private GameView.GardenWarClient client;
    private GameItem giHurri = new GameItem("HURRICANE",0);
    private GameItem giWildfire = new GameItem("WILDFIRE",0);
    private GameItem giFlood = new GameItem("FLOOD",0);
    private GameItem giPest = new GameItem("PEST",0);
    private GameItem giDrought = new GameItem("DROUGHT",0);
    private Text txtTemperature = new Text("15C");

    private Polygon bgBack = new Polygon(
            0, 0,
            25, 15,
            0, 30
    );

    public SliderPane(GameView.GardenWarClient client) {
        this.client = client;
        getStyleClass().add("sliderPane");
        buildAvatar();
        buildBackButton();
        addCategoryLabel();
        addResourceInfoPane();
    }

    private void buildAvatar() {
        icon = new ImageView(new Image(getClass().getResourceAsStream(DEFAULTURL)));
        icon.setFitWidth(40);
        icon.setFitHeight(40);
        icon.setLayoutX(35.0);
        icon.setLayoutY(15.0);
        getChildren().add(icon);
    }


    private void addCategoryLabel() {
        lblCategory.setLayoutX(150);
        lblCategory.setLayoutY(25);
        lblCategory.getStyleClass().add("lblSlideTitle");
        getChildren().add(lblCategory);
    }

    private void buildBackButton() {
        bgBack.setStroke(Color.BLUE);
        bgBack.setEffect(new GaussianBlur());
        bgBack.fillProperty().bind(
                Bindings.when(pressedProperty())
                        .then(Color.color(1, 1, 1, 0.9))
                        .otherwise(Color.CYAN)
        );
        bgBack.layoutXProperty().bind(layoutXProperty().add(WIDTH).subtract(40));
        bgBack.layoutYProperty().bind(layoutYProperty().add(15));
        getChildren().add(bgBack);
    }

    private void addResourceInfoPane(){
        resourceInfoPane.getStyleClass().add("resourceInfoPane");
        resourceInfoPane.setPrefSize(435, 290);
        resourceInfoPane.setLayoutX(0);
        resourceInfoPane.setLayoutY(70);
        getChildren().add(resourceInfoPane);
    }

    public void clearResource(){
        icon.setImage(new Image(getClass().getResourceAsStream(DEFAULTURL)));
        lblCategory.setText("");
        resourceInfoPane.getChildren().clear();
    }

    public void build(Category category){
        clearResource();
        if(category.equals(Category.FARMERCENTER)) {
            icon.setImage(new Image(getClass().getResourceAsStream(FARMERURL)));
            lblCategory.setText("FARMER CENTER");
            ItemTree tvSeedsItems = new ItemTree(client,"Seeds Items", Category.FARMERCENTER);
            tvSeedsItems.getStyleClass().add("seedsTree");
            tvSeedsItems.addItem(new GameItem("PUMPKIN SEED", 1));
            tvSeedsItems.addItem(new GameItem("GOURD SEED", 1));
            tvSeedsItems.addItem(new GameItem("PATATO SEED", 1));
            ItemTree tvFarmerItems = new ItemTree(client,"Farmer Items", Category.FARMERCENTER);
            tvFarmerItems.getStyleClass().add("farmerTree");
            tvFarmerItems.addItem(new GameItem("FERTILIZER",1));
            tvFarmerItems.addItem(new GameItem("PESTICIDE",1));
            tvFarmerItems.addItem(new GameItem("PLOWER",2));
            tvFarmerItems.addItem(new GameItem("COLLECT", 1));
            resourceInfoPane.getChildren().addAll(tvSeedsItems, tvFarmerItems);
        }

        else if(category.equals(Category.WEATHER)){
            icon.setImage(new Image(getClass().getResourceAsStream(WEATHERURL)));
            lblCategory.setText("WEATHER REPORT");
            HBox yearBox = new HBox(30);
            Label lblYear = new Label("YEAR");
            lblYear.getStyleClass().add("lblSlideTitle");
            Text txtYear = new Text("2020");
            txtYear.getStyleClass().add("txtSlideTitle");
            yearBox.getChildren().addAll(lblYear,txtYear);
            HBox temperatureBox = new HBox(30);
            Label lblTemperature = new Label("Global Temperature");
            lblTemperature.getStyleClass().add("lblSlideTitle");
            txtTemperature.getStyleClass().add("txtSlideTitle");
            yearBox.setPadding(new Insets(0, 0, 20, 20));
            temperatureBox.setPadding(new Insets(0, 0, 20, 20));
            temperatureBox.getChildren().addAll(lblTemperature,txtTemperature);
            ItemTree tvDisaster = new ItemTree(client,"Natural Disaster", Category.WEATHER);
            tvDisaster.getStyleClass().add("farmerTree");
            tvDisaster.addItem(giHurri);
            tvDisaster.addItem(giWildfire);
            tvDisaster.addItem(giDrought);
            tvDisaster.addItem(giFlood);
            tvDisaster.addItem(giPest);
            resourceInfoPane.getChildren().addAll(yearBox,temperatureBox,tvDisaster);
        }

        else if(category.equals(Category.GOVERNMENT)){
            icon.setImage(new Image(getClass().getResourceAsStream(GOVURL)));
            lblCategory.setText("GOVERNMENT");
            ItemTree tvPolicies = new ItemTree(client,"Policy Items", Category.GOVERNMENT);
            tvPolicies.getStyleClass().add("policyTree");
            tvPolicies.addItem(new GameItem("STARVATION", 1));
            tvPolicies.addItem(new GameItem("PLANT TREE", 1));
            tvPolicies.addItem(new GameItem("GREEN ENERGY", 2));
            tvPolicies.addItem(new GameItem("ACCEPT REFUGEES", 2));
            tvPolicies.addItem(new GameItem("WAR", 3));
            resourceInfoPane.getChildren().add(tvPolicies);
        }

        else if(category.equals((Category.INFORMATION))){
            icon.setImage(new Image(getClass().getResourceAsStream(INFOURL)));
            lblCategory.setText("INFORMATION");
            DescriptionArea daFarmer = new DescriptionArea(Category.INFOFARMER);
            DescriptionArea daWeather = new DescriptionArea(Category.INFOWEATHER);
            DescriptionArea daGov = new DescriptionArea(Category.INFOGOV);
            resourceInfoPane.getChildren().addAll(daFarmer,daGov,daWeather);
        }
    }

    public Label getLblName() {
        return lblName;
    }

    public void setLblName(Label lblName) {
        this.lblName = lblName;
    }

    public Polygon getBgBack() {
        return bgBack;
    }

    public GameItem getGiHurri() {
        return giHurri;
    }

    public GameItem getGiWildfire() {
        return giWildfire;
    }

    public GameItem getGiFlood() {
        return giFlood;
    }

    public GameItem getGiPest() {
        return giPest;
    }

    public GameItem getGiDrought() {
        return giDrought;
    }

    public Text getTxtTemperature() {
        return txtTemperature;
    }
}
