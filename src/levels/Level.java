package levels;

import entities.Crabby;
import main.Game;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utilz.HelpMethods.*;

public class Level {
    private BufferedImage img;
    private int[][] lvData;
    private ArrayList<Crabby> crabs;
    private int lvlTilesWide;
    private int maxTilesOffset;
    private int maxLvlOffsetX;
    private Point playerSpawn;

    public Level(BufferedImage img) {
        this.img = img;
        createLevelData();
        createEnemies();
        calcLvlOffsets();
        calcPlayerSpawn();
    }

    private void calcPlayerSpawn(){
        playerSpawn = GetPlayerSpawn(img);
    }

    private void createLevelData (){
        lvData = GetLevelData(img);
    }

    private void createEnemies() {
        crabs = GetCrabs(img);
    }

    private void calcLvlOffsets (){
        lvlTilesWide = img.getWidth();
        maxTilesOffset = lvlTilesWide - Game.TILES_IN_WIDTH;
        maxLvlOffsetX = Game.TILES_SIZE * maxTilesOffset;
    }

//    private void

    public int getSpriteIndex(int x, int y) {
        return lvData[y][x];
    }
    public int[][] getLevelData() {
        return lvData;
    }

    public ArrayList<Crabby> getCrabs() {
        return crabs;
    }

    public int getMaxLvlOffsetX() {
        return maxLvlOffsetX;
    }

    public Point getPlayerSpawn() {
        return playerSpawn;
    }
}
