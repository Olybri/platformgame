package platform.game.actor;

import platform.util.*;

/**
 * Simple solid actor that does nothing.
 */
public class Block extends Actor
{
    private Box box;
    private String spriteName;
    
    /**
     * @param box        bounding box of the block
     * @param spriteName name of the sprite to draw
     */
    public Block(Box box, String spriteName)
    {
        if(box == null || spriteName == null)
            throw new NullPointerException();
        
        this.box = box;
        this.spriteName = spriteName;
        
        priority = 20;
    }
    
    @Override
    public void update(Input input)
    {
        super.update(input);
        sprite = getSprite(spriteName);
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
