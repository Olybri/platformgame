package platform.game.actor;// Created by Loris Witschard on 25.11.16.

import platform.util.Box;
import platform.util.Input;
import platform.util.Output;
import platform.util.Vector;

/**
 * Abstract particle that displays an animation before disappearing.
 */
public abstract class Particle extends Actor
{
    private String[] sprites;
    private double frameDuration;
    private double elapsedTime = 0;
    
    private Vector position;
    private double[] sizes;
    private int index = 0;
    
    /**
     * @param position      position of the particle
     * @param sprites       sprites to display one after another
     * @param sizes         sizes of each sprite
     * @param frameDuration delay between each frame
     */
    public Particle(Vector position, String[] sprites, double[] sizes, double frameDuration)
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
