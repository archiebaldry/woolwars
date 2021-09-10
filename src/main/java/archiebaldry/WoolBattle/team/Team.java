package archiebaldry.WoolBattle.team;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class Team {

    private boolean active = true;
    private final String name;
    private final List<String> members = new ArrayList<>();
    private final Location spawn;

    public Team(String name, Location spawn) {
        this.name = name;
        this.spawn = spawn;
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

    public Location getSpawn() {
        return spawn;
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
