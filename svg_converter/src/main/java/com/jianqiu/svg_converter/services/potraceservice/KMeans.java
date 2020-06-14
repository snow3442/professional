package com.jianqiu.svg_converter.services.potraceservice;

import lombok.Getter;

import java.awt.image.BufferedImage;
import java.util.Arrays;

@Getter
public class KMeans {
    private int[] lut;
    //default constructor
    public KMeans() {
    }

    /**
     * computes an array of clusters that might contain duplicates because the user
     * might have specified a large number of clusters
     *
     * @param image            {BufferedImage} the target BufferedImage to cluster
     * @param numberOfClusters {int} number of clusters specified by the user
     * @return {Cluster[]} An array of clusters with the specified number of clusters
     */

    public Cluster[] getRoughClusters(BufferedImage image,
                                      int numberOfClusters) {
        int w = image.getWidth();
        int h = image.getHeight();
        // create clusters
        Cluster[] clusters = createClusters(image, numberOfClusters);
        // create cluster lookup table
        lut = new int[w * h];
        Arrays.fill(lut, -1);
        // at first loop all pixels will move their clusters
        boolean pixelChangedCluster = true;
        // loop until all clusters are stable!
        int loops = 0;
        while (pixelChangedCluster) {
            pixelChangedCluster = false;
            loops++;
            for (int y = 0; y < h; y++) {
                for (int x = 0; x < w; x++) {
                    if (((image.getRGB(x, y) & 0xff000000) >> 24) == 0) {
                        //Do Nothing
                    } else {
                        int pixel = image.getRGB(x, y);
                        java.awt.Color color = new java.awt.Color(pixel);
                        Cluster cluster = findMinimalCluster(pixel, clusters);
                        if (lut[w * y + x] != cluster.getId()) {
                            // cluster changed

                            if (lut[w * y + x] != -1) {
                                // remove from possible previous
                                // cluster
                                clusters[lut[w * y + x]].removePixel(
                                        pixel);
                            }
                            // add pixel to cluster
                            cluster.addPixel(pixel);

                            // continue looping
                            pixelChangedCluster = true;

                            // update lut
                            lut[w * y + x] = cluster.getId();
                        }
                    }
                }
            }

        }
        return clusters;
    }

    /**
     * Creating initial clusters with the number of clusters specified
     *
     * @param image {BufferedImage} Buffered Image to cluster
     * @param k     {int} Number of Clusters for the clustering
     * @return
     */
    public Cluster[] createClusters(BufferedImage image, int k) {
        // Here the clusters are taken with specific steps,
        // so the result looks always same with same image.
        // You can randomize the cluster centers, if you like.
        Cluster[] result = new Cluster[k];
        int x = 0;
        int y = 0;
        int dx = image.getWidth() / k;
        int dy = image.getHeight() / k;
        for (int i = 0; i < k; i++) {
            result[i] = new Cluster(i, image.getRGB(x, y));
            x += dx;
            y += dy;
        }

        return result;
    }

    public Cluster findMinimalCluster(int rgb, Cluster[] clusters) {
        Cluster cluster = null;
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < clusters.length; i++) {
            int distance = clusters[i].distance(rgb);
            if (distance < min) {
                min = distance;
                cluster = clusters[i];
            }
        }
        return cluster;
    }


}
