package background;

import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.imageio.ImageIO;

import gameobj.player;
import main.gamepannel;

public class backgroundmanager extends gamepannel {
    public tile[] tile;
    private player p1;
    public int[][] Maprowcol;
    private int n = 10;

    public backgroundmanager(player p1) {
        this.p1 = p1;
        tile = new tile[n];
        Maprowcol = new int[maprow][mapcol];
        
        backgroundImage();
        readDungeonFile("resource/50x40.txt");
    }

    public void backgroundImage() {
        try {

            for (int i = 0; i < n; i++) 
            {
                tile[i] = new tile();
                
            }

            tile[0].image = ImageIO.read(new File("resource/object/ground.png"));

            tile[1].image = ImageIO.read(new File("resource/object/water.png"));
         
            tile[2].image = ImageIO.read(new File("resource/object/wall.png"));

            tile[3].image = ImageIO.read(new File("resource/object/bigrock.png"));


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readDungeonFile(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            int readline = 0;
            int i = 0;
            String line;
            while ((line = reader.readLine()) != null && readline < maprow) {  
                {String[] num = line.trim().split("\\s+");

                for( int j= 0; j < mapcol; j++)
                {
                    Maprowcol[i][j] = Integer.parseInt(num[j]);
                }
                i++;
            }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2) 
    {
        // Draw tiles within the 16X12 viewport
        for (int row = 0; row < maprow; row++) {
            for (int col = 0; col < mapcol; col++) {
                
                    int tileType = Maprowcol[row][col];

                    int x = col * tiles; 
                    int y = row * tiles;

                    int mapx = x- p1.entity_map_X+ p1.centerx;
                    int mapy = y - p1.entity_map_Y + p1.centery;

                    // int mapx = col * gp.tiles; 
                    // int mapy = row * gp.tiles;


                    switch (tileType) {
                        case 0: // Wall
                            g2.drawImage(tile[2].image, mapx, mapy, tiles, tiles, null);
                            break;
                        case 1: // Stone (assuming stone uses ground image; adjust if needed)
                            g2.drawImage(tile[3].image, mapx, mapy, tiles, tiles, null);
                            break;
                        case 2: // Ground
                            g2.drawImage(tile[0].image, mapx, mapy, tiles,tiles, null);
                            break;
                        case 3: // Water
                            g2.drawImage(tile[1].image, mapx, mapy, tiles, tiles, null);
                            break;
                        default:
                            // Draw nothing or a default tile if needed
                            break;
                }
            }
        }
    }
}