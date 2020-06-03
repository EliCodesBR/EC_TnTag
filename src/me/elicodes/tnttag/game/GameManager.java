package me.elicodes.tnttag.game;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;

import me.elicodes.tnttag.TNTMain;
import me.elicodes.tnttag.playerdata.PlayerManager;

public class GameManager implements Listener {

    private TNTMain plugin = TNTMain.getInstance();

    private int lobbyCountdown = TNTMain.getInstance().getConfig().getInt("GameManager.LobbyCountdown");
    public int explosionCountdown = TNTMain.getInstance().getConfig().getInt("GameManager.ExplosionCountdown");
    public int playersNeeded = TNTMain.getInstance().getConfig().getInt("GameManager.PlayersNeeded");
    public boolean isStarted = false;

    public Location lobbySpawn;
    public Location gameSpawn;

    public void setupGame() {
        if (plugin.getConfig().contains("gameSpawn")) {
            this.gameSpawn = (Location) plugin.getConfig().get("gameSpawn");
            plugin.getServer().getConsoleSender().sendMessage("§2§lSUCESSO §aSpawn do jogo localizado.");
        }

        if (plugin.getConfig().contains("lobbySpawn")) {
            this.lobbySpawn = (Location) plugin.getConfig().get("lobbySpawn");
            plugin.getServer().getConsoleSender().sendMessage("§2§lSUCESSO §aLobby/Spawn localizado.");
        }

        playerCheck(Bukkit.getOnlinePlayers().size());
        for (Player online : Bukkit.getOnlinePlayers()) {
            plugin.playersInGame.add(online.getUniqueId());
            plugin.playermanager.put(online.getUniqueId(),
                    new PlayerManager(online.getUniqueId(), false, 0, false, false));
            lobbyWait(online);
            online.setFoodLevel(20);
            online.setHealth(20);
            plugin.playerScoreboard.scoreLobby(online);
            online.teleport(lobbySpawn);
        }
    }

    public void lobbyWait(Player player) {
        int online = Bukkit.getOnlinePlayers().size();
        player.sendMessage("§7Jogadores: §a" + online + "/" + playersNeeded);
        playerCheck(online);
    }

    public void gameStart() {
        isStarted = true;
        explosionCountdown();
        plugin.gameMechanics.tntPlacer();

        Bukkit.getOnlinePlayers().forEach(player -> {
            player.setWalkSpeed(.5f);
            player.setInvulnerable(false);
            player.getInventory().clear();
            player.setPlayerListName(ChatColor.GREEN + player.getName());
            player.teleport(gameSpawn);
        });

    }

    public void gameStop(Player player) {
        player.setWalkSpeed(.2f);
        player.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
        player.getInventory().setHelmet(null);
        player.getInventory().clear();

        player.setGameMode(GameMode.ADVENTURE);
        isStarted = false;
        plugin.playersInGame.clear();
        plugin.playermanager.clear();

        player.setPlayerListName(ChatColor.GREEN + player.getName());

        if (lobbySpawn != null) {
            player.teleport(lobbySpawn);
        }
    }

    public boolean playerCheck(int online) {
        if (online >= playersNeeded) {
            if (!isStarted) {
                lobbyCountdown();
                setStarted(true);
            }
            return true;
        } else {
            return false;
        }
    }

    public void explosionCountdown() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (explosionCountdown > 0) {
                    explosionCountdown--;

                    Bukkit.getOnlinePlayers().forEach(player -> plugin.playerScoreboard.scoreGame(player, explosionCountdown));

                } else {
                    plugin.gameMechanics.tntCheck(this);
                }
            }

        }.runTaskTimer(plugin, 0, 20);

    }

    public void lobbyCountdown() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (lobbyCountdown > 0) {
                    if (playerCheck(plugin.playersInGame.size())) {
                        lobbyCountdown--;
                        Bukkit.getServer().broadcastMessage("§7O jogo esta iniciando em §e" + lobbyCountdown + " segundo(s)..");
                        for (Player online : Bukkit.getOnlinePlayers()) {
                        	GameMechanics.mandarTitulo(online, "", "§7Jogo iniciando em: §e" + lobbyCountdown + " §7segundo(s)..", 2);
                            online.playSound(online.getLocation(), Sound.BLOCK_NOTE_PLING, 2, 2);
                        }
                    } else {
                        Bukkit.getServer().broadcastMessage("§7Um jogador(s) saiu. Jogadores utils: §a" + playersNeeded + " §7para o jogo iniciar..");
                        this.cancel();
                        lobbyCountdown = 10;
                    }
                } else {
                    this.cancel();
                    gameStart();
                    for (Player online : Bukkit.getOnlinePlayers()) {
                        Bukkit.getServer().broadcastMessage("§6§lAVISO §eBoa sorte para todos!");
                        GameMechanics.mandarTitulo(online, "§6§lAVISO", "§eBoa sorte para todos em seu jogo!", 2);
                    }
                }
            }
        }.runTaskTimer(plugin, 0, 20);
    }

    public void setStarted(boolean isStarted) {
        this.isStarted = isStarted;
    }
}
