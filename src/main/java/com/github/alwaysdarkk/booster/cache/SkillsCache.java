package com.github.alwaysdarkk.booster.cache;

import com.gmail.nossr50.datatypes.skills.SkillType;
import com.google.common.collect.Lists;
import lombok.experimental.Delegate;

import java.util.List;

public class SkillsCache {

    @Delegate
    private final List<SkillType> skills = Lists.newLinkedList();

    public List<SkillType> getSkills() {
        return skills;
    }
}