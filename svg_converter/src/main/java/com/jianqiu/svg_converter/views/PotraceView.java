package com.jianqiu.svg_converter.views;

import com.jianqiu.svg_converter.services.potraceservice.Cluster;
import com.jianqiu.svg_converter.views.ui.ResultJPanel;
import lombok.Getter;
import org.apache.batik.anim.dom.SAXSVGDocumentFactory;
import org.apache.batik.dom.util.DOMUtilities;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * this is the view for render svgs as well as saving svg files
 * it is independent of color selection view and should always
 * stay that way
 */
@Getter
public class PotraceView {
    public static int startID = 0;
    private HashMap<Cluster, BufferedImage> map;
    private ArrayList<ResultJPanel> resultJPanels = new ArrayList<>();
    private ArrayList<String> files = new ArrayList<>();

    //constructor
    public PotraceView(HashMap<Cluster, BufferedImage> map) {
        this.map = map;
        initGUI();
    }

    /**
     * initialize the graphical user interface
     */
    public void initGUI() {
        JFrame frame = new JFrame("SVG Conversion result");
        JMenu menu;
        JMenuItem iMergeSave;
        JMenuBar mb = new JMenuBar();
        menu = new JMenu("File");
        iMergeSave = new JMenuItem("Save as SVG");
        menu.add(iMergeSave);
        mb.add(menu);
        frame.setJMenuBar(mb);
        frame.setPreferredSize(new Dimension(1200, 680));
        frame.setLayout(new GridLayout(2, 3));
        //clear previous rendering results
        resultJPanels.clear();
        for (Map.Entry me : map.entrySet()) {
            Cluster c = (Cluster) me.getKey();
            ResultJPanel rj = new ResultJPanel(getColorFromCluster(c));
            rj.setId(++startID);
            resultJPanels.add(rj);
            frame.add(rj.initGUI((BufferedImage) me.getValue()));
        }

        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        //enable saving as 1 svg file util
        iMergeSave.addActionListener(e -> {
            //Save the individual files
            for (ResultJPanel rj : resultJPanels) {
                try (OutputStream outputStream = new FileOutputStream(rj.getSvgFileName());
                     ){
                    Writer outputStreamWriter = new OutputStreamWriter(outputStream);
                    rj.getSvgGraphics2D().stream(outputStreamWriter, false);
                } catch (FileNotFoundException fileNotFoundException) {
                    fileNotFoundException.printStackTrace();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }

            // combine all files together as 1
            for (ResultJPanel rj : resultJPanels) {
                //add all individual svgs file names to list
                files.add(rj.getSvgFileName());
            }

            try {
                String parser = XMLResourceDescriptor.getXMLParserClassName();
                SAXSVGDocumentFactory sax = new SAXSVGDocumentFactory(parser);
                //create the document from the first svg file
                Document doc = sax.createSVGDocument(files.get(0));
                for (int i = 1; i < files.size(); i++) {
                    //create the next document
                    Document docToAppend = sax.createSVGDocument(files.get(i));
                    //fetch the document element
                    Element elem = docToAppend.getDocumentElement();
                    elem.setNodeValue("svg");
                    Element elemToAppend = (Element) doc.importNode(elem, true);
                    doc.getDocumentElement().appendChild(elemToAppend);
                }
                //saving the file to the current directory
                JFileChooser chooser = new JFileChooser();
                int retrival = chooser.showSaveDialog(null);
                if (retrival == JFileChooser.APPROVE_OPTION) {
                    try (FileOutputStream outStream = new FileOutputStream(chooser.getSelectedFile().getAbsolutePath()+".svg");
                         Writer outf = new OutputStreamWriter(outStream, "UTF-8");){
                        DOMUtilities.writeDocument(doc, outf);
                        outf.flush();
                    } catch (Exception ex) {
                        System.err.println("JFileChooser Error");
                        ex.printStackTrace();
                    }
                }

            } catch (IOException ex) {
                System.err.println("Error Saving Files");
            }
            //delete the individual svgs components
            //disable the following try catch if you want to keep
            //individual color svgs
            try{
                for(String s: files){
                    File f = new File(s);
                    if(f.delete()){
                        System.out.println("1 svg deleted!");
                    }
                    else{
                        System.out.println("error: deletion failed! Make sure resources are closed!");
                    }
                }
            } catch(Exception ex){
                System.out.println("Error Deleting individual files");
            }
        });
    }

    /**
     * get java.awt.color from a cluster
     *
     * @param c {Cluster} the cluster to get color from
     * @return {java.awt.color} the color built from the cluster
     */
    private Color getColorFromCluster(Cluster c) {
        Color color = new Color(c.getRed(), c.getGreen(), c.getBlue());
        return color;
    }


}
