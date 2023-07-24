package net.starly.home.util;

import net.starly.home.builder.ItemBuilder;
import net.starly.home.context.MessageContent;
import net.starly.home.manager.PlayerHomeDataManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

import static net.starly.home.data.PlayerGUIMap.playerGuiMap;

public class HomeUtil {

    private final MessageContent config = MessageContent.getInstance();
    private static HomeUtil instance;

    public static HomeUtil getInstance() {
        if (instance == null) instance = new HomeUtil();
        return instance;
    }

    public void openHomeMenu(Player player) {
        int amount = PlayerHomeDataManager.getInstance().getPlayerHomeAmount(player);
        List<Object> list = PlayerHomeDataManager.getInstance().getPlayerHomeLocation(player);
        Inventory inventory = Bukkit.createInventory(player, 9 * 3, "홈 메뉴");
        ItemStack itemStack;
        itemStack = new ItemBuilder(Material.BARRIER)
                .setName("지정 되지 않음")
                .build();

        for (int i = 1; i <= 5; i++) {
            inventory.setItem(10 + i, itemStack);
        }

        for (int i = 0; i <= amount-1; i++) {
            itemStack = new ItemBuilder(Material.BED)
                    .setName((i+1) + "번 홈")
                    .setLore((String) list.get(i))
                    .build();
            inventory.setItem(11 + i, itemStack);
        }
        player.openInventory(inventory);
        playerGuiMap.put(player.getUniqueId(), true);
    }


}
