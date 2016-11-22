package platform.game;

import platform.util.*;

/**
 * Simple solid actor that does nothing.
 */
public class Block extends Actor
{
    private Box box;
    
    public Block(Box box)
    {
        if(box == null)
            throw new NullPointerException();
        
        this.box = box;
        
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
    
    @Override
    public void draw(Input input, Output output)
    {
        super.draw(input, output);
        Sprite sprite = getSprite("box.empty");
        output.drawSprite(sprite, getBox());
    }
}
