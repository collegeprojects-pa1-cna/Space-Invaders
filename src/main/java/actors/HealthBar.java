package actors;

import game.Stage;

public class HealthBar extends Actor {

    public HealthBar(Stage canvas, int width, int height, int collisionWidth, int collisionHeight) {
        super(canvas, width, height, collisionWidth, collisionHeight);
        posX = 50;
        posY = 35;

        sprites = new String[]{"health_100.png"};
        frame = 0;
    }

    public void updateHealthbar(int health)
    {
        if (health <= 0) {
            sprites[0] = "health_0.png";
        }
        else {
            if (health > 200){
                health = 200;
            }
            sprites[0] = "health_" + health + ".png";
        }
    }
}
