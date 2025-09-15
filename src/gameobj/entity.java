package gameobj;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import main.gamepannel;

public class entity extends gamepannel{

    public int entity_map_X,entity_map_Y,entitylife;
    public float entityspeed;

    public BufferedImage down1,down2,left1,left2,up1,up2,right1,right2,idle1,idle2,attack1,attack2;
    public String direction;

    public int count = 0;
    public int flip = 1;

    public Rectangle colisionarea;
    
    public boolean colision;
    
}
