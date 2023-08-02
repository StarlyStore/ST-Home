package net.starly.home.command;

import net.starly.home.message.MessageContent;
import net.starly.home.message.MessageType;
import net.starly.home.util.HomeUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HomeSettingCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        MessageContent content = MessageContent.getInstance();
        if (!(sender instanceof Player)) {
            content.getMessageAfterPrefix(MessageType.ERROR, "noConsoleCommand").ifPresent(sender::sendMessage);
            return true;
        }

        Player player = (Player) sender;

        if (!player.isOp()) {
            content.getMessageAfterPrefix(MessageType.ERROR, "noPermission").ifPresent(sender::sendMessage);
            return false;
        }

        HomeUtil.getInstance().openHomeSettingMenu(player);

        return false;
    }
}
