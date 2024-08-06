package main;


import GameState.GameState;
import GameState.Menu;
import GameState.Playing;


import java.awt.*;

public class Game implements Runnable{
    private final GamePanel gamePanel;
    private GameWindow gameWindow;

    private Playing playing;
    private Menu menu;

    public final static int TILES_DEFAULT_SIZE = 32; // default size of block
    public final static float SCALE = 1.5f;
    public final static int TILES_IN_WIDTH = 26; // number of horizontal block
    public final static int TILES_IN_HEIGHT = 14; // number of Vertical block
    public final static int TILES_SIZE = (int) (TILES_DEFAULT_SIZE * SCALE); // real block size
    public final static int GAME_WIDTH = TILES_SIZE * TILES_IN_WIDTH; // game width size
    public final static int GAME_HEIGHT = TILES_SIZE * TILES_IN_HEIGHT; // game height size


    //Constructor
    public Game() {
        initClasses();
        this.gamePanel = new GamePanel(this);
        this.gameWindow = new GameWindow(gamePanel);
        gamePanel.requestFocus();

        startGameLoop();
    }

    private void initClasses() {
        playing = new Playing(this);
        menu = new Menu(this);
    }


    public void render(Graphics g) {
        switch (GameState.state) {
            case MENU:
                menu.draw(g);
                break;
            case PLAYING:
                playing.draw(g);
                break;
            default:
                break;
        }
    }


    public void update() {
        switch (GameState.state) {
            case MENU:
                menu.update();
                break;
            case PLAYING:
                playing.update();
                break;
            case OPTIONS:
            case QUIT:
            default:
                System.exit(0);
                break;
        }
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

    public void winFocusLost (){
        if (GameState.state == GameState.PLAYING)
            playing.getPlayer().resetDirBooleans();
    }

    //Getter

    public Playing getPlaying() {
        return playing;
    }

    public Menu getMenu() {
        return menu;
    }
}
