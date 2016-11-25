package platform.game.actor;// Created by Loris Witschard on 24.11.16.

import platform.util.Box;
import platform.util.Input;
import platform.util.Output;
import platform.util.Vector;

public class Overlay extends Actor
{
    private Player player;
    final double SIZE = 0.25;
    
    public Overlay(Player player)
    {
        if(player == null)
            throw new NullPointerException();
        
        this.player = player;
        
        priority = 1000;
    }
    
    @Override
    public void update(Input input)
    {
        super.update(input);
        
        if(player.getWorld() == null)
            player.getWorld().unregister(this);
    }
    
    @Override
    public void draw(Input input, Output output)
    {
        double health = (double) Math.round(10.0 * player.getHealth() / player.getHealthMax()) / 2;
        for(int i = 1; i <= 5; ++i)
        {
            String name;
            if(health >= i)
                name = "heart.full";
            else if(health >= i - 0.5)
                name = "heart.half";
            else
                name = "heart.empty";
            
            Vector position = player.getPosition().add(new Vector(SIZE * (i - 3), player.getSize() / 2 + 0.25));
            output.drawSprite(getSprite(name), new Box(position, SIZE, SIZE));
        }
    }
}
