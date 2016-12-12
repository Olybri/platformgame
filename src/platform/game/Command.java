package platform.game;

// Created by Loris Witschard on 11/28/2016.

import platform.util.Button;
import platform.util.Input;

import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Class with static methods providing information about user commands
 */
public class Command
{
    private static Input input;
    private static HashMap<String, Integer> commands = new HashMap<>();
    private static boolean enabled = true;
    
    /**
     * Load a list of mapped commands from a file.
     *
     * @param filename     name of the file
     * @param commandInput input system of the game
     */
    public static void load(String filename, Input commandInput)
    {
        input = commandInput;
        
        try(InputStream in = new FileInputStream(filename);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in)))
        {
            String line;
            while((line = reader.readLine()) != null)
            {
                int value;
                String[] control = line.split("[\\s]*:[\\s]*");
                switch(control[1].toLowerCase())
                {
                    case "leftarrow":
                        value = KeyEvent.VK_LEFT;
                        break;
                    case "rightarrow":
                        value = KeyEvent.VK_RIGHT;
                        break;
                    case "uparrow":
                        value = KeyEvent.VK_UP;
                        break;
                    case "downarrow":
                        value = KeyEvent.VK_DOWN;
                        break;
                    case "space":
                        value = KeyEvent.VK_SPACE;
                        break;
                    case "enter":
                        value = KeyEvent.VK_ENTER;
                        break;
                    case "backspace":
                        value = KeyEvent.VK_BACK_SPACE;
                        break;
                    default:
                        value = control[1].toUpperCase().charAt(0);
                }
                
                commands.put(control[0], value);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * Enable or disable user commands. When disabled, every command is ignored.
     *
     * @param value true to enable, false to disable
     */
    public static void enable(boolean value)
    {
        enabled = value;
    }
    
    /**
     * @param key command name
     * @return information about the button mapped to the command
     */
    private static Button getButton(String key)
    {
        return input.getKeyboardButton(commands.get(key));
    }
    
    /**
     * @param key command name
     * @return true if the button mapped to the command is down, false otherwise
     */
    public static boolean isButtonDown(String key)
    {
        return getButton(key).isDown() && enabled;
    }
    
    /**
     * @param key command name
     * @return true if the button mapped to the command has been pressed, false otherwise
     */
    public static boolean isButtonPressed(String key)
    {
        return getButton(key).isPressed() && enabled;
    }
}
