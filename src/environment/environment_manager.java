package environment;

import java.awt.Graphics2D;

import main.gamehandler;

public class environment_manager 
{

    gamehandler gh;
    lighting lighting;
    light light;

    public environment_manager (gamehandler gh) 
    {
        this.gh = gh;
    }
    public void setup() 
    {
        lighting = new lighting(gh, 750);
        light = new light(gh, 650);
    }

    public void draw(Graphics2D g2) 
    {
        lighting.draw(g2);
    }

        public void draw1(Graphics2D g2) 
    {
        light.draw(g2);
    }

}