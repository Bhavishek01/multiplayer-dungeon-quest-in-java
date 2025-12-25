package items;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class sword extends itemsdetail {
    public sword() throws IOException
    {
        System.out.println("sword");
        name = "sword";
        id = 8;
        image = ImageIO.read(new File("resource/items/sword.png"));
        about = "A normal sword";
    }
    
}
