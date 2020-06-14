package com.jianqiu.svg_converter;

import com.jianqiu.svg_converter.controllers.WindowsController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        //creates the main window through the windows controller
        WindowsController.getInstance().createMainWindow(stage);
        //exit the software if main window is closed
        stage.setOnCloseRequest(e->{
            Platform.exit();
            System.exit(0);
        });
    }
}
