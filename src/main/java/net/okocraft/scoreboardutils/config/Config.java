package net.okocraft.scoreboardutils.config;

import net.okocraft.configurationapi.BaseConfig;
import net.okocraft.scoreboardutils.ScoreboardUtils;

public final class Config extends BaseConfig {

    private static ScoreboardUtils plugin = ScoreboardUtils.getInstance();
    private static Config instance = new Config();

    public Config() {
        super("config.yml", plugin.getDataFolder(), plugin.getResource("config.yml"));
    }

    public static Config getInstance() {
        return instance;
    }

    public boolean isScoreMigrationEnabled() {
        return getConfig().getBoolean("score-migration.enable", true);
    }

    public boolean isScoreMigrationResetOldScore() {
        return getConfig().getBoolean("score-migration.reset-oldname-score", true);
    }

    public void reload() {
        reloadConfig();
    }
}