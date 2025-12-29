package items;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class light extends itemsdetail {

    public light() throws IOException
    {
        System.out.println("Light");
        name = "light";
        id = 9;
        image = ImageIO.read(new File("resource/items/light.png"));
        about = "kill the darkness inside the dungeon";
    }
    
}
