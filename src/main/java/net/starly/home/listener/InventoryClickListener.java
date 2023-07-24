package net.starly.home.listener;

import net.starly.home.manager.PlayerHomeDataManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;

import static net.starly.home.data.PlayerGUIMap.playerGuiMap;

public class InventoryClickListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if (playerGuiMap.get(player.getUniqueId())) {
            if (event.isLeftClick() && event.isShiftClick()) {
                if (!(11 <= event.getSlot() && event.getSlot() <= 15)) {
                    return;
                }
                PlayerHomeDataManager.getInstance().updatePlayerLocation(player, 11-event.getSlot());
            }
            if (event.isRightClick() && event.isShiftClick()) {
                if (!(11 <= event.getSlot() && event.getSlot() <= 15)) {
                    return;
                }
            }
        }
    }
}
