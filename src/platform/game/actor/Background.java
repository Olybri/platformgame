package platform.game.actor;// Created by Loris Witschard on 25.11.16.

import platform.util.*;

public class Background extends Actor
{
    public Background(Sprite sprite)
    {
        if(sprite == null)
            throw new NullPointerException();
        
        this.sprite = sprite;
        
        priority = 0;
    }
    
    @Override
    public Box getBox()
    {
        Vector position = getWorld().getViewCenter().mul(0.5).add(new Vector(0, 1));
        double factor = 0.06;
        return new Box(position, sprite.getWidth() * factor, sprite.getHeight() * factor);
    }
}
