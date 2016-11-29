package platform.game.actor;// Created by Loris Witschard on 22.11.16.

import platform.game.Command;
import platform.game.Damage;
import platform.util.*;

import java.util.HashMap;

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
    
    private double cooldown = 0;
    private final double hurtCooldownMax = 1;
    private final double deathCooldownMax = 2;
    
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
    
    public boolean hasMoved()
    {
        return hasMoved;
    }
    
    private boolean addHealth(double value)
    {
        if(health + value <= 0)
            cooldown = deathCooldownMax;
        else if(value < 0)
            cooldown = hurtCooldownMax;
        else if(health >= healthMax)
            return false;
        
        health = Math.min(healthMax, health + value);
        return true;
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
        
        if(health <= 0 && !dead)
        {
            dead = true;
            Command.enable(false);
            getWorld().register(new Fadeout(deathCooldownMax, 1));
        }
        
        if(dead && cooldown <= 0)
        {
            getWorld().nextLevel();
            Command.enable(true);
            return;
        }
        
        if(collisions.get(Side.DOWN) && collisions.get(Side.UP))
            addHealth(-0.4);
        
        if(collisions.get(Side.LEFT) && collisions.get(Side.RIGHT))
            addHealth(-0.4);
        
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
        if(cooldown > 0)
            if(health > 0)
            {
                size += +SIZE * Math.cos((hurtCooldownMax - cooldown) * 10) * cooldown / 3 / hurtCooldownMax;
                sprite = getSprite("blocker.sad");
            }
            else
            {
                size += +SIZE * Math.cos((deathCooldownMax - cooldown) * 10) * cooldown / 3 / deathCooldownMax;
                sprite = getSprite("blocker.dead");
            }
        else
            sprite = getSprite("blocker.happy");
        
        Box box = new Box(position, size, size);
        
        double angle = velocity.getX() / 16;
        if(dead)
            angle = Math.min((deathCooldownMax - cooldown) * 2, Math.PI / 2) * (velocity.getX() > 0 ? -1 : 1);
        
        output.drawSprite(sprite, box, angle);
    }
}
