package com.github.alwaysdarkk.booster.util;

import com.gmail.nossr50.datatypes.skills.SkillType;
import lombok.experimental.UtilityClass;
import org.bukkit.Material;

@UtilityClass
public class McMMOUtil {

    public Material getSkillIcon(SkillType skillType) {
        switch (skillType) {
            case AXES:
                return Material.DIAMOND_AXE;
            case MINING:
                return Material.DIAMOND_PICKAXE;
            case EXCAVATION:
                return Material.DIAMOND_SPADE;
            case REPAIR:
                return Material.ANVIL;
            case SWORDS:
                return Material.DIAMOND_SWORD;
            case ALCHEMY:
                return Material.BREWING_STAND;
            case ARCHERY:
                return Material.BOW;
            case HERBALISM:
                return Material.SEEDS;
            case ACROBATICS:
                return Material.DIAMOND_BOOTS;
        }

        return null;
    }
}