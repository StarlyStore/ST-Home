package net.starly.home;

import lombok.Getter;
import net.starly.home.command.HomeCommand;
import net.starly.home.command.HomeSettingCommand;
import net.starly.home.message.MessageContent;
import net.starly.home.listener.*;
import net.starly.home.manager.HomeManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Arrays;

public class HomeMain extends JavaPlugin {


    @Getter
    private static HomeMain instance;

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        /* SETUP
         ──────────────────────────────────────────────────────────────────────────────────────────────────────────────── */
//        new Metrics(this, 12345); // TODO: 수정

        /* CONFIG
         ──────────────────────────────────────────────────────────────────────────────────────────────────────────────── */
        saveDefaultConfig();
        MessageContent.getInstance().initialize(getConfig());

        HomeManager homeManager = HomeManager.getInstance();
        homeManager.loadAll();

        try {
            File configFile = new File(getDataFolder(), "config.yml");
            if (!configFile.exists()) {
                saveDefaultConfig();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        File dataFolder = new File(getDataFolder(), "data/");
        if (!dataFolder.exists()) dataFolder.mkdirs();

        /* COMMAND
         ──────────────────────────────────────────────────────────────────────────────────────────────────────────────── */
        getCommand("home").setExecutor(new HomeCommand());
        getCommand("homeSetting").setExecutor(new HomeSettingCommand());

        /* LISTENER
         ──────────────────────────────────────────────────────────────────────────────────────────────────────────────── */
        registerListeners(
                new JoinQuitListener(),
                new InventoryClickListener(),
                new PlayerMoveListener(),
                new PlayerChatListener(),
                new InventoryCloseListener()
        );
    }

    @Override
    public void onDisable() {
        HomeManager homeManager = HomeManager.getInstance();
        homeManager.saveAll();

        Bukkit.getOnlinePlayers().forEach(HumanEntity::closeInventory);
    }

    private void registerListeners(Listener... listeners) {
        Arrays.stream(listeners).forEach(listener -> {
            Bukkit.getPluginManager().registerEvents(listener, this);
        });
    }
}
