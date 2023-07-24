package net.starly.home.manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.starly.home.Home;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;

public class PlayerHomeDataManager {

    private static PlayerHomeDataManager instance;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private File dataFile;
    private final Map<UUID, Integer> playerHomeAmountMap = new HashMap<>();
    private final Map<UUID, List<Object>> playerHomeLocationMap = new HashMap<>();
    private final JsonObject jsonObject = new JsonObject();
    private final JsonArray jsonArray = new JsonArray();


    private PlayerHomeDataManager() {}


    public static PlayerHomeDataManager getInstance() {
        if (instance == null) instance = new PlayerHomeDataManager();
        return instance;
    }


    public void save() {
        for (OfflinePlayer player : Bukkit.getServer().getOfflinePlayers()) {
            dataFile = new File(Home.getInstance().getDataFolder(), "data/" + player.getUniqueId() + ".json");
            if (dataFile.exists()) {
                try (Writer writer = Files.newBufferedWriter(dataFile.toPath(), StandardCharsets.UTF_8)) {
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("amount", playerHomeAmountMap.get(player.getUniqueId()));
                    JsonObject object = new JsonObject();
                    for (int i = 0; i <= 5; i++) {
                        object.addProperty(String.valueOf(i), (String) playerHomeLocationMap.get(player.getUniqueId()).get(i));
                    }
                    jsonArray.add(object);
                    jsonObject.add("home", jsonArray);
                    gson.toJson(jsonObject, writer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public void load() {

        for (OfflinePlayer player : Bukkit.getServer().getOfflinePlayers()) {
            dataFile = new File(Home.getInstance().getDataFolder(), "data/" + player.getUniqueId() + ".json");
            if (!dataFile.exists()) {
                settingPlayerJson(player);
            }
            try (Reader reader = Files.newBufferedReader(dataFile.toPath())) {
                JsonObject json = gson.fromJson(reader, JsonObject.class);
                playerHomeAmountMap.put(player.getUniqueId(), json.get("amount").getAsInt());
            } catch (IOException e) { e.printStackTrace(); }
        }
    }


    public void settingPlayerJson(OfflinePlayer player) {
        File dataDirectory = new File(Home.getInstance().getDataFolder(), "data");
        if (!dataDirectory.exists()) {
            dataDirectory.mkdirs();
        }

        dataFile = new File(dataDirectory,player.getUniqueId() + ".json");

        jsonObject.addProperty("amount", 0);

        JsonObject object = new JsonObject();
        for (int i = 0; i <= 5; i++) {
            object.addProperty(String.valueOf(i), "none");
        }
        jsonArray.add(object);
        jsonObject.add("home", jsonArray);

        try (Writer writer = new FileWriter(dataFile)) {
            gson.toJson(jsonObject, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public int getPlayerHomeAmount(Player player) {
        return playerHomeAmountMap.get(player.getUniqueId());
    }


    public Object getPlayerHomeLocation(Player player, int index) {
        return playerHomeLocationMap.get(player.getUniqueId()).get(index);
    }


    public void updatePlayerLocation(Player player,int slot) {
        List<Object> playerHomeLocationArray = playerHomeLocationMap.get(player.getUniqueId());
        playerHomeLocationMap.remove(player.getUniqueId());
        playerHomeLocationArray.set(slot, player.getLocation());
        playerHomeLocationMap.put(player.getUniqueId(), playerHomeLocationArray);
    }

}
