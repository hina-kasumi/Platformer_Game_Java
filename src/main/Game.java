package main;


import entities.Player;
import levels.LevelManager;

import java.awt.*;

public class Game implements Runnable{
    private final GamePanel gamePanel;
    private GameWindow gameWindow;
    private final Player player;
    private LevelManager levelManager;

    public final static int TILES_DEFAULT_SIZE = 32; // default size of block
    public final static float SCALE = 1.5f;
    public final static int TILES_IN_WIDTH = 26; // number of horizontal block
    public final static int TILES_IN_HEIGHT = 14; // number of Vertical block
    public final static int TILES_SIZE = (int) (TILES_DEFAULT_SIZE * SCALE); // real block size
    public final static int GAME_WIDTH = TILES_SIZE * TILES_IN_WIDTH; // game width size
    public final static int GAME_HEIGHT = TILES_SIZE * TILES_IN_HEIGHT; // game height size


    //Constructor
    public Game() {
        player = new Player(150, 150, (int) (64 * SCALE), (int) (40 * SCALE));
        levelManager = new LevelManager(this);
        player.loadLvData(levelManager.getCurrentLevel().getLevelData());
        this.gamePanel = new GamePanel(this);
        this.gameWindow = new GameWindow(gamePanel);
        gamePanel.requestFocus();

        startGameLoop();
    }



    public void render(Graphics g) {
        levelManager.draw(g);
        player.render(g);
    }


    public void update() {
        player.update();
    }


    private void startGameLoop(){
        Thread thread = new Thread(this);
        thread.start();
    }


    @Override
    public void run() {
        int FPS_SET = 120;
        int UPS_SET = 200;


        double timePerFrame = 1_000_000_000.0 / FPS_SET;
        double timePerUpdate = 1_000_000_000.0 / UPS_SET;

        long previousTime = System.nanoTime();

        int frames = 0;
        int updates = 0;
        long lastCheck = System.currentTimeMillis();

        double deltaU = 0;
        double deltaF = 0;

        while (true) {
            long currentTime = System.nanoTime();

            deltaU += (currentTime - previousTime) / timePerUpdate;
            deltaF += (currentTime - previousTime) / timePerFrame;
            previousTime = currentTime;

            if (deltaU >= 1) {
                update();
                updates++;
                deltaU--;
            }

            if (deltaF >= 1) {
                gamePanel.repaint();
                frames++;
                deltaF--;
            }

            if (System.currentTimeMillis() - lastCheck >= 1000) {
                lastCheck = System.currentTimeMillis();
                System.out.println("FPS: " + frames + " | UPS: " + updates);
                frames = 0;
                updates = 0;

            }
        }
    }

    //Getter
    public Player getPlayer() {
        return player;
    }
}
