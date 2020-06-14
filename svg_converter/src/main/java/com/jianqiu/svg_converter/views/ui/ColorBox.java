package com.jianqiu.svg_converter.views.ui;

import com.jianqiu.svg_converter.services.potraceservice.Cluster;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Custom VBox to store color information
 */
@Getter
public class ColorBox extends VBox {
    //contains all chosen colors
    private ArrayList<Color> colors = new ArrayList<>();
    //contains all UI that matches the list of colors
    private ArrayList<ColorRow> colorRows = new ArrayList<>();
    public ColorBox(){
        setSpacing(10);
    }

    /**
     * re-initialize the VBox for color rendering results
     */
    public void reloadColors(HashSet<Cluster> clusterSet){
        //refresh all
        colors.clear();
        colorRows.clear();
        getChildren().clear();
        //load the colors from set to the list
        for(Cluster c: clusterSet){
            Color color = Color.rgb(c.getRed(),c.getGreen(),c.getBlue());
            colors.add(color);
        }
        //add colorRows to colorRows list as well as to UI
        for(Color color: colors){
            ColorRow cr = new ColorRow(color);
           colorRows.add(cr);
           getChildren().add(cr);
        }

    }

}
