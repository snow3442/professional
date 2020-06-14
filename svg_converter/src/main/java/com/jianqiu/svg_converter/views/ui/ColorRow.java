package com.jianqiu.svg_converter.views.ui;

import javafx.scene.control.CheckBox;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import lombok.Getter;

@Getter
public class ColorRow extends HBox {
    private Color color;
    private CheckBox cb = new CheckBox();
    private Rectangle colorRectangle;

    public ColorRow(Color color){
        this.color = color;
        colorRectangle = new Rectangle(0,0, 100, 20);
        colorRectangle.setFill(color);
        getChildren().addAll(colorRectangle, cb);
    }
}
