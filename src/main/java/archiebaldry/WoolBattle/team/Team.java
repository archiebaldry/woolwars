package archiebaldry.WoolBattle.team;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class Team {

    private boolean active = true;
    private final String name;
    private final List<String> members = new ArrayList<>();
    private final String prefix;
    private final Location spawnLocation;
    private final Location woolLocation;

    public Team(String name, String prefix, Location spawnLocation, Location woolLocation) {
        this.name = name;
        this.prefix = prefix;
        this.spawnLocation = spawnLocation;
        this.woolLocation = woolLocation;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getName() {
        return name;
    }

    public String getPrefix() {
        return prefix;
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    public Location getWoolLocation() {
        return woolLocation;
    }

    public void addMember(String playerName) {
        members.add(playerName);
    }

    public void removeMember(String playerName) {
        members.remove(playerName);
    }

    public boolean hasMember(String playerName) {
        return members.contains(playerName);
    }

}
