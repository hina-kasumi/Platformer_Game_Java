package entities;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public abstract class Entity {
    //Attribute
    protected float x, y;
    protected int width, height;
    protected Rectangle2D.Float hitBox;

    //Constructor
    public Entity(float x, float y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    //Method
    protected void drawHitBox(Graphics g){
        //for debug
        g.setColor(Color.red);
        g.drawRect((int) hitBox.x, (int) hitBox.y, (int) hitBox.width, (int) hitBox.height);
    }

    protected void initHitBox(float x, float y, float width, float height) {
        hitBox = new Rectangle2D.Float(x, y, width, height);
    }

    //getter
    public Rectangle2D.Float getHitBox() {
        return hitBox;
    }
}
