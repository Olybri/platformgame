package platform.game;// Created by Loris Witschard on 25.11.16.

public class ItemColor
{
    private String color;
    
    public ItemColor(String color)
    {
        this.color = color;
    }
    
    @Override
    public String toString()
    {
        return color;
    }
    
    public static ItemColor RED = new ItemColor("red");
    public static ItemColor BLUE = new ItemColor("blue");
    public static ItemColor GREEN = new ItemColor("green");
    public static ItemColor YELLOW = new ItemColor("yellow");
}
