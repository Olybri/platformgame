package platform.game.actor;// Created by Loris Witschard on 23.11.16.

import platform.game.Damage;
import platform.util.*;

/**
 * Solid item that make actors jump high.
 */
public class Jumper extends Actor
{
    private Vector position;
    private final double SIZE = 1;
    
    private double cooldown = 0;
    private final double cooldownMax =  0.5;
    
    /**
     * @param position position of the item
     */
    public Jumper(Vector position)
    {
        if(position == null)
            throw new NullPointerException();
        
        this.position = position;
        
        priority = 50;
    }
    
    @Override
    public Box getBox()
    {
        return new Box(position.sub(new Vector(0, SIZE * 0.25)), SIZE, SIZE * 0.75);
    }
    
    @Override
    public void update(Input input)
    {
        super.update(input);
        cooldown -= input.getDeltaTime();
        
        sprite = getSprite("jumper.extended");
    }
    
    @Override
    public void interact(Actor other)
    {
        super.interact(other);
        if(cooldown <= 0 && getBox().isColliding(other.getBox())
            && other.getPosition().getY() > position.getY())
        {
            Vector below = new Vector(position.getX(), position.getY() - 1);
            if(other.hurt(this, Damage.AIR, 10.0, below))
                cooldown = cooldownMax;
    
        }
        else if(cooldown < cooldownMax - 0.1)
            other.interact(this);
    }
    
    @Override
    public boolean isSolid()
    {
        return true;
    }
    
    @Override
    public void draw(Input input, Output output)
    {
        if(cooldown <= 0)
            output.drawSprite(sprite, new Box(position, SIZE, SIZE));
        else
        {
            double offset = SIZE * Math.sin((cooldownMax - cooldown) * 30) * cooldown * 1.5;
            output.drawSprite(sprite, new Box(position.add(new Vector(0, offset / 2)), SIZE, SIZE + offset));
        }
    }
}
