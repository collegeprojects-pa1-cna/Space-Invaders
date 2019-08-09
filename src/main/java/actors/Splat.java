package actors;

import game.Stage;

import java.util.Random;

public class Splat extends Actor {
    Random rand;
    public Splat(Stage canvas) {
        super(canvas, 128, 98, 100, 100);
        sprites = new String[] {"hit_2.png"};
        rand = new Random();
    }

    public void update() {
        super.update();
    }

    public void randomizeHit() {
        sprites[0] = "hit_" + rand.nextInt(3) + ".png";
    }

}
