package com.mark.captchademo.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public final class ImageUtil {
    private ImageUtil() {

    }

    public static byte[] bufferedImageToBytes(BufferedImage bImg, String imageType) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(bImg, "jpg", baos);
            return baos.toByteArray();
        }
    }
}
