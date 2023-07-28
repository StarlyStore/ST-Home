package net.starly.home.command;

import net.starly.home.HomeMain;
import net.starly.home.context.MessageContent;
import net.starly.home.context.MessageType;
import net.starly.home.manager.HomeManager;
import net.starly.home.util.HomeUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class HomeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        MessageContent content = MessageContent.getInstance();
        Plugin plugin = HomeMain.getInstance();

        if (args.length > 0 && ("리로드".equalsIgnoreCase(args[0]) || "reload".equalsIgnoreCase(args[0]))) {
            if (!sender.isOp()) {
                content.getMessageAfterPrefix(MessageType.ERROR, "noPermission").ifPresent(sender::sendMessage);
                return false;
            }

            HomeManager homeManager = HomeManager.getInstance();
            homeManager.saveAll();

            plugin.reloadConfig();
            content.initialize(plugin.getConfig());
            content.getMessageAfterPrefix(MessageType.NORMAL, "reloadComplete").ifPresent(sender::sendMessage);
            return true;
        }

        if (!(sender instanceof Player)) {
            content.getMessageAfterPrefix(MessageType.ERROR, "noConsoleCommand").ifPresent(sender::sendMessage);
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            // TODO 홈 메뉴
            HomeUtil.getInstance().openHomeMenu(player);
            return true;
        }
        return false;
    }
}
