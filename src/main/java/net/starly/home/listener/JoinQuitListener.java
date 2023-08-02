package net.starly.home.listener;

import net.starly.home.HomeMain;
import net.starly.home.manager.HomeManager;
import net.starly.home.util.HomeUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class JoinQuitListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        HomeManager homeManager = HomeManager.getInstance();
        Plugin plugin = HomeMain.getInstance();

        File directory = new File(plugin.getDataFolder(),"data");
        File dataFile = new File(directory, player.getUniqueId() + ".json");

        if (!dataFile.exists()) {
            homeManager.settingPlayerMap(player.getUniqueId());
            return;
        }

        homeManager.load(player.getUniqueId());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        HomeManager homeManager = HomeManager.getInstance();

        homeManager.save(player.getUniqueId());
    }
}
