package archiebaldry.woolwars;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.UUID;

public class WoolWarsTeam {

    String name;
    ChatColor colour;
    Location spawn;
    Location wool;

    ArrayList<UUID> members = new ArrayList<>();

    public WoolWarsTeam(String name, ChatColor colour, Location spawn, Location wool) {
        this.name = name;
        this.colour = colour;
        this.spawn = spawn;
        this.wool = wool;
    }

    public boolean hasWool() {
        return wool.getBlock().getType() == Material.WOOL;
    }

    @Override
    public String toString() {
        return name + ": " + members.toString();
    }

}
