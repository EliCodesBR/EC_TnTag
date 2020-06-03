package me.elicodes.tnttag;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public class PlayerScoreboard {

    private static ScoreboardManager m;
    private static Scoreboard b;
    private static Objective o;
    private static Score gameMode;
    private static Score time;
    private static Score coins;
    private static Score alive;
    private static Score dead;


    private static TNTMain plugin = TNTMain.getPlugin(TNTMain.class);

    public void scoreGame(Player player, int timeLeft) {
        m = Bukkit.getScoreboardManager();
        b = m.getNewScoreboard();
        o = b.registerNewObjective("tnttag", "");
        o.setDisplaySlot(DisplaySlot.SIDEBAR);
        o.setDisplayName("�e�lTNT TAG");

        if (plugin.playersInGame.size() == 1) {
            time = o.getScore(ChatColor.WHITE + "Tempo: " + ChatColor.GREEN + "Completo");
            time.setScore(4);
            return;
        } else {
            time = o.getScore(ChatColor.WHITE + "Tempo: " + ChatColor.GREEN + timeLeft);
            time.setScore(4);
        }
        coins = o.getScore(ChatColor.WHITE + "Coins: " + ChatColor.GREEN + "100");
        coins.setScore(3);

        gameMode = o.getScore(ChatColor.WHITE + "Jogo: " + ChatColor.GREEN + "TNT TAG");
        gameMode.setScore(2);

        alive = o.getScore(ChatColor.WHITE + "Jogadores vivos: " + ChatColor.GREEN + plugin.playersInGame.size());
        alive.setScore(1);

        dead = o.getScore(ChatColor.WHITE + "Jogadores vivos: " + ChatColor.GREEN + (Bukkit.getOnlinePlayers().size() - plugin.playersInGame.size()));
        dead.setScore(0);

        player.setScoreboard(b);
    }

    public void scoreLobby(Player player) {
        m = Bukkit.getScoreboardManager();
        b = m.getNewScoreboard();
        o = b.registerNewObjective("tnttag", "");
        o.setDisplaySlot(DisplaySlot.SIDEBAR);
        o.setDisplayName("�e�lTNT TAG");

        time = o.getScore(ChatColor.WHITE + "Tempo: " + ChatColor.GRAY + "Jogo ainda nao iniciado.");
        time.setScore(4);


        gameMode = o.getScore(ChatColor.WHITE + "Jogo: " + ChatColor.GREEN + "TNT TAG");
        gameMode.setScore(3);

        coins = o.getScore(ChatColor.WHITE + "Coins: " + ChatColor.GREEN + "100");
        coins.setScore(2);

        alive = o.getScore(ChatColor.WHITE + "Jogadores: " + ChatColor.GREEN + plugin.playersInGame.size() + "" + ChatColor.WHITE + "/" + ChatColor.GREEN + plugin.gameManager.playersNeeded);
        alive.setScore(1);

        player.setScoreboard(b);
    }
}
