package UI;

import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static utilz.Constants.UI.URMButtons.*;

public class UrmButton extends PauseButton {
    private BufferedImage[] images;
    private int rowIndex, index;
    private boolean mouseOver, mousePressed;


    public UrmButton(int x, int y, int width, int height, int rowIndex) {
        super(x, y, width, height);
        this.rowIndex = rowIndex;
        loadImage();
    }

    private void loadImage() {
        images = new BufferedImage[3];
        BufferedImage temp = LoadSave.getSpriteAtlas(LoadSave.URM_BUTTONS);
        for (int i = 0; i < images.length; i++) {
            images[i] = temp.getSubimage(i * URM_SIZE_DEFAULT, rowIndex * URM_SIZE_DEFAULT, URM_SIZE_DEFAULT, URM_SIZE_DEFAULT);
        }
    }

    public void update() {
        rowIndex = 0;
        if (mouseOver)
            rowIndex = 1;
        if (mousePressed)
            rowIndex = 2;
    }

    public void draw(Graphics g) {
        g.drawImage(images[rowIndex], x, y, width, height, null);
    }

    public void restBools() {
        mouseOver = false;
        mousePressed = false;
    }

    public void setMouseOver(boolean mouseOver) {
        this.mouseOver = mouseOver;
    }

    public void setMousePressed(boolean mousePressed) {
        this.mousePressed = mousePressed;
    }

    public boolean isMouseOver() {
        return mouseOver;
    }

    public boolean isMousePressed() {
        return mousePressed;
    }


}
