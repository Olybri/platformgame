package platform.game.signal;// Created by Loris Witschard on 11/28/2016.

/**
 * Constant signal that never changes
 */
public class Constant implements Signal
{
    private final boolean value;
    
    /**
     * @param value default value
     */
    public Constant(boolean value)
    {
        this.value = value;
    }
    
    @Override
    public boolean isActive()
    {
        return value;
    }
}
