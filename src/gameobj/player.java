package gameobj;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.gamepannel;
import main.input;

public class player extends entity{

    gamepannel p;
    input key;

    public player(gamepannel p, input key){
        this.p = p;
        this.key = key;
        setDefaultValues();
        setimage();

    }

    public void setDefaultValues(){
        playerX = 100;
        playerY = 100;
        playerlife = 100;
        playerspeed = 2;
        status ="idle";
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
            // idle1 = ImageIO.read(new File("resource/player/idle1.png"));
            // idle2 = ImageIO.read(new File("resource/player/idle2.png"));
            // attacking1 = ImageIO.read(new File("resource/player/attacking1.png"));
            // attacking2 = ImageIO.read(new File("resource/player/attacking2.png"));
            
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update()
    {
        if(key.up == true || key.down == true || key.left == true || key.right == true || key.attack == true)
    {
        if(key.up == true)
        {
            status ="up";
            playerY -= playerspeed;
        }
        else if(key.down == true)
        {
            status ="down";
            playerY += playerspeed;
        }
        else if(key.left == true)
        {
            status ="left";
            playerX -= playerspeed;
        }
        else if(key.right == true)
        {
            status ="right";
            playerX += playerspeed;
        }
        else if(key.attack == true)
        {
            status = "idle";
            // attack animation 
            // enemy ko life decrease if enemy sanga collide vaye
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

    public void draw(Graphics2D g2)
    {

        BufferedImage image = null;

        switch(status)
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
        }
        
        g2.drawImage(image, playerX,playerY, p.tiles,p.tiles,null);

    }
}
