package main;

public class Projectile {
    public double x, y;
    public double targetX, targetY;
    public double speed = 8.0;
    public int maxDistance = 500;
    public boolean active = true;
    public long birthTime = System.currentTimeMillis();

    public Projectile(double startX, double startY, double targetX, double targetY) {
        this.x = startX;
        this.y = startY;
        this.targetX = targetX;
        this.targetY = targetY;

        // Small offset so it starts in front of player
        double dx = targetX - startX;
        double dy = targetY - startY;
        double dist = Math.hypot(dx, dy);
        if (dist > 0) {
            dx /= dist;
            dy /= dist;
            this.x += dx * 15;
            this.y += dy * 15;
        }
    }

    public void update() {
        if (!active) return;

        double dx = targetX - x;
        double dy = targetY - y;
        double remaining = Math.hypot(dx, dy);

        if (remaining < speed) {
            active = false;
            return;
        }

        dx /= remaining;
        dy /= remaining;

        x += dx * speed;
        y += dy * speed;

        // Optional: timeout after max distance
        double traveled = Math.hypot(x - (targetX - dx * remaining), y - (targetY - dy * remaining));
        if (traveled > maxDistance) {
            active = false;
        }
    }

    public int getScreenX(gamehandler gh) {
        return (int)(x - gh.p1.entity_map_X + gh.p1.centerx);
    }

    public int getScreenY(gamehandler gh) {
        return (int)(y - gh.p1.entity_map_Y + gh.p1.centery);
    }
}