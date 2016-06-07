package com.kozachenko.test;

import com.kozachenko.comparator.ImagesCompareUtils;

import javax.imageio.ImageIO;
import java.io.File;

/**
 * @author Andrew Kozachenko.
 */
public class Main {
    public static void main(String[] args) {

        try {
            File firstImage = new File("./resources/image1.png");
            File secondImage = new File("./resources/image2.png");
            ImagesCompareUtils utils = new ImagesCompareUtils();
            boolean compare = utils.compareImages(ImageIO.read(firstImage), ImageIO.read(secondImage));
            if(compare){
                System.out.println(" Image different");
            }


        } catch (Exception e) {
            System.out.println("OOops!");
            e.printStackTrace();
        }
    }
}
