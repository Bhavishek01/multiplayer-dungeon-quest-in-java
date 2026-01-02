// New file: main/PlayerRanking.java (duplicate for client-side)
package main;

public class PlayerRanking {
    public String id;
    public String name;
    public int wins;

    public PlayerRanking(String id, String name, int wins) {
        this.id = id;
        this.name = name;
        this.wins = wins;
        // this.kills = kills;
    }
}