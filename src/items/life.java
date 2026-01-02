package items;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class life extends itemsdetail {

    public life() throws IOException
    {
        System.out.println("health");
        id = 10;
        name = "Health";
        image = ImageIO.read(new File("resource/items/life.png"));
        about = "increases health by 1";
    }
    
}
