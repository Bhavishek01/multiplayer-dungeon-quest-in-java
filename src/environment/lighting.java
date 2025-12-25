package environment;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import main.gamehandler;

public class lighting 
{
    gamehandler gh;
    BufferedImage image;
    
    public lighting(gamehandler gh, int radius)
    {
        // Create a buffered image
        image = new BufferedImage (gh.base, gh.height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = (Graphics2D) image.getGraphics();

        // Create a screen-sized rectangle area
        Area screenArea = new Area (new Rectangle2D.Double (0,0,gh.base,gh.height));

        // Get the center x and y of the light circle
        int centerX = gh.p1.centerx;
        int centerY = gh.p1.centery;

        // Get the top left x and y of the light circle
        double x = centerX - (radius/2);
        double y = centerY - (radius/2);

        // Create a light circle shape
        Shape circleShape = new Ellipse2D.Double (x, y, radius, radius) ;

        // Create a light circle area
        Area lightArea = new Area(circleShape);

        // Subtract the light circle from the screen rectangle
        screenArea.subtract (lightArea);
        

        Color[] color = new Color[35];
        float fraction[] = new float[35];

        // Starting darker — no truly "clear" area, only dim visibility at the very center
        color[0]  = new Color(0, 0, 0, 0.1f);   // Dim center (barely visible)
        color[1]  = new Color(0, 0, 0, 0.15f);
        color[2]  = new Color(0, 0, 0, 0.25f);
        color[3]  = new Color(0, 0, 0, 0.30f);
        color[4]  = new Color(0, 0, 0, 0.40f);
        color[5]  = new Color(0, 0, 0, 0.50f);
        color[6]  = new Color(0, 0, 0, 0.60f);
        color[7]  = new Color(0, 0, 0, 0.70f);
        color[8]  = new Color(0, 0, 0, 0.75f);
        color[9]  = new Color(0, 0, 0, 0.80f);
        color[10] = new Color(0, 0, 0, 0.83f);
        color[11] = new Color(0, 0, 0, 0.85f);
        color[12] = new Color(0, 0, 0, 0.87f);
        color[13] = new Color(0, 0, 0, 0.89f);
        color[14] = new Color(0, 0, 0, 0.90f);
        color[15] = new Color(0, 0, 0, 0.91f);
        color[16] = new Color(0, 0, 0, 0.92f);
        color[17] = new Color(0, 0, 0, 0.93f);
        color[18] = new Color(0, 0, 0, 0.935f);
        color[19] = new Color(0, 0, 0, 0.94f);
        color[20] = new Color(0, 0, 0, 0.945f);
        color[21] = new Color(0, 0, 0, 0.95f);
        color[22] = new Color(0, 0, 0, 0.955f);
        color[23] = new Color(0, 0, 0, 0.96f);
        color[24] = new Color(0, 0, 0, 0.965f);
        color[25] = new Color(0, 0, 0, 0.97f);
        color[26] = new Color(0, 0, 0, 0.972f);
        color[27] = new Color(0, 0, 0, 0.975f);
        color[28] = new Color(0, 0, 0, 0.978f);
        color[29] = new Color(0, 0, 0, 0.980f);
        color[30] = new Color(0, 0, 0, 0.983f);
        color[31] = new Color(0, 0, 0, 0.986f);
        color[32] = new Color(0, 0, 0, 0.989f);
        color[33] = new Color(0, 0, 0, 0.992f);
        color[34] = new Color(0, 0, 0, 0.995f);   // Almost pure black at the edge

        // Tight fractions — rapid falloff early, then very slow near edge for smooth blend into total darkness
        fraction[0]  = 0.00f;
        fraction[1]  = 0.08f;
        fraction[2]  = 0.14f;
        fraction[3]  = 0.19f;
        fraction[4]  = 0.23f;
        fraction[5]  = 0.27f;
        fraction[6]  = 0.31f;
        fraction[7]  = 0.35f;
        fraction[8]  = 0.39f;
        fraction[9]  = 0.43f;
        fraction[10] = 0.47f;
        fraction[11] = 0.50f;
        fraction[12] = 0.53f;
        fraction[13] = 0.56f;
        fraction[14] = 0.59f;
        fraction[15] = 0.62f;
        fraction[16] = 0.65f;
        fraction[17] = 0.68f;
        fraction[18] = 0.71f;
        fraction[19] = 0.74f;
        fraction[20] = 0.77f;
        fraction[21] = 0.80f;
        fraction[22] = 0.83f;
        fraction[23] = 0.86f;
        fraction[24] = 0.89f;
        fraction[25] = 0.91f;
        fraction[26] = 0.93f;
        fraction[27] = 0.95f;
        fraction[28] = 0.96f;
        fraction[29] = 0.97f;
        fraction[30] = 0.978f;
        fraction[31] = 0.984f;
        fraction[32] = 0.99f;
        fraction[33] = 0.995f;
        fraction[34] = 1.00f;

        // Create a gradation paint settings for the light circle
        RadialGradientPaint gPaint = new RadialGradientPaint (centerX, centerY, (radius/2), fraction, color);

        // Set the gradient data on g2
        g2.setPaint(gPaint);

        // Draw the light circlel
        g2.fill(lightArea);

        // Draw the screen rectangle without the light circle area
        g2.fill(screenArea);

        g2.dispose();
    }

    public void draw(Graphics2D g2)
    {
        g2.drawImage(image, 0,0, null);
    }
    
}