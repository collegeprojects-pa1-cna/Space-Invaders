package game;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


import javax.swing.*;

import actors.*;

public class DriveDemo extends Stage implements KeyListener {

    private static final long serialVersionUID = 1L;


    private InputHandler keyPressedHandlerLeft;
    private InputHandler keyReleasedHandlerLeft;

    public long usedTime;//time taken per game step
    public BufferStrategy strategy;	 //double buffering strategy
    public int roadVerticalOffset;
    Graphics2D g;

    //hazardsList
    private List<Hazards> hazards = new ArrayList<Hazards>();

    private Splat splat;
    private int splatFrames;
    private Car car;
    private HealthBar healthBar;

    JPanel panel;
    private Light light;
    private Light light2;
    private Light light3;
    private Light light4;

    private MenuButton retryButton;
    private MenuButton quitButton;

    public DriveDemo() {
        //init the UI
        setBounds(0,0,Stage.WIDTH, Stage.HEIGHT);
        setBackground(Color.BLUE);

        panel = new JPanel();
        panel.setPreferredSize(new Dimension(Stage.WIDTH, Stage.HEIGHT));
        panel.setLayout(null);

        panel.add(this);

        JFrame frame = new JFrame("Driving Game");
        frame.add(panel);

        frame.setBounds(0,0,Stage.WIDTH,Stage.HEIGHT);
        frame.setResizable(false);
        frame.setVisible(true);

        //cleanup resources on exit
        frame.addWindowListener( new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                ResourceLoader.getInstance().cleanup();
                System.exit(0);
            }
        });


        addKeyListener(this);

        //create a double buffer
        createBufferStrategy(2);
        strategy = getBufferStrategy();
        requestFocus();
        initWorld();

        keyPressedHandlerLeft = new InputHandler(this, car);
        keyPressedHandlerLeft.action = InputHandler.Action.PRESS;
        keyReleasedHandlerLeft = new InputHandler(this, car);
        keyReleasedHandlerLeft.action = InputHandler.Action.RELSEASE;

        roadVerticalOffset = 0;
    }



    public void initWorld() {
        car = new Car(this, Car.ePlayerNumber.PN_ONE);
        healthBar = new HealthBar(this, 128, 32, 0, 0);
        hazards = new ArrayList<Hazards>();
//        hazards = new Hazards(this, "moose");
        spawnHazard("pothole");
        light = new Light(this,300, 300, 0, 0, -90, 0);
        light2 = new Light(this,300, 300, 0, 0, 520, -500);
        light3 = new Light(this,300, 300, 0, 0, -90, -1500);
        light4 = new Light(this,300, 300, 0, 0, 520, -2000);

        retryButton = new MenuButton(this, 256, 128, Stage.WIDTH/3 - 13, 450, "retry");
        quitButton = new MenuButton(this, 256, 128, Stage.WIDTH/3 - 13, 600, "quit");

        //get the graphics from the buffer
        g = (Graphics2D) strategy.getDrawGraphics();
    }

    public void paintWorld() {

                //init image to background

        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());
        g.drawImage(ResourceLoader.getInstance().getSprite("road-hotline.png"), 0, roadVerticalOffset - Stage.HEIGHT, this);
        g.drawImage(ResourceLoader.getInstance().getSprite("road-hotline.png"), 0, roadVerticalOffset, this);

        //paint the actors
        for (int i = 0; i < actors.size(); i++) {
            Actor actor = actors.get(i);
            actor.paint(g);
        }

        car.paint(g);

        float transparentAlpha = (float) 0.4;
        float opaqueAlpha = (float) 1.0; //draw half transparent
        AlphaComposite transparent = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,transparentAlpha);
        AlphaComposite opaque = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,opaqueAlpha);
        g.setComposite(transparent);

        light.paint(g);
        light2.paint(g);
        light3.paint(g);
        light4.paint(g);
        g.setComposite(opaque);

        healthBar.paint(g);

        if( splat != null ) {
            splat.paint(g);
        }

        if(isGameOver()){
            retryButton.paint(g);
            quitButton.paint(g);
        }
        paintFPS(g);
        //swap buffer
        strategy.show();


    }

    public void paintFPS(Graphics g) {
        g.setColor(Color.RED);
        if (usedTime > 0)
            g.drawString(String.valueOf(1000/usedTime)+" fps",Stage.HEIGHT-50,0);
        else
            g.drawString("--- fps",Stage.HEIGHT-50,0);
    }

    public void paint(Graphics g) {}

    /**
     * Spawn hazards method
     * Takes in desired hazard type as a string, creates a new hazard object and appends it to the hazards array.
     * @param hazardType
     */
    private void spawnHazard(String hazardType){
        Hazards hazard = new Hazards(this, hazardType);
        actors.add(hazard);
    }

    private void spawnModifier(String modifierType){
        Modifiers modifier = new Modifiers(this, modifierType);
        actors.add(modifier);
    }

    public void updateWorld() {
        if(isGameOver()){
            retryButton.update();
            quitButton.update();
        }

        roadVerticalOffset += 10;
        roadVerticalOffset %= Stage.HEIGHT;

        car.update();
        light.update();
        light2.update();
        light3.update();
        light4.update();
        healthBar.updateHealthbar(car.getHealth());


        //TODO: Possibly handle both modifiers and hazards into the actor array

        // Updating hazards position
        for (int i = 0; i < actors.size(); i++) {
            Actor actor = actors.get(i);
            actor.update();
            if (actor.getY() > Stage.HEIGHT){
                actor.setMarkedForRemoval(true);
            }
            if (actor.isMarkedForRemoval()){
                actors.remove(i);
            }
        }

        if( splat != null ) {
            splat.update();
            splatFrames++;

            if( splatFrames > 60) {
                splat = null;
            }
        }
    }

    private void checkCollision() {
        for (int i = 0; i < actors.size(); i++) {
            Actor actor = actors.get(i);
            if( car.getBounds().intersects(actor.getBounds()) ) {

                if ( actor instanceof Hazards ){
                    Hazards hazard = (Hazards) actor;
                    car.reduceHealth(hazard.dealDamage());
                    if (car.getHealth() <= 0) {
                        gameOver();
                    }

                    if( splat == null) {
                        splat = new Splat(this);
                        splat.setX(car.getX());
                        splat.setY(car.getY());

                        splatFrames = 0;
                    } // end splat

                } // end instance of Hazards

                //TODO: Add actor instance of modifier
                else if ( actor instanceof Modifiers ){
                    Modifiers modifier = (Modifiers) actor;
                    if ( modifier.getModifierType().contains("health") ){
//                        car.setHealth(car.getHealth() + modifier.getHealthIncrease());
                        car.reduceHealth( - modifier.getHealthIncrease() );
                    }
                    else if ( modifier.getModifierType().contains("speed") ){
                        car.setModifier( modifier.getSpeedIncrease() );
                    }
                }

                actor.setMarkedForRemoval(true);
            } // end car intersects
        }
    }

    private void gameOver(){
        endGame();
    }

    public void loopSound(final String name) {
        new Thread(new Runnable() {
            public void run() {
                ResourceLoader.getInstance().getSound(name).loop();
            }
        }).start();
    }


    public void game() {
        //loopSound("music.wav");
        usedTime= 0;
        Random randomValueSelector = new Random();

//===================================================GAME LOOP==========================================================

        while(!isGameOver()) {
            //TODO: Change 900 to a dynamic variable that adjusts depending on score

            // Spawn hazards
            if ( randomValueSelector.nextInt(1000) > 990 ) {
                int currentHazardSelection = randomValueSelector.nextInt(100);
                if ( currentHazardSelection < 80 ) {
                    spawnHazard("pothole");
                } else if ( currentHazardSelection > 80 ) {
                    spawnHazard("moose");
                }
            } // end spawn hazards

            // Spawn modifiers
            if ( randomValueSelector.nextInt(1000) > 990 ) {
                int currentHazardSelection = randomValueSelector.nextInt(100);
                if ( currentHazardSelection < 20 ) {
                    spawnModifier("health_s");
                } else if ( currentHazardSelection > 80 ) {
                    spawnModifier("health_l");
                } else if ( currentHazardSelection > 40 && currentHazardSelection < 50) {
                    spawnModifier("speed_increase");
                }
            } // end spawn modifiers


            // Debug code - Will use this in the future for testing actor array
            //System.out.println(hazards.size());
            long startTime = System.currentTimeMillis();

            checkCollision();
            updateWorld();
            paintWorld();
            if (isGameOver()){
                break;
            }

            usedTime = System.currentTimeMillis() - startTime;

            //calculate sleep time
            if (usedTime == 0) usedTime = 1;
            int timeDiff = 1000/DESIRED_FPS - (int)(usedTime);
            if (timeDiff > 0) {
                try {
                    Thread.sleep(timeDiff);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            usedTime = System.currentTimeMillis() - startTime;
        } // end loop
    }

    public void keyPressed(KeyEvent e) {
        keyPressedHandlerLeft.handleInput(e);

        if( e.getKeyCode() == KeyEvent.VK_K) {
            Actor.debugCollision = !Actor.debugCollision;
        }

        //keyPressedHandlerRight.handleInput(e);
    }

    public void keyReleased(KeyEvent e) {
        keyReleasedHandlerLeft.handleInput(e);
        //keyReleasedHandlerRight.handleInput(e);
    }

    public void keyTyped(KeyEvent e) {
    }

}