package com.bitvault.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class CompressImage {

    public static void main(String[] args) throws Exception {

        int resolution= 32;

        // Read the original image from a file
        BufferedImage originalImage = ImageIO.read(new File("C:/Users/Andreas/Downloads/FF2.png"));

        // Create a new BufferedImage with a size of 32x32
        BufferedImage newImage = new BufferedImage(resolution, resolution, BufferedImage.TYPE_INT_ARGB);

        // Draw the original image onto the new image using Graphics2D
        Graphics2D g2d = newImage.createGraphics();
        g2d.drawImage(originalImage, 0, 0, resolution, resolution, null);
        g2d.dispose();

        // Write the new image to a file
        ImageIO.write(newImage, "png", new File("C:/Users/Andreas/Downloads/32moth.png"));
    }
}
