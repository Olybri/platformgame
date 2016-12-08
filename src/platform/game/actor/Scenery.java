package platform.game.actor;// Created by Loris Witschard on 25.11.16.

import platform.util.*;

/**
 * Non-solid visual items floating on the foreground or background.
 */
public class Scenery extends Actor
{
    private String spriteName;
    private Vector position;
    private double distanceFactor;
    private double sizeFactor;
    
    /**
     * @param position position of the scenery
     * @param spriteName name of the sprite to draw
     * @param sizeFactor scaling factor
     * @param distanceFactor depth factor (higher than 0: further, lower than 0: closer)
     */
    public Scenery(Vector position, String spriteName, double sizeFactor, double distanceFactor)
    {
        if(spriteName == null)
            throw new NullPointerException();
        
        this.position = position;
        this.spriteName = spriteName;
        this.sizeFactor = sizeFactor;
        this.distanceFactor = distanceFactor;
        
        priority = (int)(distanceFactor >= 0 ? (1 - distanceFactor) * 10 : 1000 + (1 - distanceFactor) * 10);
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
        Vector absolutePosition = getWorld().getViewCenter().mul(distanceFactor).add(position);
        double factor = sizeFactor * (1 - distanceFactor);
        return new Box(absolutePosition, sprite.getWidth() * factor, sprite.getHeight() * factor);
    }
}
