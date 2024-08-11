package entities;

import main.Game;

import static utilz.Constants.EnemyConstants.*;

public class Crabby extends Enemy {
    public Crabby(float x, float y) {
        super(x, y, CRABBY_WIDTH, CRABBY_HEIGHT, CRABBY);
        initHitBox(x, y, (int) (22 * Game.SCALE), (int) (19 * Game.SCALE));
    }

    public void update (int[][] lvData, Player player){
        updateMove(lvData, player);
        updateAnimationTick();
    }

    public void updateMove(int[][] lvData, Player player) {
        if (firstUpdate){
            firstUpdateCheck(lvData);
        }

        if (inAir){
            updateInAir(lvData);
        }
        else {
            switch (enemyState){
                case IDLE:
                    newState(RUNNING);
                    break;
                case RUNNING:
                    if (canSeePlayer(lvData, player))
                        turnTowardsPlayer(player);
                    if (isPlayerCloseForAttack(player))
                        newState(ATTACK);


                    move(lvData);
                    break;
            }
        }
    }

}
