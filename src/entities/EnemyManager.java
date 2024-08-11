package entities;

import GameState.Playing;
import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utilz.Constants.EnemyConstants.*;

public class EnemyManager {
    private Playing playing;
    private BufferedImage[][] crabbyArr;
    private ArrayList<Crabby> crabbies = new ArrayList<>();

    public EnemyManager(Playing playing) {
        this.playing = playing;
        loadEnemyImgs();
        addEnemy();
    }

    private void loadEnemyImgs() {
        crabbyArr = new BufferedImage[5][9];
        BufferedImage temp = LoadSave.getSpriteAtlas(LoadSave.CRABBY_SPRITE);
        for (int j = 0; j < crabbyArr.length; j++)
            for (int i = 0; i < crabbyArr[j].length; i++)
                crabbyArr[j][i] = temp.getSubimage(i * CRABBY_WIDTH_DEFAULT,
                        j * CRABBY_HEIGHT_DEFAULT,
                        CRABBY_WIDTH_DEFAULT,
                        CRABBY_HEIGHT_DEFAULT);
    }

    private void addEnemy() {
        crabbies = LoadSave.GetCrabs();
    }

    public void update(int[][] lvData, Player player) {
        for (Crabby crabby : crabbies) {
            crabby.update(lvData, player);
        }
    }

    private void drawCrabs(Graphics g, int xLvlOffset) {
        for (Crabby c : crabbies) {
            g.drawImage(crabbyArr[c.getEnemyState()][c.getAniIndex()],
                    (int) c.getHitBox().x - xLvlOffset - CRABBY_DRAWOFFSET_X,
                    (int) c.getHitBox().y - CRABBY_DRAWOFFSET_Y,
                    CRABBY_WIDTH,
                    CRABBY_HEIGHT, null);
            c.drawHitBox(g, xLvlOffset);
        }

    }

    public void draw(Graphics g, int xlvOfSet) {
        drawCrabs(g, xlvOfSet);
    }
}
