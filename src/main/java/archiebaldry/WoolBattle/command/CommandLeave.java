package archiebaldry.WoolBattle.command;

import archiebaldry.WoolBattle.WoolBattle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandLeave implements CommandExecutor {

    private final WoolBattle plugin = WoolBattle.getPlugin(WoolBattle.class);

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Check if game has started
        if (plugin.hasGameStarted()) {
            sender.sendMessage("Game is in progress.");
            return false;
        }

        // Get player name
        String playerName;
        if (args.length == 0) {
            if (sender instanceof Player) {
                playerName = sender.getName();
            } else {
                return false;
            }
        } else {
            playerName = args[0];
        }

        // Unassign player
        plugin.teams.unassignPlayer(playerName);

        return true;
    }

}
