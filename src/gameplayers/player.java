package gameplayers;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.colisiondetection;
import main.gamehandler;
import main.input;

public class player extends entity {

    
    public int centerx , centery;
    input key;
    colisiondetection cd;
    public gamehandler gh;

    public player( input key,colisiondetection cd, gamehandler gh){
        this.key = key;
        this.cd = cd;
        this.gh = gh;

        centerx = (base/2)-tiles/2;
        centery = ((height)/2)-tiles/2;
        

        colisionarea = new Rectangle();

        colisionarea.x = 15;
        colisionarea.y = 10;
        colisionarea.width = 18;
        colisionarea.height = 33;
        
        setDefaultValues();
        setimage();
    }

    
    public void setDefaultValues(){
        entity_map_X = 96;
        entity_map_Y = 96;
        entitylife = 100;
        direction ="idle";
        gh.ispaused = false;
        gh.ischatting = false;
        gh.isinventory_open =false;
    }
    
    public void setimage(){
        
        try {

            up1 = ImageIO.read(new File("resource/player/up_1.png"));
            up2 = ImageIO.read(new File("resource/player/up_2.png"));
            down1 = ImageIO.read(new File("resource/player/down_1.png"));
            down2 = ImageIO.read(new File("resource/player/down_2.png"));
            right1 = ImageIO.read(new File("resource/player/right_1.png"));
            right2 = ImageIO.read(new File("resource/player/right_2.png"));
            left1 = ImageIO.read(new File("resource/player/left_1.png"));
            left2 = ImageIO.read(new File("resource/player/left_2.png"));
            idle1 = ImageIO.read(new File("resource/player/idle1.png"));
            idle2 = ImageIO.read(new File("resource/player/idle2.png"));
            // attack1 = ImageIO.read(new File("resource/player/attack1.png"));
            // attack2 = ImageIO.read(new File("resource/player/attack2.png"));
            
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update()
    {
        checkplayer();  
                if (gh.gc != null && gh.gc.connection() && key.pressed) 
                    {
                        String posMsg = "POS|" + entity_map_X + "|" + entity_map_Y + "|" + direction;
                        gh.gc.send(posMsg);
                    }
    }

    public void draw(Graphics2D g2)
    {

        BufferedImage image = null;

        switch(direction)
        {
            case "up":
            if(flip ==1 )
                image = up1;
            if(flip ==2 )
                image = up2;
                break;

            case "down":
            if(flip == 1)
                image = down1;
            if(flip ==2 )
                image = down2;
                break;
   
            case "right":
            if(flip == 1)
                image = right1;
            if(flip ==2 )
                image = right2;
                break;

            case "left":
            if(flip == 1)
                image = left1;
            if(flip ==2 )
                image = left2;
                break;

            case "attack":
            if(flip == 1)
                image = attack1;
            if(flip ==2 )
                image = attack2;
                break;

            case "idle":
            if(flip == 1)
                image = idle1;
            if(flip ==2 )
                image = idle2;
                break;
        }
        
        g2.drawImage(image, centerx,centery, tiles,tiles,null);

    }

    public void checkplayer()
    {
        if (key.pause == true) {
            if (!gh.resumed) {
                gh.ispaused = true;
                key.pause = false;
            }
            direction = "idle";
        }

        if (key.chat == true) {
            if (!gh.resumed) {
                gh.ischatting = true;
                key.chat = false;
            }
            direction = "idle";
        }

        if (key.inventory_open == true) {
            if (!gh.resumed) {
                gh.isinventory_open = true;
                key.inventory_open = false;
            }
           direction = "idle";
        }

        if (key.resume == true) {
            if (key.pause) {  // Simplified, removed isresumed
                gh.resumed = false;
                gh.ispaused = false;
                key.resume = false;  // NEW: Reset
            }
            else if (key.chat) {  // Simplified, removed isresumed
                gh.resumed = false;
                gh.ischatting = false;
                key.resume = false;
            }
            else if (key.inventory_open) {  // Simplified, removed isresumed
                gh.resumed = false;
                gh.isinventory_open = false;
                key.resume = false;
            }
             else {
                direction = "idle";
            }
        }

        if((key.up == false || key.down == false || key.left == false || key.right == false || key.attack == false))
        {
            key.pressed = false;
            count ++;
            if(count >250)
            {
                direction = "idle";
            }
        }

        if(key.up == true || key.down == true || key.left == true || key.right == true || key.attack == true )
        {

            key.pressed = true;
            if(key.up == true)
            {
                direction ="up";
            }
            else if(key.down == true)
            {
                direction ="down";
            }
            else if(key.left == true)
            {
                direction ="left";
            }
            else if(key.right == true)
            {
                direction ="right";
            }
            else if(key.attack == true)
            {
                direction = "attack";
                // attack animation 
                // enemy ko life decrease if enemy sanga collide vaye
            }

            colision = false;
            entityspeed = 3;
            cd.checkcolision(this);
            if(!colision )
            {
                switch(direction)
                {
                    case "up":
                            entity_map_Y -= entityspeed;
                            break;
                    case "down":
                            entity_map_Y += entityspeed;
                            break;
                    case "right":
                            entity_map_X += entityspeed;
                            break;
                    case "left":
                            entity_map_X -= entityspeed;
                            break;
                    case "attack":// attack animation
                    break;
                }
                    
            }

            count ++;
            if(count >12)
            {
                if(flip == 1)
                {flip = 2;}
                else 
                {flip = 1;}
            count = 0;
            }
        }
    }
}
