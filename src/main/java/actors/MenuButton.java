package actors;

import game.Stage;

public class MenuButton extends Actor {
    int buttonWidth;
    int buttonHeight;
    public MenuButton(Stage canvas, int width, int height, int positionX, int positionY, String imageName) {
        super(canvas, width, height, 0, 0);
        buttonHeight = height;
        buttonWidth = width;
        posX = positionX;
        posY = positionY;
        sprites = new String[]{imageName + ".png"};
    }

    public boolean contains(int x, int y) {
        return ((x > posX && x <= posX + buttonWidth) && (y > posY && y <= posY + buttonHeight));
    }
}
