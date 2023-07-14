package messager.util;

import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageTest {

    @Test
    void test() throws IOException {
        BufferedImage img = ImageIO.read(new File("test_resources/test.png"));

        String encodedImage = ImageUtils.encodeImage(img, "png");
        ImageUtils.decodeImage(encodedImage);
    }

    @Test
    void testCropAtCenter() throws IOException {
        BufferedImage img = ImageIO.read(new File("test_resources/test.png"));
        BufferedImage cropImage = ImageUtils.cropImageAtCenter(img);
        ImageIO.write(cropImage, "png", new File("test_resources/test cropped.png"));
    }

    @Test
    void testScale() throws IOException {
        BufferedImage img = ImageIO.read(new File("test_resources/test.png"));
        int width = 48;
        int height = 48;
        BufferedImage bufferedImage = ImageUtils.scaleImage(img, width, height);
        ImageIO.write(bufferedImage, "png", new File("test_resources/test scaled.png"));
    }

}
