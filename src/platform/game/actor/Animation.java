package platform.game.actor;

import platform.util.Input;
import platform.util.Output;
import platform.util.Vector;

/**
 * Created by Saralfddin on 07.12.16.
 */
public class Animation {
    private String[] sprites;
    private Vector position;

    private double frameDuration;
    private double elapsedTime = 0;
    private boolean once;
    private int index = 0;

    public Animation(String[] sprites, Vector position, double frameDuration, boolean once) {
        this.sprites = sprites;
        this.position = position;
        this.frameDuration = frameDuration;
        this.once = once;
    }

    public void update(Input input) {
        elapsedTime += input.getDeltaTime();

        index = (int) Math.floor(elapsedTime / frameDuration);

        if (once)
            if (index > sprites.length - 1)
                index = sprites.length - 1;
        else
            if (index >= sprites.length)
                index = 0;
    }

    public String getSprite() {
        return sprites[index];
    }
}
