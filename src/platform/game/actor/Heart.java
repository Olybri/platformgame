package platform.game.actor;// Created by Loris Witschard on 24.11.16.

import platform.util.Box;
import platform.util.Input;
import platform.util.Vector;

public class Heart extends Actor
{
    private Vector position;
    private final double SIZE = 0.5;
    
    private double cooldown = 0;
    private final double cooldownMax = 10;
    
    public Heart(Vector position)
    {
        if(position == null)
            throw new NullPointerException();
        
        this.position = position;
        
        priority = 50;
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
        cooldown -= input.getDeltaTime();
        
        if(cooldown <= 0)
            sprite = getSprite("heart.full");
        else
            sprite = null;
    }
    
    @Override
    public void interact(Actor other)
    {
        super.interact(other);
        if(cooldown <= 0 && getBox().isColliding(other.getBox()))
            if(other.hurt(this, Damage.HEAL, 0.2, position))
                cooldown = cooldownMax;
    }
}
