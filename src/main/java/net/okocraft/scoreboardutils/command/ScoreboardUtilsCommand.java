package net.okocraft.scoreboardutils.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import net.okocraft.scoreboardutils.ScoreboardUtils;
import net.okocraft.scoreboardutils.config.Messages;

public class ScoreboardUtilsCommand implements CommandExecutor, TabCompleter {

    protected static ScoreboardUtils plugin = ScoreboardUtils.getInstance();
    private static ScoreboardUtilsCommand instance;

    private enum SubCommand {
        RANKING(new Ranking());

        private final BaseSubCommand subCommand;

        private SubCommand(BaseSubCommand subCommand) {
            this.subCommand = subCommand;
        }

        public BaseSubCommand get() {
            return subCommand;
        }

        public static SubCommand getCommand(String name) {
            for (SubCommand command : values()) {
                if (command.toString().equalsIgnoreCase(name)) {
                    return command;
                }
            }

            throw new IllegalArgumentException("There is no command named " + name);
        }

        public static boolean exists(String name) {
            return getNames().contains(name.toLowerCase(Locale.ROOT));
        }

        public static List<String> getNames() {
            return Arrays.stream(values()).map(SubCommand::toString).collect(Collectors.toList());
        }

        @Override
        public String toString() {
            return name().replaceAll("_", "").toLowerCase(Locale.ROOT);
        }
    }

    public ScoreboardUtilsCommand() {
        if (instance != null) {
            throw new IllegalStateException("Command executor is already set.");
        }

        PluginCommand command = Objects.requireNonNull(plugin.getCommand("scoreboardutils"), "Command is not set in plugin.yml");
        if (command != null) {
            command.setExecutor(this);
            command.setTabCompleter(this);
        } else {
            plugin.getLogger().warning("Command scoreboardutils is not registered in plugin.yml!");
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            Messages.getInstance().notEnoughArguments(sender);
            return false;
        }
        
        if (!SubCommand.exists(args[0])) {
            Messages.getInstance().invalidArgument(sender, args[0]);
            return false;
        }
        BaseSubCommand subCommand = SubCommand.getCommand(args[0]).get();

        if (!sender.hasPermission(subCommand.getPermissionNode())) {
            Messages.getInstance().noPermission(sender);
            return false;
        }

        if (subCommand.getLeastArgsLength() < args.length) {
            Messages.getInstance().notEnoughArguments(sender);
            return false;
        }
        
        return subCommand.execute(sender, args);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return StringUtil.copyPartialMatches(args[0], SubCommand.getNames(), new ArrayList<>());
        }

        if (!SubCommand.exists(args[0])) {
            return List.of();
        }

        BaseSubCommand subCommand = SubCommand.getCommand(args[0]).get();
        if (!sender.hasPermission(subCommand.getPermissionNode())) {
            return List.of();
        }

        return subCommand.tabComplete(sender, args);
    }
} 