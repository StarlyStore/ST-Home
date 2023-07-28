package net.starly.home.listener;

import net.starly.home.HomeMain;
import net.starly.home.context.MessageContent;
import net.starly.home.context.MessageType;
import net.starly.home.enums.ChatType;
import net.starly.home.enums.GuiType;
import net.starly.home.util.HomeUtil;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.UUID;

public class PlayerChatListener implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        HomeUtil homeUtil = HomeUtil.getInstance();
        HashMap<UUID, GuiType> playerGuiTypeMap = homeUtil.getPlayerGuiTypeMap();
        HashMap<UUID, ChatType> playerChatTypeMap = homeUtil.getPlayerChatMap();
        GuiType playerGuiType = playerGuiTypeMap.get(player.getUniqueId());
        ChatType playerChatType = playerChatTypeMap.get(player.getUniqueId());
        String message;
        MessageContent content = MessageContent.getInstance();
        Plugin plugin = HomeMain.getInstance();

        if (playerGuiType == null || playerGuiType == GuiType.OPEN) return;

        event.setCancelled(true);
        message = event.getMessage();

        switch (playerChatType) {

            case TIME:

                if (!message.matches("[0-9]+")) {
                    content.getMessageAfterPrefix(MessageType.ERROR, "inputInvalidValue").ifPresent(player::sendMessage);
                    return;
                }
                plugin.getConfig().set("data.time", message);
                plugin.reloadConfig();

                playerChatTypeMap.remove(player.getUniqueId());
                playerGuiTypeMap.remove(player.getUniqueId());
                content.getMessageAfterPrefix(MessageType.NORMAL, "setValueSuccessfully").ifPresent(player::sendMessage);

                return;

            case MOVE:

                if (!(message.equals("true") || message.equals("false"))) {
                    content.getMessageAfterPrefix(MessageType.ERROR, "inputInvalidValue").ifPresent(player::sendMessage);
                    return;
                }
                plugin.getConfig().set("data.move", message);
                plugin.saveConfig();

                playerChatTypeMap.remove(player.getUniqueId());
                playerGuiTypeMap.remove(player.getUniqueId());
                content.getMessageAfterPrefix(MessageType.NORMAL, "setValueSuccessfully").ifPresent(player::sendMessage);
                return;
        }


    }
}
