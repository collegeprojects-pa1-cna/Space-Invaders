package actors;

import game.Stage;

public class Pothole extends Actor {
    public Pothole(Stage canvas) {
        super(canvas, 80, 80, 80, 80);

        vy = 10;

        sprites = new String[] {"pothole.png"};

        posX = 500;
        posY = -500;
    }


    public void update() {
        super.update();
        posX += vx;
        posY += vy;
    }


}
