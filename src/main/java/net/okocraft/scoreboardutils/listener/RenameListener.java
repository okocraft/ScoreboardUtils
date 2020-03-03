package net.okocraft.scoreboardutils.listener;

import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import net.okocraft.altmanager.event.PlayerRenameLoginEvent;
import net.okocraft.scoreboardutils.ScoreboardUtils;
import net.okocraft.scoreboardutils.config.Config;

public class RenameListener implements Listener {

    public static RenameListener INSTANCE = new RenameListener();
    private RenameListener() {}

    public static void start() {
        stop();
        Bukkit.getPluginManager().registerEvents(INSTANCE, ScoreboardUtils.getInstance());
    }

    public static void stop() {
        HandlerList.unregisterAll(INSTANCE);
    }

    @EventHandler
    public void onRenameLogin(PlayerRenameLoginEvent event) {
        if (!Config.getInstance().isScoreMigrationEnabled()) {
            return;
        }

        String oldName = event.getOldName();
        String newName = event.getPlayer().getName();

        Scoreboard mainScoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        Set<Objective> Objectives = mainScoreboard.getObjectives();

        Objectives.forEach(objective -> {
            Score score = objective.getScore(oldName);
            if (score.isScoreSet()) {
                objective.getScore(newName).setScore(score.getScore());
            }
        });

        if (Config.getInstance().isScoreMigrationResetOldScore()) {
            mainScoreboard.resetScores(oldName);
        }
    }
}