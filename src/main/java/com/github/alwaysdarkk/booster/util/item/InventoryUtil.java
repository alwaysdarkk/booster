package com.github.alwaysdarkk.booster.util.item;

import lombok.experimental.UtilityClass;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

@UtilityClass
public class InventoryUtil {

    public void subtractOneOnHand(Player player) {
        final ItemStack itemStack = player.getItemInHand();
        int amount = itemStack.getAmount();

        if (amount > 1) {
            itemStack.setAmount(amount - 1);

            player.setItemInHand(itemStack);
            return;
        }

        itemStack.setAmount(0);
        itemStack.setType(Material.AIR);
        itemStack.setData(new MaterialData(Material.AIR));
        itemStack.setItemMeta(null);
        player.setItemInHand(new ItemStack(Material.AIR));
    }
}