package platform.game.actor;// Created by Loris Witschard on 22.11.16.

import platform.game.Command;
import platform.game.Damage;
import platform.game.World;
import platform.util.*;

import java.util.HashMap;

/**
 * Player controlled by inputs.
 */
public class Player extends Actor
{
    private Vector position;
    private Vector velocity = Vector.ZERO;
    private final double SIZE = 0.75;
    
    private double healthMax = 1;
    private double health = healthMax;
    private boolean hasMoved = false;
    private boolean dead = false;
    private HashMap<Side, Boolean> collisions = new HashMap<>();
    
    private double hurtCooldown = 0;
    private final double hurtCooldownMax = 0.75;
    private final double deathCooldownMax = 2;
    private final double hurtDelay = 0.3;
    
    private double inputCooldown = 1;
    
    private enum Side
    {
        LEFT,
        RIGHT,
        UP,
        DOWN
    }
    
    /**
     * @param position initial position
     */
    public Player(Vector position)
    {
        if(position == null)
            throw new NullPointerException();
        
        this.position = position;
        
        priority = 40;
    }
    
    /**
     * @return current health of the player
     */
    public double getHealth()
    {
        return health;
    }
    
    /**
     * @return maximum health of the player
     */
    public double getHealthMax()
    {
        return healthMax;
    }
    
    /**
     * @return true if the player has moved since his instantiation, false otherwise
     */
    public boolean hasMoved()
    {
        return hasMoved;
    }
    
    /**
     * Add or subtract health to the player
     *
     * @param value quantity of health added, can be negative
     * @return true if the current health has changed, false otherwise
     */
    private boolean addHealth(double value)
    {
        if(value < 0)
        {
            if(hurtCooldown > hurtCooldownMax - hurtDelay)
                return false;
            hurtCooldown = hurtCooldownMax;
        }
        else if(health >= healthMax)
            return false;
        
        health = Math.min(healthMax, health + value);
        return true;
    }
    
    @Override
    public void register(World world)
    {
        super.register(world);
        
        world.register(new Overlay(this));
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
        
        hurtCooldown -= input.getDeltaTime();
        inputCooldown -= input.getDeltaTime();
        
        Command.enable(hurtCooldown <= 0 && inputCooldown <= 0);
        
        if(health <= 0.05 && !dead)
        {
            dead = true;
            hurtCooldown = deathCooldownMax;
            getWorld().register(new Fadeout(deathCooldownMax, 1));
        }
        
        if(dead && hurtCooldown <= 0)
        {
            getWorld().nextLevel();
            return;
        }
        
        if(collisions.get(Side.DOWN))
        {
            double scale = Math.pow(0.0005, input.getDeltaTime());
            velocity = new Vector(velocity.getX() * scale, velocity.getY());
        }
        
        double maxSpeed = 4.0;
        if(Command.isButtonDown("walk_right"))
        {
            hasMoved = true;
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
            hasMoved = true;
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
        {
            hasMoved = true;
            velocity = new Vector(velocity.getX(), 7.0);
        }
        
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
                if(!dead
                    && other.getBox().getMin().getX() < getBox().getMin().getX()
                    && other.getBox().getMin().getY() < getBox().getMin().getY()
                    && other.getBox().getMax().getX() > getBox().getMax().getX()
                    && other.getBox().getMax().getY() > getBox().getMax().getY())
                    addHealth(-0.5);
                
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
        if(instigator == this || dead)
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
                addHealth(-amount);
                return true;
            
            case FIRE:
                velocity = new Vector(velocity.getX(), 3.0);
                position = position.add(new Vector(0, 0.1));
                addHealth(-amount);
                return true;
            
            case HEAL:
                return addHealth(amount);
            
            case ACTIVATION:
                return true;
            
            default:
                return super.hurt(instigator, type, amount, location);
        }
    }
    
    @Override
    public void draw(Input input, Output output)
    {
        double size = SIZE;
        if(hurtCooldown > 0)
            if(!dead)
            {
                size += +SIZE * Math.cos((hurtCooldownMax - hurtCooldown) * 10) * hurtCooldown / 3 / hurtCooldownMax;
                sprite = getSprite("blocker.sad");
            }
            else
            {
                size += +SIZE * Math.cos((deathCooldownMax - hurtCooldown) * 10) * hurtCooldown / 3 / deathCooldownMax;
                sprite = getSprite("blocker.dead");
            }
        else
            sprite = getSprite("blocker.happy");
        
        Box box = new Box(position, size, size);
        
        double angle = velocity.getX() / 16;
        if(dead)
            angle = Math.min((deathCooldownMax - hurtCooldown) * 2, Math.PI / 2) * (velocity.getX() > 0 ? -1 : 1);
        
        output.drawSprite(sprite, box, angle);
    }
}
