package client.uicomponents;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;


public class PlayerPane extends Pane {

    private int id;
    private String userName;
    private GridPane playerInfo;
    private Text txtFood;
    private Text txtActionPoints;
    private Text txtConsumption;
    private Text txtUserName;
    private Text txtEarning;
    private Glow lightUp;

    public PlayerPane(int id){
        this.id = id;
        setPrefSize(235,120);
        getStyleClass().add("playerPane"+String.valueOf(id));
        if(id==1){
            lightUp = new Glow(0.1);
            Light.Distant light = new Light.Distant();
            light.setAzimuth(45.0);
            light.setElevation(45.0);
            light.setColor(Color.color(0.65, 0.05, 0.43, 0.7));
            Lighting initLighting = new Lighting();
            initLighting.setLight(light);
            lightUp.setInput(initLighting);
        }
        else{
            lightUp = new Glow(0.1);
            Light.Distant light = new Light.Distant();
            light.setAzimuth(45.0);
            light.setElevation(45.0);
            light.setColor(Color.color(0.07, 0.76, 0.17, 0.7));
            Lighting initLighting = new Lighting();
            initLighting.setLight(light);
            lightUp.setInput(initLighting);
        }
        addAvatar();
        addUserName();
        addLine();
        addInfoPane();
    }

    private void addAvatar(){
        Avatar userIcon = new Avatar(id);
        userIcon.setRadius(15);
        userIcon.setLayoutX(10);
        userIcon.setLayoutY(10);
        getChildren().add(userIcon);
    }

    private void addUserName() {
        userName = "player"+String.valueOf(id);
        txtUserName = new Text();
        txtUserName.setText(userName);
        txtUserName.getStyleClass().add("userName");
        txtUserName.setLayoutX(100);
        txtUserName.setLayoutY(25);
        getChildren().add(txtUserName);
    }

    private void addLine(){
        Line line = new Line();
        line.setStartX(30);
        line.setStartY(40);
        line.setEndX(200);
        line.setEndY(40);
        line.setStrokeWidth(2);
        line.setStroke(Color.color(1, 1, 1, 0.75));
        line.setEffect(new DropShadow(3, Color.GOLDENROD));
        getChildren().add(line);
    }

    private void addInfoPane(){
        playerInfo = new GridPane();
        playerInfo.setPrefSize(200, 70);
        ColumnConstraints column1 = new ColumnConstraints();
        column1.setPercentWidth(75);
        ColumnConstraints column2 = new ColumnConstraints();
        column2.setPercentWidth(25);
        playerInfo.getColumnConstraints().addAll(column1, column2);
        Label lblFood = new Label("Food");
        Label lblActionPoints = new Label("Action Points");
        Label lblConsumption = new Label("Consumption ");
        Label lblEarning = new Label("Income");
        txtFood = new Text("0");
        txtActionPoints = new Text("0");
        txtConsumption = new Text("0");
        txtEarning = new Text("0");
        playerInfo.add(lblFood, 0,0);
        playerInfo.add(txtFood, 1, 0);
        playerInfo.add(lblActionPoints, 0,1);
        playerInfo.add(txtActionPoints, 1,1 );
        playerInfo.add(lblConsumption, 0,2);
        playerInfo.add(txtConsumption,1,2);
        playerInfo.add(lblEarning,0,3);
        playerInfo.add(txtEarning, 1,3);
        lblFood.getStyleClass().add("lblPlayerInfo");
        lblActionPoints.getStyleClass().add("lblPlayerInfo");
        lblConsumption.getStyleClass().add("lblPlayerInfo");
        lblEarning.getStyleClass().add("lblPlayerInfo");
        txtFood.getStyleClass().add("txtPlayerInfo");
        txtActionPoints.getStyleClass().add("txtPlayerInfo");
        txtConsumption.getStyleClass().add("txtPlayerInfo");
        txtEarning.getStyleClass().add("txtPlayerInfo");
        playerInfo.setAlignment(Pos.TOP_CENTER);
        playerInfo.setLayoutX(30);
        playerInfo.setLayoutY(70);
        getChildren().add(playerInfo);
    }

    public Text getTxtFood() {
        return txtFood;
    }

    public Text getTxtActionPoints() {
        return txtActionPoints;
    }

    public Text getTxtConsumption() {
        return txtConsumption;
    }

    public Text getTxtUserName() {
        return txtUserName;
    }

    public Text getTxtEarning() {
        return txtEarning;
    }

    public int getIdNum() {
        return id;
    }

    public Glow getLightUp() {
        return lightUp;
    }
}
