package platform.game;// Created by Loris Witschard on 25.11.16.

/**
 * Color of an item
 */
public class ItemColor
{
    private String color;
    
    /**
     * @param color name of the color
     */
    public ItemColor(String color)
    {
        this.color = color.toLowerCase();
    }
    
    /**
     * @return name of the color
     */
    @Override
    public String toString()
    {
        return color;
    }
    
    // Available colors:
    public static ItemColor RED = new ItemColor("red");
    public static ItemColor BLUE = new ItemColor("blue");
    public static ItemColor GREEN = new ItemColor("green");
    public static ItemColor YELLOW = new ItemColor("yellow");
}
