package com.github.alwaysdarkk.booster.listener;

import com.github.alwaysdarkk.booster.cache.BoosterCache;
import com.github.alwaysdarkk.booster.data.Booster;
import com.gmail.nossr50.events.experience.McMMOPlayerXpGainEvent;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@RequiredArgsConstructor
public class McMMOPlayerXpGainListener implements Listener {

    private final BoosterCache boosterCache;

    @EventHandler
    private void onXpGain(McMMOPlayerXpGainEvent event) {
        final Player player = event.getPlayer();

        if (boosterCache.hasBooster(player.getName(), event.getSkill())) {
            final Booster booster = boosterCache.get(player.getName());
            event.setXpGained(event.getXpGained() * booster.getMultiplier());
        }
    }
}