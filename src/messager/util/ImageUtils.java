package messager.util;

import lombok.SneakyThrows;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

public class ImageUtils {

    @SneakyThrows
    public static String encodeImage(BufferedImage image, String format) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, format, baos);
        baos.flush();
        String encodedImage = Base64.getEncoder().encodeToString(baos.toByteArray());
        baos.close();
        return encodedImage;
    }

    public static BufferedImage decodeImage(String encodedImage) {
        byte[] bytes = Base64.getDecoder().decode(encodedImage);
        try {
            return ImageIO.read(new ByteArrayInputStream(bytes));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static BufferedImage cropImageAtCenter(BufferedImage img) {
        int size = Math.min(img.getWidth(), img.getHeight());
        int x1 = img.getWidth() / 2 - size / 2;
        int y1 = img.getHeight() / 2 - size / 2;
        int x2 = img.getWidth() / 2 + size / 2;
        int y2 = img.getHeight() / 2 + size / 2;
        return cropImage(img, x1, y1, x2, y2);
    }

    public static BufferedImage cropImage(java.awt.Image img, int x1, int y1, int x2, int y2) {
        BufferedImage cropImage = new BufferedImage(x2 - x1, y2 - y1, BufferedImage.TYPE_INT_RGB);
        cropImage.getGraphics().drawImage(img, 0, 0, x2 - x1, y2 - y1, x1, y1, x2, y2, null);
        return cropImage;
    }

    public static BufferedImage scaleImage(BufferedImage img, int width, int height) {
        java.awt.Image scaledImg = img.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        bufferedImage.getGraphics().drawImage(scaledImg, 0, 0, null);
        return bufferedImage;
    }

}
