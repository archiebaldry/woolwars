package archiebaldry.WoolBattle.command;

import archiebaldry.WoolBattle.WoolBattle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandStart implements CommandExecutor {

    private final WoolBattle plugin = WoolBattle.getPlugin(WoolBattle.class);

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Check if game has started
        if (plugin.gameStarted) {
            sender.sendMessage("Game is in progress.");
            return false;
        }

        plugin.startGame();

        return true;
    }

}