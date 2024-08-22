package entities;

import GameState.Playing;
import levels.Level;
import utilz.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
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

    public void loadEnemy(Level level) {
        crabbies = level.getCrabs();
    }

    public void update(int[][] lvData, Player player) {
        boolean isAnyActive = false;
        for (Crabby crabby : crabbies) {
            if (crabby.isActive()) {
                crabby.update(lvData, player);
                isAnyActive = true;
            }
        }
        if (!isAnyActive){
            playing.setLvlCompeted(true);
        }
    }

    public void draw(Graphics g, int xlvOfSet) {
        drawCrabs(g, xlvOfSet);
    }

    private void drawCrabs(Graphics g, int xLvlOffset) {
        for (Crabby c : crabbies) {
            if (c.isActive()) {
                g.drawImage(crabbyArr[c.getEnemyState()][c.getAniIndex()],
                        (int) c.getHitBox().x - xLvlOffset - CRABBY_DRAWOFFSET_X + c.flipX(),
                        (int) c.getHitBox().y - CRABBY_DRAWOFFSET_Y,
                        CRABBY_WIDTH * c.flipW(),
                        CRABBY_HEIGHT, null);
                c.drawHitBox(g, xLvlOffset);
                c.drawAttackBox(g, xLvlOffset);
            }
        }
    }

    public void checkEnemyHit(Rectangle2D.Float attackBox) {
        for (Crabby c : crabbies) {
            if (attackBox.intersects(c.getHitBox())) {
                if (c.isActive()) {
                    c.hurt(10);
                    return;
                }
            }
        }
    }

    public void resetAllEnemy() {
        for (Crabby c : crabbies) {
            c.resetEnemy();
        }
    }
}
