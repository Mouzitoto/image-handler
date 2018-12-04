package kz.mouzitoto.imagehandler.services;


import com.twelvemonkeys.image.ResampleOp;
import kz.mouzitoto.imagehandler.exceptions.IHBadInputException;
import kz.mouzitoto.imagehandler.exceptions.IHIOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.io.File;
import java.io.IOException;
import java.net.URL;


@Service
public class ImageService {

    //todo: should we support https?
    private static final String IMG_PATH_PREFIX_HTTP = "http://";
    private static final String REGEXP_ONLY_DIGITS = "^[0-9]+$";

    private static final Logger LOGGER = LoggerFactory.getLogger(ImageService.class);


    public void validateArgs(String[] args) throws IHBadInputException {
        LOGGER.info("Start validating args;");
        LOGGER.info("   The args:");
        for (int i = 0; i < args.length; i++)
            LOGGER.info("       " + i + ": " + args[i]);

        if (args.length < 3)
            throw new IHBadInputException("Not enough arguments, must be 3 arguments.");

        if (args.length > 3)
            LOGGER.warn("There are more arguments than needed; Only first 3 will be used, others will be ignored.");

//        //todo: check file path? It can be in different OS so different symbols allowed and different startsWith regexp. Path may contain folder and may not. May contain extension and may not.
//        if (!args[0].startsWith(IMG_PATH_PREFIX_HTTP) || !args[0].startsWith(IMG_PATH_REGEXP))
//            throw new IHBadInputException("Bad image path argument (first argument); Must be started from \"http://\" or ....");

        if (!args[1].matches(REGEXP_ONLY_DIGITS))
            throw new IHBadInputException("Bad image width argument (second argument); Must be only digits.");

        if (!args[2].matches(REGEXP_ONLY_DIGITS))
            throw new IHBadInputException("Bad image height argument (third argument); Must be only digits.");

        LOGGER.info("Finish validating args;");
    }

    public String handleImage(String[] args) throws IHIOException, IHBadInputException {
        try {
            BufferedImage image = loadImage(args[0]);

            image = resizeImage(image, Integer.valueOf(args[1]), Integer.valueOf(args[2]));

            image = applyGrayScaleEffect(image);

            return saveImageToFileSystem(image);

        } catch (IOException e) {
            throw new IHIOException(e.getMessage());
        }

    }

    private BufferedImage loadImage(String path) throws IHIOException, IHBadInputException {
        try {
            LOGGER.info("Start loading image;");

            BufferedImage image;
            if (path.startsWith(IMG_PATH_PREFIX_HTTP))
                image = ImageIO.read(new URL(path));
            else
                image = ImageIO.read(new File(path));

            if (image == null)
                throw new IHBadInputException("Bad image path/url");

            LOGGER.info("Finish loading image;");

            return image;
        } catch (IOException e) {
            throw new IHIOException(e.getMessage());
        }
    }

    private String saveImageToFileSystem(BufferedImage image) throws IOException {
        LOGGER.info("Start saving image to file system;");

        String fileName = System.currentTimeMillis() + ".jpg";

        File file = new File(fileName);
        ImageIO.write(image, "jpg", file);

        LOGGER.info("Finish saving image to file system; Image path: " + file.getAbsoluteFile().toString());

        return file.getAbsoluteFile().toString();
    }

    private BufferedImage resizeImage(BufferedImage image, int newWidth, int newHeight) {
        LOGGER.info("Start resizing image;");

        BufferedImageOp resampler = new ResampleOp(newWidth, newHeight, ResampleOp.FILTER_LANCZOS);
        BufferedImage newImage = resampler.filter(image, null);

        LOGGER.info("Finish resizing image;");

        return newImage;
    }

    private BufferedImage applyGrayScaleEffect(BufferedImage sourceImage) {
        BufferedImage newImage = clone(sourceImage);

        LOGGER.info("Start grayscaling image;");

        for (int i = 0; i < newImage.getHeight(); i++) {
            for (int j = 0; j < newImage.getWidth(); j++) {
                Color color = new Color(newImage.getRGB(j, i));

                int red = (int) (color.getRed() * 0.299);
                int green = (int) (color.getGreen() * 0.587);
                int blue = (int) (color.getBlue() * 0.114);

                Color newColor = new Color(
                        red + green + blue,
                        red + green + blue,
                        red + green + blue);

                newImage.setRGB(j, i, newColor.getRGB());
            }
        }

        LOGGER.info("Finish grayscaling image;");

        return newImage;
    }

    private BufferedImage clone(BufferedImage sourceImage) {
        LOGGER.info("Start cloning image;");

        BufferedImage newImage = new BufferedImage(sourceImage.getWidth(), sourceImage.getHeight(), sourceImage.getType());
        Graphics2D g2d = newImage.createGraphics();
        g2d.drawImage(sourceImage, 0, 0, null);
        g2d.dispose();

        LOGGER.info("Finish cloning image;");

        return newImage;
    }
}
