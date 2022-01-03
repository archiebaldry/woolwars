package archiebaldry.woolwars;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.List;
import java.util.UUID;

public class WoolWars extends JavaPlugin implements CommandExecutor {

    public Scoreboard scoreboard;
    public Location spectatorSpawn;
    public World world;

    public WoolWarsTeam red;
    public WoolWarsTeam blue;

    private boolean inGame;

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new WoolWarsEvents(), this);

        saveDefaultConfig();
        readConfig();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.isOp()) {
            Bukkit.broadcastMessage(ChatColor.RED + "Only operators can perform this command");
            return true;
        }

        if (label.equalsIgnoreCase("wreload") || label.equalsIgnoreCase("wr")) {
            readConfig();
        } else if (label.equalsIgnoreCase("wstart") || label.equalsIgnoreCase("ws")) {
            startGame();
        }

        return true;
    }

    public void readConfig() {
        reloadConfig();

        FileConfiguration config = getConfig();
        scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();

        boolean friendlyFire = config.getBoolean("friendlyFire", false);

        Team redTeam = scoreboard.registerNewTeam("red");
        redTeam.setAllowFriendlyFire(friendlyFire);
        redTeam.setPrefix("§c");

        Team blueTeam = scoreboard.registerNewTeam("blue");
        blueTeam.setAllowFriendlyFire(friendlyFire);
        blueTeam.setPrefix("§9");

        String worldName = config.getString("world", "world");
        if (Bukkit.getWorld(worldName) == null) {
            world = Bukkit.getWorlds().get(0);
        } else {
            world = Bukkit.getWorld(worldName);
        }

        double spectatorSpawnX = config.getDouble("spectatorSpawn.x", 0.0D);
        double spectatorSpawnY = config.getDouble("spectatorSpawn.y", 0.0D);
        double spectatorSpawnZ = config.getDouble("spectatorSpawn.z", 0.0D);
        spectatorSpawn = new Location(world, spectatorSpawnX, spectatorSpawnY, spectatorSpawnZ);

        double redSpawnX = config.getDouble("red.spawn.x", 0.0D);
        double redSpawnY = config.getDouble("red.spawn.y", 0.0D);
        double redSpawnZ = config.getDouble("red.spawn.z", 0.0D);
        double redSpawnYaw = config.getDouble("red.spawn.yaw", 0.0D);
        double redSpawnPitch = config.getDouble("red.spawn.pitch", 0.0D);
        Location redSpawn = new Location(world, redSpawnX, redSpawnY, redSpawnZ, (float) redSpawnYaw, (float) redSpawnPitch);

        double redWoolX = config.getDouble("red.wool.x", 0.0D);
        double redWoolY = config.getDouble("red.wool.y", 0.0D);
        double redWoolZ = config.getDouble("red.wool.z", 0.0D);
        Location redWool = new Location(world, redWoolX, redWoolY, redWoolZ);

        double blueSpawnX = config.getDouble("blue.spawn.x", 0.0D);
        double blueSpawnY = config.getDouble("blue.spawn.y", 0.0D);
        double blueSpawnZ = config.getDouble("blue.spawn.z", 0.0D);
        double blueSpawnYaw = config.getDouble("blue.spawn.yaw", 0.0D);
        double blueSpawnPitch = config.getDouble("blue.spawn.pitch", 0.0D);
        Location blueSpawn = new Location(world, blueSpawnX, blueSpawnY, blueSpawnZ, (float) blueSpawnYaw, (float) blueSpawnPitch);

        double blueWoolX = config.getDouble("blue.wool.x", 0.0D);
        double blueWoolY = config.getDouble("blue.wool.y", 0.0D);
        double blueWoolZ = config.getDouble("blue.wool.z", 0.0D);
        Location blueWool = new Location(world, blueWoolX, blueWoolY, blueWoolZ);

        red = new WoolWarsTeam("red", ChatColor.RED, redSpawn, redWool);
        blue = new WoolWarsTeam("blue", ChatColor.BLUE, blueSpawn, blueWool);

        List<String> configRed = config.getStringList("red.members");
        List<String> configBlue = config.getStringList("blue.members");

        if (configRed != null) {
            for (String playerName : configRed) {
                Player player = Bukkit.getPlayer(playerName);

                if (player != null) {
                    redTeam.addEntry(playerName);
                    red.members.add(player.getUniqueId());
                }
            }
        }

        if (configBlue != null) {
            for (String playerName : configBlue) {
                Player player = Bukkit.getPlayer(playerName);

                if (player != null) {
                    blueTeam.addEntry(playerName);
                    blue.members.add(player.getUniqueId());
                }
            }
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.setScoreboard(scoreboard);
        }

        Bukkit.broadcastMessage(ChatColor.GREEN + "WoolWars config was reloaded successfully");
    }

    private void startGame() {
        inGame = true;

        readConfig();

        for (Player player : Bukkit.getOnlinePlayers()) {
            setupPlayer(player);
        }

        world.strikeLightningEffect(red.spawn);
        world.strikeLightningEffect(blue.spawn);

        Bukkit.broadcastMessage(ChatColor.GREEN + "Let the battles begin!");
    }

    public void setupPlayer(Player player) {
        WoolWarsTeam team = getPlayerTeam(player);

        if (inGame && team != null && team.hasWool()) { // If in game, not a spectator and team has wool
            player.teleport(team.spawn);

            player.setGameMode(GameMode.SURVIVAL);

            player.setHealth(20.0D);
            player.setFoodLevel(20);
        } else {
            player.setGameMode(GameMode.SPECTATOR);
            player.teleport(spectatorSpawn);
        }
    }

    public WoolWarsTeam getPlayerTeam(Player player) {
        UUID uuid = player.getUniqueId();

        if (red.members.contains(uuid)) {
            return red;
        }

        if (blue.members.contains(uuid)) {
            return blue;
        }

        return null;
    }

    public WoolWarsTeam getEnemyTeam(WoolWarsTeam team) {
        if (team == red) {
            return blue;
        } else {
            return red;
        }
    }

}
