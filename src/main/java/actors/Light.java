package actors;

import game.Stage;

public class Light extends Actor {
    static int startingPosY;

    public Light(Stage canvas, int width, int height, int collisionWidth, int collisionHeight, int positionX, int positionY) {
        super(canvas, width, height, collisionWidth, collisionHeight);
        posX = positionX;
        startingPosY = positionY;
        posY = positionY;

        vy = 10;
        sprites = new String[]{"street_light.png"};
        frame = 0;
    }

    public void update() {
        super.update();
        posY += vy;
        if (posY > Stage.HEIGHT) {
            posY = startingPosY;
        }
    }
}
