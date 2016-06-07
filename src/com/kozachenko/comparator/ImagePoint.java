package com.kozachenko.comparator;

import java.awt.*;

/**
 * @author Andrew Kozachenko.
 */
public class ImagePoint implements Comparable {
    private int imageX;
    private int imageY;
    private Color pointCollor;
    private int imageRGB;


    public ImagePoint(int imageX, int imageY) {
        this.imageX = imageX;
        this.imageY = imageY;
    }



    public ImagePoint(int imageX, int imageY, int imageRGB) {
        this.imageX = imageX;
        this.imageY = imageY;
        this.imageRGB = imageRGB;
    }

    public int getImageX() {
        return imageX;
    }

    public void setImageX(int imageX) {
        this.imageX = imageX;
    }

    public int getImageY() {
        return imageY;
    }

    public void setImageY(int imageY) {
        this.imageY = imageY;
    }

    public int getImageRGB() {
        return imageRGB;
    }

    public void setImageRGB(int imageRGB) {
        this.imageRGB = imageRGB;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj)
            return true;
        if(obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ImagePoint other = (ImagePoint) obj;
        if(imageX != other.getImageX())
            return false;
        if(imageY != other.getImageY())
            return false;
        if(imageRGB != other.getImageRGB())
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + imageX;
        result = prime * result + imageY;
        return result;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(this.getClass().getName())
                .append(" [ " + "imageX = ")
                .append(imageX)
                .append(", ")
                .append("imageY = ")
                .append(imageY)
                .append(" ] ")
                .append("\n");
        return buffer.toString();
    }

    @Override
    public int compareTo(Object o) {
        ImagePoint entry = (ImagePoint) o;
        Integer compByX = (Integer) getImageX();
        Integer entryX = (Integer) entry.getImageX();
        int result = compByX.compareTo(entryX);
        if(result != 0){
            return result;
        }
        return 0;
    }
}
