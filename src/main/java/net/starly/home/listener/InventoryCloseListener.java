package net.starly.home.listener;

import net.starly.home.util.HomeUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class InventoryCloseListener implements Listener {

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        HomeUtil homeUtil = HomeUtil.getInstance();
        if (homeUtil.getPlayerGuiTypeMap().containsKey(player.getUniqueId())) homeUtil.getPlayerGuiTypeMap().remove(player.getUniqueId());
    }
}
