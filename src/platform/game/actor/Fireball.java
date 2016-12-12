package platform.game.actor;// Created by Loris Witschard on 21.11.16.

import platform.game.Damage;
import platform.util.*;

/**
 * Fire projectile that causes fire damages.
 */
public class Fireball extends Actor
{
    private Vector position;
    private Vector velocity;
    private final double SIZE = 0.4;
    private Actor owner;
    private double lifeTime = 0;
    private double lifeTimeMax = 5;
    
    /**
     * @param position initial position
     * @param velocity initial speed
     * @param owner    actor which threw the fireball
     */
    public Fireball(Vector position, Vector velocity, Actor owner)
    {
        if(position == null || velocity == null)
            throw new NullPointerException();
        
        this.position = position;
        this.velocity = velocity;
        this.owner = owner;
        
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
        
        sprite = getSprite("fireball");
        
        double delta = input.getDeltaTime();
        Vector acceleration = getWorld().getGravity();
        velocity = velocity.add(acceleration.mul(delta));
        position = position.add(velocity.mul(delta));
        
        lifeTime += delta;
        if(lifeTime >= lifeTimeMax)
        {
            getWorld().unregister(this);
            getWorld().register(new Smoke(position));
        }
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
        else if(other != owner && other.getBox().isColliding(getBox()))
        {
            if(other.hurt(this, Damage.FIRE, 0.2, getPosition()))
                getWorld().unregister(this);
        }
    }
    
    @Override
    public void draw(Input input, Output output)
    {
        output.drawSprite(sprite, getBox(), input.getTime() * 3);
    }
}
