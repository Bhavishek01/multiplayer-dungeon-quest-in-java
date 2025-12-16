package items;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class arrow extends itemsdetail {

    public arrow() throws IOException
    {
        name = "arrow";
        image = ImageIO.read(new File("resource/items/arrow.png"));
        about = "It is used with bow to attack";
        speed = 5;
    }
}
