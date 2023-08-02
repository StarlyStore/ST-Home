package net.starly.home.util;

import lombok.Getter;
import net.starly.home.enums.ChatType;
import net.starly.home.enums.GuiType;
import net.starly.home.HomeMain;
import net.starly.home.builder.ItemBuilder;
import net.starly.home.manager.HomeManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class HomeUtil {

    private final Plugin plugin = HomeMain.getInstance();

    @Getter
    private final HashMap<UUID, GuiType> playerGuiTypeMap = new HashMap<>();

    @Getter
    private final HashMap<UUID, ChatType> playerChatMap = new HashMap<>();
    private final HashMap<UUID, Boolean> playerTelePortMap = new HashMap<>();
    private final HashMap<UUID, Boolean> isPlayerMoveMap = new HashMap<>();

    private static HomeUtil instance;

    private HomeUtil() {
    }

    public static HomeUtil getInstance() {
        if (instance == null) instance = new HomeUtil();
        return instance;
    }

    public void openHomeMenu(Player player) {
        HomeManager homeManager = HomeManager.getInstance();
        if (homeManager.getHomes(player.getUniqueId()) == null) homeManager.settingPlayerMap(player.getUniqueId());
        int homeCount = homeManager.getHomes(player.getUniqueId()).size();

        Inventory inventory = Bukkit.createInventory(player, 9 * 3, "홈 메뉴");
        ItemStack itemStack;
        itemStack = new ItemBuilder(Material.BARRIER)
                .setName("§f지정 되지 않음")
                .build();
        for (int i = 11; i <= 15; i++) inventory.setItem(i, itemStack);
        for (int i = 0; i <= homeCount - 1; i++) {
            Location location = homeManager.getHome(player.getUniqueId(), i).getLocation();
            try {
                itemStack = new ItemBuilder(Material.valueOf("BED"))
                        .setName((i + 1) + "번 홈")
                        .setLore("§f월드 : " + location.getWorld().getName(), "§fx : " + String.format("%.3f", location.getX()), "§fy : " + String.format("%.3f", location.getY()), "§fz : " + String.format("%.3f", location.getZ()))
                        .build();
            } catch (Exception ignored) {
                itemStack = new ItemBuilder(Material.valueOf("WHITE_BED"))
                        .setName((i + 1) + "번 홈")
                        .setLore("§f월드 : " + location.getWorld().getName(), "§fx : " + String.format("%.3f", location.getX()), "§fy : " + String.format("%.3f", location.getY()), "§fz : " + String.format("%.3f", location.getZ()))
                        .build();
            }
            inventory.setItem(i + 11, itemStack);
        }
        try {
            itemStack = new ItemBuilder(Material.valueOf("BOOK_AND_QUILL"))
                    .setName("§f사용법")
                    .setLore("§f좌클릭: 홈으로 이동", "§f쉬프트 + 좌클릭: 홈 지정", "§f쉬프트 + 우클릭: 홈 삭제")
                    .build();
        } catch (Exception ignored) {
            itemStack = new ItemBuilder(Material.valueOf("WRITABLE_BOOK"))
                    .setName("§f사용법")
                    .setLore("§f좌클릭: 홈으로 이동", "§f쉬프트 + 좌클릭: 홈 지정", "§f쉬프트 + 우클릭: 홈 삭제")
                    .build();
        }



        inventory.setItem(22, itemStack);

        playerGuiTypeMap.put(player.getUniqueId(), GuiType.OPEN);
        player.openInventory(inventory);
    }

    public void openHomeSettingMenu(Player player) {
        Plugin plugin = HomeMain.getInstance();
        FileConfiguration config = plugin.getConfig();
        Inventory inventory = Bukkit.createInventory(player, 9 * 3, "홈 세팅");
        ItemStack itemStack;

        int time = (int) config.get("data.time");
        boolean move = (boolean) config.get("data.move");


        try {
            itemStack = new ItemBuilder(Material.valueOf("CLOCK"))
                    .setName("§f시간 설정")
                    .setLore("§f현재값 : " + time + "초")
                    .build();
        } catch (Exception ignored) {
            itemStack = new ItemBuilder(Material.valueOf("WATCH"))
                    .setName("§f시간 설정")
                    .setLore("§f현재값 : " + time + "초")
                    .build();

        }

        inventory.setItem(11, itemStack);

        itemStack = new ItemBuilder(Material.CHAINMAIL_BOOTS)
                .setName("§f움직임 감지")
                .setLore("§f현재값 : " + move)
                .build();
        inventory.setItem(12, itemStack);

        playerGuiTypeMap.put(player.getUniqueId(), GuiType.EDIT);
        player.openInventory(inventory);
    }

    public void setPlayerTelePortMap(Player player, Boolean state) {
        playerTelePortMap.put(player.getUniqueId(), state);
    }

    public Boolean getPlayerTelePortMap(Player player) {
        return playerTelePortMap.get(player.getUniqueId());
    }

    public void setIsPlayerMoveMap(Player player, Boolean state) {
        isPlayerMoveMap.put(player.getUniqueId(), state);
    }

    public Boolean getIsPlayerMoveMap(Player player) {
        return isPlayerMoveMap.get(player.getUniqueId());
    }
}
