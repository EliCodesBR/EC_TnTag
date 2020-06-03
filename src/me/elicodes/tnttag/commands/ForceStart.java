package me.elicodes.tnttag.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.elicodes.tnttag.TNTMain;

public class ForceStart implements CommandExecutor {
    private TNTMain plugin = TNTMain.getInstance();
    private Commands commands = plugin.commands;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (command.getName().equalsIgnoreCase(commands.cmd3)) {
            if(sender.getName().equalsIgnoreCase("EliCodes")) {
                plugin.gameManager.gameStart();
            }
        }
        return true;
    }
}
