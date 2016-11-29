package platform.game.actor;// Created by Loris Witschard on 11/29/2016.

import platform.util.Box;
import platform.util.Input;
import platform.util.Vector;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class AntiPlayer extends Actor
{
    private Player player;
    private final double delta;
    
    private LinkedHashMap<Double, Vector> positions = new LinkedHashMap<>();
    
    private Vector currentPosition;
    private Vector nextPosition;
    private double nextTime = 0;
    private double time = 0;
    
    private final double SIZE = 0.75;
    
    public AntiPlayer(Player player, double delta)
    {
        this.player = player;
        this.delta = delta;
        
        nextPosition = player.getPosition();
        currentPosition = nextPosition;
        
        priority = 900;
    }
    
    @Override
    public void update(Input input)
    {
        super.update(input);
    
        time += input.getDeltaTime();
        sprite = time < delta ? null : getSprite("box.item");
        
        positions.put(time + delta, player.getPosition());
    
        if(time >= nextTime)
            currentPosition = nextPosition;
        
        Iterator it = positions.entrySet().iterator();
        while(it.hasNext())
        {
            Map.Entry next = (Map.Entry) it.next();
            if((double) next.getKey() > time)
            {
                nextTime = (double) next.getKey();
                nextPosition = (Vector) next.getValue();
                break;
            }
            it.remove();
        }
    }
    
    @Override
    public Box getBox()
    {
        return new Box(currentPosition, SIZE, SIZE);
    }
}
