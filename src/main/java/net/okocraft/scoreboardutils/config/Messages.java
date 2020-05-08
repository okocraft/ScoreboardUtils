package net.okocraft.scoreboardutils.config;

import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public final class Messages extends BaseConfig {

    private static Messages instance = new Messages();

    public Messages() {
        super("messages.yml");
    }

    public static Messages getInstance() {
        return instance;
    }

    /**
     * Send message to player.
     * 
     * @param player
     * @param addPrefix
     * @param path
     * @param placeholders
     */
    private void sendMessage(final CommandSender sender, final boolean addPrefix, final String path,
            final Map<String, Object> placeholders) {
        final String prefix = addPrefix
                ? get().getString("plugin.prefix", "&8[&6ScoreboardUtils&8]&r") + " "
                : "";
        String message = ChatColor.translateAlternateColorCodes('&', prefix + getMessage(path));
        for (final Map.Entry<String, Object> placeholder : placeholders.entrySet()) {
            message = message.replace(placeholder.getKey(), placeholder.getValue().toString());
        }
        sender.sendMessage(message);
        return;
    }

    /**
     * Send message to player.
     * 
     * @param player
     * @param path
     * @param placeholders
     */
    private void sendMessage(final CommandSender sender, final String path, final Map<String, Object> placeholders) {
        sendMessage(sender, true, path, placeholders);
    }

    /**
     * Send message to player.
     * 
     * @param sender
     * @param path
     */
    private void sendMessage(final CommandSender sender, final String path) {
        sendMessage(sender, path, Map.of());
    }

    public void notEnoughArguments(CommandSender sender) {
        sendMessage(sender, "command.general.error.not-enough-arguments");
    }

    public void invalidArgument(CommandSender sender, String input) {
        sendMessage(sender, "command.general.error.invalid-argument", Map.of("%argument%", input));
    }

    public void noPermission(CommandSender sender) {
        sendMessage(sender, "command.general.no-permission");
    }

    public void objectiveDoesNotFound(CommandSender sender) {
        sendMessage(sender, "command.ranking.error.objective-does-not-exist");
    }

    public void invalidNumber(CommandSender sender) {
        sendMessage(sender, "command.general.error.invalid-number");
    }

    public void rankingHeader(CommandSender sender, String objective, int page, int maxPage) {
        sendMessage(sender, "command.ranking.info.header",
                Map.of("%scoreboard%", objective, "%page%", page, "%max-page%", maxPage));
    }

    public void rankingFormat(CommandSender sender, int rank, String entry, int score) {
        sendMessage(sender, false, "command.ranking.info.format",
                Map.of("%rank%", rank, "%entry%", String.format("%-40s", entry), "%score%", score));
    }

    public String getMessage(final String path) {
        return get().getString(path, path);
    }

    public void reload() {
        super.reload();
    }
}