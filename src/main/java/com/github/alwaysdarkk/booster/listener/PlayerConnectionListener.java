package com.github.alwaysdarkk.booster.listener;

import com.github.alwaysdarkk.booster.cache.BoosterCache;
import com.github.alwaysdarkk.booster.data.Booster;
import com.github.alwaysdarkk.booster.repository.BoosterRepository;
import com.github.alwaysdarkk.booster.runnable.RemoveExpiredBoosterRunnable;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

@RequiredArgsConstructor
public class PlayerConnectionListener implements Listener {

    private final BoosterRepository boosterRepository;
    private final BoosterCache boosterCache;
    private final RemoveExpiredBoosterRunnable removeExpiredBoosterRunnable;

    @EventHandler
    private void onJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final Booster booster = boosterRepository.selectOne(player.getName());

        if (booster == null) return;

        if (booster.isExpired()) {
            boosterRepository.deleteOne(booster);

            player.sendMessage("§cSeu booster de experiência acabou.");
            return;
        }

        boosterCache.put(booster.getOwner(), booster);
        removeExpiredBoosterRunnable.runIfNotRunning();
    }

    @EventHandler
    private void onQuit(PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        boosterCache.remove(player.getName());
    }
}