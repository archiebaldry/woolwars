package archiebaldry.WoolBattle;

import archiebaldry.WoolBattle.team.Team;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class EventListener implements Listener {

    private final WoolBattle plugin = WoolBattle.getPlugin(WoolBattle.class);

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        World world = plugin.getServer().getWorlds().get(0);
        Location redWool = new Location(world, 0.0D, 65.0D, -9.0D);
        Location blueWool = new Location(world, 0.0D, 65.0D, 9.0D);

        Block block = event.getBlock();
        if (block.getType() != Material.WOOL) {
            return;
        }

        Location location = block.getLocation();

        Player player = event.getPlayer();
        Team team = plugin.teams.getPlayerTeam(player.getName());

        if (location.equals(redWool)) {
            if (team == null) {
                event.getPlayer().sendMessage("You can't break that wool!");
                event.setCancelled(true);
            } else if (team.getName().equals("red")) {
                event.getPlayer().sendMessage("You can't break your own wool!");
                event.setCancelled(true);
            } else if (team.getName().equals("blue")) {
                world.strikeLightningEffect(location);
                Bukkit.broadcastMessage(player.getName() + " broke red's wool!");
                plugin.teams.getTeam("red").setActive(false);
            }
        } else if (location.equals(blueWool)) {
            if (team == null) {
                event.getPlayer().sendMessage("You can't break that wool!");
                event.setCancelled(true);
            } else if (team.getName().equals("blue")) {
                event.getPlayer().sendMessage("You can't break your own wool!");
                event.setCancelled(true);
            } else if (team.getName().equals("red")) {
                world.strikeLightningEffect(location);
                Bukkit.broadcastMessage(player.getName() + " broke blue's wool!");
                plugin.teams.getTeam("blue").setActive(false);
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        World world = plugin.getServer().getWorlds().get(0);
        Location redSpawnFoot = new Location(world, -3.0D, 64.0D, -9.0D);
        Location redSpawnHead = new Location(world, -3.0D, 65.0D, -9.0D);
        Location blueSpawnFoot = new Location(world, 3.0D, 64.0D, 9.0D);
        Location blueSpawnHead = new Location(world, 3.0D, 65.0D, 9.0D);

        Block block = event.getBlock();
        Location location = block.getLocation();

        if (location.equals(redSpawnFoot) || location.equals(redSpawnHead) || location.equals(blueSpawnFoot) || location.equals(blueSpawnHead)) {
            event.getPlayer().sendMessage("You can't place a block there!");
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        String playerName = event.getPlayer().getName();
        Team team = plugin.teams.getPlayerTeam(playerName);
        if (team == null) {
            event.setFormat("§7" + playerName + ": " + event.getMessage());
        } else if (team.getName().equals("red")) {
            event.setFormat("§c" + playerName + "§r: " + event.getMessage());
        } else if (team.getName().equals("blue")) {
            event.setFormat("§9" + playerName + "§r: " + event.getMessage());
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        String playerName = event.getPlayer().getName();
        Team team = plugin.teams.getPlayerTeam(playerName);
        if (team == null) {
            event.getPlayer().setGameMode(GameMode.SPECTATOR);
        } else {
            event.setRespawnLocation(team.getSpawn());
            if (!team.isActive()) {
                event.getPlayer().setGameMode(GameMode.SPECTATOR);
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        plugin.teams.updateScoreboard();
    }

}
