package colorsearch;
import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import java.awt.Color;
import javax.imageio.ImageIO;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author Emma Adelmann
 */
public class ColorFilter extends FileFilter
{
    private Color myColor;
    
    // threshold: maximum allowed distance between the goal color and the pixel color
    private int myThreshold;
    
    // area: a picture is returned if this percentage, given as a decimal, of pixels pass
    private double myArea;
    
    public ColorFilter(Color color, int threshold, double area)
    {
        myColor = color;
        myThreshold = threshold;
        myArea = area;
    }
    
    public int getThreshold() 
    { 
        return myThreshold; 
    }
    
    public double getArea()
    {
        return myArea;
    }
    
    @Override
    public String getDescription()
    {
        return "Filtered by color";
    }
    
    // calculates the color difference based on the Euclidean model
    public double calcColorDifference(Color pixel)
    {
        int r = myColor.getRed();
        int pr = pixel.getRed();
        int g = myColor.getGreen();
        int pg = pixel.getGreen();
        int b = myColor.getBlue();
        int pb = pixel.getBlue();
        double distance = Math.sqrt(Math.pow((r - pr), 2) 
                          + Math.pow((g - pg), 2)
                          + Math.pow((b - pb), 2));
        return distance;
    }
    
    // returns true if the pixel is within the acceptable range
    public boolean checkThreshold(Color pixel)
    {
        return (calcColorDifference(pixel) <= myThreshold);
    }
    
    // reuturns a "map" (2D array) of pixel xy coordinates
    public static String[][] createPixelMap(BufferedImage img)
    {
        int width = img.getWidth();
        int height = img.getHeight();
        String[][] pixels = new String[width][height];
        for (int x = 0; x < pixels.length; x++)
        {
            for (int y = 0; y < pixels[0].length; y++)
                pixels[x][y] = x + "," + y;
        }
        return pixels;
    }
    
    // returns true if the format is one of the common acceptable image file types
    public static boolean checkFormat(File f)
    {
        String[] formats = {".png", ".jpg", ".jpeg", ".gif", ".tiff", ".tif"};
        for (String s : formats)
        {
            if (f.getAbsolutePath().contains(s))
                return true;
        }
        return false;
    }
    
    @Override
    public boolean accept(File f)
    {
        BufferedImage img;
        try
        {
            if (checkFormat(f))
                img = ImageIO.read(f);
            else
                return false;
        }
        catch (IOException e)
        {
            return false;
        }
        String[][] pixels = createPixelMap(img);
        
        // now let's run each pixel through checkThreshold(...) to see if it's valid
        int numValid = 0;
        for (int x = 0; x < pixels.length; x++)
        {
            for (int y = 0; y < pixels[0].length; y++)
            {
                String[] xy = pixels[x][y].split(",");
                Color pixcolor = new Color(img.getRGB(Integer.parseInt(xy[0]), Integer.parseInt(xy[1])));
                if (checkThreshold(pixcolor))
                    numValid++;
            }
        }
        int totalpixels = img.getWidth() * img.getHeight();

        // if at least myArea percentage of pixels pass, return true
        return numValid >= (myArea * totalpixels);
    }
}
