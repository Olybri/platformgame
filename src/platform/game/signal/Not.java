package platform.game.signal;// Created by Loris Witschard on 25.11.16.

/**
 * Signal which is the invert of another signal
 */
public class Not implements Signal
{
    private final Signal signal;
    
    /**
     * @param signal signal to invert
     */
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