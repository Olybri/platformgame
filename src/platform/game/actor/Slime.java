package platform.game.actor;

import platform.game.Damage;
import platform.util.*;

/**
 * Created by Saralfddin on 05.12.16.
 */
public class Slime extends Actor {
    private Vector destination;
    private Box box;
    private Animation animationRight;
    private Animation animationLeft;

    private double velocity;
    private boolean alive = true;
    private double cooldown;
    private double cooldownMax = 0.3;
    private double current = 0;
    private boolean forward = true;
    private boolean right;
    private final String[] rightSprites = {"slime.right.1", "slime.right.2", "slime.right.3", "slime.right.2"};
    private final String[] leftSprites = {"slime.left.1", "slime.left.2", "slime.left.3", "slime.left.2"};

    public Slime(Box box, Vector destination, double velocity, boolean right) {
        if (box == null || destination == null)
            throw new NullPointerException();
        this.box = box;
        this.destination = destination;
        this.velocity = velocity;
        this.right = right;
        priority = 50;
        animationRight = new Animation(this.rightSprites, this.getPosition(), 0.3, true);
        animationLeft = new Animation(this.leftSprites, this.getPosition(), 0.3, true);
    }

    @Override
    public void update(Input input) {
        super.update(input);
        animationRight.update(input);
        animationLeft.update(input);

        if (alive) {
            if (right)
                // sprite = getSprite("slime.right.1");
                sprite = getSprite(animationRight.getSprite());
            else
                // sprite = getSprite("slime.left.1");
                sprite = getSprite(animationLeft.getSprite());

            if (forward) {
                current += input.getDeltaTime() * velocity;
                if (current > 1.0) {
                    current = 1.0;
                    forward = false;
                    right = !right;
                }
            } else {
                current -= input.getDeltaTime() * velocity;
                if (current < 0.0) {
                    current = 0.0;
                    forward = true;
                    right = !right;
                }
            }
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

    /**
     * Once the slime is getting hit by an attack from the player (i.e. : fireball) the slime die
     * @param input
     * @param output
     */
    @Override
    public void draw (Input input, Output output) {
        double factor = 0;
        if (cooldown > 0) {
            if (!alive) {
                factor = Math.cos((cooldownMax - cooldown) * 10) * cooldown / 3 / cooldownMax;
                if (right)
                    sprite = getSprite("slime.right.dead");
                else
                    sprite = getSprite("slime.left.dead");
            }
        }

        Box newBox = new Box(getBox().getCenter(), box.getWidth(), box.getHeight() + (box.getHeight() * factor));
        output.drawSprite(sprite, newBox);
    }
}
