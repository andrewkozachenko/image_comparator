package com.kozachenko.comparator;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;

/**
 * @author Andrew Kozachenko.
 */
public class ImagesCompareUtils {

    private ArrayList<ImagePoint> differentImagePixel = new ArrayList<ImagePoint>();
    private LinkedList<ArrayList<ImagePoint>> blocksByXY = new LinkedList<>();
    private static ArrayList<Integer> listPixelRanges = new ArrayList<>();


    public boolean isPointsDifferent(int firstImagePoint, int secondImagePoint, int x, int y) {
        boolean isDifferent = false;

        if (firstImagePoint != secondImagePoint) {
            Color firstPoint = new Color(firstImagePoint);
            Color secondPoint = new Color(secondImagePoint);

            int diffR = Math.abs(firstPoint.getRed() - secondPoint.getRed());
            int diffG = Math.abs(firstPoint.getGreen() - secondPoint.getGreen());
            int diffB = Math.abs(firstPoint.getBlue() - secondPoint.getBlue());

            double sumDiffs = diffR + diffG + diffB;

            int percentage = (int) Math.round(((sumDiffs / 255) / 3) * 100.0);

            if (percentage > 10) {
                isDifferent = true;
                differentImagePixel.add(new ImagePoint(x, y, secondImagePoint));
            }
        }


        return isDifferent;
    }

    public boolean compareImages(BufferedImage firstImage, BufferedImage secondImage) {
        long start = 0l;
        long end = 0l;
        boolean flag = false;
        if (firstImage.getHeight() == secondImage.getHeight()) {
            int imageHeigh = firstImage.getHeight();
            if (firstImage.getWidth() == secondImage.getWidth()) {
                int imageWidth = firstImage.getWidth();
                start = System.currentTimeMillis();
                System.out.println(" Time start = " + start);
                for (int y = 0; y < imageHeigh; y++) {
                    for (int x = 0; x < imageWidth; x++) {
                        int firstImageRGB = firstImage.getRGB(x, y);
                        int secondImageRGB = secondImage.getRGB(x, y);
                        if (isPointsDifferent(firstImageRGB, secondImageRGB, x, y)) {
                            if (!flag) {
                                flag = true;
                            }
                        }
                    }
                }
            } else {
                return flag;
            }
        }
        end = System.currentTimeMillis();
        System.out.println("Time end = " + end);
        long duration = end - start;
        System.out.println("Duration " + duration);
        if (flag) {
            drawDifference(secondImage);
        }
        return flag;
    }

    public boolean drawDifference(BufferedImage imageSource) {
        boolean flag = false;

        try {
            File newImage = new File("./resources/imageCompare.png");
            if (!newImage.exists()) {
                newImage.createNewFile();
            }
            generateBlocksOfDifference(imageSource);
            for (ArrayList<ImagePoint> list : getBlocksByXY()) {
                Graphics2D graph = imageSource.createGraphics();
                graph.setColor(Color.RED);
                graph.drawRect(getRectangle(list).x, getRectangle(list).y, getRectangle(list).width, getRectangle(list).height);
                graph.dispose();
            }
            if (!ImageIO.write(imageSource, "PNG", newImage)) {
                //throw  new RuntimeException("Unexpected error writing image");
                return false;
            }
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }


        return flag;
    }

    private Rectangle getRectangle(ArrayList<ImagePoint> list) {
        Rectangle rectangle = new Rectangle();

        int x = list.get(0).getImageX() - 2;
        int listLength = list.size();
        int width = (list.get(listLength - 1).getImageX() - list.get(0).getImageX()) + 4;

        list = sortListOfImagePointByCondition(list, true);

        int y = list.get(0).getImageY() - 2;
        int height = (list.get(listLength - 1).getImageY() - list.get(0).getImageY()) + 4;

        rectangle.setBounds(x, y, width, height);
        return rectangle;

    }


    public void generateBlocksOfDifference(BufferedImage image) {
        int deltaPix;
        if (image.getHeight() > image.getWidth()) {
            deltaPix = (int) Math.round(image.getWidth() / 100.0);
        } else {
            deltaPix = (int) Math.round(image.getHeight() / 100.0);
        }
        LinkedList<ArrayList<ImagePoint>> listByY = getBlocks(deltaPix, getDifferentImagePixel(), false);
        LinkedList<ArrayList<ImagePoint>> listByXandY = new LinkedList<>();
        for (ArrayList list : listByY) {
            list = sortListOfImagePointByCondition(list,false);
            listByXandY.addAll(getBlocks(deltaPix, list, true));
        }

        setBlocksByXY(listByXandY);

    }

    private ArrayList<ImagePoint> sortListOfImagePointByCondition(ArrayList<ImagePoint> list, boolean yCondition) {
        if (!yCondition) {
            Collections.sort(list, new Comparator<ImagePoint>() {
                @Override
                public int compare(ImagePoint imagePoint1, ImagePoint imagePoint2) {

                    return imagePoint1.compareTo(imagePoint2);
                }
            });
            return list;
        }
        Collections.sort(list, new Comparator<ImagePoint>() {
            @Override
            public int compare(ImagePoint imagePoint1, ImagePoint imagePoint2) {
                Integer imageY1 = imagePoint1.getImageY();
                Integer imageY2 = imagePoint2.getImageY();
                return imageY1.compareTo(imageY2);
            }
        });
        return list;


    }

    private LinkedList<ArrayList<ImagePoint>> getBlocks(int deltaPix, ArrayList<ImagePoint> listOfPoint, boolean dimension) {
        LinkedList<ArrayList<ImagePoint>> blocks = new LinkedList<>();
        int listLength = listOfPoint.size();
        for (int i = 0; i < listLength; i++) {
            int pixelRange = 0;
            if (i > 0) {
                if (!dimension) {
                    pixelRange = listOfPoint.get(i).getImageY() - listOfPoint.get(i - 1).getImageY();
                } else {
                    pixelRange = listOfPoint.get(i).getImageX() - listOfPoint.get(i - 1).getImageX();
                }
            }
            if (deltaPix >= pixelRange) {
                if (blocks.size() == 0) {
                    ArrayList list = new ArrayList();
                    blocks.add(list);
                }
                blocks.getLast().add(listOfPoint.get(i));
            } else {
                blocks.add(new ArrayList<ImagePoint>());
                blocks.getLast().add(listOfPoint.get(i));
            }
        }
        return blocks;
    }

    public LinkedList<ArrayList<ImagePoint>> getBlocksByXY() {
        return blocksByXY;
    }

    public void setBlocksByXY(LinkedList<ArrayList<ImagePoint>> blocks) {
        this.blocksByXY = blocks;
    }

    public ArrayList<ImagePoint> getDifferentImagePixel() {
        return differentImagePixel;
    }

    public void setDifferentImagePixel(ArrayList<ImagePoint> differentImagePixel) {
        this.differentImagePixel = differentImagePixel;
    }

    public static ArrayList<Integer> getListPixelRanges() {
        return listPixelRanges;
    }

    public static void setListPixelRanges(ArrayList<Integer> listPixelRanges) {
        ImagesCompareUtils.listPixelRanges = listPixelRanges;
    }
}
