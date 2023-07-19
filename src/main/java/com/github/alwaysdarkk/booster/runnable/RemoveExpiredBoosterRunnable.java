package com.github.alwaysdarkk.booster.runnable;

import com.github.alwaysdarkk.booster.BoosterPlugin;
import com.github.alwaysdarkk.booster.cache.BoosterCache;
import com.github.alwaysdarkk.booster.data.Booster;
import com.github.alwaysdarkk.booster.repository.BoosterRepository;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Iterator;

import static com.github.alwaysdarkk.booster.BoosterConstants.TASK_ID;

@RequiredArgsConstructor
public class RemoveExpiredBoosterRunnable extends BukkitRunnable {

    private final BoosterCache boosterCache;
    private final BoosterRepository boosterRepository;

    public void runIfNotRunning() {
        if (TASK_ID == null)
            TASK_ID = Bukkit.getScheduler()
                    .runTaskTimerAsynchronously(BoosterPlugin.getInstance(), this, 20L, 20L)
                    .getTaskId();
    }

    @Override
    public void run() {
        if (boosterCache.isEmpty()) {
            Bukkit.getScheduler().cancelTask(TASK_ID);

            TASK_ID = null;
            return;
        }

        final Iterator<Booster> iterator = boosterCache.values().iterator();

        while (iterator.hasNext()) {
            final Booster booster = iterator.next();

            if (booster.isExpired()) {
                iterator.remove();
                boosterRepository.deleteOne(booster);

                final Player player = Bukkit.getPlayer(booster.getOwner());

                if (player != null) player.sendMessage("§cSeu booster de experiência acabou.");
            }
        }
    }
}