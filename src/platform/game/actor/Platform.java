package platform.game.actor;// Created by Loris Witschard on 11/29/2016.

import platform.util.Box;
import platform.util.Input;
import platform.util.Output;
import platform.util.Vector;

/**
 * Block that is solid for actor falling from above.
 */
public class Platform extends Block
{
    private Box fullbox;
    
    /**
     * @param box bounding box of the platform
     * @param spriteName name of the sprite to draw
     */
    public Platform(Box box, String spriteName)
    {
        super(box.add(new Vector(0, 0.01)), spriteName);
    
        fullbox = box;
    }
    
    @Override
    public void preUpdate()
    {
        super.preUpdate();
        priority = 50;
    }
    
    @Override
    public void interact(Actor other)
    {
        super.interact(other);
        
        if(getBox().isColliding(other.getBox())
            && other.getPosition().getX() > getBox().getMin().getX() && other.getPosition().getX() < getBox().getMax().getX()
            && other.getBox().getMin().getY() > getBox().getMax().getY() - 0.2)
                other.interact(this);
    }
    
    @Override
    public void draw(Input input, Output output)
    {
        output.drawSprite(sprite, fullbox);
    }
}
