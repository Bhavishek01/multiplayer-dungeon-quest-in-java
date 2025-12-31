package main;

import java.awt.Rectangle;
import java.util.Collection;
import java.util.Iterator;

import gameplayers.entity;
import gameplayers.OtherPlayer;
import gameplayers.player;

public class ProjectileCollision {

    /**
     * Main method to check all projectile collisions.
     * - Local projectiles: vs tiles and other players
     * - Client projectiles: vs tiles and self (for instant hit feedback)
     */
    public static void checkCollisions(gamehandler gh) {
        // Local projectiles (your shots) - collide with map and other players
        synchronized (gh.localProjectiles) {
            Iterator<Projectile> it = gh.localProjectiles.iterator();
            while (it.hasNext()) {
                Projectile proj = it.next();
                if (!proj.active) continue;

                // Check tile collision (wall/stone)
                if (checkTileCollision(proj.x, proj.y, gh)) {
                    proj.active = false;
                    continue;
                }

                // Check collision with other players
                checkPlayerCollisions(proj, gh.otherPlayers.values(), gh);
            }
        }

        // Client projectiles (other players' shots) - collide with map and self
        synchronized (gh.clientProjectiles) {
            for (Projectile proj : gh.clientProjectiles) {
                if (!proj.active) continue;

                // Check tile collision (wall/stone)
                if (checkTileCollision(proj.x, proj.y, gh)) {
                    proj.active = false;
                    continue;
                }

                // Check collision with self only
                checkPlayerCollisions(proj, java.util.List.of(gh.p1), gh);
            }
        }
    }

    /**
     * Checks if projectile hits a wall (0) or stone (1) tile.
     */
    private static boolean checkTileCollision(double worldX, double worldY, gamehandler gh) {
        int col = (int) (worldX / gh.tiles);
        int row = (int) (worldY / gh.tiles);

        // Out of bounds = wall
        if (row < 0 || row >= gh.maprow || col < 0 || col >= gh.mapcol) {
            return true;
        }

        int tileType = gh.bgm.Maprowcol[row][col];
        return tileType == 0 || tileType == 1; // Wall or Stone
    }

    /**
     * Checks projectile collision with given player targets.
     * On hit: damage player, deactivate proj, handle death/kills/respawn.
     */
    private static void checkPlayerCollisions(Projectile proj, Collection<? extends entity> targets, gamehandler gh) {
        Rectangle projRect = new Rectangle((int) proj.x - 6, (int) proj.y - 6, 12, 12);

        for (entity target : targets) {
            Rectangle targetRect = new Rectangle(
                target.entity_map_X + target.colisionarea.x,
                target.entity_map_Y + target.colisionarea.y,
                target.colisionarea.width,
                target.colisionarea.height
            );

            if (projRect.intersects(targetRect)) {
                // Hit!
                target.life -= 1;
                proj.active = false;

                if (target.life <= 0) {
                    // Death: respawn
                    target.life = 5;
                    target.entity_map_X = 96;
                    target.entity_map_Y = 96;
                    target.direction = "down";

                    // Award kill to shooter
                    String killerId = proj.ownerId;
                    entity killer = null;
                    if (killerId.equals(gh.gc.id)) {
                        killer = gh.p1;
                    } else {
                        OtherPlayer killerOp = gh.otherPlayers.get(killerId);
                        if (killerOp != null) {
                            killer = killerOp;
                        }
                    }
                    if (killer != null) {
                        killer.kills += 1;
                    }
                }
                return; // One hit per projectile
            }
        }
    }
}