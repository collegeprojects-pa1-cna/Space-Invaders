/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package actors;

import game.Stage;
import java.awt.event.KeyEvent;

/**
 *
 * @author Eric
 */
public class Car extends Actor implements KeyboardControllable {

    public enum ePlayerNumber {
        PN_ONE,
        PN_TWO,
    };

    private ePlayerNumber playerNumber;

    public Car(Stage canvas, ePlayerNumber playerNo) {
        super(canvas, 125,56, 125,56);
        playerNumber = playerNo;
        sprites = new String[]{"car.png"};
        frame = 0;
        posX = Stage.WIDTH / 2 - getWidth() / 2;
        posY = Stage.HEIGHT - getHeight() * 2;
    }

    public void update() {
        super.update();
        posX += vx * 3;
        posY += vy * 3;

        if( posX > Stage.WIDTH - getWidth() ) {
            posX = Stage.WIDTH - getWidth();
        }
        else if( posX < 0 ) {
            posX = 0;
        }

        if( posY > Stage.HEIGHT - getHeight() ) {
            posY = Stage.HEIGHT - getHeight() * 2;
        }
        else if( posY < 0 ) {
            posY = 0;
        }
    }

    @Override
    public void triggerKeyPress(KeyEvent e) {
        if( e.getKeyCode() == KeyEvent.VK_D) {
            vx = 1;
        }
        else if( e.getKeyCode() == KeyEvent.VK_A) {
            vx = -1;
        }
        else if( e.getKeyCode() == KeyEvent.VK_S) {
            vy = 1;
        }
        else if( e.getKeyCode() == KeyEvent.VK_W) {
            vy = -1;
        }
    }

    @Override
    public void triggerKeyRelease(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_D) {
            vx = 0;
        } else if (e.getKeyCode() == KeyEvent.VK_A) {
            vx = 0;
        }
        if (e.getKeyCode() == KeyEvent.VK_S) {
            vy = 0;
        } else if (e.getKeyCode() == KeyEvent.VK_W) {
            vy = 0;
        }
    }
}
