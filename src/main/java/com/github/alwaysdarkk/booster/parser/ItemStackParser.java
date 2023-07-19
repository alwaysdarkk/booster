package com.github.alwaysdarkk.booster.parser;

import com.github.alwaysdarkk.booster.util.item.ItemBuilder;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

@RequiredArgsConstructor
public class ItemStackParser {

    private final ConfigurationSection configurationSection;

    public ItemStack parse() {
        final ItemBuilder itemBuilder = ItemBuilder.of(Material.getMaterial(configurationSection.getInt("id")));
        itemBuilder.data(configurationSection.getInt("data"));

        if (configurationSection.getBoolean("glow")) {
            itemBuilder.glow();
        }

        itemBuilder.displayName(configurationSection.getString("name"));
        itemBuilder.lore(configurationSection.getStringList("lore"));

        return itemBuilder.getItemStack();
    }
}