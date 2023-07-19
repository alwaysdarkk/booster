package com.github.alwaysdarkk.booster.repository.adapter;

import com.github.alwaysdarkk.booster.data.Booster;
import com.gmail.nossr50.datatypes.skills.SkillType;
import com.henryfabio.sqlprovider.executor.adapter.SQLResultAdapter;
import com.henryfabio.sqlprovider.executor.result.SimpleResultSet;

public class BoosterAdapter implements SQLResultAdapter<Booster> {

    @Override
    public Booster adaptResult(SimpleResultSet resultSet) {
        final String owner = resultSet.get("owner");
        final SkillType skillType = SkillType.getSkill(resultSet.get("skillType"));
        final long expireAt = resultSet.get("expireAt");

        return Booster.builder()
                .owner(owner)
                .skillType(skillType)
                .expireAt(expireAt)
                .build();
    }
}