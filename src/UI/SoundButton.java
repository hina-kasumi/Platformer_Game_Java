package UI;


import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static utilz.Constants.UI.PauseButtons.*;

public class SoundButton extends PauseButton {

    private BufferedImage[][] soundImages;
    private boolean mouseOver, mousePressed;
    private boolean muted;
    private int rowIndex, colIndex;

    public SoundButton(int x, int y, int width, int height) {
        super(x, y, width, height);

        loadSoundImages();
    }

    private void loadSoundImages() {
        soundImages = new BufferedImage[2][3];
        BufferedImage temp = LoadSave.getSpriteAtlas(LoadSave.SOUND_BUTTONS);
        for (int i = 0; i < soundImages.length; i++) {
            for (int j = 0; j < soundImages[i].length; j++) {
                soundImages[i][j] = temp.getSubimage(j * SOUND_SIZE_DEFAULT, i * SOUND_SIZE_DEFAULT, SOUND_SIZE_DEFAULT, SOUND_SIZE_DEFAULT);
            }
        }
    }

    public void update(){
        if (muted)
            rowIndex = 1;
        else
            rowIndex = 0;
        colIndex = 0;
        if (mouseOver)
            colIndex = 1;
        if (mousePressed)
            colIndex = 2;
    }

    public void draw (Graphics g){
        g.drawImage(soundImages[rowIndex][colIndex], x, y, width, height, null);
    }

    public void restBools (){
        mouseOver = false;
        mousePressed = false;
    }

    public void setMouseOver(boolean mouseOver) {
        this.mouseOver = mouseOver;
    }

    public void setMousePressed(boolean mousePressed) {
        this.mousePressed = mousePressed;
    }

    public boolean isMouseOver (){
        return mouseOver;
    }

    public boolean isMousePressed (){
        return  mousePressed;
    }

    public void setMuted(boolean muted) {
        this.muted = muted;
    }

    public boolean isMuted() {
        return muted;
    }

}
