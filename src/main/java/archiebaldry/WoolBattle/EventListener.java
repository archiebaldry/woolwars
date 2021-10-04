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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventListener implements Listener {

    private final WoolBattle plugin = WoolBattle.getPlugin(WoolBattle.class);

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        // Validate block type
        Block block = event.getBlock();
        if (block.getType() != Material.WOOL) {
            return;
        }

        // Get player and their team
        Player player = event.getPlayer();
        Team team = plugin.getTeams().getPlayerTeam(player.getName());

        // Validate player (not spectator)
        if (team == null) {
            player.sendMessage("Spectators are not allowed to break wool.");
            event.setCancelled(true);
            return;
        }

        // Validate location
        Location location = block.getLocation();
        if (location.equals(team.getWoolLocation())) {
            player.sendMessage("You cannot break your own wool.");
            event.setCancelled(true);
        } else {
            for (Team enemyTeam : plugin.getTeams().getEnemyTeams(team)) {
                if (enemyTeam.isActive() && location.equals(enemyTeam.getWoolLocation())) {
                    event.setCancelled(true);
                    block.setType(Material.AIR);
                    enemyTeam.setActive(false);
                    plugin.getWorld().strikeLightningEffect(location);
                    Bukkit.broadcastMessage(team.getPrefix() + player.getName() + "§r broke " + enemyTeam.getPrefix() + enemyTeam.getName() + "'s§r wool!");
                    break;
                }
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        World world = plugin.getServer().getWorlds().get(0);

        Location redSpawnFoot = plugin.toBlockLocation(plugin.getLocationFromConfig("red.spawn"));
        Location redSpawnHead = redSpawnFoot.clone();
        redSpawnHead.setY(redSpawnHead.getY() + 1.0D);

        Location blueSpawnFoot = plugin.toBlockLocation(plugin.getLocationFromConfig("blue.spawn"));
        Location blueSpawnHead = blueSpawnFoot.clone();
        blueSpawnHead.setY(blueSpawnHead.getY() + 1.0D);

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
        Team team = plugin.getTeams().getPlayerTeam(playerName);
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
        Team team = plugin.getTeams().getPlayerTeam(playerName);
        if (team == null) {
            event.getPlayer().setGameMode(GameMode.SPECTATOR);
        } else {
            event.setRespawnLocation(team.getSpawnLocation());
            if (!team.isActive()) {
                event.getPlayer().setGameMode(GameMode.SPECTATOR);
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        plugin.getTeams().updateScoreboard();
    }

}
