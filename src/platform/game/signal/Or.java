package platform.game.signal;// Created by Loris Witschard on 25.11.16.

public class Or implements Signal
{
    private final Signal left;
    private final Signal right;
    
    public Or(Signal left, Signal right)
    {
        if(left == null || right == null)
            throw new NullPointerException();
        
        this.left = left;
        this.right = right;
    }
    
    @Override
    public boolean isActive()
    {
        return left.isActive() || right.isActive();
    }
}