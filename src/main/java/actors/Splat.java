package actors;

import game.Stage;

public class Splat extends Actor {
    public Splat(Stage canvas) {
        super(canvas, 100, 100, 100, 100);
        sprites = new String[] {"splat.jpg"};
    }

    public void update() {
        super.update();
    }

}
