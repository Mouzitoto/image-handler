package kz.mouzitoto.imagehandler;

import kz.mouzitoto.imagehandler.services.ImageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

@SpringBootApplication
public class ImageHandlerApplication implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImageHandlerApplication.class);

    private final ImageService imageService;

    @Autowired
    public ImageHandlerApplication(ImageService imageService) {
        this.imageService = imageService;
    }

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(ImageHandlerApplication.class);
        application.setBannerMode(Banner.Mode.OFF);
        application.run(args);
    }

    @Override
    public void run(String... args) throws Exception {
        printWelcomeText();

        imageService.validateArgs(args);
        imageService.handleImage(args);

        pause();
    }

    private void pause() {
        LOGGER.info("Press \"Enter\" to exit");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
    }

    private void printWelcomeText() {
        LOGGER.info("");
        LOGGER.info("");
        LOGGER.info("");
        LOGGER.info("");
        LOGGER.info("");
        LOGGER.info("");
        LOGGER.info("This program will resize an image with given width and height.");
        LOGGER.info("The Grayscale effect will be applied to image.");
        LOGGER.info("New image will be saved in the same directory as this program.");
        LOGGER.info("There must be 3 arguments:");
        LOGGER.info("1. Image path;");
        LOGGER.info("2. New image width;");
        LOGGER.info("3. New image height;");
        LOGGER.info("");
        LOGGER.info("");
    }
}
