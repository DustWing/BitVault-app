package com.bitvault.util;

import com.bitvault.qr.BarcodeFormat;
import com.bitvault.qr.MultiFormatWriter;
import com.bitvault.qr.WriterException;
import com.bitvault.qr.MatrixToImageWriter;
import com.bitvault.qr.common.BitMatrix;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;


public class QrUtils {


    public static BufferedImage generateQRCode(String data, int h, int w) {
        try {
            BitMatrix matrix = new MultiFormatWriter()
                    .encode(
                            data,
                            BarcodeFormat.QR_CODE, w, h
                    );

            return MatrixToImageWriter.toBufferedImage(matrix);
        } catch (WriterException e) {
            throw new RuntimeException(e);
        }

    }

    public static Image createImage(BufferedImage bufferedImage) {
        try (final ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            ImageIO.write(bufferedImage, "png", output);

            try (InputStream is = new ByteArrayInputStream(output.toByteArray())) {
                return new Image(is);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
