package utilz;

import main.Game;

import java.awt.geom.Rectangle2D;

public class CollisionDetection {
    public static boolean canMoveHere (float x, float y, float width, float height, int[][] lvData){
        //can move here
        return !isSolid(x, y, lvData)
                && !isSolid(x, y + height, lvData)
                && !isSolid(x + width, y, lvData)
                && !isSolid(x + width, y + height, lvData);
    }

    public static boolean isSolid(float x, float y, int[][] lvData){ // is it a block
        int maxWidth = lvData[0].length * Game.TILES_SIZE;
        if(x < 0 || x >= maxWidth)
            return true;
        if(y < 0 || y >= Game.GAME_HEIGHT)
            return true;
        float xIndex = x / Game.TILES_SIZE;
        float yIndex = y / Game.TILES_SIZE;

        int value = lvData[(int) yIndex][(int) xIndex];

        return value != 11;
    }

    public static boolean onFloor (Rectangle2D.Float hitBox, int[][] lvData){
        return isSolid(hitBox.x, hitBox.y + hitBox.height + 1, lvData)
                || isSolid(hitBox.x + hitBox.width, hitBox.y + hitBox.height + 1, lvData);
    }

    public static float getEntityXPosNextToWall(Rectangle2D.Float hitbox, float xSpeed) {
        int currentTile = (int) (hitbox.x / Game.TILES_SIZE);
        if (xSpeed > 0) {
            // Right
            int tileXPos = currentTile * Game.TILES_SIZE;
            int xOffset = (int) (Game.TILES_SIZE - hitbox.width);
            return tileXPos + xOffset - 1;
        } else
            // Left
            return currentTile * Game.TILES_SIZE;
    }

    public static float getEntityYPosUnderRoofOrAboveFloor(Rectangle2D.Float hitbox, float airSpeed) {
        int currentTile = (int) (hitbox.y / Game.TILES_SIZE);
        if (airSpeed > 0) {
            // Falling - touching floor
            int tileYPos = currentTile * Game.TILES_SIZE;
            int yOffset = (int) (Game.TILES_SIZE - hitbox.height);
            return tileYPos + yOffset - 1;
        } else
            // Jumping
            return currentTile * Game.TILES_SIZE;

    }
}
