package levels;

import GameState.GameState;
import main.Game;
import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class LevelManager {
    private Game game;
    private BufferedImage[] levelSprite;
    private ArrayList<Level> levels;
    private int lvlIndex = 0;

    public LevelManager(Game game) {
        this.game = game;
        importOutsideSprites();
        levels = new ArrayList<>();
        buildAllLevels();
    }

    private void buildAllLevels() {
        BufferedImage[] allLevels = LoadSave.getAllLevels();
        for (BufferedImage lvl : allLevels) {
            levels.add(new Level(lvl));
        }
    }

    private void importOutsideSprites() {
        BufferedImage img = LoadSave.getSpriteAtlas(LoadSave.LEVEL_ATLAS);
        levelSprite = new BufferedImage[48];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 12; j++) {
                int index = i * 12 + j;
                levelSprite[index] = img.getSubimage(j * 32, i * 32, 32, 32);
            }
        }
    }

    public void draw(Graphics g, int xlvOfSet) {
        for (int i = 0; i < Game.TILES_IN_HEIGHT; i++) {
            for (int j = 0; j < levels.get(lvlIndex).getLevelData()[0].length; j++) {
                int index = levels.get(lvlIndex).getSpriteIndex(j, i);
                g.drawImage(levelSprite[index],
                        j * Game.TILES_SIZE - xlvOfSet,
                        i * Game.TILES_SIZE,
                        Game.TILES_SIZE,
                        Game.TILES_SIZE,
                        null);
            }
        }
    }

    public void loadNextLevel() {
        lvlIndex++;
        if (lvlIndex >= levels.size()){
            lvlIndex = 0;
            System.out.println("game compete");
            GameState.state = GameState.MENU;
        }

        Level nextLevel = levels.get(lvlIndex);
        game.getPlaying().getPlayer().loadLvData(nextLevel.getLevelData());
        game.getPlaying().getEnemyManager().loadEnemy(nextLevel);
        game.getPlaying().setMaxLvlOffsetX(nextLevel.getMaxLvlOffsetX());
    }

    public void update() {

    }

    public Level getCurrentLevel() {
        return levels.get(lvlIndex);
    }

    public int getAmountOfLevels() {
        return levels.size();
    }
}
