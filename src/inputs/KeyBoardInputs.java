package inputs;

import main.GamePanel;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyBoardInputs implements KeyListener {
    private final GamePanel gamePanel;

    //Constructor
    public KeyBoardInputs(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_A ->gamePanel.getGame().getPlayer().setLeft(true);
            case KeyEvent.VK_D -> gamePanel.getGame().getPlayer().setRight(true);
            case KeyEvent.VK_K -> gamePanel.getGame().getPlayer().setJump(true);
            case KeyEvent.VK_J -> gamePanel.getGame().getPlayer().setAttacking(true);
        }
        System.out.println(e.getKeyChar());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_A ->gamePanel.getGame().getPlayer().setLeft(false);
            case KeyEvent.VK_D -> gamePanel.getGame().getPlayer().setRight(false);
            case KeyEvent.VK_K -> gamePanel.getGame().getPlayer().setJump(false);
            case KeyEvent.VK_J -> gamePanel.getGame().getPlayer().setAttacking(false);
        }
    }
}
