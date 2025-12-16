package items;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class sword extends itemsdetail {
    public sword() throws IOException
    {
        name = "sword";
        image = ImageIO.read(new File("resource/object/sword.png"));

    }
    
}
