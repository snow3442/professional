package com.jianqiu.svg_converter.views;

import com.jianqiu.svg_converter.controllers.WindowsController;
import com.jianqiu.svg_converter.services.potraceservice.Cluster;
import com.jianqiu.svg_converter.views.ui.ColorBox;
import com.jianqiu.svg_converter.views.ui.ColorRow;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.embed.swing.SwingNode;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import lombok.Getter;
import javafx.scene.paint.Color;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * this view is only for color manipulation, not for svg rendering
 * allows user to choose which colors to suppress for the rendering
 */
@Getter
public class ColorSelectionView extends Stage {
    private ColorBox colorBox;
    private BufferedImage bufferedImage;
    private ImageIcon resultIcon;

    public ColorSelectionView(BufferedImage bufferedImage) {
        this.bufferedImage = bufferedImage;
        initGUI();
    }

    /**
     * initialize the graphical user interface
     */
    private void initGUI() {
        //create left pane for the bufferedImage
        SwingNode leftNode = new SwingNode();
        createLeftPane(leftNode);
        leftNode.setLayoutX(0);
        leftNode.setLayoutY(0);
        //end of left pane

        //create right pane for color manipulated bufferedImage
        SwingNode rightNode = new SwingNode();
        createRightPane(rightNode);
        rightNode.setLayoutX(500);
        rightNode.setLayoutY(0);
        //end of right node
        //color options
        VBox optionsBox = createOptionBox();
        //put them altogether
        BorderPane mainframe = new BorderPane();
        Pane pane = new Pane();
        pane.getChildren().addAll(leftNode, rightNode, optionsBox);
        mainframe.setCenter(pane);
        setTitle("Color Selection Service");
        setScene(new Scene(mainframe, 1200, 680));
        show();
    }

    /**
     * building the leftNode for containing the original BufferedImage
     *
     * @param swingNode {SwingNode} the Embeddable component into JavaFX
     */
    private void createLeftPane(SwingNode swingNode) {
        SwingUtilities.invokeLater(() -> {
            //Creating SwingNode Content
            JPanel newPanel = new JPanel(new BorderLayout());
            newPanel.setPreferredSize(new Dimension(500, 600));
            swingNode.setContent(newPanel);
            newPanel.add(new java.awt.Label("Original Image"), BorderLayout.NORTH);
            ImageIcon resultIcon = new ImageIcon(bufferedImage, "Result");
            JButton resultButton = new JButton(resultIcon);
            newPanel.add(resultButton, BorderLayout.CENTER);

        });
    }

    /**
     * building the rightPane for containing the color tuning Buffered Image
     *
     * @param swingNode {SwingNode} the Embeddable component into JavaFX
     */
    private void createRightPane(SwingNode swingNode) {
        SwingUtilities.invokeLater(() -> {
            //Creating SwingNode Content
            JPanel newPanel = new JPanel(new BorderLayout());
            newPanel.setPreferredSize(new Dimension(500, 600));
            swingNode.setContent(newPanel);
            newPanel.add(new java.awt.Label("Preview"), BorderLayout.NORTH);
            resultIcon = new ImageIcon(bufferedImage, "Result");
            JButton resultButton = new JButton(resultIcon);
            newPanel.add(resultButton, BorderLayout.CENTER);
        });
    }

    /**
     * created a VBox to contain all the options
     */
    private VBox createOptionBox() {
        //cluster pane
        VBox optionsBox = new VBox(10);
        optionsBox.setPrefSize(200, 600);
        optionsBox.setLayoutX(1000);
        optionsBox.setLayoutY(0);
        Label lblCluster = new Label("Cluster Options");
        Label lblNum = new Label("Number Of Clusters");
        TextField tfNum = new TextField();
        Button btReCluster = new Button("Cluster");
        //wireUp the cluster service
        btReCluster.setOnMouseClicked(e -> {
            WindowsController.getInstance().clusterTheImage(bufferedImage, Integer.parseInt(tfNum.getText()));

        });
        //end of cluster pane
        Label lbl = new Label("Click on checkboxes to drop " +
                "\nthe colors you don't want");
        //Color Pane
        ScrollPane sp = new ScrollPane();
        sp.setPrefSize(200, 250);
        sp.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        sp.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        colorBox = new ColorBox();
        sp.setContent(colorBox);

        //end of Color Pane
        Button btReRender = new Button("Preview");
        //re-render the right side buffered image according to selection set
        btReRender.setOnMouseClicked(e -> {
            ArrayList<Color> pickedColors = new ArrayList<>();
            for (ColorRow row : colorBox.getColorRows()) {
                if (!row.getCb().isSelected()) {
                    pickedColors.add(row.getColor());
                }
            }
            WindowsController.getInstance().reRenderBufferedImageFromColors(pickedColors, bufferedImage);

        });
        Button btSVG = new Button("Convert to SVG");
        //wireUp Conversion Service
        btSVG.setOnMouseClicked(e -> {
            WindowsController.getInstance().createSvgView(bufferedImage);
        });
        //Putting them altogether
        optionsBox.getChildren().addAll(lblCluster, lblNum, tfNum, btReCluster, lbl, sp,
                btReRender, btSVG);
        return optionsBox;

    }

    /**
     * render the UI with the hashSet of clusters that the model has provided
     *
     * @param clusterSet {HashSet<Cluster>} the cluster Set that was passed by the model
     */
    public void renderWithClusters(HashSet<Cluster> clusterSet) {
        colorBox.reloadColors(clusterSet);
    }

    /**
     * Put the new bufferImage to the right pane
     *
     * @param bufferedImage {BufferedImage} The New Buffered Image
     */
    public void reRenderRightPane(BufferedImage bufferedImage) {
        resultIcon.setImage(bufferedImage);
    }

}
