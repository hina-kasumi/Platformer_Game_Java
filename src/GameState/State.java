package GameState;

import UI.MenuButton;
import main.Game;

import java.awt.event.MouseEvent;

public class State {
    protected Game game;

    public State(Game game) {
        this.game = game;
    }

    public boolean isIn(MouseEvent e, MenuButton menuButton){
        return menuButton.getBounds().contains(e.getX(),e.getY());
    }

    public Game getGame() {
        return game;
    }
}
