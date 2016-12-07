package platform.game.actor;// Created by Loris Witschard on 11/29/2016.

import platform.util.Box;
import platform.util.Input;
import platform.util.Output;
import platform.util.Vector;

/**
 * Solid block with a slope.
 */
public class Hill extends Block
{
    private boolean right = false;
    private Box fullBox;
    private double height = 0;
    
    /**
     * @param box bounding box of the hill
     * @param spriteName name of the sprite to draw
     * @param right true for right hill, false for left hill
     */
    public Hill(Box box, String spriteName, boolean right)
    {
        super(box, spriteName);
        this.right = right;
        
        fullBox = box;
    }
    
    @Override
    public void preUpdate()
    {
        super.preUpdate();
        priority = 700;
    }
    
    @Override
    public Box getBox()
    {
        return new Box(fullBox.getMin().add(new Vector(fullBox.getWidth() / 2, height / 2)),
            fullBox.getWidth(), height);
    }
    
    @Override
    public void interact(Actor other)
    {
        super.interact(other);
    
        if(fullBox.isColliding(other.getBox()))
        {
            if(right)
                height = (fullBox.getMax().getX() - other.getPosition().getX());
            else
                height = (other.getPosition().getX() - fullBox.getMin().getX());
            
            height = height / fullBox.getWidth() * fullBox.getHeight();
            other.interact(this);
        }
    }
    
    @Override
    public void draw(Input input, Output output)
    {
        output.drawSprite(sprite, fullBox);
    }
}
