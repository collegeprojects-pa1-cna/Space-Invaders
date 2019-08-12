package actors;

import game.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Hazards extends Actor {
    private List<String> potholeImages;
    private List<String> mooseImages;
    private List<String> mooseImagesRev;

    // Attributes
    private int damage;

    //TODO: Update this so it takes in posX and posY
    public Hazards(Stage canvas, String hazardType, int width, int height, int collisionW, int collisionH) {
        super(canvas, width, height, collisionW, collisionH);
        Random randomInt = new Random();

        vy = 10;

        posX = randomInt.nextInt(500);
        posY = -500;

        potholeImages = new ArrayList<String>();
        mooseImages = new ArrayList<String>();
        mooseImagesRev = new ArrayList<String>();

        potholeImages.add("pothole");

        mooseImages.add("moose_0");
        mooseImages.add("moose_1");
        mooseImages.add("moose_2");
        mooseImages.add("moose_3");
        mooseImages.add("moose_4");
        mooseImages.add("moose_5");

        setHazard(hazardType);
    }

    /**
     * Set hazard method takes in hazard type and creates a specific hazard then modifies damage attribute accordingly
     */
    private void setHazard(String hazardType){
        //TODO: Possibly make hazards decrease score?
        if ("pothole".equals(hazardType)) {
            setDamage(10); // pothole damage value
            sprites = new String[] {"pothole.png"};
        } else if ("moose".equals(hazardType)) {
            setDamage(100); // moose damage value
            frameSpeed = 10;
            setSprites(mooseImages);
        } else {
            setDamage(10); // default value
        }
    }

    private void setSprites(List<String> hazardImages) {
        sprites= new String[hazardImages.size()];
        for (int i = 0; i < hazardImages.size(); i++) {
            sprites[i] = hazardImages.get(i) + ".png";
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
