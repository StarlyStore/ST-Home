package net.starly.home.manager;

import com.google.gson.*;
import net.starly.home.HomeMain;
import net.starly.home.data.Home;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.*;

public class HomeManager {

    private static HomeManager instance;

    private final Map<UUID, List<Home>> playerHome = new HashMap<>();

    private HomeManager() {}

    public static HomeManager getInstance() {
        if (instance == null) instance = new HomeManager();
        return instance;
    }

    public void saveAll() {
        Set<UUID> players = playerHome.keySet();
        players.forEach(this::save);
    }

    public void save(UUID uniqueId) {
        JavaPlugin plugin = HomeMain.getInstance();
        File directory = new File(plugin.getDataFolder(), "data");
        File dataFile = new File(directory, uniqueId + ".json");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject jsonObject = new JsonObject();
        JsonObject homesObject = new JsonObject();

        List<Home> playerHomes = playerHome.get(uniqueId);
        playerHomes.forEach(home -> {
            JsonObject homeObject = new JsonObject();

            Location homeLocation = home.getLocation();
            homeObject.add("location", toJsonObject(homeLocation));

            homesObject.add(String.valueOf(playerHomes.indexOf(home)), homeObject);
        });

        jsonObject.add("homes", homesObject);

        if (!directory.exists()) directory.mkdir();

        if (!dataFile.exists()) {
            try {
                dataFile.createNewFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(dataFile))) {
            gson.toJson(jsonObject, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadAll() {
        JavaPlugin plugin = HomeMain.getInstance();
        File dataFolder = new File(plugin.getDataFolder(), "data/");

        if (!dataFolder.exists()) dataFolder.mkdir();

        for (String fileName : dataFolder.list()) {
            UUID uniqueId = UUID.fromString(fileName.replace(".json", ""));
            load(uniqueId);
        }
    }


    public void load(UUID uniqueId) {
        JavaPlugin plugin = HomeMain.getInstance();
        File directory = new File(plugin.getDataFolder(),"data");
        File dataFile = new File(directory, uniqueId + ".json");

        if (!directory.exists()) directory.mkdir();

        if (!dataFile.exists()) {
            try {
                dataFile.createNewFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(dataFile))) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            JsonObject json = gson.fromJson(reader, JsonObject.class);
            JsonObject homeObject = json.get("homes").getAsJsonObject();

            List<Home> playerHomes = new ArrayList<>();
            homeObject.entrySet().forEach(entry -> {
                JsonObject jsonObject = entry.getValue().getAsJsonObject();

                Location location = parseLocation(jsonObject.get("location").getAsJsonObject());
                Home home = new Home(uniqueId, location);

                playerHomes.add(home);
            });
            playerHome.put(uniqueId, playerHomes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void settingPlayerMap(UUID uniqueId) {
        List<Home> playerHomes = new ArrayList<>();
        setHomes(uniqueId, playerHomes);
    }

    public void clear() {
        playerHome.clear();
    }


    public Home getHome(UUID uniqueId, int slot) {
        try {
            return getHomes(uniqueId).get(slot);
        } catch (IndexOutOfBoundsException ignored) {
            return null;
        }
    }

    public List<Home> getHomes(UUID uniqueId) {
        return playerHome.get(uniqueId);
    }

    public void setHomes(UUID uniqueId, List<Home> homes) {
        playerHome.put(uniqueId, homes);
    }

    public void addHome(UUID uniqueId, Home home) {
        List<Home> playerHomes = playerHome.getOrDefault(uniqueId, new ArrayList<>());
        playerHomes.add(home);
        playerHome.put(uniqueId, playerHomes);
    }


    public Location parseLocation(JsonObject jsonObject) {
        World world = Bukkit.getWorld(jsonObject.get("world").getAsString());
        double x = jsonObject.get("x").getAsDouble();
        double y = jsonObject.get("y").getAsDouble();
        double z = jsonObject.get("z").getAsDouble();
        float yaw = jsonObject.get("yaw").getAsFloat();
        float pitch = jsonObject.get("pitch").getAsFloat();
        return new Location(world, x, y, z, yaw, pitch);
    }

    private JsonObject toJsonObject(Location location) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("world", location.getWorld().getName());
        jsonObject.addProperty("x", location.getX());
        jsonObject.addProperty("y", location.getY());
        jsonObject.addProperty("z", location.getZ());
        jsonObject.addProperty("yaw", location.getYaw());
        jsonObject.addProperty("pitch", location.getPitch());
        return jsonObject;
    }
}
