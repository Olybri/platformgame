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

public class Command
{
    private static Input input;
    private static HashMap<String, Integer> commands = new HashMap<>();
    private static boolean enabled = true;
    
    public static void load(String filename, Input commandInput)
    {
        input = commandInput;
        
        try(InputStream in = new FileInputStream(filename);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in)))
        {
            String line = null;
            while((line = reader.readLine()) != null)
            {
                int value;
                String control = line.split(": ")[1];
                switch(control)
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
                        value = Character.toUpperCase(control.charAt(0));
                }
                
                commands.put(line.split(":")[0], value);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public static void enable(boolean value)
    {
        enabled = value;
    }
    
    public static Button getButton(String key)
    {
        return input.getKeyboardButton(commands.get(key));
    }
    
    public static boolean isButtonDown(String key)
    {
        return getButton(key).isDown() && enabled;
    }
    
    public static boolean isButtonPressed(String key)
    {
        return getButton(key).isPressed() && enabled;
    }
}
