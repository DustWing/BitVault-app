package com.bitvault.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class CompressImage {

   private static final String inputFilePath = "C:/Users/Andreas/Downloads/128moth.png";
    private static final String outputFilePath = "C:/Users/Andreas/Downloads/moth.ico";

    public static void main(String[] args) {
        try {
            toICO();
        }catch (Exception ex){
            ex.printStackTrace();
        }

        System.out.println("Finish");
    }

    public static void compress() throws IOException {
        int resolution = 32;

        // Read the original image from a file
        BufferedImage originalImage = ImageIO.read(new File(inputFilePath));

        // Create a new BufferedImage with a size of 32x32
        BufferedImage newImage = new BufferedImage(resolution, resolution, BufferedImage.TYPE_INT_ARGB);

        // Draw the original image onto the new image using Graphics2D
        Graphics2D g2d = newImage.createGraphics();
        g2d.drawImage(originalImage, 0, 0, resolution, resolution, null);
        g2d.dispose();

        // Write the new image to a file
        ImageIO.write(newImage, "png", new File(outputFilePath));
    }

    private static void toICO() throws IOException {
        BufferedImage image = ImageIO.read(new File(inputFilePath));

        // Write the new image to a file

        ImageIO.write(image, "ico", new File(outputFilePath));
    }

    private static BufferedImage imageToIco(BufferedImage image) {
        int[] iconSizes = {16, 24, 32, 48, 64, 128};
        BufferedImage result = new BufferedImage(256, 256, BufferedImage.TYPE_INT_ARGB);

        for (int iconSize : iconSizes) {
            BufferedImage resizedImage = resize(image, iconSize, iconSize);
            Graphics2D graphics = result.createGraphics();
            graphics.drawImage(resizedImage, (256 - iconSize) / 2, (256 - iconSize) / 2, null);
            graphics.dispose();
        }

        return result;
    }

    private static BufferedImage resize(BufferedImage image, int width, int height) {
        BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        resizedImage.createGraphics().drawImage(image, 0, 0, width, height, null);
        return resizedImage;
    }
}
