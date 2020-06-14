package com.jianqiu.svg_converter.services.potraceservice;

import lombok.Getter;
import lombok.Setter;

/**
 * This class is the wrapper Cluster class used in the K-means algorithm
 */
@Setter
@Getter
public class Cluster implements Comparable<Cluster> {
    int id;
    int pixelCount;
    int red;
    int green;
    int blue;
    int reds;
    int greens;
    int blues;

    public Cluster(int id, int rgb) {
        int r = rgb>>16&0x000000FF;
        int g = rgb>> 8&0x000000FF;
        int b = rgb>> 0&0x000000FF;
        red = r;
        green = g;
        blue = b;
        this.id = id;
        addPixel(rgb);
    }

    public void clear() {
        red = 0;
        green = 0;
        blue = 0;
        reds = 0;
        greens = 0;
        blues = 0;
        pixelCount = 0;
    }

    public int getId() {
        return id;
    }

    int getRGB() {
        int r = reds / pixelCount;
        int g = greens / pixelCount;
        int b = blues / pixelCount;
        return 0xff000000|r<<16|g<<8|b;
    }


    void addPixel(int color) {
        int r = color>>16&0x000000FF;
        int g = color>> 8&0x000000FF;
        int b = color>> 0&0x000000FF;
        reds+=r;
        greens+=g;
        blues+=b;
        pixelCount++;
        red   = reds/pixelCount;
        green = greens/pixelCount;
        blue  = blues/pixelCount;
    }

    void removePixel(int color) {
        int r = color>>16&0x000000FF;
        int g = color>> 8&0x000000FF;
        int b = color>> 0&0x000000FF;
        reds-=r;
        greens-=g;
        blues-=b;
        pixelCount--;
        red   = reds/pixelCount;
        green = greens/pixelCount;
        blue  = blues/pixelCount;
    }

    int distance(int color) {
        int r = color>>16&0x000000FF;
        int g = color>> 8&0x000000FF;
        int b = color>> 0&0x000000FF;
        int a = (color >> 24) & 0xff;
        int rx = Math.abs(red-r);
        int gx = Math.abs(green-g);
        int bx = Math.abs(blue-b);
        int d = (rx+gx+bx) / 3;
        return d;
    }

    /**
     * This method is a debugging method that shows the color information of the cluster
     */
    public void displayRGB(){
        System.out.println(" red: "+getRed()+" green: "+getGreen()+" blue: "+getBlue());
    }

    @Override
    public int compareTo(Cluster o) {
        if(this.getRed()==o.getRed()&&this.getBlue()==o.getBlue()&&
        this.getGreen()==o.getGreen()){
            return 0;
        }
        else return 1;
    }

}
