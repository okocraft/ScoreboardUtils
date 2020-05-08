package net.okocraft.scoreboardutils.config;

public final class Config extends BaseConfig {

    private static Config instance = new Config();

    public Config() {
        super("config.yml");
    }

    public static Config getInstance() {
        return instance;
    }

    public boolean isScoreMigrationEnabled() {
        return get().getBoolean("score-migration.enable", true);
    }

    public boolean isScoreMigrationResetOldScore() {
        return get().getBoolean("score-migration.reset-oldname-score", true);
    }

    public void reload() {
        super.reload();
    }
}