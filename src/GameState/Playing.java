package GameState;

import UI.GameOverOverlay;
import UI.LevelCompletedOverlay;
import UI.PauseOverlay;
import entities.EnemyManager;
import entities.Player;
import levels.LevelManager;
import main.Game;
import utilz.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import static utilz.Constants.Environment.*;


public class Playing extends State implements StateMethods {
    private Player player;
    private LevelManager levelManager;
    private PauseOverlay pauseOverlay;
    private boolean paused = false;
    private EnemyManager enemyManager;
    private boolean gameOver = false;
    private GameOverOverlay gameOverOverlay;
    private LevelCompletedOverlay levelCompletedOverlay;
    private boolean lvlCompeted;

    //screen follow player
    private int xlvOfSet;
    private int leftBorder = (int) (0.2 * Game.GAME_WIDTH);
    private int rightBorder = (int) (0.8 * Game.GAME_WIDTH);
    private int maxLvlOffsetX;

    //background
    private BufferedImage backgroundImage, bigCloud, smallCloud;
    private int[] smallCloudPos;
    private Random random = new Random();


    public Playing(Game game) {
        super(game);
        initClasses();

        backgroundImage = LoadSave.getSpriteAtlas(LoadSave.PLAYING_BG_IMG);
        bigCloud = LoadSave.getSpriteAtlas(LoadSave.BIG_CLOUDS);
        smallCloud = LoadSave.getSpriteAtlas(LoadSave.SMALL_CLOUDS);
        smallCloudPos = new int[8];
        for (int i = 0; i < smallCloudPos.length; i++) {
            smallCloudPos[i] = (int) (90 * Game.SCALE) + random.nextInt((int) (100 * Game.SCALE));
        }

        calcLvlOffset();
        loadStartLevel();
    }


    private void initClasses() {

        levelManager = new LevelManager(game);
        enemyManager = new EnemyManager(this);

        player = new Player(150, 150, (int) (64 * Game.SCALE), (int) (40 * Game.SCALE), this);
        player.loadLvData(levelManager.getCurrentLevel().getLevelData());

        pauseOverlay = new PauseOverlay(this);
        gameOverOverlay = new GameOverOverlay(this);
        levelCompletedOverlay = new LevelCompletedOverlay(this);
    }

    public void loadNextLevel() {
        resetAll();
        levelManager.loadNextLevel();
        player.setSpawn(levelManager.getCurrentLevel().getPlayerSpawn());
    }

    private void loadStartLevel() {
        enemyManager.loadEnemy(levelManager.getCurrentLevel());
    }

    private void calcLvlOffset() {
        maxLvlOffsetX = levelManager.getCurrentLevel().getMaxLvlOffsetX();
    }


    @Override
    public void update() {
        if (paused) {
            pauseOverlay.update();
        } else if (lvlCompeted) {
            levelCompletedOverlay.update();
        } else if (!gameOver) {
            player.update();
            checkCloseToBorder();
            enemyManager.update(levelManager.getCurrentLevel().getLevelData(), player);
        }
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(backgroundImage, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
        drawCloud(g);

        levelManager.draw(g, xlvOfSet);
        player.render(g, xlvOfSet);
        enemyManager.draw(g, xlvOfSet);

        if (paused) {
            g.setColor(new Color(0, 0, 0, 150));
            g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
            pauseOverlay.draw(g);
        } else if (gameOver) {
            gameOverOverlay.draw(g);
        } else if (lvlCompeted) {
            levelCompletedOverlay.draw(g);
        }
    }

    private void drawCloud(Graphics g) {
        for (int i = 0; i < 3; i++) {
            g.drawImage(bigCloud, i * BIG_CLOUD_WIDTH - (int) (xlvOfSet * 0.3), (int) (204 * Game.SCALE), BIG_CLOUD_WIDTH, BIG_CLOUD_HEIGHT, null);
        }
        for (int i = 0; i < smallCloudPos.length; i++) {
            g.drawImage(smallCloud, SMALL_CLOUD_WIDTH * 4 * i - (int) (xlvOfSet * 0.7), smallCloudPos[i], SMALL_CLOUD_WIDTH, SMALL_CLOUD_HEIGHT, null);
        }
    }

    public void checkEnemyHit(Rectangle2D.Float attackBox) {
        enemyManager.checkEnemyHit(attackBox);
    }

    private void checkCloseToBorder() {
        int playerX = (int) (player.getHitBox().x);
        int diff = playerX - xlvOfSet;

        if (diff > rightBorder) {
            xlvOfSet += diff - rightBorder;
        } else if (diff < leftBorder) {
            xlvOfSet += diff - leftBorder;
        }


        if (xlvOfSet > maxLvlOffsetX)
            xlvOfSet = maxLvlOffsetX;
        else if (xlvOfSet < 0)
            xlvOfSet = 0;

    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public void setLvlCompeted(boolean lvlCompeted) {
        this.lvlCompeted = lvlCompeted;
    }

    public void mouseDragged(MouseEvent e) {
        if (!gameOver)
            if (paused)
                pauseOverlay.mouseDragged(e);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (!gameOver) {
            if (paused)
                pauseOverlay.mousePressed(e);
            else if (lvlCompeted)
                levelCompletedOverlay.mousePressed(e);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (!gameOver) {
            if (paused)
                pauseOverlay.mouseReleased(e);
            else if (lvlCompeted)
                levelCompletedOverlay.mouseRelease(e);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (!gameOver){
            if (paused)
                pauseOverlay.mouseMoved(e);
            else if (lvlCompeted)
                levelCompletedOverlay.mouseMoved(e);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (gameOver)
            gameOverOverlay.keyPressed(e);
        else
            switch (e.getKeyCode()) {
                case KeyEvent.VK_A -> player.setLeft(true);
                case KeyEvent.VK_D -> player.setRight(true);
                case KeyEvent.VK_K -> player.setJump(true);
                case KeyEvent.VK_J -> player.setAttacking(true);
                case KeyEvent.VK_ESCAPE -> paused = !paused;
            }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (!gameOver)
            switch (e.getKeyCode()) {
                case KeyEvent.VK_A -> player.setLeft(false);
                case KeyEvent.VK_D -> player.setRight(false);
                case KeyEvent.VK_K -> player.setJump(false);
                case KeyEvent.VK_J -> player.setAttacking(false);
            }
    }

    public void resetAll() {
        gameOver = false;
        paused = false;
        lvlCompeted = false;
        player.resetAll();
        enemyManager.resetAllEnemy();
    }

    public void unpauseGame() {
        paused = false;
    }

    public Player getPlayer() {
        return player;
    }

    public EnemyManager getEnemyManager() {
        return enemyManager;
    }

    public void setMaxLvlOffsetX(int maxLvlOffsetX) {
        this.maxLvlOffsetX = maxLvlOffsetX;
    }
}
