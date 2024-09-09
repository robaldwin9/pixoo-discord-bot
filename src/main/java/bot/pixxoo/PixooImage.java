package bot.pixxoo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;

public class PixooImage {

    private static final Logger logger = LoggerFactory.getLogger(PixooImage.class);

    private BufferedImage originalImage;

    private BufferedImage scaled64x64Image;

    private BufferedImage scaled32x32Image;

    private BufferedImage scaled16x16Image;

    private String base64ImageString;

    private String base32ImageString;

    private String base16ImageString;

    private URL origionalImageUrl;

    public PixooImage(URL imageUrl) {
        try {
            origionalImageUrl = imageUrl;
            originalImage = ImageIO.read(imageUrl);
            scaled64x64Image = createScaledImage(originalImage, 64, 64, false);
            scaled32x32Image = createScaledImage(originalImage, 32, 32, true);
            scaled16x16Image = createScaledImage(originalImage, 16, 16, true);
            base64ImageString = convertToBase64String(scaled64x64Image);
            base32ImageString = convertToBase64String(scaled32x32Image);
            base16ImageString = convertToBase64String(scaled16x16Image);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public PixooImage(BufferedImage image) {
        originalImage = image;
        scaled64x64Image = createScaledImage(originalImage, 64, 64, false);
        scaled32x32Image = createScaledImage(originalImage, 32, 32, true);
        scaled16x16Image = createScaledImage(originalImage, 16, 16, true);
        base64ImageString = convertToBase64String(scaled64x64Image);
        base32ImageString = convertToBase64String(scaled32x32Image);
        base16ImageString = convertToBase64String(scaled16x16Image);
    }

    private BufferedImage createScaledImage(BufferedImage originalImage, int width, int height, boolean preserveAlpha) {
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
            for(int i = 0; i < image.getWidth(); i++) {
                for(int j = 0; j < image.getHeight(); j++){
                    Color c = new Color(image.getRGB(i,j));
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

    public String getBase64Image() {
        return base64ImageString;
    }

    public String getBase32Image() {
        return base32ImageString;
    }

    public String getBase16Image() {
        return base16ImageString;
    }

    public void writeImageToDisk(BufferedImage image, String fileName) {
        File outputfile = new File(fileName);
        try {
           boolean written = ImageIO.write(image, "bmp", outputfile);
           logger.info("{} file write success -> {}", fileName, written);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeBase64ToHtml(String data, String fileName) {;
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
            writer.write("<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "<head>\n" +
                    "    <title>Display Image</title>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "<img style='display:block; width:100px;height:100px;' id='base64image'\n" +
                    "     src='data:image/jpeg;base64, ");

            writer.write(data);
            writer.write("' />\n" +
                    "</body>\n" +
                    "</html>");
            writer.flush();
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteExistingFile(String fileName) {
        File outputfile = new File(fileName);
        if (outputfile.exists()) {
            outputfile.delete();
        }
    }

    public static ArrayList<PixooImage> getGifFrames(URL imageUrl) {
        ArrayList<PixooImage> gifImages = new ArrayList<>();
        ImageReader reader = ImageIO.getImageReadersBySuffix("GIF").next();
        try {
            InputStream is = imageUrl.openStream();
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
}
