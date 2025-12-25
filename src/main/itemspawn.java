package main;

import java.io.IOException;
import java.util.Random;

import items.*;

public class itemspawn {
    
    gamehandler gh;
    itemsdetail[] itemTypes = new itemsdetail[8];  // Only 8 types available

    public itemspawn(gamehandler gh) 
    {
        this.gh = gh;
        loadItemTypes();  // Load the 8 types once
        spawnItems();     // Spawn 10 random items with random locations
    }

    private void loadItemTypes() {
        try {
            itemTypes[0] = new arrow();
            itemTypes[1] = new bow();
            itemTypes[2] = new bullet();
            itemTypes[3] = new gold_bullet();
            itemTypes[4] = new golden_gun();
            itemTypes[5] = new shoe();
            itemTypes[6] = new silver_gun();
            itemTypes[7] = new sword();
        } catch (IOException e) {
            e.printStackTrace();  // Handle image load failures
        }
    }

    private void spawnItems() {
        Random rand = new Random();
        if (gh.gameitems == null) {
            gh.gameitems = new itemsdetail[10];  // Ensure array exists (add to gamehandler if not)
        }

        for (int i = 0; i < gh.gameitems.length; i++) {
            // Pick random type from 0-7 (allow duplicates for variety)
            int randomTypeIndex = rand.nextInt(itemTypes.length);
            if (itemTypes[randomTypeIndex] == null) continue;  // Skip if load failed

            // Clone the item type (to avoid sharing instances)
            gh.gameitems[i] = cloneItem(itemTypes[randomTypeIndex]);

            boolean placed = false;
            int attempts = 0;
            int maxAttempts = 1000;  // Safety to prevent infinite loop

            while (!placed && attempts < maxAttempts) {
                int row = rand.nextInt(gh.maprow);  // 0-49
                int col = rand.nextInt(gh.mapcol);  // 0-49
                int tileType = gh.bgm.Maprowcol[row][col];

                if (tileType == 2 || tileType == 3) {  // Ground or water
                    gh.gameitems[i].x = col * gh.tiles;
                    gh.gameitems[i].y = row * gh.tiles;
                    System.out.println("Created item " + gh.gameitems[i].name + " (" + i + ") at (" + col + ", " + row + ")");
                    placed = true;
                }
                attempts++;
            }

            if (!placed) {
                System.err.println("Failed to place item " + i + " after " + maxAttempts + " attempts!");
            }
        }
    }

    // Helper to clone an item (since constructors throw IO, we can't new them easily)
    private itemsdetail cloneItem(itemsdetail original) {
        if (original == null) return null;
        itemsdetail clone = new itemsdetail();  // Base class
        clone.name = original.name;
        clone.id = original.id;
        clone.image = original.image;  // Share image (fine, immutable)
        clone.about = original.about;
        clone.speed = original.speed;
        clone.collision = original.collision;
        return clone;
    }
}