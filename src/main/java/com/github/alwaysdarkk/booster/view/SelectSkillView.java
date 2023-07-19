package com.github.alwaysdarkk.booster.view;

import com.github.alwaysdarkk.booster.cache.BoosterCache;
import com.github.alwaysdarkk.booster.cache.SkillsCache;
import com.github.alwaysdarkk.booster.data.Booster;
import com.github.alwaysdarkk.booster.repository.BoosterRepository;
import com.github.alwaysdarkk.booster.runnable.RemoveExpiredBoosterRunnable;
import com.github.alwaysdarkk.booster.util.McMMOUtil;
import com.github.alwaysdarkk.booster.util.item.InventoryUtil;
import com.github.alwaysdarkk.booster.util.item.ItemBuilder;
import com.gmail.nossr50.datatypes.skills.SkillType;
import me.saiintbrisson.minecraft.PaginatedView;
import me.saiintbrisson.minecraft.PaginatedViewSlotContext;
import me.saiintbrisson.minecraft.ViewItem;
import me.saiintbrisson.minecraft.ViewSlotClickContext;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.function.Consumer;

public class SelectSkillView extends PaginatedView<SkillType> {

    private final BoosterCache boosterCache;
    private final BoosterRepository boosterRepository;
    private final ConfigurationSection configurationSection;
    private final RemoveExpiredBoosterRunnable removeExpiredBoosterRunnable;

    public SelectSkillView(
            SkillsCache skillsCache,
            BoosterCache boosterCache,
            BoosterRepository boosterRepository,
            ConfigurationSection configurationSection,
            RemoveExpiredBoosterRunnable removeExpiredBoosterRunnable) {
        super(3, "Selecione uma habilidade");

        setLayout("XXXXXXXXX", "<OOOOOOO>", "XXXXXXXXX");

        setSource(context -> skillsCache.getSkills());

        setPreviousPageItem((context, viewItem) -> viewItem.withItem(
                context.hasPreviousPage()
                        ? ItemBuilder.of(Material.ARROW)
                                .displayName("§aPágina anterior")
                                .getItemStack()
                        : null));
        setNextPageItem((context, viewItem) -> viewItem.withItem(
                context.hasNextPage()
                        ? ItemBuilder.of(Material.ARROW)
                                .displayName("§aPróxima página")
                                .getItemStack()
                        : null));

        setCancelOnClick(true);

        this.boosterCache = boosterCache;
        this.boosterRepository = boosterRepository;
        this.configurationSection = configurationSection;
        this.removeExpiredBoosterRunnable = removeExpiredBoosterRunnable;
    }

    @Override
    protected void onItemRender(
            @NotNull PaginatedViewSlotContext<SkillType> context,
            @NotNull ViewItem viewItem,
            @NotNull SkillType value) {
        viewItem.onRender(render -> render.setItem(buildIcon(value))).onClick(handleClick(value));
    }

    private ItemStack buildIcon(SkillType skillType) {
        return ItemBuilder.of(McMMOUtil.getSkillIcon(skillType))
                .displayName("§e" + skillType.getName())
                .lore("§7Clique para selecionar esta", "§7habilidade.")
                .itemFlags(ItemFlag.HIDE_ATTRIBUTES)
                .getItemStack();
    }

    private Consumer<ViewSlotClickContext> handleClick(SkillType skillType) {
        return handler -> {
            final Player player = handler.getPlayer();
            close();

            if (boosterCache.get(player.getName()) != null) {
                player.sendMessage("§cVocê já tem um booster ativo.");
                return;
            }

            final int boosterDuration = configurationSection.getInt("booster-duration");
            final Instant expireAt = Instant.now().plus(boosterDuration, ChronoUnit.MINUTES);

            final Booster booster = Booster.builder()
                    .owner(player.getName())
                    .skillType(skillType)
                    .expireAt(expireAt.toEpochMilli())
                    .build();

            boosterCache.put(booster.getOwner(), booster);
            boosterRepository.insertOne(booster);

            removeExpiredBoosterRunnable.runIfNotRunning();

            InventoryUtil.subtractOneOnHand(player);

            player.sendMessage("§aYeah! Booster ativado com sucesso.");
        };
    }
}