package platform.game.actor;// Created by Loris Witschard on 12/12/2016.

import platform.util.Box;
import platform.util.Vector;

public class Slime extends Enemy
{
    public Slime(Vector position, Vector destination, boolean right)
    {
        super(new Box(position.sub(new Vector(0, 0.25)), 1, 0.5),
            destination.sub(new Vector(0, 0.25)),
            new Animation(new String[]{"slime.left.1", "slime.left.2", "slime.left.3", "slime.left.2"}, 0.25, false),
            new Animation(new String[]{"slime.right.1", "slime.right.2", "slime.right.3", "slime.right.2"}, 0.25, false),
            "slime.left.dead",
            "slime.right.dead",
            0.3,
            right);
    }
}
