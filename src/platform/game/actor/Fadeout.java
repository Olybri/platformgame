package platform.game.actor;// Created by Loris Witschard on 11/29/2016.

import platform.util.Input;
import platform.util.Output;

public class Fadeout extends Actor
{
    private double countdown;
    private double countdownMax;
    private double delay;
    
    public Fadeout(double time, double delay)
    {
        countdownMax = time;
        countdown = countdownMax;
        this.delay = delay;
        
        priority = 2000;
    }
    
    @Override
    public void update(Input input)
    {
        super.update(input);
        
        countdown -= input.getDeltaTime();
    }
    
    @Override
    public void draw(Input input, Output output)
    {
        if(countdown < countdownMax - delay)
            output.drawSprite(getSprite("pixel.black"), output.getBox(), 0,
                (countdownMax - countdown - delay) / (countdownMax - delay));
    }
}
