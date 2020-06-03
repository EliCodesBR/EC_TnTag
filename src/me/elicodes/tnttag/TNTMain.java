package me.elicodes.tnttag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import me.elicodes.tnttag.commands.Commands;
import me.elicodes.tnttag.game.GameManager;
import me.elicodes.tnttag.game.GameMechanics;
import me.elicodes.tnttag.playerdata.PlayerManager;

public class TNTMain extends JavaPlugin {

    private static  TNTMain instance;
	public HashMap<UUID,PlayerManager> playermanager = new HashMap<UUID,PlayerManager>();
	public ArrayList<UUID> playersInGame = new ArrayList<>();

	public GameMechanics gameMechanics;
	public GameManager gameManager;
	public Commands commands;
	public PlayerScoreboard playerScoreboard;
	
	public void onEnable() {
	    setInstance(this);
		loadConfig();
		instanceClasses();
        gameManager.setupGame();
		commands.onEnable();
		saveDefaultConfig();

		getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "\n\n[EC_TntTAG] Plugin habilitado com sucesso.\n\n");
		getServer().getPluginManager().registerEvents(new GameMechanics(), this);
	}

    public void onDisable() {
        Bukkit.getOnlinePlayers().forEach(player -> gameManager.gameStop(player));
        getServer().getConsoleSender().sendMessage(ChatColor.RED + "\n\n[EC_TntTAG] Plugin desabilitado com sucesso.\n\n");
	}

    public static TNTMain getInstance() {
        return instance;
    }

    private static void setInstance(TNTMain instance) {
        TNTMain.instance = instance;
    }

	private void loadConfig() {
		getConfig().options().copyDefaults(true);
		saveConfig();
	}
	
	private void instanceClasses(){
		gameMechanics = new GameMechanics();
		gameManager = new GameManager();
		playerScoreboard = new PlayerScoreboard();
		commands = new Commands();
	}
}
