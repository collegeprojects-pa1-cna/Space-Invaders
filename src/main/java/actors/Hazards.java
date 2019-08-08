package actors;

import game.Stage;

import java.util.Random;

public class Hazards extends Actor {
    //TODO: Update this so it takes in posX and posY
    public Hazards(Stage canvas, String hazardType) {
        super(canvas, 80, 80, 80, 80);
        Random randomInt = new Random();

        vy = 10;

        posX = randomInt.nextInt(500);
        posY = -500;

        int damage;
        setHazard(hazardType);
    }


    private void setHazard(String hazardType){
        //TODO: Change this to call a hashmap or something similar instead of directly plunking the name to the object call
        // this should enable quick assigning of stats
        sprites = new String[] {hazardType + ".png"};

    }

    public void update() {
        super.update();
        posX += vx;
        posY += vy;
    }

}
