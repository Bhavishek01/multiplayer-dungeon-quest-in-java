package items;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class bullet extends itemsdetail {

    public bullet() throws IOException
    {
        System.out.println("bullet");
        name = "bullet";
        id = 3;
        image = ImageIO.read(new File("resource/items/bullets.png"));
        about = "A special type of bullet that travels more distance and has more speed";
        speed = 7;
        
    }
    
}
