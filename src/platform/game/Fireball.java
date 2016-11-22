package platform.game;// Created by Loris Witschard on 21.11.16.

import platform.util.*;

public class Fireball extends Actor
{
    private Vector position;
    private Vector velocity;
    private final double SIZE = 0.4;
    
    public Fireball(Vector position, Vector velocity)
    {
        if(position == null || velocity == null)
            throw new NullPointerException();
        
        this.position = position;
        this.velocity = velocity;
        
        priority = 666;
    }
    
    @Override
    public Box getBox()
    {
        return new Box(position, SIZE, SIZE);
    }
    
    @Override
    public void update(Input input)
    {
        super.update(input);
        double delta = input.getDeltaTime();
        Vector acceleration = getWorld().getGravity();
        velocity = velocity.add(acceleration.mul(delta));
        position = position.add(velocity.mul(delta));
    }
    
    @Override
    public void interact(Actor other)
    {
        super.interact(other);
        if(other.isSolid())
        {
            Vector delta = other.getBox().getCollision(position);
            if(delta != null)
            {
                position = position.add(delta);
                velocity = velocity.mirrored(delta);
            }
        }
    }
    
    @Override
    public void draw(Input input, Output output)
    {
        super.draw(input, output);
        Sprite sprite = getSprite("fireball");
        output.drawSprite(sprite, getBox(), input.getTime());
    }
}
