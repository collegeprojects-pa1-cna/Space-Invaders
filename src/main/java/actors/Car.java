/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package actors;

import game.Stage;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Eric
 */
public class Car extends Actor implements KeyboardControllable {

    Map<String, Boolean> keysPressed = new HashMap<String, Boolean>();

    public enum ePlayerNumber {
        PN_ONE,
        PN_TWO,
    };

    private ePlayerNumber playerNumber;

    public Car(Stage canvas, ePlayerNumber playerNo) {
        super(canvas, 160,160, 160,160);
        playerNumber = playerNo;
        sprites = new String[]{"Car_Idle.png"};
        frame = 0;
        posX = Stage.WIDTH / 2 - getWidth() / 2;
        posY = Stage.HEIGHT - getHeight() * 2;

        setKeys();
    }

    public void update() {
        super.update();
        updatePlayerCoords();

        posX += vx;
        posY += vy;

        vx *= 0.9;
        vy *= 0.9;

        if( posX > Stage.WIDTH - getWidth() ) {
            posX = Stage.WIDTH - getWidth();
        }
        else if( posX < 0 ) {
            posX = 0;
        }

        if( posY > Stage.HEIGHT - getHeight() ) {
            posY = Stage.HEIGHT - getHeight();
        }
        else if( posY < 0 ) {
            posY = 0;
        }
    }

    @Override
    public void triggerKeyPress(KeyEvent e) {
        if( e.getKeyCode() == KeyEvent.VK_D) {
            keysPressed.put("RIGHT", true);
        }
        if( e.getKeyCode() == KeyEvent.VK_A) {
            keysPressed.put("LEFT", true);
        }
        if( e.getKeyCode() == KeyEvent.VK_S) {
            keysPressed.put("DOWN", true);
        }
        if( e.getKeyCode() == KeyEvent.VK_W) {
            keysPressed.put("UP", true);
        }
    }

    @Override
    public void triggerKeyRelease(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_D) {
            keysPressed.put("RIGHT", false);
        }
        if (e.getKeyCode() == KeyEvent.VK_A) {
            keysPressed.put("LEFT", false);
        }
        if (e.getKeyCode() == KeyEvent.VK_S) {
            keysPressed.put("DOWN", false);
        }
        if (e.getKeyCode() == KeyEvent.VK_W) {
            keysPressed.put("UP", false);
        }
    }

    private void setKeys() {
        keysPressed.put("UP", false);
        keysPressed.put("LEFT", false);
        keysPressed.put("DOWN", false);
        keysPressed.put("RIGHT", false);
    }


    private void updatePlayerCoords() {
        if (keysPressed.get("UP")) {
            vy -= 1;
        }
        if (!keysPressed.get("UP")) {
            vy += 1.5;
        }
        if (keysPressed.get("LEFT")) {
            vx -= 2;
        }
        if (!keysPressed.get("LEFT")) {
            vx += 1.5;
        }
        if (keysPressed.get("DOWN")) {
            vy += 1;
        }
        if (!keysPressed.get("DOWN")) {
            vy -= 1.5;
        }
        if (keysPressed.get("RIGHT")) {
            vx += 2;
        }
        if (!keysPressed.get("RIGHT")) {
            vx -= 1.5;
        }
    }
}
