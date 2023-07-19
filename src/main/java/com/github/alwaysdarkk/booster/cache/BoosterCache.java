package com.github.alwaysdarkk.booster.cache;

import com.github.alwaysdarkk.booster.data.Booster;
import com.gmail.nossr50.datatypes.skills.SkillType;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;

import java.util.Map;

@RequiredArgsConstructor
public class BoosterCache {

    @Delegate
    private final Map<String, Booster> boosterMap = Maps.newHashMap();

    public boolean hasBooster(String owner, SkillType skillType) {
        final Booster booster = get(owner);
        return booster != null && booster.getSkillType() == skillType;
    }
}