package archiebaldry.woolwars;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

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
    public void onPlayerJoin(PlayerJoinEvent event) {
        plugin.readConfig();

        plugin.setupPlayer(event.getPlayer());
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        plugin.setupPlayer(event.getPlayer());
    }

}
