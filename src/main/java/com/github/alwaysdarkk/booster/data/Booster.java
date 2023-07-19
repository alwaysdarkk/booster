package com.github.alwaysdarkk.booster.data;

import com.gmail.nossr50.datatypes.skills.SkillType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Booster {

    private final String owner;
    private final SkillType skillType;
    private final long expireAt;

    public boolean isExpired() {
        return expireAt <= System.currentTimeMillis();
    }
}