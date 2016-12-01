package platform.game.actor;// Created by Loris Witschard on 25.11.16.

import platform.util.Box;
import platform.util.Input;
import platform.util.Output;
import platform.util.Vector;

public abstract class Particle extends Actor
{
    private String[] sprites;
    private double frameDuration;
    private double elapsedTime = 0;
    
    private Vector position;
    private double[] sizes;
    private int index = 0;
    
    public Particle(Vector position, double[] sizes, double frameDuration, String[] sprites)
    {
        if(position == null)
            throw new NullPointerException();
        this.position = position;
        this.sizes = sizes;
        this.frameDuration = frameDuration;
        
        this.sprites = sprites;
        priority = 900;
    }
    
    @Override
    public void update(Input input)
    {
        super.update(input);
        elapsedTime += input.getDeltaTime();
        
        index = (int) Math.floor(elapsedTime / frameDuration);
        
        if(index > sprites.length - 1)
        {
            index = sprites.length - 1;
            getWorld().unregister(this);
        }
    }
    
    @Override
    public Box getBox()
    {
        return new Box(position, sizes[index], sizes[index]);
    }
    
    @Override
    public void draw(Input input, Output output)
    {
        output.drawSprite(getSprite(sprites[index]), getBox());
    }
}
