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
    private double cooldown;
    private double cooldownMax = 0.3;
    private double current = 0;

    public Slime(Box box, Vector destination, double velocity) {
        if (box == null || destination == null)
            throw new NullPointerException();
        this.box = box;
        this.destination = destination;
        this.velocity = velocity;
        priority = 50;
    }

    @Override
    public void update(Input input) {
        super.update(input);

        if (alive) {
            sprite = getSprite("slime.left.1");


        } else if (cooldown <= 0){
            getWorld().unregister(this);
        }

        cooldown -= input.getDeltaTime();
    }

    @Override
    public Box getBox() {
        Vector position = box.getCenter();
        double multiplier = -2 * Math.pow(current, 3) + 3 * Math.pow(current, 2);
        return new Box(position.add((destination.sub(position)).mul(multiplier)), box.getWidth(), box.getHeight());
    }

    @Override
    public void interact(Actor other) {
        super.interact(other);

        if (alive && cooldown <= 0 && getBox().isColliding(other.getBox()))
            if (other.hurt(this, Damage.PHYSICAL, 0.2, getBox().getCenter()))
                cooldown = cooldownMax;
    }

    @Override
    public boolean hurt(Actor instigator, Damage type, double amount, Vector location) {
       if (type == Damage.FIRE) {
           alive = false;
           cooldown = cooldownMax;
           return true;
       }
       return false;
    }

    @Override
    public void draw (Input input, Output output) {
        double factor = 0;
        if (cooldown > 0) {
            if (!alive) {
                factor = Math.cos((cooldownMax - cooldown) * 10) * cooldown / 3 / cooldownMax;
                sprite = getSprite("slime.left.1");
            }
        }

        Box newBox = new Box(box.getCenter(), box.getWidth(), box.getHeight() + (box.getHeight() * factor));
        output.drawSprite(sprite, newBox);
    }
}
