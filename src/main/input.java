package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class input implements KeyListener
{
    public boolean up,down,left,right,attack,resume,pause,inventory_open,chat;
    public boolean pressed;

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) 
    {
        int get = e.getKeyCode();
        

        if((get == KeyEvent.VK_C)){
            chat = true;
        }

        if((get == KeyEvent.VK_E)){
            inventory_open = true; // rename your existing 'item' if needed
        }   

        if((get == KeyEvent.VK_P) ){
            pause = true;
        }

        if((get == KeyEvent.VK_Q) ){
            resume = true;
        }

        if((get == KeyEvent.VK_W) || (get == KeyEvent.VK_UP)){
            up = true;   
        }

        if((get == KeyEvent.VK_S) || (get == KeyEvent.VK_DOWN)){
            down =true;
        }
        if((get == KeyEvent.VK_A) || (get == KeyEvent.VK_LEFT)){
            left =true;
        }
        
        if((get == KeyEvent.VK_D) || (get == KeyEvent.VK_RIGHT)){
            right =true;
        }

        if(get == KeyEvent.VK_SPACE){
            attack = true;
        }

    }

    @Override
    public void keyReleased(KeyEvent e) 
    {
        int get = e.getKeyCode();

        if((get == KeyEvent.VK_W) || (get == KeyEvent.VK_UP)){
            up = false;
        }
        if((get == KeyEvent.VK_C)){
            chat = false;
        }
        if((get == KeyEvent.VK_E) ){
            inventory_open = false;
        }
        if((get == KeyEvent.VK_Q) ){
            resume = false;
        }
        if((get == KeyEvent.VK_P) ){
            pause = false;
        }
        if((get == KeyEvent.VK_S) || (get == KeyEvent.VK_DOWN)){
            down =false;
        }
        if((get == KeyEvent.VK_A) || (get == KeyEvent.VK_LEFT)){
            left =false;
        }
        if((get == KeyEvent.VK_D) || (get == KeyEvent.VK_RIGHT)){
            right =false;
        }
        if(get == KeyEvent.VK_SPACE){
            attack = false;
        }
    }   
}