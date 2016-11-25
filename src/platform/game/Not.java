package platform.game;// Created by Loris Witschard on 25.11.16.

public class Not implements Signal
{
    private final Signal signal;
    
    public Not(Signal signal)
    {
        if(signal == null)
            throw new NullPointerException();
        this.signal = signal;
    }
    
    @Override
    public boolean isActive()
    {
        return !signal.isActive();
    }
}