package platform.game.actor;// Created by Loris Witschard on 11/29/2016.

import platform.util.Box;

public class Platform extends Block
{
    public Platform(Box box, String spriteName)
    {
        super(box, spriteName);
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
            && other.getBox().getMin().getY() > getBox().getMax().getY() - 0.1)
                other.interact(this);
    }
}
