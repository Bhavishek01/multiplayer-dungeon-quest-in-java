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

public class light 
{
    gamehandler gh;
    BufferedImage image;
    
    public light(gamehandler gh, int radius)
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
        

        Color[] colors = new Color[25];
        float fractions[] = new float[25];

        // Center: somewhat clear (but still dim — ~40% visibility)
        colors[0]  = new Color(0, 0, 0, 0.40f);
        colors[1]  = new Color(0, 0, 0, 0.55f);
        colors[2]  = new Color(0, 0, 0, 0.65f);
        colors[3]  = new Color(0, 0, 0, 0.72f);
        colors[4]  = new Color(0, 0, 0, 0.78f);
        colors[5]  = new Color(0, 0, 0, 0.82f);
        colors[6]  = new Color(0, 0, 0, 0.85f);
        colors[7]  = new Color(0, 0, 0, 0.88f);
        colors[8]  = new Color(0, 0, 0, 0.90f);
        colors[9]  = new Color(0, 0, 0, 0.92f);
        colors[10] = new Color(0, 0, 0, 0.93f);
        colors[11] = new Color(0, 0, 0, 0.94f);
        colors[12] = new Color(0, 0, 0, 0.95f);
        colors[13] = new Color(0, 0, 0, 0.96f);
        colors[14] = new Color(0, 0, 0, 0.965f);
        colors[15] = new Color(0, 0, 0, 0.97f);
        colors[16] = new Color(0, 0, 0, 0.975f);
        colors[17] = new Color(0, 0, 0, 0.98f);
        colors[18] = new Color(0, 0, 0, 0.982f);
        colors[19] = new Color(0, 0, 0, 0.984f);
        colors[20] = new Color(0, 0, 0, 0.986f);
        colors[21] = new Color(0, 0, 0, 0.988f);
        colors[22] = new Color(0, 0, 0, 0.990f);
        colors[23] = new Color(0, 0, 0, 0.992f);
        colors[24] = new Color(0, 0, 0, 0.995f);  // Edge is nearly black

        // Fractions: fade very quickly at first, then slower toward the edge
        fractions[0]  = 0.00f;
        fractions[1]  = 0.10f;
        fractions[2]  = 0.18f;
        fractions[3]  = 0.25f;
        fractions[4]  = 0.31f;
        fractions[5]  = 0.36f;
        fractions[6]  = 0.41f;
        fractions[7]  = 0.46f;
        fractions[8]  = 0.51f;
        fractions[9]  = 0.56f;
        fractions[10] = 0.60f;
        fractions[11] = 0.64f;
        fractions[12] = 0.68f;
        fractions[13] = 0.72f;
        fractions[14] = 0.76f;
        fractions[15] = 0.79f;
        fractions[16] = 0.82f;
        fractions[17] = 0.85f;
        fractions[18] = 0.88f;
        fractions[19] = 0.91f;
        fractions[20] = 0.94f;
        fractions[21] = 0.96f;
        fractions[22] = 0.98f;
        fractions[23] = 0.99f;
        fractions[24] = 1.00f;

        // Create a gradation paint settings for the light circle
        RadialGradientPaint gPaint = new RadialGradientPaint (centerX, centerY, (radius/2), fractions, colors);

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