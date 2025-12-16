package items;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class bullet extends itemsdetail {

    public bullet() throws IOException
    {
        name = "bullet";
        image = ImageIO.read(new File("resource/object/bullet.png"));
        about = "It is used with gun to attack";
        speed = 6;
    }
    
}
