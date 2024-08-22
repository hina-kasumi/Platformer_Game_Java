package entities;

import GameState.Playing;
import main.Game;
import utilz.LoadSave;

import static utilz.CollisionDetection.*;
import static utilz.Constants.PlayerConstant.*;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class Player extends Entity {
    int[][] lvData;
    private Playing playing;

    // animation attribute
    private BufferedImage[][] animations;
    private final float xDrawOffset = 21 * Game.SCALE;
    private final float yDrawOffset = 4 * Game.SCALE;
    private int aniTick = 0, aniIndex = 0;
    private final int aniSpeed = 25;
    private int playerAction = IDLE;
    private boolean moving = false, attacking = false;

    //status bar UI
    private BufferedImage statusBarImg;

    private int statusBarWidth = (int) (192 * Game.SCALE);
    private int statusBarHeight = (int) (58 * Game.SCALE);
    private int statusBarX = (int) (10 * Game.SCALE);
    private int statusBarY = (int) (10 * Game.SCALE);

    private int healthBarWidth = (int) (150 * Game.SCALE);
    private int healthBarHeight = (int) (4 * Game.SCALE);
    private int healthBarXStart = (int) (34 * Game.SCALE);
    private int healthBarYStart = (int) (14 * Game.SCALE);

    private int maxHealth = 100;
    private int currentHealth = maxHealth;
    private int healthWidth = healthBarWidth;

    // AttackBox
    private Rectangle2D.Float attackBox;
    private int flipX = 0;
    private int flipW = 1;

    private boolean attackChecked;

    // move left, right Attribute
    private float playerSpeed = 1.0f * Game.SCALE;
    private boolean left, right, jump;

    // jump and fall Attribute
    private float airSpeed = 0f;
    private final float gravity = 0.04f * Game.SCALE;
    private final float jumpSpeed = -2.5f * Game.SCALE;
    private final float fallSpeedAfterCollision = 0.5f * Game.SCALE;
    private boolean inAir = false;

    // Constructor
    public Player(float x, float y, int width, int height, Playing playing) {
        super(x, y, width, height);
        loadAnimation();
        this.playing = playing;
        initHitBox(x, y, (int) (20 * Game.SCALE), (int) (27 * Game.SCALE));
        initAttackBox();
    }

    public void setSpawn(Point spawn) {
        this.x = spawn.x;
        this.y = spawn.y;
        hitBox.x = x;
        hitBox.y = y;
    }

    // Method
    public void render(Graphics g, int xlvOfSet) {
        g.drawImage(animations[playerAction][aniIndex],
                (int) (hitBox.x - xDrawOffset) - xlvOfSet + flipX,
                (int) (hitBox.y - yDrawOffset),
                width * flipW,
                height, null);

        drawHitBox(g, xlvOfSet);
        drawAttackHitBox(g, xlvOfSet);
        drawUI(g);
    }

    public void update() {
        updateHealthBar();

        if (currentHealth <= 0) {
            playing.setGameOver(true);
            return;
        }
        updateAttackBox();

        updatePos();
        if (attacking)
            checkAttack();
        updateAnimationTick();
        setAnimation();

    }


    public void loadLvData(int[][] lvData) {
        this.lvData = lvData;
    }

    //drawUI
    private void drawUI(Graphics g) {
        g.drawImage(statusBarImg, statusBarX, statusBarY, statusBarWidth, statusBarHeight, null); // draw status bar
        g.setColor(Color.red);
        g.fillRect(healthBarXStart + statusBarX, healthBarYStart + statusBarY, healthWidth, healthBarHeight);
    }

    private void updateHealthBar() {
        healthWidth = (int) ((currentHealth / (float) maxHealth) * healthBarWidth);
    }

    // Animation method
    private void loadAnimation() {
        BufferedImage image = LoadSave.getSpriteAtlas(LoadSave.PLAYER_ATLAS);
        animations = new BufferedImage[7][8];
        for (int i = 0; i < animations.length; i++) {
            for (int j = 0; j < animations[i].length; j++) {
                animations[i][j] = image.getSubimage(j * 64, i * 40, 64, 40);
            }
        }

        statusBarImg = LoadSave.getSpriteAtlas(LoadSave.STATUS_BAR);
    }

    private void updateAnimationTick() {
        aniTick++;
        if (aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= getSpriteAmount(playerAction)) {
                aniIndex = 0;
                attackChecked = false;
                attacking = false;
            }
        }
    }

    private void setAnimation() {
        int startAni = playerAction;
        if (moving) {
            playerAction = RUNNING;
        } else {
            playerAction = IDLE;
        }
        if (inAir) {
            if (airSpeed > 0)
                playerAction = FALLING;
            else
                playerAction = JUMP;
        }
        if (attacking) {
            playerAction = ATTACK;
            if (startAni != ATTACK) {
                aniIndex = 1;
                aniTick = 0;
                return;
            }
        }

        if (startAni != playerAction) {
            resetAniTick();
        }
    }

    private void resetAniTick() {
        aniTick = 0;
        aniIndex = 0;
    }

    //Attack
    private void checkAttack() {
        if (attackChecked || aniIndex != 1){
            return;
        }
        attackChecked = true;
        playing.checkEnemyHit(attackBox);
    }

    private void updateAttackBox() {
        if (right)
            attackBox.x = hitBox.x + hitBox.width + (int) (Game.SCALE * 10);
        else if (left)
            attackBox.x = hitBox.x - hitBox.width - (int) (Game.SCALE * 10);

        attackBox.y = hitBox.y + (Game.SCALE * 10);
    }

    // moving
    private void jump() {
        if (!inAir) {
            inAir = true;
            airSpeed = jumpSpeed;
        }
    }

    private void updatePos() {
        moving = false;
        float xSpeed = 0;
        if (jump) {
            jump();
        }
        if (left) {
            xSpeed -= playerSpeed;
            flipX = width;
            flipW = -1;
        }
        if (right) {
            xSpeed += playerSpeed;
            flipX = 0;
            flipW = 1;
        }
        if (xSpeed != 0)
            moving = true;
        if (!onFloor(hitBox, lvData))
            inAir = true;

        if (inAir) {
            if (canMoveHere(hitBox.x, hitBox.y + airSpeed, hitBox.width, hitBox.height, lvData)) {
                hitBox.y += airSpeed;
                airSpeed += gravity;
            } else {
                hitBox.y = getEntityYPosUnderRoofOrAboveFloor(hitBox, airSpeed);
                if (airSpeed > 0)
                    resetInAir();
                else
                    airSpeed = fallSpeedAfterCollision;
            }
        }

        updateXPos(xSpeed);
    }

    public void resetAll (){
        resetDirBooleans();
        inAir = false;
        attacking = false;
        moving = false;
        playerAction = IDLE;
        currentHealth = maxHealth;
        attackBox.x = x;
        hitBox.x = x;
        hitBox.y = y;

        if (!onFloor(hitBox, lvData))
            inAir = true;
    }

    private void resetInAir() {
        inAir = false;
        airSpeed = 0;
    }

    private void updateXPos(float xSpeed) {
        if (canMoveHere(hitBox.x + xSpeed, hitBox.y, hitBox.width, hitBox.height, lvData)) {
            hitBox.x += xSpeed;
        } else {
            hitBox.x = getEntityXPosNextToWall(hitBox, xSpeed);
        }
    }

    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(x, y, (int) (20 * Game.SCALE), (int) (20 * Game.SCALE));
    }

    private void drawAttackHitBox(Graphics g, int xlvOfSet) {
        g.setColor(Color.red);
        g.drawRect((int) attackBox.x - xlvOfSet, (int) attackBox.y, (int) attackBox.width, (int) attackBox.height);
    }

    public void resetDirBooleans() {
        left = false;
        right = false;
        jump = false;
    }

    public void changeHealth(int value) {
        currentHealth += value;

        if (currentHealth <= 0)
            currentHealth = 0;
        else if (currentHealth >= maxHealth)
            currentHealth = maxHealth;
    }

    // Setter

    public void setLvData(int[][] lvData) {
        this.lvData = lvData;
    }

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
