package net.starly.home.listener;

import net.starly.core.data.Time;
import net.starly.home.HomeMain;
import net.starly.home.context.MessageContent;
import net.starly.home.context.MessageType;
import net.starly.home.data.Home;
import net.starly.home.enums.ChatType;
import net.starly.home.enums.GuiType;
import net.starly.home.manager.HomeManager;
import net.starly.home.util.HomeUtil;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class InventoryClickListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        Player player = (Player) event.getWhoClicked();
        MessageContent content = MessageContent.getInstance();
        HomeManager homeManager = HomeManager.getInstance();
        HomeUtil homeUtil = HomeUtil.getInstance();
        HashMap<UUID, GuiType> playerGuiTypeMap = homeUtil.getPlayerGuiTypeMap();

        event.setCancelled(true);

        if (playerGuiTypeMap.get(player.getUniqueId()) == GuiType.OPEN) {

            int slot = event.getSlot() - 11;

            if (event.isLeftClick() && event.isShiftClick()) {
                event.setCancelled(true);
                if (!(11 <= event.getSlot() && event.getSlot() <= 15)) {
                    return;
                }
                if (homeManager.getHome(player.getUniqueId(), slot) != null) {
                    content.getMessageAfterPrefix(MessageType.ERROR, "alreadyHomeInSlot").ifPresent(player::sendMessage);
                    return;
                }

                Home home = new Home(player.getUniqueId(), player.getLocation());
                homeManager.addHome(player.getUniqueId(), home);

                player.closeInventory();
                content.getMessageAfterPrefix(MessageType.NORMAL, "setHome").ifPresent(message -> {
                    String replacedMessage = message.replace("{slot}", String.valueOf(homeManager.getHomes(player.getUniqueId()).size()));
                    player.sendMessage(replacedMessage);
                });

            } else if (event.isRightClick() && event.isShiftClick()) {
                event.setCancelled(true);
                if (!(11 <= event.getSlot() && event.getSlot() <= 15)) {
                    return;
                }
                if (homeManager.getHome(player.getUniqueId(), slot) == null) {
                    content.getMessageAfterPrefix(MessageType.ERROR, "noHomeInSlot").ifPresent(player::sendMessage);
                    return;
                }

                List<Home> homes = homeManager.getHomes(player.getUniqueId());
                homes.remove(slot);

                player.closeInventory();
                content.getMessageAfterPrefix(MessageType.NORMAL, "removeHome").ifPresent(message -> {
                    String replacedMessage = message.replace("{slot}", String.valueOf(slot + 1));
                    player.sendMessage(replacedMessage);
                });
            } else if (event.isLeftClick()) {
                Plugin plugin = HomeMain.getInstance();
                FileConfiguration config = plugin.getConfig();
                event.setCancelled(true);
                if (!(11 <= event.getSlot() && event.getSlot() <= 15)) {
                    return;
                }
                if (homeManager.getHome(player.getUniqueId(), slot) == null) {
                    content.getMessageAfterPrefix(MessageType.ERROR, "noHomeInTelePortSlot").ifPresent(player::sendMessage);
                    return;
                }
                Location location = homeManager.getHome(player.getUniqueId(), slot).getLocation();
                player.closeInventory();

                int timeValue = config.getInt("data.time");
                Time time = new Time(timeValue);

                homeUtil.setPlayerTelePortMap(player, true);
                homeUtil.setIsPlayerMoveMap(player, false);

                new BukkitRunnable() {
                    @Override
                    public void run() {

                        if (homeUtil.getIsPlayerMoveMap(player)) {
                            content.getMessageAfterPrefix(MessageType.NORMAL, "onMoveInTelePort").ifPresent(player::sendMessage);
                            homeUtil.setPlayerTelePortMap(player, false);
                            homeUtil.setIsPlayerMoveMap(player, false);
                            this.cancel();
                        }
                        else if (time.getSeconds() <= 0) {
                            player.teleport(location);
                            content.getMessageAfterPrefix(MessageType.NORMAL, "teleportHome").ifPresent(message -> {
                                String replacedMessage = message.replace("{slot}", String.valueOf(slot + 1));
                                player.sendMessage(replacedMessage);
                            });
                            this.cancel();
                        } else {
                            content.getMessageAfterPrefix(MessageType.NORMAL, "sendLeftTime").ifPresent(message -> {
                                String replacedMessage = message.replace("{time}",  (int) time.getSeconds() + "");
                                player.sendMessage(replacedMessage);
                            });
                        }
                        time.subtract(1);
                    }
                }.runTaskTimer(plugin, 0, 20);
            }

        }

        if (playerGuiTypeMap.get(player.getUniqueId()) == GuiType.EDIT) {

            switch (event.getSlot()) {
                case 11:
                    homeUtil.getPlayerChatMap().put(player.getUniqueId(), ChatType.TIME);
                    player.sendMessage("채팅에 값을 입력해주세요 (정수형)");
                    player.closeInventory();
                    return;

                case 12:
                    homeUtil.getPlayerChatMap().put(player.getUniqueId(), ChatType.MOVE);
                    player.sendMessage("채팅에 값을 입력해주세요 (true/false)");
                    player.closeInventory();
                    return;
            }

        }
    }
}
