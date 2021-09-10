package archiebaldry.WoolBattle;

import archiebaldry.WoolBattle.command.CommandJoin;
import archiebaldry.WoolBattle.command.CommandLeave;
import archiebaldry.WoolBattle.command.CommandStart;
import archiebaldry.WoolBattle.team.Team;
import archiebaldry.WoolBattle.team.Teams;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class WoolBattle extends JavaPlugin {

    private boolean gameStarted;
    public Teams teams;

    @Override
    public void onEnable() {
        gameStarted = false;

        // Events
        getServer().getPluginManager().registerEvents(new EventListener(), this);

        // Commands
        getCommand("join").setExecutor(new CommandJoin());
        getCommand("leave").setExecutor(new CommandLeave());
        getCommand("start").setExecutor(new CommandStart());

        teams = new Teams(getServer().getWorlds().get(0));
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
                player.teleport(team.getSpawn());
            }
        }
    }

    public boolean hasGameStarted() {
        return gameStarted;
    }

}
