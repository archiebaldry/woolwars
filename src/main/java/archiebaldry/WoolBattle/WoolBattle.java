package archiebaldry.WoolBattle;

import archiebaldry.WoolBattle.command.CommandJoin;
import archiebaldry.WoolBattle.command.CommandLeave;
import archiebaldry.WoolBattle.command.CommandStart;
import archiebaldry.WoolBattle.team.Team;
import archiebaldry.WoolBattle.team.Teams;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class WoolBattle extends JavaPlugin {

    private boolean gameStarted;
    private World world;

    private Teams teams;

    FileConfiguration config = getConfig();

    @Override
    public void onEnable() {
        config.options().copyDefaults(true);
        saveConfig();

        gameStarted = false;
        world = getServer().getWorlds().get(0);

        teams = new Teams();

        // Events
        getServer().getPluginManager().registerEvents(new EventListener(), this);

        // Commands
        getCommand("join").setExecutor(new CommandJoin());
        getCommand("leave").setExecutor(new CommandLeave());
        getCommand("start").setExecutor(new CommandStart());
    }

    public Location getLocationFromConfig(String path) {
        double x = config.getDouble(path + ".x");
        double y = config.getDouble(path + ".y");
        double z = config.getDouble(path + ".z");
        if (config.contains(path + ".yaw")) {
            float yaw = (float) config.getDouble(path + ".yaw");
            float pitch = (float) config.getDouble(path + ".pitch");
            return new Location(getWorld(), x, y, z, yaw, pitch);
        }
        return new Location(getWorld(), x, y, z);
    }

    public Location toBlockLocation(Location loc) {
        return new Location(loc.getWorld(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
    }

    public void startGame() {
        gameStarted = true;

        Bukkit.broadcastMessage("The game has started!");

        for (Player player : Bukkit.getOnlinePlayers()) {
            Team team = teams.getPlayerTeam(player.getName());
            if (team == null) { // Spectator
                player.setGameMode(GameMode.SPECTATOR);
            } else {
                player.setGameMode(GameMode.SURVIVAL);
                player.teleport(team.getSpawnLocation());
            }
        }
    }

    public boolean hasGameStarted() {
        return gameStarted;
    }

    public Teams getTeams() {
        return teams;
    }

    public World getWorld() {
        return world;
    }

}
