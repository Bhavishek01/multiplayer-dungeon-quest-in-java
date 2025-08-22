package background;

import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.gamepannel;

public class backgroundmanager {

    ground g;
    wall w;
    gamepannel p;
    obstacles o;
    water l;

    int row = 100;
    int col = 100;
    int[][] map;



    public backgroundmanager(gamepannel gp)
    {
        this.p = gp;
        g = new ground();
        w = new wall();
        l = new water();
        o = new obstacles();
        map = new int[row][col];

        backgroundImage();
        readDungeonFile("map.txt");
    }

    public void backgroundImage()
    {
        try {

            g.image = ImageIO.read(new File("resource/walls/ground.png"));
            l.image = ImageIO.read(new File("resource/walls/water.png"));
            w.image = ImageIO.read(new File("resource/walls/wall.png"));
            o.image = ImageIO.read(new File("resource/walls/bigrock.png"));
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readDungeonFile(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            for (int i = 0; i < row; i++) {
                String[] line = reader.readLine().trim().split("\\s+");
                for (int j = 0; j < col; j++) {
                    map[i][j] = Integer.parseInt(line[j]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error reading dungeon.txt");
        }
    }

    public void draw(Graphics2D g2)
    {

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; i++){

                int tileType = map[row][col];
                int x = col * 16;
                    int y = row * 16;

                    switch (tileType) {
                        case 0: // Wall
                            g2.drawImage(w.image, x, y, p.tiles, p.tiles, null);
                            break;
                        case 1: // Stone (assuming stone uses ground image; adjust if needed)
                            g2.drawImage(g.image, x, y, p.tiles, p.tiles, null);
                            break;
                        case 2: // Ground
                            g2.drawImage(g.image, x, y, p.tiles, p.tiles, null);
                            break;
                        case 3: // Water
                            g2.drawImage(l.image, x, y, p.tiles, p.tiles, null);
                            break;
                        case 4: // Obstacle
                            g2.drawImage(o.image, x, y, p.tiles, p.tiles, null);
                            break;
                        default:
                            // Draw nothing or a default tile if needed
                            break;
                
            }
            
        }
        
        

    }
    }
}
