package platform.game;// Created by Loris Witschard on 22.11.16.

import platform.util.*;

import java.awt.event.KeyEvent;

public class Player extends Actor
{
    private Vector position;
    private Vector velocity;
    private final double SIZE = 0.5;
    
    private boolean colliding = false;
    
    public Player(Vector position, Vector velocity, Sprite sprite)
    {
        if(position == null || velocity == null || sprite == null)
            throw new NullPointerException();
        
        this.position = position;
        this.velocity = velocity;
        this.sprite = sprite;
        
        priority = 42;
    }
    
    @Override
    public Box getBox()
    {
        return new Box(position, SIZE, SIZE);
    }
    
    @Override
    public void update(Input input)
    {
        double maxSpeed = 4.0;
        if(input.getKeyboardButton(KeyEvent.VK_RIGHT).isDown())
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
        if(input.getKeyboardButton(KeyEvent.VK_LEFT).isDown())
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
        if(colliding && input.getKeyboardButton(KeyEvent.VK_UP).isPressed())
            velocity = new Vector(velocity.getX(), 7.0);
        
        if(input.getKeyboardButton(KeyEvent.VK_SPACE).isPressed())
        {
            Sprite s = getWorld().getLoader().getSprite("fireball");
            getWorld().register(new Fireball(position, velocity.add(velocity.resized(2.0)), s));
        }
        
        super.update(input);
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
}
