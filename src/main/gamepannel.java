package main;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;

public class gamepannel extends JPanel
{

    public int tiles = 48 ;
    public int maxcol = 16;
    public int maxrow = 12;

    public int height = tiles * maxrow;
    public int base = tiles *maxcol;

    public int maprow = 50;
    public int mapcol = 50;
    public int mapheight = tiles * mapcol;
    public int mapwidth = tiles * maprow;

    public gamepannel()
    {
        this.setPreferredSize(new Dimension(base,height));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
    }


}
