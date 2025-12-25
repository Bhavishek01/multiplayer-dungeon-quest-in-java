package items;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class shoe extends itemsdetail{
    public shoe() throws IOException
    {
        System.out.println("shoe");

        name = "shoe";
        id = 6;
        image = ImageIO.read(new File("resource/items/fast_shoe.png"));
        about = "A shoe that increases your speed for 1 minute";
    }
}
