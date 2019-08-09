package actors;

import game.Stage;

public class MenuButton extends Actor {
    public MenuButton(Stage canvas, int width, int height, int positionX, int positionY, String imageName) {
        super(canvas, width, height, 0, 0);
        posX = positionX;
        posY = positionY;
        sprites = new String[]{imageName + ".png"};
    }
}
