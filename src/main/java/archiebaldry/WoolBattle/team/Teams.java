package archiebaldry.WoolBattle.team;

import archiebaldry.WoolBattle.WoolBattle;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Teams {

    private final WoolBattle plugin = WoolBattle.getPlugin(WoolBattle.class);

    private Scoreboard scoreboard;
    private final Map<String, Team> teams = new HashMap<>();

    public Teams() {
        scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();

        org.bukkit.scoreboard.Team red = scoreboard.registerNewTeam("red");
        org.bukkit.scoreboard.Team blue = scoreboard.registerNewTeam("blue");

        red.setAllowFriendlyFire(false);
        blue.setAllowFriendlyFire(false);

        red.setCanSeeFriendlyInvisibles(true);
        blue.setCanSeeFriendlyInvisibles(true);

        red.setPrefix("§c");
        blue.setPrefix("§9");

        World world = plugin.getWorld();
        teams.put("red", new Team("red", "§c", new Location(world, -2.5D, 64.0D, -8.5D, 0.0F, 0.0F), new Location(world, 0.0D, 65.0D, -9.0D)));
        teams.put("blue", new Team("blue", "§9", new Location(world, 2.5D, 64.0D, 8.5D, 180.0F, 0.0F), new Location(world, 0.0D, 65.0D, 9.0D)));
    }

    public void updateScoreboard() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.setScoreboard(scoreboard);
        }
    }

    public boolean assignPlayer(String playerName, String teamName) {
        // Validate team name
        Team team = getTeam(teamName.toLowerCase());
        if (team == null) {
            return false;
        }

        // Leave current team if applicable
        unassignPlayer(playerName);

        // Join new team
        scoreboard.getTeam(teamName).addEntry(playerName);
        team.addMember(playerName);
        Bukkit.broadcastMessage(playerName + " joined the " + team.getName() + " team.");

        return true;
    }

    public void unassignPlayer(String playerName) {
        // Get current team
        Team currentTeam = getPlayerTeam(playerName);

        // Leave current team if applicable
        if (currentTeam != null) {
            scoreboard.getTeam(currentTeam.getName()).removeEntry(playerName);
            currentTeam.removeMember(playerName);
            Bukkit.broadcastMessage(playerName + " left the " + currentTeam.getName() + " team.");
        }
    }

    public Team getTeam(String teamName) {
        return teams.get(teamName);
    }

    public Team getPlayerTeam(String playerName) {
        for (Team team : teams.values()) {
            if (team.hasMember(playerName)) {
                return team;
            }
        }
        return null;
    }

    public List<Team> getEnemyTeams(Team team) {
        List<Team> enemyTeams = new ArrayList<>();
        for (Team enemyTeam : teams.values()) {
            if (!enemyTeam.equals(team)) {
                enemyTeams.add(enemyTeam);
            }
        }
        return enemyTeams;
    }

}
