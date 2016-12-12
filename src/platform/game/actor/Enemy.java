package platform.game.actor; // Created by Saralfddin on 05.12.16.

import platform.game.Damage;
import platform.util.*;

/**
 * Abstract class that implements basic enemies dying from fireballs
 */
public abstract class Enemy extends Actor
{
    private Vector destination;
    private Box box;
    
    private final Animation animationRight;
    private final Animation animationLeft;
    private final String deadLeft;
    private final String deadRight;
    
    private double velocity;
    private boolean alive = true;
    private double cooldown;
    private double cooldownMax = 0.5;
    private double current = 0;
    private boolean forward = true;
    private boolean right;
    
    /**
     * @param box            initial bounding box
     * @param destination    destination to reach
     * @param animationLeft  left walking animation
     * @param animationRight right walking animation
     * @param deadLeft       left dead sprite name
     * @param deadRight      right dead sprite name
     * @param velocity       speed factor
     * @param right          true if it should travel right, false otherwise
     */
    public Enemy(Box box, Vector destination, Animation animationLeft, Animation animationRight,
                 String deadLeft, String deadRight, double velocity, boolean right)
    {
        if(box == null || destination == null)
            throw new NullPointerException();
        
        this.box = box;
        this.destination = destination;
        this.animationLeft = animationLeft;
        this.animationRight = animationRight;
        this.deadLeft = deadLeft;
        this.deadRight = deadRight;
        this.velocity = velocity;
        this.right = right;
        
        priority = 50;
    }
    
    @Override
    public void update(Input input)
    {
        super.update(input);
        
        double dt = input.getDeltaTime();
        animationRight.update(dt);
        animationLeft.update(dt);
        
        if(alive)
        {
            if(right)
                sprite = getSprite(animationRight.getSprite());
            else
                sprite = getSprite(animationLeft.getSprite());
            
            if(forward)
            {
                current += input.getDeltaTime() * velocity;
                if(current > 1.0)
                {
                    current = 1.0;
                    forward = false;
                    right = !right;
                }
            }
            else
            {
                current -= input.getDeltaTime() * velocity;
                if(current < 0.0)
                {
                    current = 0.0;
                    forward = true;
                    right = !right;
                }
            }
        }
        else if(cooldown <= 0)
        {
            getWorld().register(new Smoke(getBox().getCenter()));
            getWorld().unregister(this);
        }
        
        cooldown -= input.getDeltaTime();
    }
    
    @Override
    public Box getBox()
    {
        Vector position = box.getCenter();
        double multiplier = -2 * Math.pow(current, 3) + 3 * Math.pow(current, 2);
        return new Box(position.add((destination.sub(position)).mul(multiplier)), box.getWidth(), box.getHeight());
    }
    
    @Override
    public void interact(Actor other)
    {
        super.interact(other);
        
        if(alive && cooldown <= 0 && getBox().isColliding(other.getBox()))
            if(other.hurt(this, Damage.PHYSICAL, 0.2, getBox().getCenter()))
                cooldown = cooldownMax;
    }
    
    @Override
    public boolean hurt(Actor instigator, Damage type, double amount, Vector location)
    {
        if(!alive)
            return false;
        
        if(type == Damage.FIRE)
        {
            alive = false;
            cooldown = cooldownMax;
            return true;
        }
        return false;
    }
    
    @Override
    public void draw(Input input, Output output)
    {
        double factor = 1;
        if(cooldown > 0)
        {
            if(!alive)
            {
                factor += Math.cos((cooldownMax - cooldown) * 20) * cooldown / 2 / cooldownMax;
                if(right)
                    sprite = getSprite(deadRight);
                else
                    sprite = getSprite(deadLeft);
            }
        }
        
        Box newBox = new Box(getBox().getCenter(), box.getWidth() * factor, box.getHeight() * factor);
        output.drawSprite(sprite, newBox, alive ? 0 : (cooldownMax - cooldown) * (right ? 0.5 : -0.5));
    }
}
