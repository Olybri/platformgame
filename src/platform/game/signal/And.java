package platform.game.signal;// Created by Loris Witschard on 25.11.16.

/**
 * Signal which is the result of a conjunction between two other signals
 */
public class And implements Signal
{
    private final Signal left;
    private final Signal right;
    
    /**
     * @param left first signal
     * @param right second signal
     */
    public And(Signal left, Signal right)
    {
        if(left == null || right == null)
            throw new NullPointerException();
        
        this.left = left;
        this.right = right;
    }
    
    @Override
    public boolean isActive()
    {
        return left.isActive() && right.isActive();
    }
}