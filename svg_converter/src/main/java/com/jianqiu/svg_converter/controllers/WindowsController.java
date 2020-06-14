package com.jianqiu.svg_converter.controllers;

import com.jianqiu.svg_converter.services.potraceservice.Cluster;
import com.jianqiu.svg_converter.services.potraceservice.PotraceService;
import com.jianqiu.svg_converter.services.potraceservice.compat.PathElement;
import com.jianqiu.svg_converter.services.potraceservice.potracej.Bitmap;
import com.jianqiu.svg_converter.services.potraceservice.potracej.param_t;
import com.jianqiu.svg_converter.views.*;
import com.jianqiu.svg_converter.views.PotraceView;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import lombok.Getter;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashSet;

@Getter
public class WindowsController {
    //WindowsController is a Singleton
    private static WindowsController instance;
    private MainView mainView;
    private PotraceService potraceService;
    private ColorSelectionView colorSelectionView;
    private PotraceView potraceView;

    //private constructor
    private WindowsController() {

    }

    /**
     * only access entry for getting the instance of this class
     *
     * @return {WindowsController} the singleton instance of this class
     */
    public static synchronized WindowsController getInstance() {
        if (instance == null) {
            instance = new WindowsController();
        }
        return instance;
    }

    /**
     * creates the Main window of the app from the stage
     *
     * @param stage {Stage} the host stage of MainView
     */
    public void createMainWindow(Stage stage) {
        mainView = new MainView(stage);
    }


    /**
     * instantiate potraceService and colorSelectionView
     * convert the javafx image to BufferedImage for Swing utilities
     *
     * @param image {JavaFX Image} the image from javafx framework
     */
    public void createColorSelectionWindow(Image image) {
        Image img = makeTransparent(image);
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(img, null);
        colorSelectionView = new ColorSelectionView(bufferedImage);
        potraceService = new PotraceService();
    }

    /**
     * This method gets rid of the background color of the original image
     * to make sure the clustering algorithm will not take background into account
     * Whatever is close to transparent, we will make it transparent
     * @param inputImage {Image} the original image
     * @return the result transparent image
     */
    private Image makeTransparent(Image inputImage) {
        int W = (int) inputImage.getWidth();
        int H = (int) inputImage.getHeight();
        final int TOLERANCE_THRESHOLD = 0xCF;
        WritableImage outputImage = new WritableImage(W, H);
        PixelReader reader = inputImage.getPixelReader();
        PixelWriter writer = outputImage.getPixelWriter();
        for (int y = 0; y < H; y++) {
            for (int x = 0; x < W; x++) {
                int argb = reader.getArgb(x, y);
                int r = (argb >> 16) & 0xFF;
                int g = (argb >> 8) & 0xFF;
                int b = argb & 0xFF;
                if (r >= TOLERANCE_THRESHOLD
                        && g >= TOLERANCE_THRESHOLD
                        && b >= TOLERANCE_THRESHOLD) {
                    argb &= 0x00FFFFFF;
                }

                writer.setArgb(x, y, argb);
            }
        }

        return outputImage;
    }



    /**
     * creates the View that connects to svg conversion algorithms
     *
     * @param bufferedImage
     */
    public void createSvgView(BufferedImage bufferedImage) {
        potraceView = new PotraceView(potraceService.getBufferedImageMap(bufferedImage));
    }

    /**
     * cluster the Image again for a new set of parameters that user has requested
     * @param colors {ArrayList<Color>} the list of newly selected colors
     * @param image {BufferedImage} the buffered Image that will be clustered on
     */
    public void reRenderBufferedImageFromColors(ArrayList<Color> colors, BufferedImage image) {
        BufferedImage resultImage = potraceService.getReclusteredImage(image, colors);
        colorSelectionView.reRenderRightPane(resultImage);
    }


    /**
     * Handles the call from the view for re clustering the Buffered Image
     *
     * @param NumberOfClusters {int} number of clusters requested by the user
     */
    public void clusterTheImage(BufferedImage bufferedImage, int NumberOfClusters) {
        HashSet<Cluster> clusterSet = potraceService.getClusters(bufferedImage, NumberOfClusters);
        colorSelectionView.renderWithClusters(clusterSet);
    }


    /**
     * get the path elements to draw the rendered svg in terms of graphics2D
     * @param param {param_t} the parameters for drawing the graphics
     * @param bmp {Bitmap} bit map used for drawing the graphics
     * @return the Paths that instruct the view on how to draw the lines, curves, etc
     */
    public ArrayList<PathElement> getPathElements(param_t param, Bitmap bmp){
        return potraceService.getPathElements(param, bmp);
    }
}
