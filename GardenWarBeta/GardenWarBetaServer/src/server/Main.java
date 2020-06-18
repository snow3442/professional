package server;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    private int port;
    GardenWarHub server;
    @Override
    public void start(Stage primaryStage) throws Exception{
        //server information GUI
        VBox root = new VBox(20);
        Label lblPort = new Label("Hosting Port");
        TextField tfPort = new TextField();
        tfPort.setPromptText("eg: 8000");
        HBox portBox = new HBox(10);
        Button btHost = new Button("Host");
        portBox.getChildren().addAll(lblPort, tfPort,btHost);
        portBox.setAlignment(Pos.CENTER);
        portBox.setPadding(new Insets(20, 0,0,  0 ));
        TextArea taLog = new TextArea();
        //Create a scene and place it in the stage
        Scene scene = new Scene(root, 450, 230);
        root.getChildren().addAll(portBox, new ScrollPane(taLog));
        primaryStage.setTitle("Garden War Beta Server");
        primaryStage.setScene(scene);
        primaryStage.show();
        btHost.setOnAction(e->{
            try{
                port = Integer.parseInt(tfPort.getText().trim());
                if (port <= 0||port>65535)
                    throw new Exception();
            }
            catch (Exception ex){
                Alert alert = new Alert(Alert.AlertType.ERROR, "Sorry, Invalid Port Number to host, Try again");
                alert.show();
                System.out.println("The user has tried with an incorrect port number");
            }

            try {
                server = new GardenWarHub(port);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            btHost.setVisible(false);
        });
        //close the server upon closing window
        primaryStage.setOnCloseRequest(e -> {
            if(server!=null) {
                server.shutDownHub();
            }
            System.exit(0);
            Platform.exit();
        });
    }


    public static void main(String[] args) {
        launch(args);
    }
}
