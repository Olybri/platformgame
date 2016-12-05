package platform.game.actor;

import platform.game.Damage;
import platform.util.*;

/**
 * Created by Saralfddin on 05.12.16.
 */
public class Slime extends Actor {
    private Vector destination;
    private Box box;
    private double velocity;
    private boolean alive = true;
    private final double SIZE = 1;
    private double cooldown;
    private double cooldownMax = 0.3;
    private double current = 0;

    public Slime(Box box, Vector destination, double velocity) {
        this.box = box;
        this.destination = destination;
        this.velocity = velocity;
    }

    @Override
    public void update(Input input) {
        super.update(input);

        if (alive) {
            sprite = getSprite("slime.left.1");

            if (current == 0)
                current += input.getDeltaTime() * velocity;
            if (current == 1.0)
                current -= input.getDeltaTime() * velocity;
        }

        cooldown -= input.getDeltaTime();

    }

    @Override
    public Box getBox() {
        Vector position = box.getCenter();
        double multiplier = -2 * Math.pow(current, 3) + 3 * Math.pow(current, 2);
        return new Box(position.add((destination.sub(position)).mul(multiplier)), SIZE, SIZE);
    }

    @Override
    public void interact(Actor other) {
        super.interact(other);

        if (alive && cooldown <= 0 && getBox().isColliding(other.getBox()))
            if (other.hurt(this, Damage.PHYSICAL, 0.2, getBox().getCenter()))
                cooldown = cooldownMax;
    }

    @Override
    public void draw(Input input, Output output) {
        if (sprite != null)
            output.drawSprite(sprite, getBox(), velocity, 0);
    }
}
