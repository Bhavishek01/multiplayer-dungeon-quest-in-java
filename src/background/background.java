package background;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.IOException;
import javax.swing.JPanel;
import java.awt.Color;

public class background extends JPanel {
    private BufferedImage image;

    public background() {
        // Load background image
        try {
            image = ImageIO.read(new File("resource/bg.png"));
        } catch (IOException e) {
            e.printStackTrace();
            image = null; // Handle missing image gracefully
        }
        setOpaque(true); // Ensure panel is opaque
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        if (image != null) {
            // Scale and draw the background image to fit panel size
            g2.drawImage(image, 0, 0, getWidth(), getHeight(), null);
        } else {
            // Fallback to black background if image fails to load
            g2.setColor(Color.BLACK);
            g2.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}