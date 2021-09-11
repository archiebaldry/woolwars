package archiebaldry.WoolBattle.command;

import archiebaldry.WoolBattle.WoolBattle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandJoin implements CommandExecutor {

    private final WoolBattle plugin = WoolBattle.getPlugin(WoolBattle.class);

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Check if game has started
        if (plugin.hasGameStarted()) {
            sender.sendMessage("Game is in progress.");
            return false;
        }

        // Check if enough args
        if (args.length == 0) {
            return false;
        }

        // Get player name
        String playerName;
        if (args.length == 1) {
            if (sender instanceof Player) {
                playerName = sender.getName();
            } else {
                return false;
            }
        } else {
            playerName = args[1];
        }

        // Assign player
        if (!plugin.getTeams().assignPlayer(playerName, args[0])) {
            sender.sendMessage("Invalid team; try 'red' or 'blue'.");
        }

        return true;
    }

}
