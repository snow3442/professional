package com.jianqiu.svg_converter.views.ui;

import com.jianqiu.svg_converter.controllers.WindowsController;
import com.jianqiu.svg_converter.services.potraceservice.compat.PathElement;
import com.jianqiu.svg_converter.services.potraceservice.potracej.Bitmap;
import com.jianqiu.svg_converter.services.potraceservice.potracej.param_t;
import lombok.Getter;
import lombok.Setter;
import org.apache.batik.anim.dom.SVGDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

import javax.swing.*;
import javax.swing.plaf.metal.MetalButtonUI;
import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.ArrayList;

/**
 * This class serves as the UI for drawing BufferedImage into a graphics2d
 * by using the potraceJ library, the result drawing contains lines
 * curves and many different path elements that looks like the svg we
 * actually want at the end, except it is not yet converted to svg files
 * to convert to svg files, we will make use of the Apache Batik SVGGraphics2d
 * generator combined with DOMimplementation. For more details, read on
 * Apache Batik SVGGraphics2D and related classes
 */

@Getter
public class ResultJPanel {
    @Setter
    private int id;
    private BufferedImage result;
    private Bitmap bmp;
    private param_t param = new param_t();
    private double scale = 1;
    private ImageIcon resultIcon;
    private ImageIcon srcIcon;
    private boolean renderSourceImage = false;
    private Color color;
    private Graphics2D g2;
    private SVGGraphics2D svgGraphics2D;
    private String svgFileName;
    private Document document;

    public ResultJPanel(Color color){
        this.color = color;
    }

    /**
     * Create a single JPanel that holds the bufferedImage passed into the
     * method, this method will create a Jpanel with the initial SVG drawings
     * @param sourceImage {BufferedImage} the BufferedImage to be drawn
     * @return {JPanel} the UI JPanel
     */
    public JPanel initGUI(BufferedImage sourceImage){
        //Toolkit.getDefaultToolkit().
        WritableRaster raster = sourceImage.getRaster();
        int[] iarr = new int[4];
        bmp = new Bitmap((int)(sourceImage.getWidth()), (int)(sourceImage.getHeight()));
        for(int y=0; y<sourceImage.getHeight(); y++) {
            for(int x=0; x<sourceImage.getWidth(); x++) {
                int[] pixel = raster.getPixel(x, y, iarr);
                if (pixel[0] + pixel[1] + pixel[2] + pixel[3] != 0) {
                    bmp.put(x, y, 1);
                }
            }
        }
        BufferedImage d2 = new BufferedImage((int) (scale * sourceImage.getWidth()), (int)(scale * sourceImage.getHeight()), BufferedImage.TYPE_INT_ARGB);Graphics2D d2g = (Graphics2D) d2.getGraphics();
        d2g.scale(scale, scale);
        d2g.drawImage(sourceImage, 0, 0, null);
        d2g.dispose();
        sourceImage.flush();
        srcIcon = new ImageIcon(d2);
        doTrace(scale, color);
        JPanel panel = new JPanel() {
            {
                setLayout(new BorderLayout());
                resultIcon = new ImageIcon(result, "Result");
                JButton resultButton = new JButton(resultIcon);
                resultButton.setUI(new MetalButtonUI() {
                    @Override
                    protected void paintButtonPressed(Graphics g, AbstractButton b) {

                    }
                });
                add(resultButton, BorderLayout.CENTER);
                resultButton.setPressedIcon(srcIcon);
                JPanel stuff = new JPanel();
                add(stuff, BorderLayout.NORTH);
                stuff.setLayout(new GridLayout(4, 2));
                stuff.add(new JLabel("Suppress speckles"));
                final JSlider turdSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, param.turdsize);
                stuff.add(turdSlider);
                turdSlider.addChangeListener(e -> {
                    param.turdsize = turdSlider.getValue();
                    doRetrace();
                });
                stuff.add(new JLabel("Smooth corners"));
                final JSlider smoothSlider = new JSlider(JSlider.HORIZONTAL, 0, 300, (int) (param.opttolerance * 100));
                stuff.add(smoothSlider);
                smoothSlider.addChangeListener(e -> {
                    param.opttolerance = smoothSlider.getValue() / 100.0;
                    doRetrace();
                });
                stuff.add(new JLabel("Optimize paths"));
                final JSlider optSlider = new JSlider(JSlider.HORIZONTAL, 0, 125, (int) (param.alphamax * 100));
                stuff.add(optSlider);
                optSlider.addChangeListener(e -> {
                    param.alphamax = optSlider.getValue()/100.0;
                    doRetrace();
                });
                final JCheckBox renderSource = new JCheckBox("Render source");
                stuff.add(renderSource);
                renderSource.addActionListener(e -> {
                    renderSourceImage = renderSource.getModel().isArmed();
                    doRetrace();
                });

            }
            //call retrace again and repaint
            private void doRetrace() {
                doTrace(scale, color);
                resultIcon.setImage(result);
                repaint();
            }
        };
        panel.setVisible(true);
        panel.setPreferredSize(new Dimension(400, 400));
        return panel;
    }

    /**
     * trace out the graphics2D from the path elements obtained from the potraceService model
     * @param scale {double} the magnifier constant of the drawing
     * @param color {java.awt.Color} the original color from the image
     */
    private void doTrace(double scale, Color color) {
        if (result != null)
            result.flush();
        result = new BufferedImage((int)(scale * bmp.getWidth()), (int)(scale * bmp.getHeight()), BufferedImage.TYPE_INT_ARGB);
        g2 = (Graphics2D)result.getGraphics();
        ArrayList<PathElement> al = WindowsController.getInstance().getPathElements(param, bmp);
        paintG2(g2, scale, color, al);
        svgFileName= autoGenSvg(scale, color, al);
    }

    /**
     * this method accepts a Graphics2D object either be it Java Graphics2D or
     * SVGGraphics2D for svg files generation and paint it.
     * @param g2 {Graphics2D} the Graphics2D to paint on
     * @param scale {double} the magnifier variable to determine how large the paint would be
     * @param color {java.awt.color} the color used for the painting
     * @param al {ArrayList<PathElement>} the list of paths that instruct the program how to draw the figure
     */
    private void paintG2(Graphics2D g2, double scale, Color color, ArrayList<PathElement> al){
        g2.scale(scale, scale);
        //g2.setColor(Color.white);
        //g2.fillRect(0, 0, bmp.getWidth(), bmp.getHeight());
        g2.setColor(Color.BLACK);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        GeneralPath path = new GeneralPath();
        for (PathElement pathElement : al) {
            switch (pathElement.getType()) {
                case CLOSE_PATH:
                    path.closePath();
                    break;
                case LINE_TO:
                    path.lineTo(pathElement.getP0x(), pathElement.getP0y());
                    break;
                case MOVE_TO:
                    path.moveTo(pathElement.getP0x(), pathElement.getP0y());
                    break;
                case CURVE_TO:
                    path.curveTo(pathElement.getP0x(), pathElement.getP0y(), pathElement.getP1x(), pathElement.getP1y(), pathElement.getP2x(), pathElement.getP2y());
                    break;
            }
        }
        g2.setPaint(color);
        g2.fill(path);
    }

    /**
     * NOTE: For svgGraphics2D to properly generate svg files, one must
     * paint the svgGraphics2D once. please refer to documentations on
     * apache batik svgGraphics2D for more details
     * automatically prepare the output svg file
     * waiting for an action event to trigger save as individual files
     * or save as 1 merge files. Files will always be svgs
     * @param scale {double} scale passed from the paintG2 method
     * @param color {java.awt.Color} color passed from the paintG2 method
     * @param al {ArrayList<PathElement>} the list of paths that instructs the drawing
     * @return the svg file name and extension combined
     */
    public String autoGenSvg(double scale, Color color, ArrayList<PathElement> al){
        // Get a DOMImplementation.
        DOMImplementation impl = SVGDOMImplementation.getDOMImplementation();
        String svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;
        Document doc = impl.createDocument(svgNS, "svg", null);
        // Create an instance of the SVG Generator.
        svgGraphics2D = new SVGGraphics2D(doc);
        // Ask the test to render into the SVG Graphics2D implementation.
        paintG2(svgGraphics2D, scale, color, al);
        // Finally, stream out SVG to the standard output using
        // UTF-8 encoding.
        String svgFile = "output"+id+".svg";
        document = doc;
        return svgFile;
    }

}