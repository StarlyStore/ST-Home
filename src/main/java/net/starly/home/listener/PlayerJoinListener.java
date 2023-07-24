package net.starly.home.listener;

import net.starly.home.Home;
import net.starly.home.manager.PlayerHomeDataManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.File;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        File file = new File(Home.getInstance().getDataFolder(), "data/" + player.getUniqueId() + ".json");
        if (!file.exists()) PlayerHomeDataManager.getInstance().settingPlayerJson(player);
    }
}
