package com.github.alwaysdarkk.booster;

import com.github.alwaysdarkk.booster.cache.BoosterCache;
import com.github.alwaysdarkk.booster.cache.SkillsCache;
import com.github.alwaysdarkk.booster.command.CustomCommand;
import com.github.alwaysdarkk.booster.command.impl.BoosterCommand;
import com.github.alwaysdarkk.booster.listener.McMMOPlayerXpGainListener;
import com.github.alwaysdarkk.booster.listener.PlayerConnectionListener;
import com.github.alwaysdarkk.booster.listener.PlayerInteractListener;
import com.github.alwaysdarkk.booster.parser.ItemStackParser;
import com.github.alwaysdarkk.booster.parser.SkillsParser;
import com.github.alwaysdarkk.booster.repository.BoosterRepository;
import com.github.alwaysdarkk.booster.repository.provider.RepositoryProvider;
import com.github.alwaysdarkk.booster.runnable.RemoveExpiredBoosterRunnable;
import com.github.alwaysdarkk.booster.view.SelectSkillView;
import com.henryfabio.sqlprovider.connector.SQLConnector;
import com.henryfabio.sqlprovider.executor.SQLExecutor;
import lombok.SneakyThrows;
import me.saiintbrisson.minecraft.ViewFrame;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;

import static com.github.alwaysdarkk.booster.BoosterConstants.BOOSTER_ITEMSTACK;

public class BoosterPlugin extends JavaPlugin {

    public static BoosterPlugin getInstance() {
        return getPlugin(BoosterPlugin.class);
    }

    @Override
    public void onLoad() {
        saveDefaultConfig();
    }

    @Override
    public void onEnable() {
        final SkillsCache skillsCache = new SkillsCache();
        final SkillsParser skillParser =
                new SkillsParser(getConfig().getConfigurationSection("settings"), getLogger(), skillsCache);

        skillParser.parse();

        final ItemStackParser itemStackParser = new ItemStackParser(getConfig().getConfigurationSection("itemStack"));
        BOOSTER_ITEMSTACK = itemStackParser.parse();

        final SQLConnector sqlConnector = RepositoryProvider.of(this).setup(null);
        final SQLExecutor sqlExecutor = new SQLExecutor(sqlConnector);

        final BoosterRepository boosterRepository = new BoosterRepository(sqlExecutor);
        boosterRepository.createTable();

        final BoosterCache boosterCache = new BoosterCache();

        final RemoveExpiredBoosterRunnable removeExpiredBoosterRunnable =
                new RemoveExpiredBoosterRunnable(boosterCache, boosterRepository);

        final ViewFrame viewFrame = ViewFrame.of(
                        this,
                        new SelectSkillView(
                                skillsCache,
                                boosterCache,
                                boosterRepository,
                                getConfig().getConfigurationSection("settings"),
                                removeExpiredBoosterRunnable))
                .register();

        Bukkit.getPluginManager()
                .registerEvents(
                        new PlayerConnectionListener(boosterRepository, boosterCache, removeExpiredBoosterRunnable),
                        this);

        Bukkit.getPluginManager().registerEvents(new McMMOPlayerXpGainListener(boosterCache), this);

        Bukkit.getPluginManager().registerEvents(new PlayerInteractListener(viewFrame), this);

        registerCommand(new BoosterCommand(boosterCache));

        removeExpiredBoosterRunnable.runIfNotRunning();
    }

    @SneakyThrows
    private void registerCommand(CustomCommand... customCommands) {
        final Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
        commandMapField.setAccessible(true);

        final CommandMap commandMap = (CommandMap) commandMapField.get(Bukkit.getServer());

        for (CustomCommand customCommand : customCommands) commandMap.register(customCommand.getName(), customCommand);
    }
}