package items;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import main.gamehandler;

public class itemsdetail 
{
    
    public String name;
    public int id;
    public BufferedImage image;
    public int x,y;
    public boolean collision = true;
    public String about;
    public int speed = 0;
    public Rectangle collision_area = new Rectangle(15,10,18,33);
    public int colisionarea_default_x=10,colisionarea_default_y=10;

    public void draw(Graphics2D g2, gamehandler gh)
    {
        // Compute screen position (player-centered)
        int mapx = x - gh.p1.entity_map_X + gh.p1.centerx;
        int mapy = y - gh.p1.entity_map_Y + gh.p1.centery;

        // Check if item is within screen bounds
        if (mapx >= 0 && mapx < gh.base && mapy >= 0 && mapy < gh.height) {
            g2.drawImage(image, mapx, mapy, 48, 48, null);
        }
    }
    
}