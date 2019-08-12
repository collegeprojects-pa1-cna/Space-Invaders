package actors;

import game.Stage;

import java.util.Random;

public class Hazards extends Actor {

    // Attributes
    private int damage;

    //TODO: Update this so it takes in posX and posY
    public Hazards(Stage canvas, String hazardType) {
        super(canvas, 80, 80, 80, 80);
        Random randomInt = new Random();

        vy = 10;

        posX = randomInt.nextInt(500);
        posY = -500;


        setHazard(hazardType);
    }

    /**
     * Set hazard method takes in hazard type and creates a specific hazard then modifies damage attribute accordingly
     * @param hazardType
     */
    private void setHazard(String hazardType){
        //TODO: Possibly make hazards decrease score?
        sprites = new String[] {hazardType + ".png"};

        if ("pothole".equals(hazardType)) {
            setDamage(10); // pothole damage value
        } else if ("moose".equals(hazardType)) {
            setDamage(100); // moose damage value
        } else {
            setDamage(10); // default value
        }
    }

    /**
     * Setter for damage attribute
     * @param damage
     */
    private void setDamage(int damage){
        this.damage = damage;
    }

    /**
     * Getter for damage attribute
     * @return
     */
    public int dealDamage(){
        return damage;
    }

    public void update() {
        super.update();
        posX += vx;
        posY += vy;
    }

}
