package items;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class gold_bullet extends itemsdetail {

    public gold_bullet() throws IOException
    {
        name = "gold_bullet";
        image = ImageIO.read(new File("resource/object/gold_bullet.png"));
        about = "A special type of bullet that travels more distance and has more speed";
        speed = 7;
        
    }
    
}
