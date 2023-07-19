package com.github.alwaysdarkk.booster.command.impl;

import com.github.alwaysdarkk.booster.cache.BoosterCache;
import com.github.alwaysdarkk.booster.command.CustomCommand;
import com.github.alwaysdarkk.booster.command.impl.subcommand.GiveSubCommand;
import com.github.alwaysdarkk.booster.data.Booster;
import com.github.alwaysdarkk.booster.util.TimeFormatter;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BoosterCommand extends CustomCommand {

    private final BoosterCache boosterCache;

    public BoosterCommand(BoosterCache boosterCache) {
        super("booster", null, false);

        registerSubCommands(new GiveSubCommand());

        this.boosterCache = boosterCache;
    }

    @Override
    protected void onCommand(CommandSender commandSender, String[] arguments) {
        if (!(commandSender instanceof Player)) return;

        final Player player = (Player) commandSender;
        final Booster booster = boosterCache.get(player.getName());

        if (booster == null) {
            player.sendMessage("§cVocê não possui nenhum booster ativo.");
            return;
        }

        if (booster.isExpired()) {
            player.sendMessage("§cVocê não possui nenhum booster ativo.");
            return;
        }

        player.sendMessage(String.format(
                "§eVocê possui §f%s §erestantes na habilidade §f%s§e.",
                TimeFormatter.format(booster.getExpireAt() - System.currentTimeMillis()),
                booster.getSkillType().getName()));
    }
}