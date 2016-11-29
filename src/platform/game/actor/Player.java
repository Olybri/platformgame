package platform.game.actor;// Created by Loris Witschard on 22.11.16.

import platform.game.Command;
import platform.game.Damage;
import platform.util.*;

import java.awt.event.KeyEvent;
import java.util.HashMap;

public class Player extends Actor
{
    private Vector position;
    private Vector velocity = Vector.ZERO;
    private final double SIZE = 0.75;
    
    private double healthMax = 1;
    private double health = healthMax;
    
    private HashMap<Side, Boolean> collisions = new HashMap<>();
    
    private enum Side
    {
        LEFT,
        RIGHT,
        UP,
        DOWN
    }
    
    public Player(Vector position)
    {
        if(position == null)
            throw new NullPointerException();
        
        this.position = position;
        
        priority = 40;
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
        
        if(collisions.get(Side.DOWN) && collisions.get(Side.UP))
            health -= 0.4;
    
        if(collisions.get(Side.LEFT) && collisions.get(Side.RIGHT))
            health -= 0.4;
        
        if(collisions.get(Side.DOWN))
        {
            double scale = Math.pow(0.0005, input.getDeltaTime());
            velocity = new Vector(velocity.getX() * scale, velocity.getY());
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
        
        if(collisions.get(Side.DOWN) && Command.isButtonPressed("jump"))
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
        collisions.put(Side.LEFT, false);
        collisions.put(Side.RIGHT, false);
        collisions.put(Side.UP, false);
        collisions.put(Side.DOWN, false);
    }
    
    @Override
    public void postUpdate()
    {
        getWorld().setView(position, 8);
        
        System.out.print("\r                                    \r");
        for(Side side : collisions.keySet())
            if(collisions.get(side))
                System.out.print(side + " ");
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
                if(other.getBox().getMax().getX() < position.getX())
                    collisions.put(Side.LEFT, true);
                else if(other.getBox().getMin().getX() > position.getX())
                    collisions.put(Side.RIGHT, true);
                if(other.getBox().getMax().getY() < position.getY())
                {
                    collisions.put(Side.DOWN, true);
                    collisions.put(Side.LEFT, false);
                    collisions.put(Side.RIGHT, false);
                }
                else if(other.getBox().getMin().getY() > position.getY())
                {
                    collisions.put(Side.UP, true);
                    collisions.put(Side.LEFT, false);
                    collisions.put(Side.RIGHT, false);
                }
                
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
                
            case SPIKE:
                if(velocity.getY() > 0 || position.getY() < location.getY())
                    return false;
                // fallthrough
            case PHYSICAL:
                velocity = velocity.normalized().mul(-5);
                position = position.add(new Vector(0, 0.1));
                // fallthrough
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
