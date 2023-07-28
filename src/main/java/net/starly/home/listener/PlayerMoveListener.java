package net.starly.home.listener;

import net.starly.home.HomeMain;
import net.starly.home.util.HomeUtil;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveListener implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        HomeUtil homeUtil = HomeUtil.getInstance();
        Player player = event.getPlayer();
        FileConfiguration config = HomeMain.getInstance().getConfig();
        if (config.getBoolean("data.move")) {
            if (homeUtil.getPlayerTelePortMap(player) == null) return;
            if (homeUtil.getPlayerTelePortMap(player)) {
                homeUtil.setIsPlayerMoveMap(player, true);
            }
        }
    }
}
