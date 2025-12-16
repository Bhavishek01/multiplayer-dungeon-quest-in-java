package items;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class golden_gun extends itemsdetail {

    public golden_gun() throws IOException
    {
        name = "golden_gun";
        image = ImageIO.read(new File("resource/items/golden_gun.png"));
        about = " A special type of gun that shoots gold bullet only";
    }
    
}
