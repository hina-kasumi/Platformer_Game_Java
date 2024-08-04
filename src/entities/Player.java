package entities;

import main.Game;
import utilz.LoadSave;

import static utilz.CollisionDetection.*;
import static utilz.Constants.PlayerConstant.*;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Player extends Entity{
    //animation attribute
    private BufferedImage[][] animations;
    private final float xDrawOffset = 21 * Game.SCALE;
    private final float yDrawOffset = 4 * Game.SCALE;
    private int aniTick = 0, aniIndex = 0;
    private final int aniSpeed = 25;
    private int playerAction = IDLE;
    private boolean moving = false, attacking = false;


    //move left, right Attribute
    private float playerSpeed = 2.0f;
    private boolean left, right, jump;

    //jump and fall Attribute
    private float airSpeed = 0f;
    private final float gravity = 0.04f * Game.SCALE;
    private final float jumpSpeed = -2.25f * Game.SCALE;
    private final float fallSpeedAfterCollision = 0.5f * Game.SCALE;
    private boolean inAir = false;

    //Constructor
    public Player(float x, float y, int width, int height) {
        super(x, y, width, height);
        loadAnimation();
        initHitBox(x, y, 20 * Game.SCALE, 28 * Game.SCALE);
    }


    //Method
    public void render (Graphics g) {
        g.drawImage(animations[playerAction][aniIndex], (int) (hitBox.x - xDrawOffset), (int) (hitBox.y - yDrawOffset), width, height, null);
        drawHitBox(g);
    }

    public void update() {

        updateAnimationTick();
        setAnimation();
        updatePos();
    }


    //Animation method
    private void loadAnimation (){
        BufferedImage image = LoadSave.getSpriteAtlas(LoadSave.PLAYER_ATLAS);
        animations = new BufferedImage[9][6];
        for (int i = 0; i < animations.length; i++) {
            for (int j = 0; j < animations[i].length; j++) {
                animations[i][j] = image.getSubimage(j * 64, i * 40, 64,40);
            }
        }
    }

    private void  updateAnimationTick(){
        aniTick++;
        if(aniTick >= aniSpeed){
            aniTick = 0;
            aniIndex++;
            if (aniIndex >=  getSpriteAmount(playerAction)){
                aniIndex = 0;
            }
        }
    }

    private void setAnimation(){
        int startAni = playerAction;
        if(moving){
            playerAction = RUNNING;
        }
        else {
            playerAction = IDLE;
        }
        if (inAir){
            if (airSpeed > 0)
                playerAction = FALLING;
            else
                playerAction = JUMP;
        }
        if (attacking) {
            playerAction = ATTACK_1;
            if (inAir){
                if (airSpeed > 0) // falling
                    playerAction = ATTACK_JUMP_1;
                else
                    playerAction = ATTACK_JUMP_2;
            }
        }

        if (startAni != playerAction){
            resetAniTick();
        }
    }

    private void resetAniTick() {
        aniTick = 0;
        aniIndex = 0;
    }


    //moving
    private void jump(){
        if (onFloor(hitBox)) {
            airSpeed = jumpSpeed;
            inAir = true;
        }
    }

    private void updatePos() {
        moving = false;
        float xSpeed = 0;

        if (jump){
            jump();
        }

        if (left)
            xSpeed -= playerSpeed;
        if (right)
            xSpeed += playerSpeed;

        if(!onFloor(hitBox)){
            inAir = true;
        }

        if (inAir){
            hitBox.y += airSpeed;
            airSpeed += gravity;
            if (onFloor(hitBox)) {
                airSpeed = 0;
                inAir = false;
            }
        }

        updateXPos(xSpeed);
        if (left || right)
            moving = true;
    }


    private void updateXPos(float xSpeed){
        if (canMoveHere(hitBox.x + xSpeed, hitBox.y, hitBox.width, hitBox.height)) {
            hitBox.x += xSpeed;
        } else {
            if (onFloor(hitBox)){
                hitBox.y--;
            }
        }
    }

    //Setter
    public void setLeft(boolean left) {
        this.left = left;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public void setJump(boolean jump) {
        this.jump = jump;
    }

    public void setAttacking(boolean attacking) {
        this.attacking = attacking;
    }
}
