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
    int health;
    double modifier = 1;
    private int score = 0;

    public enum ePlayerNumber {
        PN_ONE,
        PN_TWO,
    };

    private ePlayerNumber playerNumber;

    public Car(Stage canvas, ePlayerNumber playerNo) {
        super(canvas, 160,160, 75,140);
        playerNumber = playerNo;
        sprites = new String[]{"Car_Idle_r.png"};
        frame = 0;
        posX = Stage.WIDTH / 2 - getWidth() / 2;
        posY = Stage.HEIGHT - getHeight() * 2;
        health = 100;
        setKeys();
    }

    public void update() {
        super.update();
        updatePlayerCoords();

        posX += vx;
        posY += vy;

        vx *= 0.9;
        vy *= 0.9;

        if( posX > Stage.WIDTH - getWidth() + 24 ) {
            posX = Stage.WIDTH - getWidth() + 24;
        }
        else if( posX < -40 ) {
            posX = -40;
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
            vy -= 1 * modifier; // Movement up
        }
        if (!keysPressed.get("UP")) {
            vy += 1.5;
        }
        if (keysPressed.get("LEFT")) {
            vx -= 2 * modifier; // Movement left
        }
        if (!keysPressed.get("LEFT")) {
            vx += 1.5;
        }
        if (keysPressed.get("DOWN")) {
            vy += 1 * modifier; // Movement down
        }
        if (!keysPressed.get("DOWN")) {
            vy -= 1.5;
        }
        if (keysPressed.get("RIGHT")) {
            vx += 2 * modifier; // Movement right
        }
        if (!keysPressed.get("RIGHT")) {
            vx -= 1.5;
        }
    }

    public int getHealth() {
        return health;
    }

    public void setModifier(double modifier){
        this.modifier += modifier;
    }

    /**
     * Set health method takes in an integer value and only allows the cars health to be boosted
     * past 100 as long as the health of the car is less than or equal to 100.
     * This effect causes only the large health modifiers to have potential moose negating effects
     * while the smaller health will only provide points at low health.
     * @param health
     */
    public void setHealth(int health) {
        if (this.health <= 100 && (health - getHealth() >= 100) ){
            this.health = health;
        }
        else if (this.health <= 90 ) {
            this.health = health;
        }
    }

    public void changeScore(int score) {
        this.score += score;
    }

    public int getScore() {
        return score;
    }

    public void reduceHealth(int damage) {
        health = health - damage;
    }

}
