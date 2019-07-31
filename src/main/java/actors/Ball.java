/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package actors;

import game.Stage;

/**
 *
 * @author Eric
 */
public class Ball extends Actor {
    
    public Ball(Stage canvas) {
        super(canvas, 20, 20, 20, 20);
        sprites = new String[] {"ball.png"};
        frame = 0;
        
        posX = Stage.WIDTH / 2;
        posY = Stage.HEIGHT / 2;
        
        vx = -3;
    }
    
    public void update() {
        super.update();
        posX += vx;
        posY += vy;
        
        if( posY > (Stage.HEIGHT - getHeight()) || posY < 0) {
            vy = -vy;
        }
        
        
    }
    
    public void collision(Actor a) {		
	vx = -vx;
        
        Paddle paddle = (Paddle)a;
        vy += paddle.vy;
    }

}
