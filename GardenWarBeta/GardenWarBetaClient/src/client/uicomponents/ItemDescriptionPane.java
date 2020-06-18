package client.uicomponents;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class ItemDescriptionPane extends Stage {
    private GameItem gameItem;
    private Pane pane;


    public ItemDescriptionPane(GameItem gameItem){
        this.gameItem = gameItem;
        pane = new Pane();
        pane.getStyleClass().add("itemDescriptionPane");
        initStyle(StageStyle.TRANSPARENT);
        display(gameItem.isSeed());
        Scene scene = new Scene(pane);
        scene.getStylesheets().add("css/game.css");
        setScene(scene);
    }
    //constructor the ItemDescriptionPane from message
    public ItemDescriptionPane(String message){
        pane = new Pane();
        pane.getStyleClass().add("itemDescriptionPane");
        buildFromMessage(message);
        Scene scene = new Scene(pane);
        scene.getStylesheets().add("css/game.css");
        setScene(scene);
    }

    /**
     * Build the Description Pane with a message only
     * @param message {String} a description of the event
     */
    private void buildFromMessage(String message){
        pane.getChildren().clear();
        pane.setPrefSize(330,130);
        Text txtDescription = new Text(message);
        txtDescription.getStyleClass().add("descriptionText");
        txtDescription.setLayoutX(20);
        txtDescription.setLayoutY(35);
        txtDescription.wrappingWidthProperty().bind(pane.prefWidthProperty().subtract(40));
        pane.getChildren().add(txtDescription);
    }

    private void display(boolean isSeed){
        pane.getChildren().clear();
        if(isSeed) {
            pane.setPrefSize(430,350);
            ImageView ivItem = new ImageView(new Image(getClass().getResourceAsStream(gameItem.getImgUrl())));
            ivItem.setFitHeight(30);
            ivItem.setFitWidth(30);
            ivItem.setLayoutX(20);
            ivItem.setLayoutY(10);
            Label lblItem = new Label(gameItem.getName());
            lblItem.setLayoutX(20);
            lblItem.setLayoutY(50);
            lblItem.getStyleClass().add("lblSlideTitle");
            Text txtDescription = new Text(gameItem.getDescription());
            txtDescription.getStyleClass().add("descriptionText");
            txtDescription.setLayoutX(20);
            txtDescription.setLayoutY(85);
            txtDescription.wrappingWidthProperty().bind(pane.prefWidthProperty().subtract(20));
            GameItem gameItemEvolved = new GameItem(gameItem.getEvolutionName(gameItem.getName()),0);
            System.out.println("game item: "+gameItemEvolved.getName()+" item url: "+gameItemEvolved.getImgUrl());
            ImageView ivEvolved = new ImageView(new Image(getClass().getResourceAsStream(gameItemEvolved.getImgUrl())));
            ivEvolved.setFitWidth(30);
            ivEvolved.setFitHeight(30);
            ivEvolved.setLayoutX(20);
            ivEvolved.setLayoutY(100);
            Label lblEvolved = new Label(gameItemEvolved.getName());
            lblEvolved.setLayoutX(20);
            lblEvolved.setLayoutY(135);
            lblEvolved.getStyleClass().add("lblSlideTitle");
            Text txtEvolved = new Text(gameItemEvolved.getDescription());
            txtEvolved.getStyleClass().add("descriptionText");
            txtEvolved.setLayoutX(20);
            txtEvolved.setLayoutY(180);
            txtEvolved.wrappingWidthProperty().bind(pane.prefWidthProperty().subtract(40));
            pane.getChildren().addAll(ivItem, lblItem, txtDescription,ivEvolved,lblEvolved,txtEvolved);
        }

        else{
            pane.setPrefSize(430,290);
            ImageView ivItem = new ImageView(new Image(getClass().getResourceAsStream(gameItem.getImgUrl())));
            ivItem.setFitHeight(45);
            ivItem.setFitWidth(45);
            ivItem.setLayoutX(20);
            ivItem.setLayoutY(20);
            Label lblItem = new Label(gameItem.getName());
            lblItem.setLayoutX(20);
            lblItem.setLayoutY(70);
            lblItem.getStyleClass().add("lblSlideTitle");
            Text txtDescription = new Text(gameItem.getDescription());
            txtDescription.getStyleClass().add("descriptionText");
            txtDescription.setLayoutX(20);
            txtDescription.setLayoutY(110);
            txtDescription.wrappingWidthProperty().bind(pane.prefWidthProperty().subtract(40));
            pane.getChildren().addAll(ivItem, lblItem, txtDescription);
        }
    }
}
