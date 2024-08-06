package main;

import inputs.KeyBoardInputs;
import inputs.MouseInput;

import javax.swing.*;
import java.awt.*;

import static main.Game.GAME_HEIGHT;
import static main.Game.GAME_WIDTH;

public class GamePanel extends JPanel {
    private final Game game;

    public GamePanel(Game game) {
        this.game = game;
        MouseInput mouseInput = new MouseInput(this);
        addKeyListener(new KeyBoardInputs(this));
        addMouseListener(mouseInput);
        addMouseMotionListener(mouseInput);
        setPanelSize();

    }

    private void setPanelSize(){
        Dimension size = new Dimension(GAME_WIDTH, GAME_HEIGHT);
        setPreferredSize(size);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        game.render(g);
    }

    public Game getGame() {
        return game;
    }
}
