package main;

import java.awt.Rectangle;
import java.util.Collection;
import java.util.Iterator;
import java.util.Random;

import gameplayers.entity;


public class ProjectileCollision {

    public static void checkCollisions(gamehandler gh) {
    // === LOCAL PROJECTILES (your shots) — full client prediction ===
    synchronized (gh.localProjectiles) {
        Iterator<Projectile> it = gh.localProjectiles.iterator();
        while (it.hasNext()) {
            Projectile proj = it.next();
            if (!proj.active) continue;

            // WALL: Instant removal for shooter feedback
            if (checkTileCollision(proj.x, proj.y, gh)) {
                it.remove();
                continue;
            }

            // PLAYER HIT: Instant feel
            synchronized (gh.otherPlayers) {
                // if (checkPlayerCollisions(proj, gh.otherPlayers.values(), gh, it)) {
                //     continue;
                // }
            }
        }
    }

    // === REMOTE PROJECTILES (others' shots) — server authoritative ===
    synchronized (gh.clientProjectiles) {
        Iterator<Projectile> it = gh.clientProjectiles.iterator();
        while (it.hasNext()) {
            Projectile proj = it.next();
            if (!proj.active) continue;

            // NO WALL COLLISION CHECK — server handles it
            // Only check if it hits YOU (instant damage feel)

            // if (checkPlayerCollisions(proj, java.util.List.of(gh.p1), gh, it)) {
            //     continue;
            // }
        }
    }
}

    private static boolean checkTileCollision(double worldX, double worldY, gamehandler gh) {
        int col = (int) (worldX / gh.tiles);
        int row = (int) (worldY / gh.tiles);

        if (row < 0 || row >= gh.maprow || col < 0 || col >= gh.mapcol) {
            return true; // Out of bounds = wall
        }

        int tileType = gh.bgm.Maprowcol[row][col];
        return tileType == 0 || tileType == 1; // Wall or Stone
    }

    /**
     * Returns true if projectile hit a player and was removed
     */
    private static boolean checkPlayerCollisions(
            Projectile proj,
            Collection<? extends entity> targets,
            gamehandler gh,
            Iterator<Projectile> iterator) {  // Pass the iterator!

        Rectangle projRect = new Rectangle((int) proj.x - 6, (int) proj.y - 6, 12, 12);

        for (entity target : targets) {
            Rectangle targetRect = new Rectangle(
                target.entity_map_X + target.colisionarea.x,
                target.entity_map_Y + target.colisionarea.y,
                target.colisionarea.width,
                target.colisionarea.height
            );

            if (projRect.intersects(targetRect)) {
                // HIT!
                target.life -= 1;

                // IMMEDIATELY REMOVE using the iterator (safe & clean)
                iterator.remove();

                if (target.life <= 0) {
                    // Respawn
                    target.life = 5;
                    target.direction = "down";

                    Random rand = new Random();
                    boolean validSpawn = false;
                    int attempts = 0;
                    final int maxAttempts = 100;

                    while (!validSpawn && attempts < maxAttempts) {
                        int tileX = rand.nextInt(48) + 1;
                        int tileY = rand.nextInt(48) + 1;
                        int mapValue = gh.bgm.Maprowcol[tileY][tileX];
                        if (mapValue == 2) { // Ground
                            target.entity_map_X = tileX * gh.tiles;
                            target.entity_map_Y = tileY * gh.tiles;
                            validSpawn = true;
                        }
                        attempts++;
                    }

                    if (!validSpawn) {
                        target.entity_map_X = 96;
                        target.entity_map_Y = 96;
                    }

                    // Award kill
                    String killerId = proj.ownerId;
                    entity killer = killerId.equals(gh.gc.id) ? gh.p1 : gh.otherPlayers.get(killerId);

                    if (killer != null) {
                        killer.kills += 1;
                        System.out.println("Kill awarded to " + killerId + " | Total kills: " + killer.kills);
                    }
                }

                return true; // Projectile removed
            }
        }
        return false; // No hit
    }
}