package kz.mouzitoto.imagehandler.services;

import kz.mouzitoto.imagehandler.exceptions.IHBadInputException;
import kz.mouzitoto.imagehandler.exceptions.IHIOException;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.core.io.ClassPathResource;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Method name convention for this class: methodThatWeAreTesting_passedArgs_expectedResult
 */

public class HandleImageTest {

    private ImageService imageService = new ImageService();

    private static final String IMAGE_PATH_HTTP_JPG_GOOD = "http://fatcatart.com/wp-content/uploads/2016/09/DSC_0025-w.jpg";
    private static final String IMAGE_PATH_HTTPS_PNG_GOOD = "https://notepad-plus-plus.org/assets/img/logo-green-orange.png";
    private static final String IMAGE_PATH_HTTP_BAD = "http://fatcatart.com/wp-content/uploads/2016/09/DSC_0025-w222222.jpg";
    private static final String IMAGE_PATH_OS_BMP_GOOD = "/1.bmp";
    private static final String IMAGE_WIDTH = "500";
    private static final String IMAGE_HEIGHT = "350";

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void handleImage_allGoodHttpJpg_success() throws IHIOException, IOException, IHBadInputException {
        String newImageFileName = imageService.handleImage(new String[]{IMAGE_PATH_HTTP_JPG_GOOD, IMAGE_WIDTH, IMAGE_HEIGHT});

        checkHandleImageForSuccess(newImageFileName);
    }

    @Test
    public void handleImage_badHttp_IHIOException() throws IHIOException, IHBadInputException {
        exceptionRule.expect(IHBadInputException.class);
        exceptionRule.expectMessage("Bad image path/url");

        imageService.handleImage(new String[]{IMAGE_PATH_HTTP_BAD, IMAGE_WIDTH, IMAGE_HEIGHT});
    }


    @Test(expected = IHIOException.class)
    public void handleImage_goodHttpsPng_IHIOException() throws IHIOException, IHBadInputException {
        imageService.handleImage(new String[]{IMAGE_PATH_HTTPS_PNG_GOOD, IMAGE_WIDTH, IMAGE_HEIGHT});
    }

    @Test
    public void handleImage_goodOSBmp_success() throws IHIOException, IHBadInputException, IOException {
        ClassPathResource resource = new ClassPathResource(IMAGE_PATH_OS_BMP_GOOD);
        File file = resource.getFile();

        String newImageFileName = imageService.handleImage(new String[]{file.getAbsolutePath(), IMAGE_WIDTH, IMAGE_HEIGHT});

        checkHandleImageForSuccess(newImageFileName);
    }

    private void checkHandleImageForSuccess(String newImageFileName) throws IOException {
        BufferedImage newImage = ImageIO.read(new File(newImageFileName));

        Assert.assertEquals(Long.valueOf(newImage.getWidth()), Long.valueOf(IMAGE_WIDTH));
        Assert.assertEquals(Long.valueOf(newImage.getHeight()), Long.valueOf(IMAGE_HEIGHT));

        boolean isImageGrayScale = true;

        for (int i = 0; i < newImage.getHeight(); i++) {
            for (int j = 0; j < newImage.getWidth(); j++) {
                Color color = new Color(newImage.getRGB(j, i));

                if (color.getRed() != color.getGreen() || color.getRed() != color.getBlue())
                    isImageGrayScale = false;
            }
        }

        Assert.assertTrue(isImageGrayScale);
    }

}