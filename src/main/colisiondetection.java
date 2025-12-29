package main;

import gameplayers.OtherPlayer;
import gameplayers.entity;
import items.itemsdetail;
import java.awt.Rectangle;

public class colisiondetection extends gamepannel {

    private gamehandler gh;

    public colisiondetection(gamehandler gh) 
    {
        this.gh = gh;
    }

    // New: Predictive check for proposed movement delta
    public void checkcolision(entity entity)
    {
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
                for (int col = entity_left_col; col <= entity_right_col; col++) 
                {
                    int tileType = gh.bgm.Maprowcol[row][col];
                    if (tileType != 2 && tileType != 3) 
                    { // Non-ground tiles (0=wall, 1=stone, 3=water) are collidable
                        entity.colision = true;
                        return;
                        // Exit early if any collidable tile is found
                    }
                    if (tileType == 3) 
                    {
                        gh.p1.iswater = true;
                        gh.p1.island = false;
                    }
                    else if(tileType == 2){
                        gh.p1.iswater = false;
                        gh.p1.island = true;
                    }
                }
            }
    }

    // Updated: Predictive item check, handle pickup
    public void item_collision(entity entity, input key) 
    {
        int entity_left_axis = entity.entity_map_X + entity.colisionarea.x;
        int entity_right_axis = entity_left_axis + entity.colisionarea.width;
        int entity_top_axis = entity.entity_map_Y + entity.colisionarea.y;
        int entity_bottom_axis = entity_top_axis + entity.colisionarea.height;

        int next_left_axis = entity_left_axis;
        int next_top_axis = entity_top_axis;
        int next_bottom_axis = entity_bottom_axis;
        int next_right_axis = entity_right_axis;


        switch (entity.direction) {
            case "up":
                next_top_axis -= entity.entityspeed;
                break;
            case "down":
                next_bottom_axis += entity.entityspeed;
                break;
            case "right":
                next_right_axis += entity.entityspeed;
                break;
            case "left":
                next_left_axis -= entity.entityspeed;
                break;
            default:
                return; // No movement, no need to check collision
        }

        Rectangle entityBounds = new Rectangle(
            next_left_axis,
            next_top_axis,
            entity.colisionarea.width,
            entity.colisionarea.height
        );

        for (int i = 0; i < gh.gameitems.length; i++) {
            itemsdetail item = gh.gameitems[i];
            if (item != null) {
                Rectangle itemBounds = new Rectangle(
                    item.x + item.collision_area.x,
                    item.y + item.collision_area.y,
                    item.collision_area.width,
                    item.collision_area.height
                );

                if (entityBounds.intersects(itemBounds)) {
                    entity.colision = true;
                    entity.item_colision = true;

                    if (key.pick) 
                    {
                        for (PlayerItem invItem : gh.gc.Items)
                        {
                            if (invItem.id == item.id) {  // Compare int IDs
                                invItem.quantity += 1;           // Increase quantity
                                break;
                            }
                        }
                        
                        System.out.println("Picked up " + item.name + " (ID: " + item.id + ")");
                        gh.gameitems[i] = null; // Remove item from game world
                    }
                    return; 
                }
            }
        }
    }

    public void player_colision(entity entity) {
        if (!entity.direction.equals("up") && !entity.direction.equals("down") &&
            !entity.direction.equals("left") && !entity.direction.equals("right")) {
            return;
        }

        int left = entity.entity_map_X + entity.colisionarea.x;
        int right = left + entity.colisionarea.width;
        int top = entity.entity_map_Y + entity.colisionarea.y;
        int bottom = top + entity.colisionarea.height;

        int next_left = left, next_right = right, next_top = top, next_bottom = bottom;
        switch (entity.direction) {
            case "up":    next_top -= entity.entityspeed;    break;
            case "down":  next_bottom += entity.entityspeed; break;
            case "right": next_right += entity.entityspeed;  break;
            case "left":  next_left -= entity.entityspeed;   break;
        }

        Rectangle nextBounds = new Rectangle(next_left, next_top,
                entity.colisionarea.width, entity.colisionarea.height);

        // Check against all other players
        for (OtherPlayer other : gh.otherPlayers.values()) {
            if (other == null) continue;

            Rectangle otherBounds = new Rectangle(
                other.entity_map_X + other.colisionarea.x,
                other.entity_map_Y + other.colisionarea.y,
                other.colisionarea.width,
                other.colisionarea.height
            );

            if (nextBounds.intersects(otherBounds)) {
                entity.colision = true;  // Block movement in this direction
                return;                  // Early exit on first collision
            }
        }
    }
}