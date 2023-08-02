package net.starly.home.listener;

import net.starly.home.HomeMain;
import net.starly.home.message.MessageContent;
import net.starly.home.message.MessageType;
import net.starly.home.enums.ChatType;
import net.starly.home.util.HomeUtil;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class PlayerChatListener implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        HomeUtil homeUtil = HomeUtil.getInstance();
        HashMap<UUID, ChatType> playerChatTypeMap = homeUtil.getPlayerChatMap();
        ChatType playerChatType = playerChatTypeMap.get(player.getUniqueId());
        String message;
        MessageContent content = MessageContent.getInstance();
        File file = new File(HomeMain.getInstance().getDataFolder(), "config.yml");
        FileConfiguration configuration = YamlConfiguration.loadConfiguration(file);

        if (!playerChatTypeMap.containsKey(player.getUniqueId())) return;

        event.setCancelled(true);
        message = event.getMessage();

        switch (playerChatType) {

            case TIME:

                if (!message.matches("[0-9]+")) {
                    content.getMessageAfterPrefix(MessageType.ERROR, "inputInvalidValue").ifPresent(player::sendMessage);
                    return;
                }
                configuration.set("data.time", Integer.parseInt(message));
                try {
                    configuration.save(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                playerChatTypeMap.remove(player.getUniqueId());
                content.getMessageAfterPrefix(MessageType.NORMAL, "setValueSuccessfully").ifPresent(player::sendMessage);
                return;

            case MOVE:

                if (!(message.equals("true") || message.equals("false"))) {
                    content.getMessageAfterPrefix(MessageType.ERROR, "inputInvalidValue").ifPresent(player::sendMessage);
                    return;
                }
                configuration.set("data.move", Boolean.parseBoolean(message));
                try {
                    configuration.save(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                playerChatTypeMap.remove(player.getUniqueId());
                content.getMessageAfterPrefix(MessageType.NORMAL, "setValueSuccessfully").ifPresent(player::sendMessage);
                return;
        }


    }
}
