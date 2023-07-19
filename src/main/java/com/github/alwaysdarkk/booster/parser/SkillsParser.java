package com.github.alwaysdarkk.booster.parser;

import com.github.alwaysdarkk.booster.cache.SkillsCache;
import com.gmail.nossr50.datatypes.skills.SkillType;
import lombok.RequiredArgsConstructor;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

@RequiredArgsConstructor
public class SkillsParser {

    private final ConfigurationSection configurationSection;
    private final Logger logger;
    private final SkillsCache skillsCache;

    public void parse() {
        final List<String> skills = configurationSection.getStringList("skills");

        skills.stream().filter(Objects::nonNull).forEach(skill -> {
            final SkillType skillType = SkillType.getSkill(skill);

            if (skillType != null) skillsCache.add(skillType);
        });

        logger.info(String.format("Foram carregadas '%s' skills.", skillsCache.size()));
    }
}