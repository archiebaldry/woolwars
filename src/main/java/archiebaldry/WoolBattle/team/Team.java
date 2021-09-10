package archiebaldry.WoolBattle.team;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class Team {

    private final String name;
    private final List<String> members = new ArrayList<>();
    private final Location spawn;
    private boolean wool = true;

    public Team(String name, Location spawn) {
        this.name = name;
        this.spawn = spawn;
    }

    public String getName() {
        return name;
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

    public void clearMembers() {
        members.clear();
    }

    public int countMembers() {
        return members.size();
    }

    public boolean hasWool() {
        return wool;
    }

    public void setWool(boolean bool) {
        wool = bool;
    }

    public Location getSpawn() {
        return spawn;
    }
}
