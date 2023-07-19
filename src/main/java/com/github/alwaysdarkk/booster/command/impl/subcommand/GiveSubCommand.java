package com.github.alwaysdarkk.booster.command.impl.subcommand;

import com.github.alwaysdarkk.booster.command.CustomCommand;
import com.google.common.primitives.Ints;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import static com.github.alwaysdarkk.booster.BoosterConstants.BOOSTER_ITEMSTACK;

public class GiveSubCommand extends CustomCommand {

    public GiveSubCommand() {
        super("give", "booster.admin", false, "dar", "enviar");
    }

    @Override
    protected void onCommand(CommandSender commandSender, String[] arguments) {
        if (arguments.length != 2) {
            commandSender.sendMessage("§cUse /booster give <player> <quantia>.");
            return;
        }

        final Player target = Bukkit.getPlayer(arguments[0]);

        if (target == null) {
            commandSender.sendMessage("§cEste jogador não foi encontrado.");
            return;
        }

        final Integer amount = Ints.tryParse(arguments[1]);

        if (amount == null || amount <= 0) {
            commandSender.sendMessage("§cA quantia inserida é inválida.");
            return;
        }

        final ItemStack itemStack = BOOSTER_ITEMSTACK;
        itemStack.setAmount(amount);

        target.getInventory().addItem(itemStack);

        commandSender.sendMessage(
                String.format("§aYeah! §f%sx §aboosters enviados para §f%s.", amount, target.getName()));
    }
}