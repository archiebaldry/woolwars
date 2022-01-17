package archiebaldry.woolwars;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.ArrayList;
import java.util.List;

public class WoolWarsEvents implements Listener {

    private final WoolWars plugin = WoolWars.getPlugin(WoolWars.class);

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();

        if (block.getType() != Material.WOOL) {
            return;
        }

        Location loc = block.getLocation();

        boolean isRed = loc.equals(plugin.red.wool);
        boolean isBlue = loc.equals(plugin.blue.wool);

        if (!(isRed || isBlue)) {
            return;
        }

        Player player = event.getPlayer();
        WoolWarsTeam team = plugin.getPlayerTeam(player);

        event.setCancelled(true);

        if (team == null) { // Spectator
            player.sendMessage(ChatColor.RED + "Spectators are not allowed to break wool; how did we get here?");
            return;
        }

        if (isRed && team.name.equals("red") || isBlue && team.name.equals("blue")) { // Breaking their own wool
            player.sendMessage(ChatColor.RED + "You're not allowed to break your own wool");
            return;
        }

        block.getWorld().strikeLightningEffect(loc);

        block.setType(Material.AIR);

        WoolWarsTeam enemy = plugin.getEnemyTeam(team);

        Bukkit.broadcastMessage(enemy.colour + enemy.name.toUpperCase() + ChatColor.RESET + " wool was broken by " + team.colour + player.getName());
    }

    @EventHandler
    public void onBlockBurn(BlockBurnEvent event) {
        Location loc = event.getBlock().getLocation();

        if (loc.equals(plugin.red.wool) || loc.equals(plugin.blue.wool)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Location loc = event.getBlock().getLocation();

        Location redFoot = plugin.red.spawn.getBlock().getLocation();
        Location redHead = redFoot.add(0.0D, 1.0D, 0.0D);

        Location blueFoot = plugin.blue.spawn.getBlock().getLocation();
        Location blueHead = blueFoot.add(0.0D, 1.0D, 0.0D);

        if (loc.equals(plugin.red.wool) || loc.equals(redFoot) || loc.equals(redHead) || loc.equals(plugin.blue.wool) || loc.equals(blueFoot) || loc.equals(blueHead)) {
            Bukkit.broadcastMessage(ChatColor.RED + "You can't place blocks here");

            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        List<Block> blocks = event.blockList();

        for (Block block : new ArrayList<>(blocks)) {
            Location loc = block.getLocation();

            if (loc.equals(plugin.red.wool) || loc.equals(plugin.blue.wool)) {
                blocks.remove(block);
            }
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        String name = event.getPlayer().getName();
        WoolWarsTeam team = plugin.getPlayerTeam(event.getPlayer());

        if (team == null) { // Spectator
            event.setFormat(ChatColor.GRAY + name + ": " + event.getMessage());
        } else  { // Player
            event.setFormat(team.colour + name + ChatColor.RESET + ": " + event.getMessage());
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        plugin.readConfig();

        plugin.setupPlayer(event.getPlayer());
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        WoolWarsTeam team = plugin.getPlayerTeam(player);

        if (plugin.inGame && team != null && team.hasWool()) { // If in game, not a spectator and team has wool
            event.setRespawnLocation(team.spawn);
            player.setGameMode(GameMode.SURVIVAL);
        } else {
            event.setRespawnLocation(plugin.spectatorSpawn);
            player.setGameMode(GameMode.SPECTATOR);
        }
    }

}
