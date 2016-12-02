package platform.game.signal;// Created by Loris Witschard on 11/28/2016.

public class Constant implements Signal
{
    private final boolean value;
    
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
