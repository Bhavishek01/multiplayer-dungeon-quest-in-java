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
                }
                i++;
            }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2) {
        // Draw tiles within the 16X12 viewport
        for (int row = 0; row < gp.mapcol; row++) {
            for (int col = 0; col < gp.maprow; col++) {
                
                    int tileType = dungeonMap[row][col];

                    // int x = col * gp.tiles; 
                    // int y = row * gp.tiles;

                    // int mapx = x- gp.p1.player_map_X + gp.p1.centerx;
                    // int mapy = y - gp.p1.player_map_Y + gp.p1.centery;

                    int mapx = col * gp.tiles; 
                    int mapy = row * gp.tiles;


                    switch (tileType) {
                        case 0: // Wall
                            g2.drawImage(w.image, mapx, mapy, gp.tiles, gp.tiles, null);
                            break;
                        case 1: // Stone (assuming stone uses ground image; adjust if needed)
                            g2.drawImage(o.image, mapx, mapy, gp.tiles, gp.tiles, null);
                            break;
                        case 2: // Ground
                            g2.drawImage(g.image, mapx, mapy, gp.tiles, gp.tiles, null);
                            break;
                        case 3: // Water
                            g2.drawImage(l.image, mapx, mapy, gp.tiles, gp.tiles, null);
                            break;
                        default:
                            // Draw nothing or a default tile if needed
                            break;
                }
            }
        }
    }
}