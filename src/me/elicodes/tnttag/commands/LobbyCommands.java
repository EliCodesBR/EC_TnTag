package me.elicodes.tnttag.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.elicodes.tnttag.TNTMain;
import me.elicodes.tnttag.game.GameMechanics;

public class LobbyCommands implements CommandExecutor {

    private TNTMain plugin = TNTMain.getInstance();
    private Commands commands = plugin.commands;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (command.getName().equalsIgnoreCase(commands.cmd1)) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                Location pLoc = player.getLocation();
                if (!player.isOp()) return true;
                if (args.length == 1) {
                    if(args[0].equalsIgnoreCase("spawn")){
                        plugin.getConfig().set("lobbySpawn", pLoc);
                        plugin.saveConfig();
                        player.sendMessage("§2§lSUCESSO §aLobby definido com sucesso.");
                        GameMechanics.mandarTitulo(player, "§2§lSUCESSO", "§aLobby definido com sucesso.", 2);
                    }
                } else {
                    player.sendMessage("§4§lERRO §cArgumentos invalidos.");
                }
            } else {
                sender.sendMessage("Apenas jogadores podem executar este comando.");
            }
        }
        return true;
    }
}
