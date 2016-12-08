package platform.game.actor; // Created by Saralfddin on 07.12.16.

/**
 * Class that gives a sprite name depending on the time
 */
public class Animation
{
    private String[] sprites;
    
    private double frameDuration;
    private double elapsedTime = 0;
    private boolean once;
    private int index = 0;
    
    /**
     * @param sprites array of different sprite names
     * @param frameDuration delay between each frame
     * @param once true to play the animation just once, false otherwise
     */
    public Animation(String[] sprites, double frameDuration, boolean once)
    {
        this.sprites = sprites;
        this.frameDuration = frameDuration;
        this.once = once;
    }
    
    /**
     * Update the current frame index depending on the time
     *
     * @param dt time elapsed since last frame
     */
    public void update(double dt)
    {
        elapsedTime += dt;
        
        index = (int) Math.floor(elapsedTime / frameDuration);
        
        if(once)
        {
            if(index > sprites.length - 1)
                index = sprites.length - 1;
        } else
        {
            if(index > sprites.length - 1)
            {
                index = 0;
                elapsedTime = 0;
            }
        }
    }
    
    /**
     * @return sprite name associated to current frame
     */
    public String getSprite()
    {
        return sprites[index];
    }
}
