package platform;

import java.awt.*;
import java.io.File;
import java.net.URI;
import java.nio.file.Files;

import platform.game.Command;
import platform.game.Simulator;
import platform.util.BufferedLoader;
import platform.util.DefaultLoader;
import platform.util.Display;
import platform.util.FileLoader;
import platform.util.Loader;
import platform.util.SwingDisplay;

import javax.swing.*;

/**
 * Provides main entry point.
 */
public class Program
{
    public static void main(String[] args) throws Exception
    {
        System.setProperty("sun.java2d.opengl", "true");
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    
        File resDir = new File("res");
        if(!resDir.exists() || !resDir.isDirectory())
        {
            int n = JOptionPane.showConfirmDialog(null,
                "Missing resources folder 'res'.\nDo you want to download resources now?", "Error",
                JOptionPane.YES_NO_OPTION);
        
            if(n == JOptionPane.YES_OPTION && Desktop.isDesktopSupported())
                Desktop.getDesktop().browse(new URI("https://olybri.github.io/platformgame/platformgame-res.zip"));
        
            return;
        }
    
        // Create components
        Loader loader = new BufferedLoader(new FileLoader("res/", DefaultLoader.INSTANCE));
        Display display = new SwingDisplay();
        display.setBackground(Color.WHITE);
        
        try
        {
            Command.load("res/controls.cfg", display);
            
            // Game loop
            Simulator simulator = new Simulator(loader, args);
            double avg = 0.02;
            double last = display.getTime();
            while(!display.isCloseRequested())
            {
                // Do frame
                display.begin();
                simulator.update(display, display);
                display.end();
                
                // Update framerate
                avg = avg * 0.95 + display.getDeltaTime() * 0.05;
                if(display.getTime() - last > 1)
                {
                    last = display.getTime();
                    System.out.println(avg);
                }
            }
            // Close window
        } finally
        {
            display.close();
        }
    }
}