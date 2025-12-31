// socket_and_Jdbc/Projectile.java
package socket_and_Jdbc;

public class Projectile {
    public long id;
    public String ownerId;
    public double x, y;
    public double startX, startY;
    public double targetX, targetY;
    public double speed = 8.0;
    public int maxDistance = 400;
    public boolean active = true;

    public Projectile(double startX, double startY, double targetX, double targetY, String ownerId) {
        this.ownerId = ownerId;
        this.startX = startX;
        this.startY = startY;
        this.x = startX;
        this.y = startY;
        this.targetX = targetX;
        this.targetY = targetY;

        // Normalize direction and offset slightly
        double dx = targetX - startX;
        double dy = targetY - startY;
        double dist = Math.hypot(dx, dy);
        if (dist > 0) {
            dx /= dist;
            dy /= dist;
            this.x += dx * 15;  // Start a bit in front of player
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

        double traveled = Math.hypot(x - startX, y - startY);
        if (traveled >= maxDistance) {
            active = false;
        }
    }
}