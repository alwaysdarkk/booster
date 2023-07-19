package com.github.alwaysdarkk.booster.listener;

import com.github.alwaysdarkk.booster.view.SelectSkillView;
import lombok.RequiredArgsConstructor;
import me.saiintbrisson.minecraft.ViewFrame;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import static com.github.alwaysdarkk.booster.BoosterConstants.BOOSTER_ITEMSTACK;

@RequiredArgsConstructor
public class PlayerInteractListener implements Listener {

    private final ViewFrame viewFrame;

    @EventHandler
    private void onInteract(PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        final ItemStack itemStack = player.getItemInHand();

        if (itemStack == null || itemStack.getType() == Material.AIR) return;
        if (!itemStack.isSimilar(BOOSTER_ITEMSTACK)) return;

        event.setCancelled(true);

        viewFrame.open(SelectSkillView.class, player);
    }
}