package gameobj;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.input;
import main.pannel;

public class player extends entity{

    pannel p;
    input key;

    public player(pannel p, input key){
        this.p = p;
        this.key = key;
        setDefaultValues();
        setimage();;

    }

    public void setDefaultValues(){
        playerX = 100;
        playerY = 100;
        playerlife = 100;
        playerspeed = 2;
        status ="up";
    }
    
    public void setimage(){
        
        try {

            up1 = ImageIO.read(getClass().getResourceAsStream("/player/up_1.png"));
            up2 = ImageIO.read(getClass().getResourceAsStream("/player/up_2.png"));
            down1 = ImageIO.read(getClass().getResourceAsStream("/player/down_1.png"));
            down2 = ImageIO.read(getClass().getResourceAsStream("/player/down_2.png"));
            right1 = ImageIO.read(getClass().getResourceAsStream("/player/right_1.png"));
            right2 = ImageIO.read(getClass().getResourceAsStream("/player/right_2.png"));
            left1 = ImageIO.read(getClass().getResourceAsStream("/player/left_1.png"));
            left2 = ImageIO.read(getClass().getResourceAsStream("/player/left_2.png"));
            idle1 = ImageIO.read(getClass().getResourceAsStream("/player/idle1.png"));
            idle2 = ImageIO.read(getClass().getResourceAsStream("/player/idle2.png"));
            attacking1 = ImageIO.read(getClass().getResourceAsStream("/player/attacking1.png"));
            attacking2 = ImageIO.read(getClass().getResourceAsStream("/player/attacking2.png"));
            
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update()
    {
        if(key.up == true)
        {
            status ="up";
            playerY -= playerspeed;
        }

        else if(key.down == true)
        {
            status ="up";
            playerY += playerspeed;
        }

        else if(key.left == true)
        {
            status ="up";
            playerX -= playerspeed;
        }

        else if(key.right == true)
        {
            status ="up";
            playerX += playerspeed;
        }

        else if(key.attack == true)
        {
            status = "idle";
            // attack animation 
            // enemy ko life decrease if enemy sanga collide vaye
        }
        
    }

    public void draw(Graphics2D g2)
    {
        // g2.setColor(Color.white);
        // g2.fillRect(playerX, playerY, p.tiles, p.tiles);

        BufferedImage image = null;

        switch(status)
        {
            case "up":
                image = up1;
                break;

            case "down":
                image = down1;
                break;
            
            case "right":
                image = right1;
                break;
            
            case "left":
                image = left1;
                break;

        }

        g2.drawImage(image, playerX,playerY, p.tiles,p.tiles,null);

    }
}
