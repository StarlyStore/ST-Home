package net.starly.home;

import lombok.Getter;
import net.starly.core.bstats.Metrics;
import net.starly.home.command.HomeCommand;
import net.starly.home.context.MessageContent;
import net.starly.home.listener.PlayerJoinListener;
import net.starly.home.manager.PlayerHomeDataManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Home extends JavaPlugin {

    @Getter private static Home instance;
    private PlayerHomeDataManager manager;

    @Override
    public void onLoad() { instance = this; }

    @Override
    public void onEnable() {
        /* SETUP
         ──────────────────────────────────────────────────────────────────────────────────────────────────────────────── */
//        new Metrics(this, 12345); // TODO: 수정

        /* CONFIG
         ──────────────────────────────────────────────────────────────────────────────────────────────────────────────── */
        saveDefaultConfig();
        MessageContent.getInstance().initialize(getConfig());

        manager = PlayerHomeDataManager.getInstance();
        manager.load();

        /* COMMAND
         ──────────────────────────────────────────────────────────────────────────────────────────────────────────────── */
        getCommand("home").setExecutor(new HomeCommand());

        /* LISTENER
         ──────────────────────────────────────────────────────────────────────────────────────────────────────────────── */
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), this);
    }


    @Override
    public void onDisable() {
        if (manager != null) manager.save();
    }
}
