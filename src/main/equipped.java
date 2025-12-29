package main;

import items.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.SwingUtilities;

public class equipped 
{
    private final int[] equippedItemIds; // size 3
    private final gamehandler gh;

    private Timer shoeTimer = null;
    private boolean shoeWasConsumed = false;

    private final Map<Integer, itemsdetail> itemCache = new HashMap<>();

    public equipped(int[] equippedItemIds, gamehandler gh) {
        this.equippedItemIds = equippedItemIds;
        this.gh = gh;
        cacheItems();
    }

    public void cacheItems() {
        try {
            itemCache.put(1, new arrow());
            itemCache.put(2, new bow());
            itemCache.put(3, new bullet());
            itemCache.put(4, new gold_bullet());
            itemCache.put(5, new golden_gun());
            itemCache.put(6, new shoe());
            itemCache.put(7, new silver_gun());
            itemCache.put(8, new sword());
            itemCache.put(9, new light());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Apply effects of ALL currently equipped items */
    public void apply_effects() {
        resetTemporaryEffects();

        for (int i = 0; i < equippedItemIds.length; i++) {
        int id = equippedItemIds[i];
        if (id == 0) {
            gh.equippedHudIcons[i] = null;
            gh.equippedHudNames[i] = null;
        } else {
            itemsdetail item = itemCache.get(id);
            if (item != null) {
                gh.equippedHudIcons[i] = item.image;
                gh.equippedHudNames[i] = item.name.toUpperCase();
            }
        }
    }

        for (int id : equippedItemIds) {
            if (id == 0) continue; // empty slot

            switch (id) {
                case 1: // arrow
                    equip_arrow();
                    break;
                case 2: // bow
                    equip_bow();
                    break;
                case 3: // bullet
                    using_bullet();
                    break;
                case 4: // gold_bullet
                    // using_gold_bullet();
                    break;
                case 5: // golden_gun
                    // equip_golden_gun();
                    break;
                case 6: // Shoe 
                    equip_shoe();
                    break;
                case 7:  // silver_gun
                    equip_silver_gun();
                    break;
                case 8: // sword
                    // equip_sword();
                    break;
                case 9: // light 
                    USing_light();
                    break;
            }
        }
    }

    /** Remove/cancel all temporary effects (call before re-applying) */
    public void remove_effects(int itemId) 
    {
        boolean found = false;
        for (int i = 0; i < equippedItemIds.length; i++) {
            if (equippedItemIds[i] == itemId) {
                equippedItemIds[i] = 0;

            switch (itemId) {
                case 1: // arrow
                    unequip_arrow();
                    break;
                case 2: // bow
                    // unequip_bow();
                    break;
                case 3: // bullet
                    // using_bullet();
                    break;
                case 4: // gold_bullet
                    // using_bullet();
                    break;
                case 5: // golden_gun
                    // unequip_golden_gun();
                    break;
                case 6: // Shoe 
                    unequip_shoe();
                    gh.shoeStartTime = 0; 
                    break;
                case 7:  // silver_gun
                    unequip_silver_gun();
                    break;
                case 8: // sword
                    // unequip_sword();
                    break;
                case 9: // light 
                    close_light();
                    break;

            }
            found = true;
            break;
        }
    }
    if (found) {
        apply_effects(); // Re-apply remaining effects

        // Update UI
        if (gh.gameinventory != null && gh.gameinventory.isVisible()) {
            SwingUtilities.invokeLater(() -> gh.gameinventory.updateEquippedDisplay());
        }
    }
    }

    // ==================== EQUIP METHODS ====================
    private void equip_arrow()
    {

    }

    private void equip_bow()
    {

    }

    private void using_bullet()
    {

    }

    private void using_gold_bullet()
    {

    }

    private void equip_golden_gun()
    {

    }

    private void equip_shoe() {
        gh.p1.using_shoe = true;

        gh.shoeStartTime = System.currentTimeMillis();

        // Cancel any existing timer
        if (shoeTimer != null) {
            shoeTimer.cancel();
        }

        shoeWasConsumed = false;

        shoeTimer = new Timer();
        shoeTimer.schedule(new TimerTask() {
            @Override
            public void run() 
            {
                // Time's up!
                shoeWasConsumed = true;
                gh.p1.using_shoe = false;
                gh.shoeStartTime = 0;

                remove_effects(6);
                // Notify UI to refresh equipped slots
                if (gh.gameinventory != null && gh.gameinventory.isVisible()) {
                    SwingUtilities.invokeLater(() -> gh.gameinventory.updateEquippedDisplay());
                }
            }
        }, 60_000); // 60 seconds
    }

    private void equip_silver_gun()
    {

    }

    private void equip_sword()
    {

    }

    private void USing_light() {
        gh.light_use = true;
    }

    // ==================== UNEQUIP METHODS ====================
    private void unequip_arrow()
    {

    }

    private void unequip_golden_gun()
    {

    }

    private void unequip_shoe() 
    {
        if (shoeTimer != null && !shoeWasConsumed) 
        {
            shoeTimer.cancel();
            shoeTimer = null;
        }
        gh.p1.using_shoe = false;
        gh.shoeStartTime = 0;
    }

    private void unequip_silver_gun()
    {

    }

    private void unequip_sword()
    {

    }

    private void close_light() {
        gh.light_use = false;
    }


    public int[] getEquippedItems() {
        return equippedItemIds.clone();
    }

    public boolean isItemEquipped(int itemId) {
        for (int id : equippedItemIds) {
            if (id == itemId) return true;
        }
        return false;
    }

    private void resetTemporaryEffects() {
        // Reset any temporary state
        if (!isItemEquipped(6)) {
            gh.p1.using_shoe = false;
        }
        if (!isItemEquipped(9)) {
            gh.light_use = false;
        }
    }
}