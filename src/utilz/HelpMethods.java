package utilz;

import entities.Crabby;
import main.Game;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utilz.Constants.EnemyConstants.CRABBY;

public class HelpMethods {
    public static int[][] GetLevelData (BufferedImage img) {
        int[][] lvData = new int[img.getHeight()][img.getWidth()];

        for (int i = 0; i < img.getHeight(); i++) {
            for (int j = 0; j < img.getWidth(); j++) {
                Color color = new Color(img.getRGB(j, i));
                int value = color.getRed();
                lvData[i][j] = (value >= 48) ? 0 : value;
            }
        }
        return lvData;
    }

    public static ArrayList<Crabby> GetCrabs(BufferedImage img) {
        ArrayList<Crabby> list = new ArrayList<>();
        for (int j = 0; j < img.getHeight(); j++)
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int value = color.getGreen();
                if (value == CRABBY)
                    list.add(new Crabby(i * Game.TILES_SIZE, j * Game.TILES_SIZE));
            }
        return list;
    }

    public static Point GetPlayerSpawn (BufferedImage image) {
        for (int i = 0; i < image.getHeight(); i++) {
            for (int j = 0; j < image.getWidth(); j++) {
                Color color = new Color(image.getRGB(j, i));
                int value = color.getGreen();
                if (value == 100)
                    return new Point(j * Game.TILES_SIZE, i * Game.TILES_SIZE);
            }
        }
        return new Point(Game.TILES_SIZE, Game.TILES_SIZE);
    }
}
