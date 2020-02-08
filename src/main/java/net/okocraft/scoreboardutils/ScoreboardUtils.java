package net.okocraft.scoreboardutils;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import net.okocraft.scoreboardutils.command.ScoreboardUtilsCommand;
import net.okocraft.scoreboardutils.config.Messages;

public class ScoreboardUtils extends JavaPlugin {

	private static ScoreboardUtils instance;

	@Override
	public void onEnable() {
		Messages.getInstance().reload();
		new ScoreboardUtilsCommand();
	}

	@Override
	public void onDisable() {
	};

	public static ScoreboardUtils getInstance() {
		if (instance == null) {
			instance = (ScoreboardUtils) Bukkit.getPluginManager().getPlugin("ScoreboardUtils");
			if (instance == null) {

			}
		}

		return instance;
	}
}
