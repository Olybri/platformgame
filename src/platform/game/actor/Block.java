package platform.game.actor;

import platform.util.*;

/**
 * Simple solid actor that does nothing.
 */
public class Block extends Actor
{
    private Box box;
    
    public Block(Box box, Sprite sprite)
    {
        if(box == null || sprite == null)
            throw new NullPointerException();
        
        this.box = box;
        this.sprite = sprite;
        
        priority = 0;
    }
    
    @Override
    public Box getBox()
    {
        return box;
    }
    
    @Override
    public boolean isSolid()
    {
        return true;
    }
}
