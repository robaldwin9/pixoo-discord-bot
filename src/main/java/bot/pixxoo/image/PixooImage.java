package bot.pixxoo.image;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.Base64;

public class PixooImage {

    private static final Logger logger = LoggerFactory.getLogger(PixooImage.class);

    private final BufferedImage originalImage;

    private final BufferedImage scaled64x64Image;

    private final BufferedImage scaled32x32Image;

    private final BufferedImage scaled16x16Image;

    private final String base64ImageString;

    private final String base32ImageString;

    private final String base16ImageString;

    public PixooImage(URI imageUrl) {
        try {
            originalImage = ImageIO.read(imageUrl.toURL());
            scaled64x64Image = createScaledImage(originalImage, 64, 64);
            scaled32x32Image = createScaledImage(originalImage, 32, 32);
            scaled16x16Image = createScaledImage(originalImage, 16, 16);
            base64ImageString = convertToBase64String(scaled64x64Image);
            base32ImageString = convertToBase64String(scaled32x32Image);
            base16ImageString = convertToBase64String(scaled16x16Image);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public PixooImage(BufferedImage image) {
        originalImage = image;
        scaled64x64Image = createScaledImage(originalImage, 64, 64);
        scaled32x32Image = createScaledImage(originalImage, 32, 32);
        scaled16x16Image = createScaledImage(originalImage, 16, 16);
        base64ImageString = convertToBase64String(scaled64x64Image);
        base32ImageString = convertToBase64String(scaled32x32Image);
        base16ImageString = convertToBase64String(scaled16x16Image);
    }

    private static BufferedImage createScaledImage(BufferedImage originalImage, int width, int height) {
        Image tmp = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage scaledImaged = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = scaledImaged.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return scaledImaged;
    }

    private String convertToBase64String(BufferedImage image) {
        ByteArrayOutputStream baos=new ByteArrayOutputStream(32000);
        String base64String = "";

        try {
            for(int i = 0; i < image.getHeight(); i++) {
                for(int j = 0; j < image.getWidth(); j++){
                    Color c = new Color(image.getRGB(j,i));
                    baos.write(c.getRed());
                    baos.write(c.getGreen());
                    baos.write(c.getBlue());
                }
            }
            baos.flush();
            byte[] bytes = baos.toByteArray();
            base64String  = Base64.getEncoder().encodeToString(bytes);
            baos.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        return  base64String;
    }

    public static ArrayList<PixooImage> createGifFrames(URI imageUrl) {
        ArrayList<PixooImage> gifImages = new ArrayList<>();
        ImageReader reader = ImageIO.getImageReadersBySuffix("GIF").next();
        try {
            InputStream is = imageUrl.toURL().openStream();
            ImageInputStream in = ImageIO.createImageInputStream(is);
            reader.setInput(in);
            for (int i = 0, count = reader.getNumImages(true); i < count; i++) {
                BufferedImage image = reader.read(i);
                gifImages.add(new PixooImage(image));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return gifImages;
    }

    public String getBase64Image() {
        return base64ImageString;
    }

    public String getBase32Image() {
        return base32ImageString;
    }

    public String getBase16Image() {
        return base16ImageString;
    }

    public BufferedImage get64x64ScaledImage() {
        return scaled64x64Image;
    }

    public BufferedImage get32x32ScaledImage() {
        return scaled32x32Image;
    }

    public BufferedImage get16x16ScaledImage() {
        return scaled16x16Image;
    }

    public static void writeImageToDisk(BufferedImage image) {
        writeImageToDisk(image, "png");
    }

    public static void writeImageToDisk(BufferedImage image, String formatName) {
        File outputFile = new File("last-image." + formatName);
        try {
            ImageIO.write(image, formatName, outputFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeImageToDisk(BufferedImage image, int width, int height) {
        BufferedImage scaledImage = createScaledImage(image, width, height);
        writeImageToDisk(scaledImage);
    }
}
