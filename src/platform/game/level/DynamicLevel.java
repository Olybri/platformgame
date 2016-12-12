package platform.game.level;// Created by Loris Witschard on 12/2/2016.

import platform.game.ItemColor;
import platform.game.World;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;

import platform.game.actor.*;
import platform.game.signal.*;
import platform.game.signal.Constant;
import platform.game.signal.Signal;
import platform.util.Box;
import platform.util.Vector;

/**
 * Level that loads its content dynamically from a file.
 * See README.md for file syntax.
 */
public class DynamicLevel extends Level
{
    private String filename;
    private HashMap<String, Object> variables = new HashMap<>();
    
    /**
     * @param filename name of the level file
     */
    public DynamicLevel(String filename)
    {
        this.filename = filename;
    }
    
    /**
     * Cast a string into a vector
     *
     * @param string string to parse
     * @return vector interpreted from the string
     */
    private Vector parseVector(String string) throws NumberFormatException
    {
        String[] pieces = string.split(":");
        if(pieces.length != 2)
            throw new NumberFormatException("Cannot convert string '" + string + "' to " + Vector.class);
        
        return new Vector(Double.parseDouble(pieces[0]), Double.parseDouble(pieces[1]));
    }
    
    /**
     * Cast a string into a box
     *
     * @param string string to parse
     * @return box interpreted from the string
     */
    private Box parseBox(String string) throws NumberFormatException
    {
        String[] pieces = string.split(":");
        if(pieces.length != 4)
            throw new NumberFormatException("Cannot convert string '" + string + "' to " + Box.class);
        
        return new Box(new Vector(Double.parseDouble(pieces[0]), Double.parseDouble(pieces[1])),
            Double.parseDouble(pieces[2]), Double.parseDouble(pieces[3]));
    }
    
    /**
     * Get reference on a stored object
     *
     * @param symbol name of the object
     * @param type type of the object
     * @return object associated to the name, if it exists
     */
    private <T> T getVariable(String symbol, Class<T> type) throws IllegalArgumentException
    {
        if(type == Signal.class && (symbol.equals("true") || symbol.equals("false")))
            return type.cast(new Constant(Boolean.parseBoolean(symbol)));
        
        if(!variables.containsKey(symbol))
            throw new IllegalArgumentException("Cannot resolve symbol '" + symbol + "'.");
        
        if(!type.isInstance(variables.get(symbol)))
            throw new IllegalArgumentException("Variable '" + symbol + "' must be an instance of '" + type + "'.");
        
        return type.cast(variables.get(symbol));
    }
    
    /**
     * Store a reference on a object
     *
     * @param symbol name of the object
     * @param value instance of the object
     */
    private void addVariable(String symbol, Object value) throws IllegalArgumentException
    {
        if(variables.containsKey(symbol))
            throw new IllegalArgumentException("Variable '" + symbol + "' is already defined.");
        
        variables.put(symbol, value);
    }
    
    /**
     * Check if an array has enough arguments
     *
     * @param args arguments array
     * @param count number of arguments expected
     */
    private void checkArgs(String[] args, int count) throws IllegalArgumentException
    {
        if(args.length < count + 1)
            throw new IllegalArgumentException("Too few arguments to construct actor '" + args[0] + "'.");
        
        if(args.length > count + 1)
            throw new IllegalArgumentException("Too many arguments to construct actor '" + args[0] + "'.");
    }
    
    @Override
    public void register(World world)
    {
        super.register(world);
        
        world.setNextLevel(new DynamicLevel(filename));
        
        try(InputStream in = new FileInputStream(filename);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in)))
        {
            int lineNumber = 0;
            
            String line;
            while((line = reader.readLine()) != null)
            {
                ++lineNumber;
                try
                {
                    String symbol = null;
                    Object object;
                    
                    String[] args = line.trim().split("[\\s]+");
                    
                    if(args.length > 2 && args[1].equals("="))
                    {
                        symbol = args[0];
                        args = Arrays.copyOfRange(args, 2, args.length);
                    }
                    
                    if(args.length == 0 || args[0].equals("") || !Character.isAlphabetic(args[0].charAt(0)))
                        continue;
                    
                    switch(args[0].toLowerCase())
                    {
                        case "player":
                            checkArgs(args, 1);
                            object = new Player(parseVector(args[1]));
                            world.resetView(Actor.class.cast(object).getPosition(), 10);
                            break;
                        case "slime":
                            checkArgs(args, 3);
                            object = new Slime(parseVector(args[1]), parseVector(args[2]), Boolean.parseBoolean(args[3]));
                            break;
                        case "block":
                            checkArgs(args, 2);
                            object = new Block(parseBox(args[1]), args[2]);
                            break;
                        case "spike":
                            checkArgs(args, 1);
                            object = new Spike(parseVector(args[1]));
                            break;
                        case "platform":
                            checkArgs(args, 2);
                            object = new Platform(parseBox(args[1]), args[2]);
                            break;
                        case "door":
                            checkArgs(args, 3);
                            object = new Door(parseVector(args[1]), new ItemColor(args[2]),
                                getVariable(args[3], Signal.class));
                            break;
                        case "antiplayer":
                            checkArgs(args, 2);
                            object = new AntiPlayer(getVariable(args[1], Player.class), Double.parseDouble(args[2]));
                            break;
                        case "torch":
                            checkArgs(args, 2);
                            object = new Torch(parseVector(args[1]), Boolean.parseBoolean(args[2]));
                            break;
                        case "exit":
                            checkArgs(args, 3);
                            object = new Exit(parseVector(args[1]), new DynamicLevel(args[2]),
                                getVariable(args[3], Signal.class));
                            break;
                        case "heart":
                            checkArgs(args, 1);
                            object = new Heart(parseVector(args[1]));
                            break;
                        case "hill":
                            checkArgs(args, 3);
                            object = new Hill(parseBox(args[1]), args[2], Boolean.parseBoolean(args[3]));
                            break;
                        case "jumper":
                            checkArgs(args, 1);
                            object = new Jumper(parseVector(args[1]));
                            break;
                        case "key":
                            checkArgs(args, 2);
                            object = new Key(parseVector(args[1]), new ItemColor(args[2]));
                            break;
                        case "lever":
                            checkArgs(args, 1);
                            object = new Lever(parseVector(args[1]));
                            break;
                        case "limits":
                            checkArgs(args, 1);
                            object = new Limits(parseBox(args[1]));
                            break;
                        case "mover":
                            checkArgs(args, 5);
                            object = new Mover(parseBox(args[1]), args[2], parseVector(args[3]),
                                Double.parseDouble(args[4]), getVariable(args[5], Signal.class));
                            break;
                        case "scenery":
                            checkArgs(args, 4);
                            object = new Scenery(parseVector(args[1]), args[2],
                                Double.parseDouble(args[3]), Double.parseDouble(args[4]));
                            break;
                        case "and":
                            checkArgs(args, 2);
                            object = new And(getVariable(args[1], Signal.class), getVariable(args[2], Signal.class));
                            break;
                        case "or":
                            checkArgs(args, 2);
                            object = new Or(getVariable(args[1], Signal.class), getVariable(args[2], Signal.class));
                            break;
                        case "not":
                            checkArgs(args, 1);
                            object = new Not(getVariable(args[1], Signal.class));
                            break;
                        default:
                            throw new IllegalArgumentException("Unknown object type '" + args[0] + "'.");
                    }
                    
                    if(symbol != null)
                        addVariable(symbol, object);
                    
                    if(object instanceof Actor)
                        world.register(Actor.class.cast(object));
                }
                catch(Exception e)
                {
                    System.out.println("\033[31mLevel file: " + filename + " | Ignored line " + lineNumber
                        + " | " + e.getMessage() + "\033[0m");
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
