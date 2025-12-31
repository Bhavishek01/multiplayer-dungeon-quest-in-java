package main;

public class Projectile {
    public double x, y;
    public double targetX, targetY;
    public String ownerId;
    public double speed = 8.0;
    public int maxDistance = 500;
    public boolean active = true;
    private double startX, startY; // For distance tracking
    private long birthTime = System.currentTimeMillis();

    public Projectile(double currX, double currY, double targX, double targY, String owner) {
        this.startX = currX;
        this.startY = currY;
        this.x = currX;
        this.y = currY;
        this.targetX = targX;
        this.targetY = targY;
        this.ownerId = owner;

        // Offset start position slightly in front of shooter
        double dx = targX - currX;
        double dy = targY - currY;
        double dist = Math.hypot(dx, dy);
        if (dist > 0) {
            dx /= dist;
            dy /= dist;
            this.x += dx * 15;
            this.y += dy * 15;
            this.startX += dx * 15; // Update start too
            this.startY += dy * 15;
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

        // Timeout by distance traveled
        double traveled = Math.hypot(x - startX, y - startY);
        if (traveled > maxDistance) {
            active = false;
        }
    }

    public int getScreenX(gamehandler gh) {
        return (int) (x - gh.p1.entity_map_X + gh.p1.centerx);
    }

    public int getScreenY(gamehandler gh) {
        return (int) (y - gh.p1.entity_map_Y + gh.p1.centery);
    }
}