package net.okocraft.scoreboardutils.command;

import java.util.List;

import org.bukkit.command.CommandSender;

import net.okocraft.scoreboardutils.config.Messages;

public abstract class BaseSubCommand {

    protected static Messages messages = Messages.getInstance();
    protected final String name;
    protected final int leastArgsLength;
    protected final String usage;

    BaseSubCommand(String name, int leastArgsLength, String usage) {
        this.name = name;
        this.leastArgsLength = leastArgsLength;
        this.usage = usage;
    }

    abstract boolean execute(CommandSender sender, String[] args);
    abstract List<String> tabComplete(CommandSender sender, String[] args);

    String getName() {
        return name;
    }

    String getDescription() {
        return Messages.getInstance().getMessage("command-description." + name);
    }

    int getLeastArgsLength() {
        return leastArgsLength;
    }
    
    String getPermissionNode() {
        return "scoreboardutils." + name;
    }
    String getUsage() {
        return usage; 
    }
}