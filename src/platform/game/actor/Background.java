package platform.game.actor;// Created by Loris Witschard on 25.11.16.

import platform.util.*;

public class Background extends Actor
{
    private String spriteName;
    
    public Background(String spriteName)
    {
        if(spriteName == null)
            throw new NullPointerException();
        
        this.spriteName = spriteName;
        
        priority = 0;
    }
    
    @Override
    public void preUpdate()
    {
        super.preUpdate();
        sprite = getSprite(spriteName);
    }
    
    @Override
    public Box getBox()
    {
        Vector position = getWorld().getViewCenter().mul(0.5).add(new Vector(0, 1));
        double factor = 0.06;
        return new Box(position, sprite.getWidth() * factor, sprite.getHeight() * factor);
    }
}
