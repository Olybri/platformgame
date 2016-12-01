package platform.game.actor;// Created by Loris Witschard on 24.11.16.

import platform.util.Box;
import platform.util.Input;
import platform.util.Output;
import platform.util.Vector;

public class Overlay extends Actor
{
    private Player player;
    private final double SIZE = 0.25;
    
    private double countdown = 0;
    private final double countdownMax = 0.3;
    private double currentHealth = 0;
    
    public Overlay(Player player)
    {
        if(player == null)
            throw new NullPointerException();
        
        this.player = player;
        currentHealth = player.getHealthMax() * 5;
        
        priority = 900;
    }
    
    @Override
    public void update(Input input)
    {
        super.update(input);
        
        countdown -= input.getDeltaTime();
        
        if(player.getWorld() == null)
            player.getWorld().unregister(this);
    }
    
    @Override
    public void draw(Input input, Output output)
    {
        double health = (double) Math.round(10.0 * player.getHealth() / player.getHealthMax()) / 2;
        
        if(currentHealth != health)
            countdown = countdownMax;
        
        currentHealth = health;
        
        for(int i = 1; i <= 5; ++i)
        {
            String name;
            if(health >= i)
                name = "heart.full";
            else if(health >= i - 0.5)
                name = "heart.half";
            else
                name = "heart.empty";
            
            double size = SIZE;
            if(countdown > 0)
                size += countdown / 3;
            
            Vector position = player.getPosition().add(new Vector(size * (i - 3), player.getSize() / 2 + 0.25));
            double angle = 0;
            if(health <= 1)
            {
                position = position.add(new Vector(0, Math.sin(input.getTime() * 20) / 24 * Math.pow(-1, i)));
                angle = Math.sin(input.getTime() * 40) / 10 * Math.pow(-1, i);
            }
            
            output.drawSprite(getSprite(name), new Box(position, size, size), angle);
        }
    }
}
