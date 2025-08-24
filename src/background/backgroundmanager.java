package background;

import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.imageio.ImageIO;
import main.gamepannel;

public class backgroundmanager {
    private ground g;
    private wall w;
    private water l;
    private obstacles o;
    private gamepannel gp;
    private int[][] dungeonMap;
    private static final int VIEWPORT_WIDTH = 16; // 16 tiles wide
    private static final int VIEWPORT_HEIGHT = 12; // 12 tiles high

    public backgroundmanager(gamepannel gp) {
        this.gp = gp;
        g = new ground();
        w = new wall();
        l = new water();
        o = new obstacles();
        dungeonMap = new int[gp.maprow][gp.mapcol];

        backgroundImage();
        readDungeonFile("resource/50x40.txt");
    }

    public void backgroundImage() {
        try {
            g.image = ImageIO.read(new File("resource/walls/ground.png"));
            l.image = ImageIO.read(new File("resource/walls/water.png"));
            w.image = ImageIO.read(new File("resource/walls/wall.png"));
            o.image = ImageIO.read(new File("resource/walls/bigrock.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readDungeonFile(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            int readline = 0;
            int i = 0;
            String line;
            while ((line = reader.readLine()) != null && readline < gp.mapcol) {

                
                {String[] num = line.trim().split("\\s+");

                for( int j= 0; j < gp.maprow; j++)
                {
                    dungeonMap[i][j] = Integer.parseInt(num[j]);
                    System.out.print(Integer.parseInt(num[j]) + " ");
                }
                i++;
                System.out.println("");
            }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2) {
        // Draw tiles within the 16X12 viewport
        for (int row = 0; row < VIEWPORT_HEIGHT; row++) {
            for (int col = 0; col < VIEWPORT_WIDTH; col++) {
                
                    int tileType = dungeonMap[row][col];
                    int x = col * gp.tiles; // p.tiles is tile size (16 pixels)
                    int y = row * gp.tiles;

                    switch (tileType) {
                        case 0: // Wall
                            g2.drawImage(w.image, x, y, gp.tiles, gp.tiles, null);
                            break;
                        case 1: // Stone (assuming stone uses ground image; adjust if needed)
                            g2.drawImage(o.image, x, y, gp.tiles, gp.tiles, null);
                            break;
                        case 2: // Ground
                            g2.drawImage(g.image, x, y, gp.tiles, gp.tiles, null);
                            break;
                        case 3: // Water
                            g2.drawImage(l.image, x, y, gp.tiles, gp.tiles, null);
                            break;
                        default:
                            // Draw nothing or a default tile if needed
                            break;
                }
            }
        }
    }
}