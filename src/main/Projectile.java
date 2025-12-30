package main;

public class Projectile {
    public long id;
    public String ownerId;
    public double x, y;
    public boolean active = true;

    public Projectile(long id, String ownerId, double x, double y) {
        this.id = id;
        this.ownerId = ownerId;
        this.x = x;
        this.y = y;
    }

    public int getScreenX(gamehandler gh) {
        return (int) (x - gh.p1.entity_map_X + gh.p1.centerx);
    }

    public int getScreenY(gamehandler gh) {
        return (int) (y - gh.p1.entity_map_Y + gh.p1.centery);
    }
}