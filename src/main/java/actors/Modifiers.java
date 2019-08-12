package actors;

import game.Stage;

import java.util.Random;

public class Modifiers extends Actor {
    // Attributes
    String modifierType;
    int healthIncrease;
    int points;
    double speedIncrease;


    //TODO: Update this so it takes in posX and posY
    public Modifiers(Stage canvas, String modifierType) {
        super(canvas, 32, 32, 32, 32);
        Random randomInt = new Random();

        vy = 10;

        posX = randomInt.nextInt(500);
        posY = -500;

        setModifier(modifierType);
    }

    /**
     * Set hazard method takes in hazard type and creates a specific hazard then modifies damage attribute accordingly
     * @param modifierType
     */
    private void setModifier(String modifierType){

        sprites = new String[] {modifierType + ".png"};
        this.modifierType = modifierType;

        if ("health_s".equals(modifierType)) {
            setHealthIncrease(10); // pothole damage value
            setPoints(20);
        } else if ("health_l".equals(modifierType)) {
            setHealthIncrease(100); // moose damage value
            setPoints(50);
        } else if ("speed_increase".equals(modifierType)) {
            setSpeedIncrease(.25); // default value
            setPoints(50);
        }
    }

    public String getModifierType(){
        return modifierType;
    }

    public int getHealthIncrease() {
        return healthIncrease;
    }

    public double getSpeedIncrease(){
        return speedIncrease;
    }

    public int getPoints(){
        return points;
    }

    /**
     * Setter for health attribute
     * @param healthIncrease
     */
    private void setHealthIncrease(int healthIncrease){
        this.healthIncrease = healthIncrease;
    }

    private void setPoints(int points){
        this.points = points;
    }

    /**
     * Setter for speed attribute
     * @param speedIncrease
     */
    private void setSpeedIncrease(double speedIncrease) {
        this.speedIncrease = speedIncrease;
    }

    public void update() {
        super.update();
        posX += vx;
        posY += vy;
    }

}
