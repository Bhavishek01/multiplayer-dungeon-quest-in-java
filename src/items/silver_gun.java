package items;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class silver_gun extends itemsdetail {

    public silver_gun() throws IOException
    {
        name = "silver_gun";
        id = 7;
        image = ImageIO.read(new File("resource/items/silver_gun.png"));
        about = "A type of weapon, that uses bullet to attack";
    }
    
}
