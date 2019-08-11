package game;

import java.awt.Canvas;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import java.util.List;

import actors.Actor;

public class Stage extends Canvas implements ImageObserver {

	private static final long serialVersionUID = 1L;
	public static final int WIDTH = 735;
	public static final int HEIGHT = 1000;
	public static final int DESIRED_FPS = 60; 
	
	protected boolean gameWon = false;
	protected boolean gameOver = false;
	protected boolean paused = false;
	protected boolean mainMenu = false;

	public List<Actor> actors = new ArrayList<Actor>();	
	
	public Stage() {
	}

	public void pauseGame() {
		paused = true;
	}

	public void unPauseGame() {
		paused = false;
	}

	public void endGame() {
		gameOver = true;
	}

	public void displayMainScreen() {
	    mainMenu = true;
    }

    public void hideMainMenu() {
	    mainMenu = false;
    }

    public boolean isMainMenuDisplaying() {
	    return mainMenu;
    }
	
	public boolean isGameOver() {
		return gameOver;
	}

	public boolean isPaused() {
		return paused;
	}
	
	public boolean imageUpdate(Image img, int infoflags, int x, int y,
			int width, int height) {
		return false;
	}
        
    public void initWorld() {

    }

    public void game() {

    }
    
    public void resetGame() {
   		gameOver = false;
    }
}
