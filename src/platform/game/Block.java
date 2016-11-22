package platform.game;

import platform.util.*;

/**
 * Simple solid actor that does nothing.
 */
public class Block extends Actor
{
    protected Box box;
    
    public Block(Box box)
    {
        sprite = getWorld().getLoader().getSprite("block.empty");
        priority = 0;
        
        if(box == null || sprite == null)
            throw new NullPointerException();
        
        this.box = box;
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
