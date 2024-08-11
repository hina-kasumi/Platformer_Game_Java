package entities;

import main.Game;

import static utilz.CollisionDetection.*;
import static utilz.Constants.Directions.*;
import static utilz.Constants.EnemyConstants.*;

public class Enemy extends Entity {
    protected int aniIndex, aniTick, enemyType, enemyState, aniSpeed = 25;
    protected boolean firstUpdate = true;
    protected boolean inAir;
    protected float fallSpeed;
    protected final float gravity = 0.04f * Game.SCALE;
    protected final float walkSpeed = 0.35f * Game.SCALE;
    protected int walkDir = LEFT;
    protected int tileY;
    protected final float attackDistance = Game.TILES_SIZE;


    public Enemy(float x, float y, int width, int height, int enemyType) {
        super(x, y, width, height);
        this.enemyType = enemyType;
        initHitBox(x, y, width, height);
    }

    protected void firstUpdateCheck(int[][] lvlData) {
        if (!onFloor(hitBox, lvlData))
            inAir = true;
        firstUpdate = false;
    }

    protected void updateInAir (int[][] lvData){
        if (canMoveHere(hitBox.x, hitBox.y + fallSpeed, hitBox.width, hitBox.height, lvData)){
            hitBox.y += fallSpeed;
            fallSpeed += gravity;
        } else {
            inAir = false;
            hitBox.y = getEntityYPosUnderRoofOrAboveFloor(hitBox, fallSpeed);
            tileY = (int) (hitBox.y / Game.TILES_SIZE);
        }
    }

    protected void move (int[][] lvData) {
        float xSpeed = 0;


        if (walkDir == LEFT){
            xSpeed = -walkSpeed;
        } else if (walkDir == RIGHT){
            xSpeed = walkSpeed;
        }

        if (canMoveHere(hitBox.x + xSpeed, hitBox.y, hitBox.width, hitBox.height, lvData)){
            if (isFloor(hitBox, xSpeed, lvData)){
                hitBox.x += xSpeed;
                return;
            }
        }

        changeWalkDir();
    }

    protected void changeWalkDir() {
        if (walkDir == LEFT)
            walkDir = RIGHT;
        else
            walkDir = LEFT;

    }

    protected void turnTowardsPlayer (Player player){
        if (player.hitBox.x > hitBox.x)
            walkDir = RIGHT;
        else
            walkDir = LEFT;
    }

    protected boolean canSeePlayer (int[][] lvData, Player player) {
        int playerTileY = (int) (player.getHitBox().y / Game.TILES_SIZE);
        if (playerTileY == tileY){
            if (isPlayerInRange(player)){
                if (isSightClear(lvData, hitBox, player.hitBox, tileY)){
                    return true;
                }
            }
        }
        return  false;
    }

    protected boolean isPlayerInRange (Player player) {
        int absValue = (int) Math.abs(player.hitBox.x - hitBox.x);
        return absValue <= attackDistance * 5;
    }

    protected boolean isPlayerCloseForAttack(Player player) {
        int absValue = (int) Math.abs(player.hitBox.x - hitBox.x);
        return absValue <= attackDistance;
    }

    protected void newState(int enemyState) {
        this.enemyState = enemyState;
        aniTick = 0;
        aniIndex = 0;
    }

    protected void updateAnimationTick() {
        aniTick++;
        if (aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= GetSpriteAmount(enemyType, enemyState)) {
                aniIndex = 0;
                if(enemyState == ATTACK)
                    enemyState = IDLE;

            }
        }
    }

    public int getAniIndex() {
        return aniIndex;
    }

    public int getEnemyState() {
        return enemyState;
    }

}
