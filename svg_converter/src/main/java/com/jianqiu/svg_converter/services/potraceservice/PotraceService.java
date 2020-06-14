package com.jianqiu.svg_converter.services.potraceservice;

import com.jianqiu.svg_converter.services.potraceservice.compat.ConvertToJavaCurves;
import com.jianqiu.svg_converter.services.potraceservice.compat.PathElement;
import com.jianqiu.svg_converter.services.potraceservice.potracej.Bitmap;
import com.jianqiu.svg_converter.services.potraceservice.potracej.PoTraceJ;
import com.jianqiu.svg_converter.services.potraceservice.potracej.param_t;
import com.jianqiu.svg_converter.services.potraceservice.potracej.path_t;
import javafx.scene.paint.Color;
import lombok.Getter;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@Getter
public class PotraceService {
    private int[] lut;
    private Cluster[] clusters;
    private HashSet<Cluster> refinedClusters = new HashSet<>();
    private HashSet<Cluster> filteredClusters;
    private KMeans kmeans = new KMeans();
    public static final int THRESHOLD = 10; //color distinguishable threshold constant

    //default constructor
    public PotraceService() {
    }

    /**
     * Suppress all other colors that are not in the list of colors picked passed
     * into this method and get a BufferedImage with the a list of colors picked
     * from the original buffered image
     *
     * @param bufferedImage {BufferedImage} The original buffered image to filter
     * @param colorsPicked  {ArrayList<Color>} The list of picked colors
     * @return {BufferedImage} The resulting, refined buffered image
     */
    public BufferedImage getReclusteredImage(BufferedImage bufferedImage, ArrayList<Color> colorsPicked) {
        //setting number of threads to number of processors to optimize performance
        int numOfThreads = Runtime.getRuntime().availableProcessors();
        List<Thread> threads = new ArrayList<>();
        int w = bufferedImage.getWidth();
        int h = bufferedImage.getHeight() / numOfThreads;
        // create result image
        BufferedImage result = new BufferedImage(w, bufferedImage.getHeight(),
                BufferedImage.TYPE_INT_ARGB);
        filteredClusters = getClustersFromColors(refinedClusters, colorsPicked);
        for (int i = 0; i < numOfThreads; i++) {
            final int threadMultiplier = i;
            Thread thread = new Thread(() -> {
                int xOrigin = 0;
                int yOrigin = h * threadMultiplier;
                //subtasks
                recolorImage(w, h, result, xOrigin, yOrigin, filteredClusters);
            });

            threads.add(thread);
        }
        //fire all threads at the same time
        for (Thread thread : threads) {
            thread.start();
        }
        //let all threads terminate their jobs for the program to proceed
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
            }
        }

        return result;
    }

    /**
     * recolors a sub part of a buffered image
     *
     * @param width            {int} width of the image part to recolor
     * @param height           {int} height of the image part to recolor
     * @param result           {BufferedImage} the buffered image to recolor
     * @param leftCorner       {int} the startX of the recoloring
     * @param topCorner        {int} the startY of the recoloring
     * @param filteredClusters {Cluster[]} the clusters date for recoloring
     */
    private void recolorImage(int width, int height, BufferedImage result, int leftCorner, int topCorner,
                              HashSet<Cluster> filteredClusters) {
        for (int y = topCorner; y < topCorner + height; y++) {
            for (int x = leftCorner; x < leftCorner + width; x++) {
                int clusterId = lut[width * y + x];
                if (idExists(clusterId, filteredClusters)) {
                    result.setRGB(x, y, getClusterById(clusterId, filteredClusters).getRGB());
                } else {
                    //paint it transparent
                    result.setRGB(x, y, 0x00000000);
                }
            }
        }
    }

    /**
     * Check if the specified id still exists in the filtered cluster
     *
     * @param id       {int} id of search
     * @param clusters {HashSet<Cluster>} the cluster set to search in
     * @return {boolean} true if ID exists, false otherwise
     */
    private boolean idExists(int id, HashSet<Cluster> clusters) {
        for (Cluster c : clusters) {
            if (c.getId() == id) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get the cluster in an array of cluster by a specified search ID
     *
     * @param id       {int} id of search
     * @param clusters {HashSet<Cluster>} the cluster set to search in
     * @return {Cluster} the cluster that needs to be found
     */
    private Cluster getClusterById(int id, HashSet<Cluster> clusters) {
        for (Cluster c : clusters) {
            if (c.getId() == id) {
                return c;
            }
        }
        return null;
    }

    /**
     * get the cluster array that matches user selected colors for rendering
     *
     * @param clusters {HashSet<Cluster>} cluster set to filter from
     * @param colors   {ArrayList<Color>} List of colors picked by the user
     * @return
     */
    private HashSet<Cluster> getClustersFromColors(HashSet<Cluster> clusters, ArrayList<Color> colors) {
        int i = -1;
        HashSet<Cluster> result = new HashSet<>();
        for (Cluster c : clusters) {
            for (Color color : colors) {
                if ((int) (color.getRed() * 255) == c.getRed()
                        && (int) (color.getGreen() * 255) == c.getGreen()
                        && (int) (color.getBlue() * 255) == c.getBlue()) {
                    result.add(c);
                }

            }
        }
        return result;
    }

    /**
     * Removes duplicate and very similar color clusters
     * and return it as a HashSet
     *
     * @param img           {BufferedImage} BufferedImage to extract color clusters from
     * @param numOfClusters {int} number
     * @return {HashSet<Cluster>} A refined HashSet of unique clusters
     */
    public HashSet<Cluster> getClusters(BufferedImage img, int numOfClusters) {
        //computes the clusters using Kmeans algorithm
        clusters = kmeans.getRoughClusters(img, numOfClusters);
        //register the new matrix of clusterInfo
        lut = kmeans.getLut();
        //creates a new HashSet to collect all good clusters
        HashSet<Cluster> clusterSet = new HashSet<>();
        for (Cluster c : clusters) {
            //if no similar cluster found in the Set, add it to the set
            if (!setContainsSimilarCluster(clusterSet, c)) {
                clusterSet.add(c);
            }
        }
        refinedClusters = clusterSet;
        filteredClusters = clusterSet;
        return clusterSet;
    }

    /**
     * check if the HashSet contains a very similar color cluster to the passed one
     *
     * @param clusterSet {HashSet<Cluster>} HashSet to loop through to make comparisons
     * @param cluster    {Cluster} the cluster to check with
     * @return {boolean} returns true if there exists a very similar color cluster in the Set,
     * false otherwise
     */
    private boolean setContainsSimilarCluster(HashSet<Cluster> clusterSet, Cluster cluster) {
        for (Cluster c : clusterSet) {
            if (twoClustersApproximatelySame(c, cluster)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Compares between 2 clusters and see if they are approximately the same
     * current distance threshold is set at 10
     *
     * @param c1 {Cluster} the first cluster in the comparison
     * @param c2 {Cluster} the second cluster in the comparison
     * @return {boolean} returns true if two clusters are approximately the same,
     * false otherwise.
     */
    private boolean twoClustersApproximatelySame(Cluster c1, Cluster c2) {
        //compute the 3d distance of RGB between two clusters
        int r1 = c1.getRed();
        int g1 = c1.getGreen();
        int b1 = c1.getBlue();
        int r2 = c2.getRed();
        int g2 = c2.getGreen();
        int b2 = c2.getBlue();
        double dist = Math.sqrt(Math.pow((r1 - r2), 2) + Math.pow((g1 - g2), 2) + Math.pow((b1 - b2), 2));
        return (dist < THRESHOLD);
    }

    /**
     * IMPORTANT METHOD:
     * this method gets a hashmap of <Color, BufferedImage> where each
     * buffered image has 1 color, and the key matching the buffered image
     * is the color in the image. This map will be passed to view for
     * the grid view display
     *
     * @param bufferedImage {BufferedImage} original buffered image
     * @return {HashMap<Cluster, BufferedImage>} the map that contains all color, bufferedimage pair information
     */
    public HashMap<Cluster, BufferedImage> getBufferedImageMap(BufferedImage bufferedImage) {
        HashMap<Cluster, BufferedImage> resultMap = new HashMap<>();
        for (Cluster c : filteredClusters) {
            BufferedImage value = getMultithreadedSingleRecolor(bufferedImage, c);

            resultMap.put(c, value);
        }
        return resultMap;
    }

    /**
     * A multithreaded method to recolor an entire buffered image with 1 color cluster
     *
     * @param bufferedImage {BufferedImage} the BufferedImage to recolor
     * @param cluster       {Cluster} the specified the cluster that contains the color that we want
     * @return {BufferedImage} the result buffered image that only has 1 color
     */
    private BufferedImage getMultithreadedSingleRecolor(BufferedImage bufferedImage, Cluster cluster) {
        int numOfThreads = Runtime.getRuntime().availableProcessors();
        List<Thread> threads = new ArrayList<>();
        int w = bufferedImage.getWidth();
        int h = bufferedImage.getHeight() / numOfThreads;
        // create result image
        BufferedImage result = new BufferedImage(w, bufferedImage.getHeight(),
                BufferedImage.TYPE_INT_ARGB);
        for (int i = 0; i < numOfThreads; i++) {
            final int threadMultiplier = i;
            Thread thread = new Thread(() -> {
                int xOrigin = 0;
                int yOrigin = h * threadMultiplier;
                //subtasks
                recolorSinglePart(w, h, result, xOrigin, yOrigin, cluster);
            });

            threads.add(thread);
        }
        //fire all threads at the same time
        for (Thread thread : threads) {
            thread.start();
        }
        //let all threads terminate their jobs for the program to proceed
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
            }
        }
        return result;
    }

    /**
     * Recolor a part of the image with a specified color cluster
     *
     * @param width      {int} the width of the recoloring
     * @param height     {int} the height of the recoloring
     * @param result     {BufferedImage} the buffered image to recolor on and to return
     * @param leftCorner {int} the start X position of the recoloring
     * @param topCorner  {int} the start Y position of the recoloring
     * @param cluster    {Cluster} the specified cluster that contains the color we want
     */
    private void recolorSinglePart(int width, int height, BufferedImage result, int leftCorner, int topCorner,
                                   Cluster cluster) {
        for (int y = topCorner; y < topCorner + height; y++) {
            for (int x = leftCorner; x < leftCorner + width; x++) {
                int clusterId = lut[width * y + x];
                if (cluster.getId() == clusterId) {
                    result.setRGB(x, y, cluster.getRGB());
                } else {
                    result.setRGB(x, y, 0x00000000);
                }

            }
        }
    }

    public ArrayList<PathElement> getPathElements(param_t param, Bitmap bmp) {
        PoTraceJ poTraceJ = new PoTraceJ(param);
        long l = System.currentTimeMillis();
        path_t trace = null;
        for (int i = 0; i < 10; i++) {
            trace = poTraceJ.trace(bmp); //program can stuck here
            Thread.yield();
        }
        poTraceJ.resetTimers();
        for (int i = 0; i < 100; i++) {
            trace = poTraceJ.trace(bmp);
        }
        poTraceJ.printTimers();
        l = System.currentTimeMillis() - l;
        System.out.println("L="+l);
        ArrayList<PathElement> al = new ArrayList<>();
        ConvertToJavaCurves.convert(trace, new HashSet<>(), al);
        return al;
    }


}