package main;

import gameobj.entity;

public class colisiondetection extends gamepannel{

    private gamehandler gh;

    public colisiondetection(gamehandler gh)
    {
        this.gh = gh;
    }

    public void checkcolision(entity entity) {
    // Get player's collision area bounds in map coordinates
    int entity_left_axis = entity.entity_map_X + entity.colisionarea.x;
    int entity_right_axis = entity_left_axis + entity.colisionarea.width;
    int entity_top_axis = entity.entity_map_Y + entity.colisionarea.y;
    int entity_bottom_axis = entity_top_axis + entity.colisionarea.height;

    // Calculate tile indices for the player's collision area after potential move
    int entity_left_col, entity_right_col, entity_top_row, entity_bottom_row;

    switch (entity.direction) {
        case "up":
            entity_top_axis -= entity.entityspeed;
            break;
        case "down":
            entity_bottom_axis += entity.entityspeed;
            break;
        case "right":
            entity_right_axis += entity.entityspeed;
            break;
        case "left":
            entity_left_axis -= entity.entityspeed;
            break;
    }

    // Convert to tile indices
    entity_left_col = entity_left_axis / tiles;
    entity_right_col = entity_right_axis / tiles;
    entity_top_row = entity_top_axis / tiles;
    entity_bottom_row = entity_bottom_axis / tiles;

    for (int row = entity_top_row; row <= entity_bottom_row; row++) 
        {
            for (int col = entity_left_col; col <= entity_right_col; col++) {
                int tileType = gh.bgm.Maprowcol[row][col];
                if (tileType != 2 && tileType != 3) { // Non-ground tiles (0=wall, 1=stone, 3=water) are collidable
                    entity.colision = true;
                    return;
                    // Exit early if any collidable tile is found
                }
                if (tileType == 3) 
                    {
                    entity.entityspeed = 1;
                    return;
                    }
            }
        }
    }
}