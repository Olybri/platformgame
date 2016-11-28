package platform.game.actor;// Created by Loris Witschard on 22.11.16.

import platform.game.Command;
import platform.game.Damage;
import platform.util.*;

import java.awt.event.KeyEvent;

public class Player extends Actor
{
    private Vector position;
    private Vector velocity = Vector.ZERO;
    private final double SIZE = 0.75;
    
    private double healthMax = 1;
    private double health = healthMax;
    
    private boolean colliding = false;
    
    public Player(Vector position)
    {
        if(position == null)
            throw new NullPointerException();
        
        this.position = position;
        
        priority = 42;
    }
    
    public double getHealth()
    {
        return health;
    }
    
    public double getHealthMax()
    {
        return healthMax;
    }
    
    public double getSize()
    {
        return SIZE;
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
        
        sprite = getSprite("blocker.happy");
        
        if(health <= 0)
        {
            getWorld().nextLevel();
            return;
        }
        
        double maxSpeed = 4.0;
        if(Command.isButtonDown("walk_right"))
        {
            if(velocity.getX() < maxSpeed)
            {
                double increase = 60.0 * input.getDeltaTime();
                double speed = velocity.getX() + increase;
                if(speed > maxSpeed)
                    speed = maxSpeed;
                velocity = new Vector(speed, velocity.getY());
            }
        }
        if(Command.isButtonDown("walk_left"))
        {
            if(velocity.getX() > -maxSpeed)
            {
                double increase = 60.0 * input.getDeltaTime();
                double speed = velocity.getX() - increase;
                if(speed < -maxSpeed)
                    speed = -maxSpeed;
                velocity = new Vector(speed, velocity.getY());
            }
        }
        if(!Command.isButtonDown("walk_left") && !Command.isButtonDown("walk_right"))
        {
            if(colliding && velocity.getX() != 0)
                velocity = velocity.mul(0.9);
        }
        
        if(colliding && Command.isButtonPressed("jump"))
            velocity = new Vector(velocity.getX(), 7.0);
        
        if(Command.isButtonPressed("attack"))
        {
            Vector fireballVelocity = velocity.add(velocity.resized(2.0));
            getWorld().register(new Fireball(position, fireballVelocity, this));
        }
        
        if(Command.isButtonPressed("blow"))
        {
            getWorld().hurt(getBox(), this, Damage.AIR, 1.0, getPosition());
            getWorld().register(new Smoke(position));
        }
    
        if(Command.isButtonPressed("activate"))
            getWorld().hurt(getBox(), this, Damage.ACTIVATION, 1.0, getPosition());
        
        double delta = input.getDeltaTime();
        Vector acceleration = getWorld().getGravity();
        velocity = velocity.add(acceleration.mul(delta));
        position = position.add(velocity.mul(delta));
    }
    
    @Override
    public void preUpdate()
    {
        colliding = false;
    }
    
    @Override
    public void postUpdate()
    {
        getWorld().setView(position, 8);
    }
    
    @Override
    public void interact(Actor other)
    {
        super.interact(other);
        if(other.isSolid())
        {
            Vector delta = other.getBox().getCollision(getBox());
            if(delta != null)
            {
                colliding = true;
                position = position.add(delta);
                if(delta.getX() != 0.0)
                    velocity = new Vector(0.0, velocity.getY());
                if(delta.getY() != 0.0)
                    velocity = new Vector(velocity.getX(), 0.0);
            }
        }
    }
    
    @Override
    public boolean hurt(Actor instigator, Damage type, double amount, Vector location)
    {
        if(instigator == this)
            return false;
        
        switch(type)
        {
            case AIR:
                velocity = new Vector(position.getX(), position.sub(location).resized(amount).getY());
                return true;
            
            case PHYSICAL:
                if(velocity.getY() > 0 || position.getY() < location.getY())
                    return false;
                velocity = velocity.normalized().mul(-5);
            case VOID:
                health -= amount;
                return true;
    
            case FIRE:
                velocity = new Vector(velocity.getX(), 3.0);
                position = position.add(new Vector(0, 0.1));
                health -= amount;
                return true;
            
            case HEAL:
                if(health < healthMax)
                {
                    health = Math.min(health + amount, healthMax);
                    return true;
                }
                return false;
            
            case ACTIVATION:
                return true;
            
            default:
                return super.hurt(instigator, type, amount, location);
        }
    }
}
