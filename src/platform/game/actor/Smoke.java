package platform.game.actor;// Created by Loris Witschard on 25.11.16.

import platform.util.Vector;

public class Smoke extends Particle
{
    private final static String[] sprites = {"smoke.gray.1", "smoke.gray.2", "smoke.gray.3"};
    private final static double[] sizes = {0.5, 0.7, 0.8};
    
    public Smoke(Vector position)
    {
        super(position, sizes, 0.1, sprites);
    }
}
