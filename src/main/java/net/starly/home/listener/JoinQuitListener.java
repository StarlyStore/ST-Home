package net.starly.home.listener;

import net.starly.home.manager.HomeManager;
import net.starly.home.util.HomeUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinQuitListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        HomeManager homeManager = HomeManager.getInstance();

        homeManager.load(player.getUniqueId());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        HomeManager homeManager = HomeManager.getInstance();

        homeManager.save(player.getUniqueId());
    }
}
