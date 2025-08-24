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
    private gamepannel p;
    private int[][] dungeonMap;
    private static final int GRID_width = 16; // 50x50 tile map
    private static final int GRID_height = 16; // 50x50 tile map
    private static final int VIEWPORT_WIDTH = 16; // 16 tiles wide
    private static final int VIEWPORT_HEIGHT = 12; // 12 tiles high

    public backgroundmanager(gamepannel gp) {
        this.p = gp;
        g = new ground();
        w = new wall();
        l = new water();
        o = new obstacles();
        dungeonMap = new int[GRID_width][GRID_height];

        backgroundImage();
        readDungeonFile("src/background/16x12.txt");
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
            while ((line = reader.readLine()) != null && readline < GRID_height) {

                
                {String[] num = line.trim().split("\\s+");

                for( int j= 0; j < GRID_width; j++)
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
                    int x = col * 48; // p.tiles is tile size (16 pixels)
                    int y = row * 48;

                    switch (tileType) {
                        case 0: // Wall
                            g2.drawImage(w.image, x, y, p.tiles, p.tiles, null);
                            break;
                        case 1: // Stone (assuming stone uses ground image; adjust if needed)
                            g2.drawImage(o.image, x, y, p.tiles, p.tiles, null);
                            break;
                        case 2: // Ground
                            g2.drawImage(g.image, x, y, p.tiles, p.tiles, null);
                            break;
                        case 3: // Water
                            g2.drawImage(l.image, x, y, p.tiles, p.tiles, null);
                            break;
                        default:
                            // Draw nothing or a default tile if needed
                            break;
                }
            }
        }
    }
}