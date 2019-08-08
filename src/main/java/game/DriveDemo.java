package game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
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

    //hazardsList
    private List<Hazards> hazards = new ArrayList<Hazards>();

    private Splat splat;
    private int splatFrames;
    private Car car;
    private HealthBar healthBar;

    JPanel gameOverPanel;

    public DriveDemo() {
        //init the UI
        setBounds(0,0,Stage.WIDTH, Stage.HEIGHT);
        setBackground(Color.BLUE);

        JPanel panel = new JPanel();
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
    }

    public void paintWorld() {

        //get the graphics from the buffer
        Graphics g = strategy.getDrawGraphics();
        //init image to background
        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());



        g.drawImage(ResourceLoader.getInstance().getSprite("road-hotline.png"), 0, roadVerticalOffset - Stage.HEIGHT, this);
        g.drawImage(ResourceLoader.getInstance().getSprite("road-hotline.png"), 0, roadVerticalOffset, this);

        //load subimage from the background

        //paint the actors
        for (int i = 0; i < hazards.size(); i++) {
            Hazards hazard = hazards.get(i);
            hazard.paint(g);
        }

        car.paint(g);
        healthBar.paint(g);


        if( splat != null ) {
            splat.paint(g);
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
        hazards.add(hazard);
    }

    public void updateWorld() {

        roadVerticalOffset += 10;
        roadVerticalOffset %= Stage.HEIGHT;

        car.update();
        healthBar.updateHealthbar(car.getHealth());

        //TODO: Possibly handle both modifiers and hazards into the actor array

        // Updating hazards position
        for (int i = 0; i < hazards.size(); i++) {
            Hazards hazard = hazards.get(i);
            hazard.update();
            if (hazard.getY() > Stage.HEIGHT){
                hazard.setMarkedForRemoval(true);
            }
            if (hazard.isMarkedForRemoval()){
                hazards.remove(i);
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
        for (int i = 0; i < hazards.size(); i++) {
            Hazards hazard = hazards.get(i);
            if( car.getBounds().intersects(hazard.getBounds())) {
                //TODO: Change 10 to retrieve hazard damage value
                car.reduceHealth(hazard.dealDamage());
                if (car.getHealth() <= 0) {
                    gameOver();
                }

                hazard.setMarkedForRemoval(true);
                if( splat == null) {
                    splat = new Splat(this);
                    splat.setX(car.getX());
                    splat.setY(car.getY());

                    splatFrames = 0;
                }
            }
        }
    }

    private void gameOver(){
        setVisible(false);
    }

    public void loopSound(final String name) {
        new Thread(new Runnable() {
            public void run() {
                ResourceLoader.getInstance().getSound(name).loop();
            }
        }).start();
    }


    public void game() {
        loopSound("music.wav");
        usedTime= 0;
        Random randomValueSelector = new Random();

//===================================================GAME LOOP==========================================================

        while(isVisible()) {
            //TODO: Change 900 to a dynamic variable that adjusts depending on score
            if (randomValueSelector.nextInt(1000) > 990) {
                int currentHazardSelection = randomValueSelector.nextInt(100);
                if (currentHazardSelection < 80) {
                    spawnHazard("pothole");
                } else if (currentHazardSelection > 80) {
                    spawnHazard("moose");
                }
            }
            // Debug code - Will use this in the future for testing actor array
            //System.out.println(hazards.size());
            long startTime = System.currentTimeMillis();

            checkCollision();
            updateWorld();
            paintWorld();

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