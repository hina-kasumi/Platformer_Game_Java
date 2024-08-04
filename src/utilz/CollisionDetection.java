package utilz;

import main.Game;

import java.awt.geom.Rectangle2D;

public class CollisionDetection {
    public static boolean canMoveHere (float x, float y, float width, float height){
        //can move here
        return !isSolid(x, y)
                && !isSolid(x, y + height)
                && !isSolid(x + width, y)
                && !isSolid(x + width, y + height);
    }

    public static boolean isSolid(float x, float y){ // is it a block
        if(x < 0 || x >= Game.GAME_WIDTH)
            return true;
        if(y < 0 || y >= Game.GAME_HEIGHT)
            return true;
        return false;
    }

    public static boolean onFloor (Rectangle2D.Float hitBox){
        return isSolid(hitBox.x, hitBox.y + hitBox.height + 1)
                || isSolid(hitBox.x + hitBox.width, hitBox.y + hitBox.height + 1);
    }

}
