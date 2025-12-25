package items;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class bow extends itemsdetail {

    public bow() throws IOException
    {
        System.out.println("bow");
        id = 2;
        name = "bow";
        image = ImageIO.read(new File("resource/items/bow.png"));
        about = "A type of weapon, that uses arrow to attack";
    }
    
}
