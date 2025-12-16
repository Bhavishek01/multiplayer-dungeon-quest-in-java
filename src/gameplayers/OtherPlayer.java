package gameplayers;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class OtherPlayer extends entity {
    public String playerName;

    public OtherPlayer(String name) {
        this.playerName = name;
        colisionarea = new Rectangle(15, 10, 18, 33);
        direction = "down";
    }


    public void draw(Graphics2D g2, int screenX, int screenY) {
        BufferedImage image = null;
        int flipFrame = count / 12 % 2 == 0 ? 1 : 2;

        switch (direction) {
            case "up":     image = (flipFrame == 1) ? up1 : up2; break;
            case "down":   image = (flipFrame == 1) ? down1 : down2; break;
            case "left":   image = (flipFrame == 1) ? left1 : left2; break;
            case "right":  image = (flipFrame == 1) ? right1 : right2; break;
            // case "attack": image = (flipFrame == 1) ? attack1 : attack2; break;
            default:       image = idle1; break;
        }

        if (image != null) {
            g2.drawImage(image, screenX, screenY, 48, 48, null);
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.BOLD, 12));
            g2.drawString(playerName, screenX + 12, screenY - 5);
        }
    }
}