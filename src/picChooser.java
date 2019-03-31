package colorsearch;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileSystemView;

/**
 *
 * @author Emma Adelmann
 */
public class picChooser 
{   
    public picChooser(ColorFilter filter)
    {
        JFileChooser fc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
       
        int returnval = 0;
        while (returnval != JFileChooser.CANCEL_OPTION)  {
            // outer loop to make as many separate searches as you want
            // pressing cancel while in this outer loop results in program exit
            
            returnval = fc.showDialog(null, "Search");
            if (returnval == JFileChooser.APPROVE_OPTION)
            {
                String chosenDirectory;
                chosenDirectory = fc.getSelectedFile().getAbsolutePath();
                JFileChooser fs = new JFileChooser(chosenDirectory);
                fs.setFileFilter(filter);
                if (returnval == JFileChooser.APPROVE_OPTION)
                {
                    returnval = fs.showOpenDialog(null);
                    while (returnval != JFileChooser.CANCEL_OPTION) {
                        // loop to keep on opening as many files as you want
                        // pressing cancel while in this inner loop allows you to start a new search with same color
                        
                        File photo = fs.getSelectedFile();
                        try
                        {
                            Desktop.getDesktop().open(photo);
                        }
                        catch (IOException e)
                        {
                            JOptionPane.showMessageDialog(fc, e + "\nPlease try again and/or report this problem to the developer.");
                        }
                        returnval = fs.showOpenDialog(null);
                    }
                    returnval = 0;
                }
            }
        }
    }
}
